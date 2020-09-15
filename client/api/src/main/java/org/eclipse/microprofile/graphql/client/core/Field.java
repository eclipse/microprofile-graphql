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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.eclipse.microprofile.graphql.client.core.utils.ServiceUtils.getNewInstanceOf;

public interface Field extends Buildable {
    /*
        Static factory methods
    */
    static List<Field> fields(Field... fields) {
        return asList(fields);
    }

    // (name)
    static Field field(String name) {
        Field field = getNewInstanceOf(Field.class);

        field.setName(name);
        field.setArguments(emptyList());
        field.setFields(emptyList());

        return field;
    }

    // (name, subfields)
    static Field field(String name, Field... fields) {
        Field field = getNewInstanceOf(Field.class);

        field.setName(name);
        field.setArguments(emptyList());
        field.setFields(asList(fields));

        return field;
    }

    // (name, args)
    static Field field(String name, Argument... args) {
        Field field = getNewInstanceOf(Field.class);

        field.setName(name);
        field.setArguments(asList(args));
        field.setFields(emptyList());

        return field;
    }

    // (name, args, subfields)
    static Field field(String name, List<Argument> args, Field... fields) {
        Field field = getNewInstanceOf(Field.class);

        field.setName(name);
        field.setArguments(args);
        field.setFields(asList(fields));

        return field;
    }

    /*
        Getter/Setter
    */
    String getName();

    void setName(String name);

    List<Argument> getArguments();

    void setArguments(List<Argument> arguments);

    List<Field> getFields();

    void setFields(List<Field> fields);
}
