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

import static org.eclipse.microprofile.graphql.client.core.OperationType.QUERY;
import static org.eclipse.microprofile.graphql.client.core.utils.ServiceUtils.getNewInstanceOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public interface Operation extends FragmentOrOperation {
    /*
        Helpers
     */
    static List<Operation> operations(Operation... operations) {
        return asList(operations);
    }

    // (fields)
    static Operation operation(FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(QUERY);
        operation.setName("");
        operation.setVariables(emptyList());
        operation.setFields(asList(fields));

        return operation;
    }

    // (vars, fields)
    static Operation operation(List<Variable> vars, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(QUERY);
        operation.setName("");
        operation.setVariables(vars);
        operation.setFields(asList(fields));

        return operation;
    }

    // (type, fields)
    static Operation operation(OperationType type, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(type);
        operation.setName("");
        operation.setVariables(emptyList());
        operation.setFields(asList(fields));

        return operation;
    }

    // (type, vars, fields)
    static Operation operation(OperationType type, List<Variable> vars, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(type);
        operation.setName("");
        operation.setVariables(vars);
        operation.setFields(asList(fields));

        return operation;
    }

    // (name, fields)
    static Operation operation(String name, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(QUERY);
        operation.setName(name);
        operation.setVariables(emptyList());
        operation.setFields(asList(fields));

        return operation;
    }

    // (type, name, fields)
    static Operation operation(OperationType type, String name, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(type);
        operation.setName(name);
        operation.setVariables(emptyList());
        operation.setFields(asList(fields));

        return operation;
    }

    // (name, vars, fields)
    static Operation operation(String name, List<Variable> vars, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(QUERY);
        operation.setName(name);
        operation.setVariables(vars);
        operation.setFields(asList(fields));

        return operation;
    }

    // (type, name, vars, fields)
    static Operation operation(OperationType type, String name, List<Variable> vars, FieldOrFragment... fields) {
        Operation operation = getNewInstanceOf(Operation.class);

        operation.setType(type);
        operation.setName(name);
        operation.setVariables(vars);
        operation.setFields(asList(fields));

        return operation;
    }

    /*
     * Getter/Setter
     */
    OperationType getType();

    void setType(OperationType type);

    String getName();

    void setName(String name);

    List<Variable> getVariables();

    void setVariables(List<Variable> vars);

    List<FieldOrFragment> getFields();

    void setFields(List<FieldOrFragment> fields);
}
