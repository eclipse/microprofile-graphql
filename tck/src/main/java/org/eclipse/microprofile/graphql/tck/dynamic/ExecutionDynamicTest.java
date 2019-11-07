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

import org.eclipse.microprofile.graphql.tck.dynamic.execution.PrintUtil;
import org.eclipse.microprofile.graphql.tck.dynamic.execution.TestData;
import java.io.BufferedReader;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import org.jboss.shrinkwrap.api.Archive;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.eclipse.microprofile.graphql.tck.dynamic.execution.GraphQLTestDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * This test runs all test defined in the implementation 'src/test/resources' folder 
 * and all test included here in the archive's /tests folder
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class ExecutionDynamicTest extends Arquillian {
    private static final Logger LOG = Logger.getLogger(ExecutionDynamicTest.class.getName());   
    
    private static final String PATH = "graphql"; // Default. TODO: Test when configured
    
    private static final String UTF8 = "utf-8";
    private static final String MEDIATYPE_JSON = "application/json";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";
    
    private TestData currentTestData = null;
    private String currentOutput = null;
    
    @ArquillianResource
    private URI uri;
    
    @Deployment
    public static Archive<?> getDeployment() throws Exception {
        return DeployableUnit.getDeployment("tck-dynamic");
    }
    
    @RunAsClient
    @Test(dataProvider="specification", dataProviderClass = GraphQLTestDataProvider.class)
    public void testSpecification(TestData testData){
        runTest(testData);
    }
    
    @RunAsClient
    @Test(dataProvider="implementation", dataProviderClass = GraphQLTestDataProvider.class)
    public void testImplementationSpecific(TestData testData) throws IOException {
        runTest(testData);
    }

    private void runTest(TestData testData){
        if(testData!=null && isValidInput(testData.getInput())) {
            this.currentTestData = testData;
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
            this.currentOutput = postHTTPRequest(testData.getInput(),testData.getVariables(),httpHeaders);
            
            // Cleanup if needed
            if(isValidInput(testData.getCleanup())){
                postHTTPRequest(testData.getCleanup(),testData.getVariables(),httpHeaders);
            }

            // Compare to expected output
            try{
                JSONAssert.assertEquals(testData.getName(),testData.getOutput(), this.currentOutput, false);
            } catch (JSONException ex) {
                Assert.fail(ex.getMessage());
            }
        }else{
            this.currentTestData = null;
            this.currentOutput = null;
            LOG.warning("Could not find any tests to run...");
        }
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
       if (result.getStatus() == ITestResult.FAILURE) {
            PrintUtil.toDisk(currentTestData,currentOutput,result.getThrowable().getMessage());
       }
       this.currentTestData = null;
       this.currentOutput = null;
    }
    
    private boolean isValidInput(String input){
        return input!=null && !input.isEmpty();
    }
    
    private String postHTTPRequest(String graphQL, JsonObject variables, Map<String, String> httpHeaders){
        try {
            URL url = new URL(this.uri + PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
//        connection.setConnectTimeout(Integer.valueOf(CONNECT_TIMEOUT));
//        connection.setReadTimeout(Integer.valueOf(READ_TIMEOUT));
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
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
                String responseLine;
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