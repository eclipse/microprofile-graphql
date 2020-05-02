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
package org.eclipse.microprofile.graphql.tck.dynamic.schema;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the csv-files used for schema validity tests.
 *
 * Uses {@code |} as separator and {@code ´} as escape character.
 *
 * @author Yannick Bröker (ybroeker@techfak.uni-bielefeld.de)
 */
public class CsvParser {

    private CsvParser() {
    }

    public static List<TestData> parse(Path testFile) throws IOException {
        Reader reader = Files.newBufferedReader(testFile);

        try {
            return parse(testFile.getFileName().toString(), reader);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed parsing '" + testFile + "'", e);
        }
    }

    public static List<TestData> parse(String fileName, Reader reader) throws IOException {
        List<TestData> testData = new ArrayList<>();

        String currentHeader = "";

        int partIdx = 0;
        String[] parts = new String[4];

        boolean isHeader = false;
        boolean escaped = false;
        int last = '\n';

        StringBuilder stringBuilder = new StringBuilder();
        for (int c = reader.read(); c != -1; c = reader.read()) {
            if (c == ESCAPE) {
                //start/end of escaping
                escaped = !escaped;
                if (last == ESCAPE) {
                    //double-escape
                    stringBuilder.appendCodePoint(ESCAPE);
                    last = -1;
                    continue;
                }
            } else if (c == HEADER && last == '\n' && !escaped) {
                //start of header, can only happen on new line
                isHeader = true;
            } else {
                if ((c == NEWLINE) && !escaped) {
                    String token = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    //end of header or
                    if (isHeader) {
                        //end of header
                        currentHeader = token;
                        isHeader = false;
                    } else if (partIdx == 3) {
                        parts[partIdx] = token;
                        testData.add(createTestData(fileName, currentHeader, parts));
                        partIdx = 0;
                    }
                } else if (c == SEPARATOR && !escaped && !isHeader) {
                    parts[partIdx] = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    partIdx++;
                } else {
                    stringBuilder.appendCodePoint(c);
                }
            }
            last = c;
        }

        if (partIdx == 3) {
            //clean up remaining
            parts[partIdx] = stringBuilder.toString();
            testData.add(createTestData(fileName, currentHeader, parts));
        }

        return testData;
    }

    private static TestData createTestData(String filename, String header, String[] parts) {
        int count = Integer.parseInt(parts[0].trim());
        TestData testData = new TestData();
        testData.setName(filename);
        testData.setHeader(header.trim());
        testData.setCount(count);
        String snippet = parts[1].trim();
        if (snippet.isEmpty()) {
            snippet = null;
        }
        testData.setSnippetSearchTerm(snippet);

        String containsString = parts[2].trim();
        if (containsString.contains(OR)) {
            String[] containsStrings = containsString.split(OR);
            for (String oneOf : containsStrings) {
                testData.addContainsString(oneOf.trim());
            }
        } else {
            testData.addContainsString(containsString);
        }
        testData.setErrorMessage("(" + count + ") - " + parts[3].trim());

        return testData;
    }

    private static final char ESCAPE = '´';
    private static final char HEADER = '#';
    private static final char SEPARATOR = '|';
    private static final char NEWLINE = '\n';
    private static final String OR = "'OR'";

}
