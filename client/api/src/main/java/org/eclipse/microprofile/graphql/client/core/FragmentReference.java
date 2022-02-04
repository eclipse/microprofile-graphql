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


import static org.eclipse.microprofile.graphql.client.core.utils.ServiceUtils.getNewInstanceOf;

/**
 * Represents a reference to a named fragment.
 */
public interface FragmentReference extends FieldOrFragment {

    /**
     * Create a fragment reference by specifying the name of the target fragment.
     * In the resulting document, this will appear as `...FRAGMENTNAME`
     */
    static FragmentReference fragmentRef(String name) {
        FragmentReference ref = getNewInstanceOf(FragmentReference.class);
        ref.setName(name);
        return ref;
    }

    /**
     * Create a fragment reference by providing a built instance of a named fragment.
     * This will actually only use the name of the fragment - in the resulting document,
     * this will appear as `...FRAGMENTNAME`
     */
    static FragmentReference fragmentRef(Fragment fragment) {
        FragmentReference ref = getNewInstanceOf(FragmentReference.class);
        ref.setName(fragment.getName());
        return ref;
    }

    String getName();

    void setName(String name);

}
