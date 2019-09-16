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
package org.eclipse.microprofile.graphql.tck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.Archive;
import org.testng.annotations.Test;

import org.jboss.arquillian.testng.Arquillian;
import org.eclipse.microprofile.graphql.tck.apps.superhero.api.HeroFinder;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroDatabase;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHero;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;

/**
 * This really just test if there is a schema available on graphql/schema.graphql
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class SchemaAvailableTest extends Arquillian {
    private static final Logger LOG = Logger.getLogger(SchemaAvailableTest.class.getName());   
    private static final String PATH = "graphql/schema.graphql";
    
    @ArquillianResource
    private URI uri;
    
    @Deployment
    public static Archive getDeployment() throws Exception {
        return ShrinkWrap.create(WebArchive.class, "tck.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addPackage(HeroFinder.class.getPackage())
                .addPackage(HeroDatabase.class.getPackage())
                .addPackage(SuperHero.class.getPackage());
    }

    @Test
    @RunAsClient
    public void testResponse() throws Exception {
        // Check that there is some content
        String content = getSchemaContent();
        LOG.info("Schema: " + System.lineSeparator() + content);
        Assert.assertTrue(content.length() > 0);
    }

    @Test
    @RunAsClient
    public void testIgnoreOnFieldExcludedFromInputAndOutputTypes() throws Exception {
        String schema = getSchemaContent();
        int index = schema.indexOf("type Item ");
        Assert.assertTrue(index > -1, "Cannot find \"type Item\" in schema"); 
        String snippet = schema.substring(index, schema.indexOf("}", index + 1));
        Assert.assertTrue(snippet.contains("powerLevel"), "Missing expected, un-ignored field, \"powerLevel\"");
        Assert.assertFalse(snippet.contains("invisible"), "Found field \"invisible\" that should be ignored");

        index = schema.indexOf("input ItemInput ");
        Assert.assertTrue(index > -1, "Cannot find \"input ItemInput\" in schema");
        snippet = schema.substring(index, schema.indexOf("}", index + 1));
        Assert.assertTrue(snippet.contains("powerLevel"), "Missing expected, un-ignored field, \"powerLevel\"");
        Assert.assertFalse(snippet.contains("invisible"), 
                "Found field \"invisible\" that should be ignored in input type");
    }
    
    @Test
    @RunAsClient
    public void testIgnoreOnGetterExcludedOnOutputType() throws Exception {
        String schema = getSchemaContent();
        int index = schema.indexOf("type Item ");
        Assert.assertTrue(index > -1, "Cannot find \"type Item\" in schema");
        String snippet = schema.substring(index, schema.indexOf("}", index + 1));
        Assert.assertTrue(snippet.contains("artificialIntelligenceRating"), 
                "Missing expected, un-ignored field, \"artificialIntelligenceRating\"");
        Assert.assertFalse(snippet.contains("canWield"), "Found field \"canWield\" that should be ignored");
    }

    @Test
    @RunAsClient
    public void testIgnoreOnSetterExcludedOnInputType() throws Exception {
        String schema = getSchemaContent();
        int index = schema.indexOf("input ItemInput ");
        Assert.assertTrue(index > -1, "Cannot find \"input ItemInput\" in schema");
        String snippet = schema.substring(index, schema.indexOf("}", index + 1));
        Assert.assertTrue(snippet.contains("canWield"), "Missing expected, un-ignored field, \"canWield\"");
        Assert.assertFalse(snippet.contains("artificialIntelligenceRating"),
                "Found field \"artificialIntelligenceRating\" that should be ignored in input type");
    }

    @Test
    @RunAsClient
    public void testJsonbTransientOnSetterExcludedOnInputType() throws Exception {
        String schema = getSchemaContent();
        int index = schema.indexOf("input SuperHeroInput ");
        Assert.assertTrue(index > -1, "Cannot find \"input SuperHeroInput\" in schema");
        String snippet = schema.substring(index, schema.indexOf("}", index + 1));
        Assert.assertTrue(snippet.contains("colorOfCostume"), "Missing expected, un-ignored field, \"colorOfCostume\"");
        Assert.assertFalse(snippet.contains("knownEnemies"),
                "Found field \"knownEnemies\" that should be ignored in input type");

        //now verify that the field is still in the output type section of the schema:
        index = schema.indexOf("type SuperHero ");
        Assert.assertTrue(index > -1, "Cannot find \"input SuperHeroInput\" in schema");
        snippet = schema.substring(index, schema.indexOf("}", index + 1));
        Assert.assertTrue(snippet.contains("knownEnemies"),
                "Did not find field \"knownEnemies\" that should be present (only ignored in input type)");
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

    private String getContent(HttpURLConnection connection) throws IOException{
        try (StringWriter sw = new StringWriter();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                sw.write(inputLine);
            }
            return sw.toString();
        }
    }
    
}

