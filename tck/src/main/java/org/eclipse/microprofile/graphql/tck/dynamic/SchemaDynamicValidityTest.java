/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.microprofile.graphql.tck.dynamic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.graphql.tck.dynamic.schema.SchemaTestDataProvider;
import org.eclipse.microprofile.graphql.tck.dynamic.schema.TestData;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.Archive;
import org.testng.annotations.Test;

import org.jboss.arquillian.testng.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

/**
 * Tests that the schema is available at graphql/schema.graphql and that it contains the proper content.
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class SchemaDynamicValidityTest extends Arquillian {
    private static final Logger LOG = Logger.getLogger(SchemaDynamicValidityTest.class.getName());   
    private static final String PATH = "graphql/schema.graphql";
    
    private String schema;
    
    @ArquillianResource
    private URI uri;
    
    @Deployment
    public static Archive<?> getDeployment() throws Exception {
        return DeployableUnit.getDeployment("tck-schematest");
    }

    @BeforeMethod
    public void getSchemaContentOnce() throws Exception {
        if(this.schema==null){
            LOG.log(Level.INFO, "Fetching schema from {0}", uri);
            this.schema = getSchemaContent();
            // TODO: print to file in target ?
            
            
            // LOG.log(Level.INFO, "Schema: {0}{1}", new Object[]{System.lineSeparator(), schema});
        }
    }
    
    @Test
    @RunAsClient
    public void testResponse() throws Exception {
        // Check that there is some content
        Assert.assertTrue(schema.length() > 0);
    }

    @RunAsClient
    @Test(dataProvider = "schemaSnippets", dataProviderClass = SchemaTestDataProvider.class)
    public void testPartsOfSchema(TestData input) throws Exception{
        Assert.assertNotNull(schema);
        String snippet = schema; // default search against the whole schema
        if(input.getSnippetSearchTerm()!=null){
            snippet = getSchemaSnippet(schema, input.getSnippetSearchTerm());
        }
        Assert.assertNotNull(snippet);
        Assert.assertTrue(snippet.length() > 0);
        
        // Check if this is a negative test
        if(input.getContainsString().startsWith("!")){
            Assert.assertFalse(snippet.contains(input.getContainsString().substring(1)), input.getErrorMessage());
        }else{
            Assert.assertTrue(snippet.contains(input.getContainsString()), input.getErrorMessage());
        }
    }
    
    private String getSchemaContent() throws Exception {
        URL url = new URL(this.uri + PATH);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "plain/text");

            // Check the response code
            int responseCode = connection.getResponseCode();
            Assert.assertEquals(responseCode, 200, "While testing [" + url.toString() + "]");

            return getContent(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String getSchemaSnippet(String schema, String section) throws Exception {
        int index = schema.indexOf(section);
        Assert.assertTrue(index > -1, "Cannot find " + section + " in schema");
        char[] schemaChars = schema.toCharArray();
        int closePos = schema.indexOf("{", index) + 1;
        int counter = 1;
        while (counter > 0 && closePos < schemaChars.length - 1) {
            char c = schemaChars[++closePos];
            if (c == '{') {
                counter++;
            } else if (c == '}') {
                counter--;
            }
        }

        return schema.substring(index, closePos);
    }

    private String getContent(HttpURLConnection connection) throws IOException{
        try (StringWriter sw = new StringWriter();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                sw.write(inputLine);
                sw.write("\n");
            }
            return sw.toString();
        }
    }
}