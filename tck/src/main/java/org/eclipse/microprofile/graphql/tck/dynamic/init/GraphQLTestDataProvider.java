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

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.testng.annotations.DataProvider;

/**
 * Provide test data for GraphQL Endpoint from the implementation's /src/test/resources/tests directory
 * and the specification's jar file (in /tests)
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class GraphQLTestDataProvider {
    private static final Logger LOG = Logger.getLogger(GraphQLTestDataProvider.class.getName());
    
    private GraphQLTestDataProvider(){
    }
    
    @DataProvider(name="specification")
    public static Object[][] getSpecificationTestData(){
        List<Path> testFolders = getAllFoldersForSpecification("/tests/");
        List<TestData> testDataList = toListOfTestData(testFolders);
        sort(testDataList);
        return toObjectArray(testDataList);
    }
    
    @DataProvider(name="implementation")
    public static Object[][] getImplementationTestData(){
        List<Path> testFolders = getAllFoldersForImplentation("src/test/resources/tests");
        List<TestData> testDataList = toListOfTestData(testFolders);
        sort(testDataList);
        return toObjectArray(testDataList);
    }
    
    private static List<TestData> toListOfTestData(List<Path> testFolders){
        List<TestData> testDataList = new ArrayList<>();
        for (Path testFolder : testFolders) {
            if(!testFolder.getFileName().toString().startsWith("META-INF")){// Ignore META-INF
                try {
                    testDataList.add(toTestData(testFolder));
                } catch (IOException ioe) {
                    LOG.log(Level.SEVERE, "Could not add test case {0} - {1}", new Object[]{testFolder.getFileName().toString(), ioe.getMessage()});
                }
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
                LOG.log(Level.SEVERE, "Ignoring test [{0}]", testData.getName());
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
    
    private static TestData toTestData(Path folder) throws IOException {
        TestData testData = new TestData(folder.getFileName().toString().replace("/", ""));
        Files.walkFileTree(folder,new HashSet<>(), 1, new FileVisitor<Path>() {
        
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                
                String filename = file.getFileName().toString();
                
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
                            properties.load(Files.newInputStream(file));
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
                            properties.load(Files.newInputStream(file));
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
                        LOG.log(Level.WARNING, "Ignoring unknown file {0}", filename);
                        break;
                }
                
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                LOG.log(Level.SEVERE, "Could not load file {0}[{1}]", new Object[]{file, exc.getMessage()});
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        
        return testData;
    }
    
    private static String getFileContent(Path file) throws IOException {
        return new String(Files.readAllBytes(file));
    }
    
    private static List<Path> getAllFoldersForImplentation(String resourcesDirectoryName) {
        try {
            Path folderPath = Paths.get(resourcesDirectoryName);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath);
            return toListOfPaths(directoryStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }   
    }
    
    private static List<Path> getAllFoldersForSpecification(String resourcesDirectoryName){
        try {
            URL jar = GraphQLTestDataProvider.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarFile = Paths.get(jar.toString().substring("file:".length()));
            FileSystem fs = FileSystems.newFileSystem(jarFile, null);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fs.getPath(resourcesDirectoryName));
            return toListOfPaths(directoryStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }  
    
    private static List<Path> toListOfPaths(DirectoryStream<Path> directoryStream){
        List<Path> files = new ArrayList<>();
        for(Path p: directoryStream){
            if(Files.isDirectory(p)){
                files.add(p);
            }
        }
        return files;
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
