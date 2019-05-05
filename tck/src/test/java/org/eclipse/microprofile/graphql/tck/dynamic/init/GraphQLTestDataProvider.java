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
package org.eclipse.microprofile.graphql.tck.dynamic.init;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.DataProvider;

/**
 * Provide test data for GraphQL Endpoint
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class GraphQLTestDataProvider {
    
    private GraphQLTestDataProvider(){
    }
    
    @DataProvider(name="GraphQLTestDataProvider")
    public static Object[][] getGraphQLTestDataProvider(){
        File[] testFolders = getAllFolders(new File("src/test/resources"));
        List<TestData> testDataList = toList(testFolders);
        sort(testDataList);
        return toObjectArray(testDataList);
    }
    
    private static List<TestData> toList(File[] testFolders){
        List<TestData> testDataList = new ArrayList<>();
        for (int row = 0; row < testFolders.length; row++) {
            File folder = testFolders[row];
            try {
                testDataList.add(getTestData(folder));
            } catch (IOException ioe) {
                log.log(Level.SEVERE, "Could not add test case {0} - {1}", new Object[]{folder.getName(), ioe.getMessage()});
            }
        }
        return testDataList;
    }
    
    private static Object[][] toObjectArray(List<TestData> testDataList){
        Object[][] testParameters = new Object[testDataList.size()][1];
        for (int row = 0; row < testDataList.size(); row++) {
            TestData testData = testDataList.get(row);
            if(!testData.shouldIgnore()){
                testParameters[row][0] = testData;
            } else {
                log.log(Level.SEVERE, "Ignoring test [{0}]", testData.getName());
            }
            
        }
        return testParameters;
    }
    
    private static void sort(List<TestData> testDataList){
        Collections.sort(testDataList, new Comparator<TestData>() {
            @Override
            public int compare(TestData u1, TestData u2) {
                return u1.getPriority().compareTo(u2.getPriority());
            }
        });
    }
    
    private static TestData getTestData(File folder) throws IOException{
        TestData testData = new TestData(folder.getName());
        
        File[] files = folder.listFiles();
        for(File file : files){
            String filename = file.getName();
            switch (filename) {
                case "input.graphql":
                    {
                        String content = getFileContent(file);
                        testData.setInput(content);
                        break;
                    }
                case "httpHeader.properties":
                    {
                        Properties properties = new Properties();
                        properties.load(new FileReader(file));
                        testData.setHttpHeaders(properties);
                        break;
                    }    
                case "output.json":
                    {
                        String content = getFileContent(file);
                        testData.setOutput(content);
                        break;
                    }
                case "variables.json":
                    {
                        String content = getFileContent(file);
                        testData.setVariables(toJsonObject(content));
                        break;
                    }
                case "test.properties":
                    {
                        Properties properties = new Properties();
                        properties.load(new FileReader(file));
                        testData.setProperties(properties);
                        break;
                    }
                case "cleanup.graphql":
                    {
                        String content = getFileContent(file);
                        testData.setCleanup(content);
                        break;
                    }    
                case "prepare.graphql":
                    {
                        String content = getFileContent(file);
                        testData.setPrepare(content);
                        break;
                    }    
                default:
                    log.log(Level.WARNING, "Ignoring unknow file {0}", file.getName());
                    break;
            }
        }
        return testData;
    }
    
    private static String getFileContent(File file) throws FileNotFoundException, IOException{
        try(FileReader fr = new FileReader(file);
            StringWriter sw = new StringWriter()) {
                IOUtils.copy(fr, sw);
                return sw.toString();
            }
    }
    
    private static File[] getAllFolders(File resourcesDirectory){
        File[] folders = resourcesDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return folders;
    }   
    
    private static JsonObject toJsonObject(String jsonString){
        if(jsonString==null || jsonString.isEmpty()) {
            return null;
        }
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    } 
}
