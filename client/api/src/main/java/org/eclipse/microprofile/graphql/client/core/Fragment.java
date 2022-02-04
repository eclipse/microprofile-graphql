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
 * Represents a named fragment definition in a GraphQL document. Such definition consists of a name,
 * target type, and a set of fields.
 */
public interface Fragment extends FragmentOrOperation {
    /*
     * Static factory methods
     */
    static List<Fragment> fragments(Fragment... fragments) {
        return asList(fragments);
    }

    static FragmentBuilder fragment(String name) {
        return new FragmentBuilder(name);
    }

    /*
     * Getter/Setter
     */
    String getName();

    void setName(String name);

    String getTargetType();

    void setTargetType(String name);

    List<FieldOrFragment> getFields();

    void setFields(List<FieldOrFragment> fields);

    class FragmentBuilder {

        private String name;

        private String targetType;

        private List<FieldOrFragment> fields;

        FragmentBuilder(String name) {
            this.name = name;
        }

        public Fragment on(String targetType, FieldOrFragment... fields) {
            this.targetType = targetType;
            this.fields = asList(fields);
            return build();
        }

        Fragment build() {
            Fragment fragment = getNewInstanceOf(Fragment.class);
            fragment.setName(name);
            fragment.setTargetType(targetType);
            fragment.setFields(fields);
            return fragment;
        }
    }
}
