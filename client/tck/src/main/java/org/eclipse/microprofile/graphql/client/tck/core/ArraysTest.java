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
package org.eclipse.microprofile.graphql.client.tck.core;

import org.eclipse.microprofile.graphql.client.core.Document;
import org.eclipse.microprofile.graphql.client.tck.helper.AssertGraphQL;
import org.eclipse.microprofile.graphql.client.tck.helper.Utils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;

import static org.eclipse.microprofile.graphql.client.core.Argument.arg;
import static org.eclipse.microprofile.graphql.client.core.Argument.args;
import static org.eclipse.microprofile.graphql.client.core.Document.document;
import static org.eclipse.microprofile.graphql.client.core.Field.field;
import static org.eclipse.microprofile.graphql.client.core.InputObject.inputObject;
import static org.eclipse.microprofile.graphql.client.core.InputObjectField.prop;
import static org.eclipse.microprofile.graphql.client.core.Operation.operation;
import static org.eclipse.microprofile.graphql.client.core.OperationType.QUERY;

public class ArraysTest {

    @Test
    public void arraysTest() throws IOException, URISyntaxException {
        String expectedRequest = Utils.getResourceFileContent("core/arrays.graphql");

        Document document = document(
                operation(QUERY, "arrayHolderQuery",
                        field("arrayHolder",
                                args(
                                        arg("arrayHolder", inputObject(
                                                prop("boolPrimitiveArray", new boolean[]{true, false, true}),
                                                prop("boolObjectArray", new Boolean[]{true, false, true}),

                                                prop("bytePrimitiveArray", new byte[]{0, 2, 3}),
                                                prop("byteObjectArray", new Byte[]{0, 2, 3}),

                                                prop("shortPrimitiveArray", new short[]{78, 789, 645}),
                                                prop("shortObjectArray", new Short[]{78, 789, 645}),

                                                prop("intPrimitiveArray", new int[]{78, 65, 12354}),
                                                prop("intObjectArray", new Integer[]{78, 65, 12354}),

                                                prop("longPrimitiveArray", new long[]{789L, 947894L, 1874448L}),
                                                prop("longObjectArray", new Long[]{789L, 947894L, 1874448L}),

                                                prop("floatPrimitiveArray", new float[]{1567.654f, 8765f, 123789456.1851f}),
                                                prop("floatObjectArray", new Float[]{1567.654f, 8765f, 123789456.1851f}),

                                                prop("doublePrimitiveArray", new double[]{789.3242d, 1815d, 98765421.654897d}),
                                                prop("doubleObjectArray", new Double[]{789.3242d, 1815d, 98765421.654897d}),

                                                prop("bigIntegerArray", new BigInteger[]{BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN}),
                                                prop("bigDecimalArray", new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN}),

                                                prop("charPrimitiveArray", new char[]{'f', 'o', 'o'}),
                                                prop("charObjectArray", new Character[]{'f', 'o', 'o'}),

                                                prop("stringArray", new String[]{"foo", "bar", "baz"})
                                        ))),
                                field("boolPrimitiveArray"),
                                field("boolObjectArray"),

                                field("bytePrimitiveArray"),
                                field("byteObjectArray"),

                                field("shortPrimitiveArray"),
                                field("shortObjectArray"),

                                field("intPrimitiveArray"),
                                field("intObjectArray"),

                                field("longPrimitiveArray"),
                                field("longObjectArray"),

                                field("floatPrimitiveArray"),
                                field("floatObjectArray"),

                                field("doublePrimitiveArray"),
                                field("doubleObjectArray"),

                                field("bigIntegerArray"),
                                field("bigDecimalArray"),

                                field("charPrimitiveArray"),
                                field("charObjectArray"),

                                field("stringArray")
                        )
                )
        );

        String generatedRequest = document.build();
        AssertGraphQL.assertEquivalentGraphQLRequest(expectedRequest, generatedRequest);
    }
}
