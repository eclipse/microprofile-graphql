/*
 * Copyright 2020 Contributors to the Eclipse Foundation
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
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonPatchBuilder;
import javax.json.JsonReader;
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

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This test runs all test defined in the implementation 'src/test/resources' folder 
 * and all test included here in the archive's /tests folder
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class ExecutionDynamicTest extends Arquillian {
    private static final Logger LOG = Logger.getLogger(ExecutionDynamicTest.class.getName());   

    private static final String PATH = "graphql"; // Default. TODO: Test when configured

    private static final String MEDIATYPE_JSON = "application/json";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";
    private static final String EQUALS = "=";
    private static final String AND = "&";
    private static final int CONNECT_TIMEOUT = Integer.getInteger("mp.tck.connect.timeout", 5000);
    private static final int READ_TIMEOUT = Integer.getInteger("mp.tck.read.timeout", 5000);

    private HttpMethod currentHttpMethod = null;
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
    public void testSpecificationPOST(TestData testData){
        runTest(testData, HttpMethod.POST);
    }

    @RunAsClient
    @Test(dataProvider="implementation", dataProviderClass = GraphQLTestDataProvider.class)
    public void testImplementationSpecificPOST(TestData testData) {
        runTest(testData, HttpMethod.POST);
    }

    @RunAsClient
    @Test(dataProvider="specification", dataProviderClass = GraphQLTestDataProvider.class)
    public void testSpecificationGET(TestData testData){
        runTest(testData, HttpMethod.GET);
    }

    @RunAsClient
    @Test(dataProvider="implementation", dataProviderClass = GraphQLTestDataProvider.class)
    public void testImplementationSpecificGET(TestData testData) {
        runTest(testData,HttpMethod.GET);
    }
    
    private void runTest(TestData testData, HttpMethod httpMethod){
        if(testData!=null && isValidInput(testData.getInput())) {
            LOG.info("Running test [" + httpMethod + " :: " + testData.getName() + "]");
            this.currentTestData = testData;
            this.currentHttpMethod = httpMethod;
            Map<String, String> httpHeaders = new HashMap<>();
            if(testData.getHttpHeaders()!=null && !testData.getHttpHeaders().isEmpty()){
                for(String headerName:testData.getHttpHeaders().stringPropertyNames()){
                    String value = testData.getHttpHeaders().getProperty(headerName);
                    LOG.info("setting header " + headerName + " to " + value);
                    httpHeaders.put(headerName, value);
                }
            }

            // loop through each of the input content and pass if at least one passes
            boolean success = false;
            ArrayList<Throwable> listExceptions = new ArrayList<>();
            for (String input : testData.getInput()) {
                try {
                    // Prepare if needed
                    if(isValidInput(testData.getPrepare())){
                        executeHttpRequest(HttpMethod.POST, testData.getPrepare(),testData.getVariables(),httpHeaders);
                    }

                    assertTest(input, testData, httpHeaders, httpMethod);
                    success = true;
                    break;
                } catch (AssertionError ae) {
                    // don't raise assertion failure as this is checked below
                    listExceptions.add(ae);
                }
            }
            if (!success){
                Assert.fail(getErrorMessages(listExceptions));
            }
        }else{
            clearGlobals();
            LOG.warning("Could not find any tests to run...");
        }
    }

    private void assertTest(String input, TestData testData, Map<String, String> httpHeaders, HttpMethod httpMethod){
        // We can only do Queries over GET
        if(testData.isMutation()){
            httpMethod = HttpMethod.POST;
        }

        // Run the actual test and get the response
        HttpResponse httpResponse = executeHttpRequest(httpMethod, input,testData.getVariables(),httpHeaders);
        if(httpResponse.isSuccessful()){
            this.currentOutput = httpResponse.getContent();

            // Validate the output structure
            validateResponseStructure();

            // Cleanup if needed
            if(isValidInput(testData.getCleanup())){
                executeHttpRequest(HttpMethod.POST, testData.getCleanup(),testData.getVariables(),httpHeaders);
            }

            boolean success = false;
            ArrayList<Throwable> listExceptions = new ArrayList<>();

            // Compare to expected output and pass if at least one of the output files match
            for (String output : testData.getOutput()) {
                try {
                    JSONAssert.assertEquals(testData.getFailMessage(), output, this.currentOutput, testData.beStrict());
                    success = true;
                    break;
                } catch (AssertionError ex) {
                    // don't raise assertion failure as this is checked below
                    listExceptions.add(ex);
                } catch (JSONException je) {
                    // indicates some sort of JSON formatting exception
                    Assert.fail(je.getMessage());
                }
            }
            if (!success) {
                Assert.fail(getErrorMessages(listExceptions));
            }
        } else {
            Assert.assertEquals(httpResponse.status, testData.getExpectedHttpStatusCode(),httpResponse.getContent());
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
       if (result!=null && result.getStatus() == ITestResult.FAILURE) {
            PrintUtil.toDisk(this.currentHttpMethod.toString(),
                    this.currentTestData,
                    this.currentOutput,
                    result.getThrowable());
       }
       clearGlobals();
    }

    private void validateResponseStructure(){
        JsonObject received = getJsonObject(new StringReader(this.currentOutput));
        JsonArray errors = received.getJsonArray("errors");
        validatePart(received,"root","data","errors");
        if(errors!=null){
            for(JsonObject errorJsonObject: errors.getValuesAs(JsonObject.class)){
                JsonArray locations = errorJsonObject.getJsonArray("locations");
                validatePart(errorJsonObject, "errors", "message","locations","path","extensions");
                if(locations!=null){
                    for(JsonObject locationJsonObject: locations.getValuesAs(JsonObject.class)){
                        validatePart(locationJsonObject, "errors/locations", "line", "column");
                    }
                }
            }
        }
    }

    private void validatePart(JsonObject jsonObject, String rootName, String... keys){
        JsonPatchBuilder jsonPatchBuilder = Json.createPatchBuilder();
        for(String key:keys){
            jsonPatchBuilder = remove(jsonPatchBuilder,jsonObject,key);
        }

        JsonObject emptyObject = jsonPatchBuilder.build().apply(jsonObject);

        Assert.assertTrue(emptyObject.isEmpty(),
                "Unknown elements in " + rootName + ", only " + Arrays.toString(keys)  + " expected");
    }

    private JsonPatchBuilder remove(JsonPatchBuilder emptyJsonErrorPatchBuilder,JsonObject errorJsonObject, String key){
        if(errorJsonObject.containsKey(key)){
            emptyJsonErrorPatchBuilder = emptyJsonErrorPatchBuilder.remove("/" + key);
        }
        return emptyJsonErrorPatchBuilder;
    }

    private void clearGlobals(){
        this.currentTestData = null;
        this.currentOutput = null;
    }

    private JsonObject getJsonObject(Reader input){
        JsonReader expectedReader = Json.createReader(input);
        return expectedReader.readObject();
    }
    
    private boolean isValidInput(String input){
        return input!=null && !input.isEmpty();
    }

    private boolean isValidInput(Set<String> setInput){
        for (String input : setInput) {
            if (!isValidInput(input)) {
                return false;
            }
        }
        return true;
    }

    private String getErrorMessages(ArrayList<Throwable> listExceptions){
        StringBuilder sb = new StringBuilder();
        listExceptions.forEach(ex -> sb.append(ex.getMessage()).append('\n'));
        return sb.toString();
    }


    private HttpResponse executeHttpRequest(HttpMethod httpMethod, 
            String graphQL, 
            JsonObject variables, 
            Map<String, String> httpHeaders){
        try {
            
            URL url = getURL(httpMethod,graphQL, variables);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.toString());
            setTimeouts(connection);
            addHeaders(connection,httpHeaders);
            connection.setDoOutput(true);

            if(httpMethod.POST.equals(httpMethod)){
                JsonObject body = createRequestBody(graphQL, variables);
                postRequest(connection,body);
            }
            
            int status = connection.getResponseCode();

            if(status == 200) {
                return new HttpResponse(status, getResponse(connection));
            }else{
                return new HttpResponse(status, connection.getResponseMessage());
            }

        } catch (ProtocolException pex) {
            LOG.log(Level.SEVERE, "Caught ProtocolException attempting to " + httpMethod + " an HTTP request", pex);
            throw new RuntimeException(pex);
        } catch (IOException | URISyntaxException ex) {
            LOG.log(Level.SEVERE, "Caught IOException attempting to " + httpMethod + " an HTTP request", ex);
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
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
    }

    private URL getURL(HttpMethod httpMethod,
            String graphQL, 
            JsonObject variables) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException{
        URL url = new URL(this.uri + PATH);
        
        if(HttpMethod.GET.equals(httpMethod)){
            String query = QUERY + EQUALS + URLEncoder.encode(graphQL,"UTF8");
            if(variables!=null && !variables.isEmpty()) {
                query = query + AND + VARIABLES + EQUALS + URLEncoder.encode(variables.toString(),"UTF8");
            }
            
            URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), query, null);
            url = uri.toURL();
        }
        
        return url;
    }
    
    private JsonObject createRequestBody(String graphQL, JsonObject variables){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if(graphQL!=null && !graphQL.isEmpty()) {
            builder.add(QUERY, graphQL);
        }
        if(variables!=null && !variables.isEmpty()) {
            builder.add(VARIABLES, variables);
        }
        return builder.build();
    }

    private void postRequest(HttpURLConnection connection,JsonObject body) throws IOException{
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = body.toString().getBytes(UTF_8);
            os.write(input, 0, input.length);
        }   
    }

    private String getResponse(HttpURLConnection connection) throws IOException{
        int status = connection.getResponseCode();
        if(status == 200) {
            // Read the response
            try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF_8))) {
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
    
    private static class HttpResponse {
        private final int status;
        private final String content;

        public HttpResponse(int status, String content) {
            this.status = status;
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public String getContent() {
            return content;
        }

        public boolean isSuccessful(){
            return status==200;
        }
    }
    
    enum HttpMethod{
        GET,POST
    }
}
