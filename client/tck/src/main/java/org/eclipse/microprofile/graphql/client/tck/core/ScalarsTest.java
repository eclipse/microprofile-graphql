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


import org.eclipse.microprofile.graphql.client.tck.helper.AssertGraphQL;
import org.eclipse.microprofile.graphql.client.tck.helper.Utils;
import org.eclipse.microprofile.graphql.client.core.Document;
import org.eclipse.microprofile.graphql.client.core.OperationType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;

import static org.eclipse.microprofile.graphql.client.core.Document.document;
import static org.eclipse.microprofile.graphql.client.core.Field.field;
import static org.eclipse.microprofile.graphql.client.core.InputObject.inputObject;
import static org.eclipse.microprofile.graphql.client.core.InputObjectField.prop;
import static org.eclipse.microprofile.graphql.client.core.Argument.arg;
import static org.eclipse.microprofile.graphql.client.core.Argument.args;
import static org.eclipse.microprofile.graphql.client.core.Operation.operation;

public class ScalarsTest {

    @Test
    public void scalars() throws IOException, URISyntaxException {
        String expectedRequest = Utils.getResourceFileContent("core/scalars.graphql");

        Document document = document(
                operation(OperationType.MUTATION, "scalarHolderMutation",
                        field("scalarHolder",
                                args(
                                        arg("scalarHolder", inputObject(
                                                prop("booleanPrimitive", false),
                                                prop("booleanObject", Boolean.valueOf(true)),

                                                prop("bytePrimitive", Byte.MIN_VALUE),
                                                prop("byteObject", Byte.valueOf(Byte.MAX_VALUE)),

                                                prop("shortPrimitive", Short.MIN_VALUE),
                                                prop("shortObject", Short.valueOf(Short.MAX_VALUE)),

                                                prop("intPrimitive", Integer.MIN_VALUE + 1),
                                                prop("intObject", Integer.valueOf(Integer.MAX_VALUE)),

                                                prop("longPrimitive", Long.MIN_VALUE),
                                                prop("longObject", Long.valueOf(Long.MAX_VALUE)),

                                                prop("floatPrimitive", Float.MIN_VALUE),
                                                prop("floatObject", Float.valueOf(Float.MAX_VALUE)),

                                                prop("doublePrimitive", Double.MIN_VALUE),
                                                prop("doubleObject", Double.valueOf(Double.MAX_VALUE)),

                                                prop("bigInteger", BigInteger.TEN),
                                                prop("bigDecimal", BigDecimal.TEN),

                                                prop("charPrimitive", 'a'),
                                                prop("charObject", Character.valueOf('Z')),

                                                prop("stringObject", "Hello World !")
                                        ))),
                                field("booleanPrimitive"),
                                field("booleanObject"),

                                field("bytePrimitive"),
                                field("byteObject"),

                                field("shortPrimitive"),
                                field("shortObject"),

                                field("intPrimitive"),
                                field("intObject"),

                                field("longPrimitive"),
                                field("longObject"),

                                field("floatPrimitive"),
                                field("floatObject"),

                                field("doublePrimitive"),
                                field("doubleObject"),

                                field("bigInteger"),
                                field("bigDecimal"),

                                field("charPrimitive"),
                                field("charObject"),

                                field("stringObject")
                        )
                )
        );

        String generatedRequest = document.build();
        //System.out.println(generatedRequest);
        AssertGraphQL.assertEquivalentGraphQLRequest(expectedRequest, generatedRequest);
    }
}
