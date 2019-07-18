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
        URL url = new URL(this.uri + PATH);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "plain/text");
        
        // Check the response code
        int responseCode = connection.getResponseCode();
        Assert.assertEquals(responseCode, 200, "While testing [" + url.toString() + "]");
        
        // Check the content
        String content = getContent(connection);
        Assert.assertTrue(content.length() > 0 , "While testing [" + url.toString() + "]");
        
        connection.disconnect();
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

