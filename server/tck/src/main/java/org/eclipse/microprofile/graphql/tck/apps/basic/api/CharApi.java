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

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

@GraphQLApi
public class CharApi {

    @Query
    public CharHolder charHolder() {
        return new CharHolder();
    }

    public char charPrimitiveInput(@Source CharHolder charHolder, char c) {
        return c;
    }

    public char[] charArrayInput(@Source CharHolder charHolder, char[] cs) {
        return cs;
    }

    public Character charObjectInput(@Source CharHolder charHolder, Character c) {
        return c;
    }

    /*
     * Used to have a "namespace".
     */
    public static class CharHolder {

    }

}
