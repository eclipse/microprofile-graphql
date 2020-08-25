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
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.eclipse.microprofile.graphql.client.core.VariableType.varType;
import static org.eclipse.microprofile.graphql.client.core.utils.ServiceUtils.getNewInstanceOf;

public interface Variable extends Buildable {
    /*
        Helpers
     */
    static List<Variable> vars(Variable... vars) {
        return asList(vars);
    }

    // (name, scalarType)
    static Variable var(String name, ScalarType scalarType) {
        Variable var = getNewInstanceOf(Variable.class);

        var.setName(name);
        var.setType(varType(scalarType));
        var.setDefaultValue(Optional.empty());

        return var;
    }

    // (name, scalarType, defaultValue)
    static Variable var(String name, ScalarType scalarType, Object defaultValue) {
        Variable var = getNewInstanceOf(Variable.class);

        var.setName(name);
        var.setType(varType(scalarType));
        var.setDefaultValue(Optional.of(defaultValue));

        return var;
    }

    // (name, objectType)
    static Variable var(String name, String objectTypeName) {
        Variable var = getNewInstanceOf(Variable.class);

        var.setName(name);
        var.setType(varType(objectTypeName));
        var.setDefaultValue(Optional.empty());

        return var;
    }

    // (name, objectType, defaultValue)
    static Variable var(String name, String objectTypeName, Object defaultValue) {
        Variable var = getNewInstanceOf(Variable.class);

        var.setName(name);
        var.setType(varType(objectTypeName));
        var.setDefaultValue(Optional.of(defaultValue));

        return var;
    }

    // (name, variableType)
    static Variable var(String name, VariableType varType) {
        Variable var = getNewInstanceOf(Variable.class);

        var.setName(name);
        var.setType(varType);
        var.setDefaultValue(Optional.empty());

        return var;
    }

    // (name, variableType, defaultValue)
    static Variable var(String name, VariableType varType, Object defaultValue) {
        Variable var = getNewInstanceOf(Variable.class);

        var.setName(name);
        var.setType(varType);
        var.setDefaultValue(Optional.of(defaultValue));

        return var;
    }

    /*
        Getter/Setter
    */
    String getName();

    void setName(String name);

    VariableType getType();

    void setType(VariableType value);

    Optional<Object> getDefaultValue();

    void setDefaultValue(Optional<Object> value);
}
