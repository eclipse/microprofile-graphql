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
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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

/**
 * Provide test data for GraphQL Endpoint
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class GraphQLTestDataProvider {

    private static final String[] RESOURCE_FILE_NAMES = new String[]{
        "cleanup.graphql",
        "httpHeader.properties",
        "input.graphql",
        "output.json",
        "prepare.graphql",
        "test.properties",
        "variables.json"
    };
    private GraphQLTestDataProvider() {
    }

    public static File setupTestDataDir() {
        File testDataDir;
        try {
            Path result = Files.createTempDirectory("graphQL-TCK");
            testDataDir = result.toFile();
            log.info("testDataDir = " + testDataDir.getAbsolutePath());
            testDataDir.deleteOnExit();

            // TODO: see if there is a more dynamic approach to discover and copy these resource dirs
            copyResourcesTo("addHeroToTeam", result);
            copyResourcesTo("addHeroToTeamWithVariables", result);
            copyResourcesTo("allAvengers", result);
            copyResourcesTo("allAvengersWithVariables", result);
            copyResourcesTo("allHeroes", result);
            copyResourcesTo("createNewHero", result);
            copyResourcesTo("createNewHeroWithVariables", result);

            return testDataDir;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to extract GraphQL TCK test data files to disk");
        }
    }

    public static Object[][] getGraphQLTestDataProvider(File testDataDir) {
        File[] testFolders = getAllFolders(testDataDir);
        log.log(Level.INFO, "getGraphQLTestDataProvider testFolders = {0}", testFolders);
        List<TestData> testDataList = toList(testFolders);
        sort(testDataList);
        return toObjectArray(testDataList);
    }

    private static List<TestData> toList(File[] testFolders) {
        List<TestData> testDataList = new ArrayList<>();
        for (int row = 0; row < testFolders.length; row++) {
            File folder = testFolders[row];
            try {
                testDataList.add(getTestData(folder));
            } catch (IOException ioe) {
                log.log(Level.SEVERE, "Could not add test case {0} - {1}",
                        new Object[] { folder.getName(), ioe.getMessage() });
            }
        }
        return testDataList;
    }

    private static Object[][] toObjectArray(List<TestData> testDataList) {
        Object[][] testParameters = new Object[testDataList.size()][1];
        try {
            for (int row = 0; row < testDataList.size(); row++) {
                TestData testData = testDataList.get(row);
                //if (!testData.shouldIgnore()) {
                    testParameters[row][0] = testData;
                    log.info("Including test: " + testData.getName());
                //} else {
                //    log.log(Level.SEVERE, "Ignoring test [{0}]", testData.getName());
                //}
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return testParameters;
    }

    private static void sort(List<TestData> testDataList) {
        Collections.sort(testDataList, new Comparator<TestData>() {
            @Override
            public int compare(TestData u1, TestData u2) {
                return u1.getPriority().compareTo(u2.getPriority());
            }
        });
    }

    private static TestData getTestData(File folder) throws IOException {
        TestData testData = new TestData(folder.getName());

        File[] files = folder.listFiles();
        for (File file : files) {
            String filename = file.getName();
            switch (filename) {
            case "input.graphql": {
                String content = getFileContent(file);
                testData.setInput(content);
                break;
            }
            case "httpHeader.properties": {
                Properties properties = new Properties();
                properties.load(new FileReader(file));
                testData.setHttpHeaders(properties);
                break;
            }
            case "output.json": {
                String content = getFileContent(file);
                testData.setOutput(content);
                break;
            }
            case "variables.json": {
                String content = getFileContent(file);
                testData.setVariables(toJsonObject(content));
                break;
            }
            case "test.properties": {
                Properties properties = new Properties();
                properties.load(new FileReader(file));
                testData.setProperties(properties);
                break;
            }
            case "cleanup.graphql": {
                String content = getFileContent(file);
                testData.setCleanup(content);
                break;
            }
            case "prepare.graphql": {
                String content = getFileContent(file);
                testData.setPrepare(content);
                break;
            }
            default:
                log.log(Level.WARNING, "Ignoring unknown file: {0}", file.getAbsolutePath());
                break;
            }
        }
        return testData;
    }

    private static String getFileContent(File file) throws FileNotFoundException, IOException {
        try (FileReader fr = new FileReader(file); StringWriter sw = new StringWriter()) {
            IOUtils.copy(fr, sw);
            return sw.toString();
        }
    }

    private static File[] getAllFolders(File testDataDir) {
        File[] folders = testDataDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return folders;
    }

    private static String[] getResourceFileNames(String resourceDir) {
        String[] fileNames = new String[RESOURCE_FILE_NAMES.length];
        for (int i=0; i<RESOURCE_FILE_NAMES.length; i++) {
            fileNames[i] = resourceDir + "/" + RESOURCE_FILE_NAMES[i];
        }
        return fileNames;
    }

    private static void copyResourcesTo(String resourceDir, Path directory) {
        for (String resource : getResourceFileNames(resourceDir)) {
            String resourceLocation = "/" + resource;
            Path diskLocation = directory.resolve(resource);
            Path parentDir = diskLocation.getParent();

            try (InputStream input = GraphQLTestDataProvider.class.getResourceAsStream(resourceLocation)) {
                if (input == null) {
                    log.info("File not found (could be expected): " + resource);
                    continue;
                }
                File parentDirFile = parentDir.toFile();
                if (!parentDirFile.exists() && !parentDirFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + parentDir.toFile().getAbsolutePath());
                }
                Files.copy(input, diskLocation);
                diskLocation.toFile().deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to copy " + resource + " to " + directory.toAbsolutePath(), e);
            }
        }
    }

    private static JsonObject toJsonObject(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }
}
