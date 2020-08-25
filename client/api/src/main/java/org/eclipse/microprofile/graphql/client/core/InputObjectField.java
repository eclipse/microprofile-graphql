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

public interface InputObjectField extends Buildable {

    /*
        Static factory methods
    */
    // (name, value)
    static InputObjectField prop(String name, Object value) {
        InputObjectField inputObjectField = getNewInstanceOf(InputObjectField.class);

        inputObjectField.setName(name);
        inputObjectField.setValue(value);

        return inputObjectField;
    }

    // (name, variable)
    static InputObjectField prop(String name, Variable var) {
        InputObjectField inputObjectField = getNewInstanceOf(InputObjectField.class);

        inputObjectField.setName(name);
        inputObjectField.setValue(var);

        return inputObjectField;
    }

    /*
        Getter/Setter
    */
    String getName();

    void setName(String name);

    Object getValue();

    void setValue(Object value);
}
