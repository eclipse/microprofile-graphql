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
import static org.eclipse.microprofile.graphql.client.core.utils.ServiceUtils.getNewInstanceOf;

public interface Argument extends Buildable {
    /*
        Static factory methods
     */
    static List<Argument> args(Argument... args) {
        return asList(args);
    }

    // (name, raw value)
    static Argument arg(String name, Object value)  {
        Argument argument = getNewInstanceOf(Argument.class);

        argument.setName(name);
        argument.setValue(value);

        return argument;
    }

    // (name, inputObject)
    static Argument arg(String name, InputObject inputObject) {
        Argument argument = getNewInstanceOf(Argument.class);

        argument.setName(name);
        argument.setValue(inputObject);

        return argument;
    }

    // (name, variable)
    static Argument arg(String name, Variable var) {
        Argument argument = getNewInstanceOf(Argument.class);

        argument.setName(name);
        argument.setValue(var);

        return argument;
    }

    /*
        Getter/Setter
    */
    String getName();

    void setName(String name1);

    Object getValue();

    void setValue(Object value);
}
