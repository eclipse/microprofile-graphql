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

package org.eclipse.microprofile.graphql.client.core;

import java.util.List;

import static org.eclipse.microprofile.graphql.client.core.utils.ServiceUtils.getNewInstanceOf;
import static java.util.Arrays.asList;

/**
 * Represents an inline fragment in a GraphQL document. This can be used
 * anywhere where a field is expected (thus it implements `FieldOrFragment`).
 */
public interface InlineFragment extends FieldOrFragment {

    static InlineFragment on(String type, FieldOrFragment... fields) {
        InlineFragment fragment = getNewInstanceOf(InlineFragment.class);

        fragment.setType(type);
        fragment.setFields(asList(fields));

        return fragment;
    }

    String getType();

    void setType(String name);

    List<FieldOrFragment> getFields();

    void setFields(List<FieldOrFragment> fields);

}
