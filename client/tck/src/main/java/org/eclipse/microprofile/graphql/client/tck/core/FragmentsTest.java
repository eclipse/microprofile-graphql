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

import static org.eclipse.microprofile.graphql.client.core.Document.document;
import static org.eclipse.microprofile.graphql.client.core.Field.field;
import static org.eclipse.microprofile.graphql.client.core.Fragment.fragment;
import static org.eclipse.microprofile.graphql.client.core.FragmentReference.fragmentRef;
import static org.eclipse.microprofile.graphql.client.core.Operation.operation;
import static org.eclipse.microprofile.graphql.client.core.OperationType.QUERY;

public class FragmentsTest {

    @Test
    public void fragmentTest() {
        String expectedRequest = Utils.getResourceFileContent("core/fragments.graphql");

        Document document = document(
                operation(QUERY,
                        field("people",
                                fragmentRef("sensitiveFields"))),
                fragment("sensitiveFields").on("Person",
                        field("age"),
                        field("religion")));

        String generatedRequest = document.build();
        AssertGraphQL.assertEquivalentGraphQLRequest(expectedRequest, generatedRequest);
    }
}
