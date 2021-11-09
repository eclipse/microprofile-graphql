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

package org.eclipse.microprofile.graphql.client;

import java.util.List;
import java.util.Map;

public interface GraphQLError {

    /**
     * Textual description of the error, supplied by the service.
     */
    String getMessage();

    /**
     * List of locations in the request that correspond to this error. Each location is a map that
     * should contain the `line` and `column` items.
     */
    List<Map<String, Integer>> getLocations();

    /**
     * The path to the response field where the error arose. This is represented as an array, where each item is either
     * a string (when it represents a field), or an integer (when it represents an index).
     */
    Object[] getPath();

    /**
     * Contents of the `extensions` field. This is used by GraphQL services to pass extra information about the error.
     */
    Map<String, Object> getExtensions();

    /**
     * Any other fields beyond message, locations, path and extensions. These are discouraged by the spec,
     * but if a GraphQL service adds them, they will appear in this map.
     */
    Map<String, Object> getOtherFields();
}
