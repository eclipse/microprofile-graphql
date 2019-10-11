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
 * Tests that the schema is available at graphql/schema.graphql and that it contains the proper content.
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class SchemaAvailableTest extends Arquillian {
    private static final Logger LOG = Logger.getLogger(SchemaAvailableTest.class.getName());   
    private static final String PATH = "graphql/schema.graphql";
    
    @ArquillianResource
    private URI uri;
    
    @Deployment
    public static Archive<?> getDeployment() throws Exception {
        return ShrinkWrap.create(WebArchive.class, "tck-schematest.war")
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
        String snippet = getSchemaSnippet(schema, "type Item ");
        Assert.assertTrue(snippet.contains("powerLevel"), "Missing expected, un-ignored field, \"powerLevel\"");
        Assert.assertFalse(snippet.contains("invisible"), "Found field \"invisible\" that should be ignored");

        snippet = getSchemaSnippet(schema, "input ItemInput ");
        Assert.assertTrue(snippet.contains("powerLevel"), "Missing expected, un-ignored field, \"powerLevel\"");
        Assert.assertFalse(snippet.contains("invisible"), 
                "Found field \"invisible\" that should be ignored in input type");
    }
    
    @Test
    @RunAsClient
    public void testIgnoreOnGetterExcludedOnOutputType() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "type Item ");
        Assert.assertTrue(snippet.contains("artificialIntelligenceRating"), 
                "Missing expected, un-ignored field, \"artificialIntelligenceRating\"");
        Assert.assertFalse(snippet.contains("canWield"), "Found field \"canWield\" that should be ignored");
    }

    @Test
    @RunAsClient
    public void testIgnoreOnSetterExcludedOnInputType() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input ItemInput ");
        Assert.assertTrue(snippet.contains("canWield"), "Missing expected, un-ignored field, \"canWield\"");
        Assert.assertFalse(snippet.contains("artificialIntelligenceRating"),
                "Found field \"artificialIntelligenceRating\" that should be ignored in input type");
    }

    @Test
    @RunAsClient
    public void testJsonbTransientOnSetterExcludedOnInputType() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("colorOfCostume"), "Missing expected, un-ignored field, \"colorOfCostume\"");
        Assert.assertFalse(snippet.contains("knownEnemies"),
                "Found field \"knownEnemies\" that should be ignored in input type");

        //now verify that the field is still in the output type section of the schema:
        snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertTrue(snippet.contains("knownEnemies"),
                "Did not find field \"knownEnemies\" that should be present (only ignored in input type)");
    }

    @Test
    @RunAsClient
    public void testInputTypeOnField() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("tshirtSize: ShirtSize"),
            "Missing expected field, \"tshirtSize\" specified in @InputField annotation");
        Assert.assertFalse(snippet.contains("sizeOfTShirt"),
                "Found field \"sizeOfTShirt\" that should be replaced by \"tshirtSize\" in input type");

        // now verify that the Java field name is in the output type section of the schema,
        // and that it does not include the input field name:
        snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertFalse(snippet.contains("tshirtSize"), 
            "Found field \"tshirtSize\" in output type, when it should only be found in the input type");
        Assert.assertTrue(snippet.contains("sizeOfTShirt: ShirtSize"),
                "Did not find field \"sizeOfTShirt\" in the output type");
    }

    @Test
    @RunAsClient
    public void testEnumAppearsInSchema() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "enum ShirtSize ");
        Assert.assertNotNull(snippet);
        Assert.assertTrue(snippet.contains("S"), "Missing expected enum value, \"S\"");
        Assert.assertTrue(snippet.contains("M"), "Missing expected enum value, \"M\"");
        Assert.assertTrue(snippet.contains("XL"), "Missing expected enum value, \"XL\"");
        Assert.assertTrue(snippet.contains("HULK"), "Missing expected enum value, \"HULK\"");
    }

    @Test
    @RunAsClient
    public void testDateScalarUsedForLocalDate() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("dateOfLastCheckin: Date"));

        snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertTrue(snippet.contains("dateOfLastCheckin: Date"));
    }

    @Test
    @RunAsClient
    public void testTimeScalarUsedForLocalTime() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("patrolStartTime: Time"));

        snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertTrue(snippet.contains("patrolStartTime: Time"));
    }

    @Test
    @RunAsClient
    public void testDateTimeScalarUsedForLocalDateTime() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("timeOfLastBattle: DateTime"));

        snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertTrue(snippet.contains("timeOfLastBattle: DateTime"));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForQueryMethods() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "type Query ");
        Assert.assertTrue(snippet.contains("#List all super heroes in the database"));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForMutationMethods() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "type Mutation ");
        System.out.println("snippet = " + snippet);
        Assert.assertTrue(snippet.contains("#Removes a hero... permanently..."));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForEntityTypes() throws Exception {
        String schema = getSchemaContent();
        Assert.assertTrue(schema.contains("#Something of use to a super hero"));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForInputTypes() throws Exception {
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForArguments() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "type Query ");
        Assert.assertTrue(snippet.contains("#Super hero name, not real name"));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForOutputTypeFields() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertTrue(snippet.contains("#Super hero name/nickname"));
        Assert.assertTrue(snippet.contains("#Location where you are most likely to find this hero"));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDescriptionForInputTypeFields() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("#Super hero name/nickname"));
        Assert.assertTrue(snippet.contains("#Powers that make this hero super"));
    }

    @Test
    @RunAsClient
    public void testSchemaOutputTypeFieldsContainsDescriptionFromJsonbDateFormat() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertTrue(snippet.contains("#MM/dd/yyyy"));
        Assert.assertTrue(snippet.contains("#HH:mm"));
        Assert.assertTrue(snippet.contains("#HH:mm:ss dd-MM-yyyy"));
    }

    @Test
    @RunAsClient
    public void testSchemaInputTypeFieldsContainsDescriptionFromJsonbDateFormat() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("#MM/dd/yyyy"));
        Assert.assertTrue(snippet.contains("#HH:mm"));
        Assert.assertTrue(snippet.contains("#HH:mm:ss dd-MM-yyyy"));
    }

    @Test
    @RunAsClient
    public void testNonNullOnSetterOnlyMakesInputTypeNonNull() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input SuperHeroInput ");
        Assert.assertTrue(snippet.contains("name: String!"), "Missing expected, non-null field, \"name\"");

        snippet = getSchemaSnippet(schema, "type SuperHero ");
        Assert.assertFalse(snippet.contains("name: String!"), "Found \"name\" as non-null field, expected nullable");
    }

    @Test
    @RunAsClient
    public void testNonNullOnGetterOnlyMakesOutputTypeNonNull() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input TeamInput ");
        Assert.assertFalse(snippet.contains("name: String!"), "Found \"name\" as non-null field, expected nullable");

        snippet = getSchemaSnippet(schema, "type Team ");
        Assert.assertTrue(snippet.contains("name: String!"), "Missing expected, non-null field, \"name\"");
    }

    @Test
    @RunAsClient
    public void testNonNullOnFieldMakesBothNonNull() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input ItemInput ");
        Assert.assertTrue(snippet.contains("name: String!"), "Missing expected, non-null field, \"name\" on input");

        snippet = getSchemaSnippet(schema, "type Item ");
        Assert.assertTrue(snippet.contains("name: String!"), "Missing expected, non-null field, \"name\"");
    }

    @Test
    @RunAsClient
    public void testNonNullOnFieldWithDefaultValueIsNullable() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input ItemInput ");
        Assert.assertFalse(snippet.contains("description: String!"), 
            "Field with default value is unexpectedly marked as required/non-nullable");
        Assert.assertTrue(snippet.contains("description: String = \"An unidentified item\""),
            "Field's default value is missing");

        snippet = getSchemaSnippet(schema, "type Item ");
        Assert.assertFalse(snippet.contains("description: String!"), 
            "Field with default value is unexpectedly marked as required/non-nullable");
        Assert.assertTrue(snippet.contains("description: String"),
            "Field with default value and nonnull is missing");
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDefaultValueOnEntityField() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input ItemInput ");
        Assert.assertTrue(snippet.contains("supernatural: Boolean = false"));
    }

    @Test
    @RunAsClient
    public void testSchemaContainsDefaultValueOnEntitySetter() throws Exception {
        String schema = getSchemaContent();
        String snippet = getSchemaSnippet(schema, "input ItemInput ");
        Assert.assertTrue(snippet.contains("description: String = \"An unidentified item\""));
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
            }
            return sw.toString();
        }
    }
}