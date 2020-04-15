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
package org.eclipse.microprofile.graphql.tck.apps.basic.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.eclipse.microprofile.graphql.DateFormat;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

/**
 * {@code @Source} testing.
 */
@GraphQLApi
public class SourceTestApi {

    @Query
    public SourceType getSource() {
        return new SourceType();
    }

    public String stringInput(@Source SourceType source, String input) {
        return "Input was: " + input;
    }

    public String nonNullStringInput(@Source SourceType source, @NonNull String input) {
        return "Input was: " + input;
    }

    public String namedStringInput(@Source SourceType source, @Name("in") String input) {
        return "Input was: " + input;
    }

    public String defaultStringInput(@Source SourceType source, @DefaultValue("Default value") String input) {
        return "Input was: " + input;
    }

    public String dateInput(@Source SourceType source, @DateFormat(value = "yyyy-MM-dd") LocalDate input) {
        return "Input was: " + (input != null ? input.format(DateTimeFormatter.ISO_DATE) : null);
    }

}
