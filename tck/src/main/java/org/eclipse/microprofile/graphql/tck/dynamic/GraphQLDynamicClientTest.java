/*
 * Copyright 2019 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.microprofile.graphql.tck.dynamic;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.extern.java.Log;
import org.eclipse.microprofile.graphql.tck.dynamic.init.GraphQLTestDataProvider;
import org.eclipse.microprofile.graphql.tck.dynamic.init.PrintUtil;
import org.eclipse.microprofile.graphql.tck.dynamic.init.TestData;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.graphql.tck.apps.superhero.api.HeroFinder;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroDatabase;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHero;

/**
 * This test runs all test defined in 'src/test/resources'
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class GraphQLDynamicClientTest {
    
    private static class GetSysPropAction implements PrivilegedAction<String> {
        private final String propertyName;
        private final String defaultValue;

        GetSysPropAction(String propertyName, String defaultValue) {
            this.propertyName = propertyName;
            this.defaultValue = defaultValue;
        }

        @Override
        public String run() {
            return System.getProperty(propertyName, defaultValue);
        }
        
    }

    private static final String UTF8 = "utf-8";
    private static final String MEDIATYPE_JSON = "application/json";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";
    private static final String ENDPOINT_URL = "http://localhost:" + 
            AccessController.doPrivileged(new GetSysPropAction("mp.graphql.tck.endpoint.port", "8080")) + "/SuperHeroDatabase/graphql";
    private static final String CONNECT_TIMEOUT = AccessController.doPrivileged(new GetSysPropAction("mp.graphql.tck.connect.timeout", "30000"));
    private static final String READ_TIMEOUT = AccessController.doPrivileged(new GetSysPropAction("mp.graphql.tck.read.timeout", "30000"));
    private static File testDataDir;
    

    @Deployment
    public static Archive<?> createDeployment() throws Exception {
        
        Archive<?> archive = ShrinkWrap.create(WebArchive.class, "SuperHeroDatabase.war")
            .addPackage(HeroFinder.class.getPackage())
            .addPackage(HeroDatabase.class.getPackage())
            .addPackage(SuperHero.class.getPackage())
            .addPackage(GraphQLDynamicClientTest.class.getPackage()) // ??
            .addPackage(TestData.class.getPackage())
            //.add(new StringAsset(testDataDir.getAbsolutePath()), "testDataDir.txt")
            .addAsLibrary(new File(IOUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI()))
            .addAsLibrary(new File(JSONException.class.getProtectionDomain().getCodeSource().getLocation().toURI()))
            .addAsLibrary(new File(JSONAssert.class.getProtectionDomain().getCodeSource().getLocation().toURI()));

            log.info("Creating SuperHeroDatabase.war file for deployment");
            return archive;
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Test(dataProvider="GraphQLTestDataProvider", dataProviderClass = GraphQLTestDataProvider.class)
    public void testDynamicQueries(TestData testData) throws IOException {
        if(testData!=null && isValidInput(testData.getInput())) {
            Map<String, String> httpHeaders = new HashMap<>();
            if(testData.getHttpHeaders()!=null && !testData.getHttpHeaders().isEmpty()){
                for(Map.Entry<String, String> header:httpHeaders.entrySet()){
                    httpHeaders.put(header.getKey(), header.getValue());
                }
            }

            // Prepare if needed
            if(isValidInput(testData.getPrepare())){
                postHTTPRequest(testData.getPrepare(),testData.getVariables(),httpHeaders);
            }

            // Run the actual test and get the response
            String json = postHTTPRequest(testData.getInput(),testData.getVariables(),httpHeaders);
            PrintUtil.print(testData,json);
            
            // Cleanup if needed
            if(isValidInput(testData.getCleanup())){
                postHTTPRequest(testData.getCleanup(),testData.getVariables(),httpHeaders);
            }

            // Compare to expected output
            try{
                JSONAssert.assertEquals(testData.getOutput(), json, false);
            } catch (JSONException ex) {
                Assert.fail(ex.getMessage());
            }
        }
    }
    
    private boolean isValidInput(String input){
        return input!=null && !input.isEmpty();
    }
    
    private String postHTTPRequest(String graphQL, JsonObject variables, Map<String, String> httpHeaders){
        try {
            URL graphqlEndpoint = new URL(ENDPOINT_URL);
            HttpURLConnection connection = (HttpURLConnection) graphqlEndpoint.openConnection();
            connection.setRequestMethod("POST"); // TODO: Also test with GET and query string ? Are we allowing it ?
            
            setTimeouts(connection);
            addHeaders(connection,httpHeaders);
            
            connection.setDoOutput(true);
            
            JsonObject body = createRequestBody(graphQL, variables);
            
            postRequest(connection,body);
            
            return getResponse(connection);
            
        } catch (ProtocolException pex) {
            throw new RuntimeException(pex);
        } catch (IOException ex) {
            Assert.fail("Could not open a connection to the test server, is it running ?");
            throw new RuntimeException(ex);
        }
    }
    
    private void addHeaders(HttpURLConnection connection,Map<String, String> httpHeaders){
        // Default headers
        connection.setRequestProperty(HEADER_CONTENT_TYPE, MEDIATYPE_JSON); // default header.
        connection.setRequestProperty(HEADER_ACCEPT, MEDIATYPE_JSON);

        // Provided headers
        if (httpHeaders != null) {
            for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }
    }
    
    private void setTimeouts(HttpURLConnection connection){
        // Set timeouts
        connection.setConnectTimeout(Integer.valueOf(CONNECT_TIMEOUT));
        connection.setReadTimeout(Integer.valueOf(READ_TIMEOUT));
    }
    
    private JsonObject createRequestBody(String graphQL, JsonObject variables){
        // Create the request
        if(variables==null || variables.isEmpty()) {
            variables = Json.createObjectBuilder().build();
        }
        return Json.createObjectBuilder().add(QUERY, graphQL).add(VARIABLES, variables).build();
    }
    
    private void postRequest(HttpURLConnection connection,JsonObject body) throws IOException{
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = body.toString().getBytes(UTF8);
            os.write(input, 0, input.length);           
        }   
    }
    
    private String getResponse(HttpURLConnection connection) throws IOException{
        int status = connection.getResponseCode();
        if(status == 200) {
            // Read the response
            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String jsonResult = response.toString();
                connection.disconnect();
                return jsonResult;
            }
        } else {
            connection.disconnect();
            throw new RuntimeException("Status " + status + " - " + connection.getResponseMessage());
        }
    }

}