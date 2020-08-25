/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.graphql.client.tck.helper;

import graphql.parser.InvalidSyntaxException;
import graphql.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertGraphQL {
    public static void assertEquivalentGraphQLRequest(String expectedRequest, String actualRequest) {
        validateRequest(expectedRequest);
        validateRequest(actualRequest);

        /*
        Once requests have been deemed syntactically correct, we can remove some tokens
        to actually be able to compare them without taking into account insignificant differences.
         */
        expectedRequest = unformatRequest(expectedRequest);
        actualRequest = unformatRequest(actualRequest);

        assertEquals(expectedRequest, actualRequest);
    }

    private static void validateRequest(String request) {
        Parser parser = new Parser();
        try {
            parser.parseDocument(request);
        } catch (InvalidSyntaxException e) {
            System.err.println(request);
            throw (e);
        }
    }

    private static String unformatRequest(String request) {
        return request
                .trim()
                .replaceAll("\\s+", "")
                .replaceAll(System.getProperty("line.separator"), "")
                .replaceAll(",", "");
    }

    private AssertGraphQL() {
        // HideUtilityClassConstructor
    }
}
