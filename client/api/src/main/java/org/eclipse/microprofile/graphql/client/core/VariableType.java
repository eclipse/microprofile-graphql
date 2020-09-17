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

public interface VariableType extends Buildable {

    /*
       Static factory methods
   */
    // (objectTypeName)
    static VariableType varType(String objectTypeName) {
        VariableType varType = getNewInstanceOf(VariableType.class);

        varType.setName(objectTypeName);
        varType.setNonNull(false);
        varType.setChild(null);

        return varType;
    }

    // (scalarType)
    static VariableType varType(ScalarType scalarType) {
        VariableType varType = getNewInstanceOf(VariableType.class);

        varType.setName(scalarType.toString());
        varType.setNonNull(false);
        varType.setChild(null);

        return varType;
    }

    // nonNull(scalarType)
    static VariableType nonNull(ScalarType scalarType) {
        VariableType varType = getNewInstanceOf(VariableType.class);

        varType.setName(scalarType.toString());
        varType.setNonNull(true);
        varType.setChild(null);

        return varType;
    }

    // nonNull(objectTypeName)
    static VariableType nonNull(String objectTypeName) {
        VariableType varType = getNewInstanceOf(VariableType.class);

        varType.setName(objectTypeName);
        varType.setNonNull(true);
        varType.setChild(null);

        return varType;
    }

    // nonNull(varType)
    static VariableType nonNull(VariableType varType) {
        varType.setNonNull(true);
        return varType;
    }

    // list(scalarType)
    static VariableType list(ScalarType scalarType) {
        VariableType varType = getNewInstanceOf(VariableType.class);

        varType.setName("list(" + scalarType.toString() + ")");
        varType.setNonNull(false);
        varType.setChild(varType(scalarType));

        return varType;
    }

    // list(typeName)
     static VariableType list(String objectTypeName) {
         VariableType varType = getNewInstanceOf(VariableType.class);

         varType.setName("list(" + objectTypeName + ")");
         varType.setNonNull(false);
         varType.setChild(varType(objectTypeName));

         return varType;
    }

    // list(variableType)
    static VariableType list(VariableType childVarType) {
        VariableType varType = getNewInstanceOf(VariableType.class);

        varType.setName("list(" + childVarType.getName() + ")");
        varType.setNonNull(false);
        varType.setChild(childVarType);

        return varType;
    }

    /*
        Getter/Setter
     */
    String getName();

    void setName(String name);

    boolean isNonNull();

    void setNonNull(boolean nonNull);

    VariableType getChild();

    void setChild(VariableType child);

    default boolean isList() {
        return getChild() != null;
    }
}
