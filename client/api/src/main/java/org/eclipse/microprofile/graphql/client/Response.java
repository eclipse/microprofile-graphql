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

import javax.json.JsonObject;
import java.util.List;
import java.util.Map;

public interface Response {

    /**
     * The 'data' object contained in the response.
     * Can be JsonValue.NULL if the response contains an empty field, or `null` if the response
     * does not contain this field at all.
     */
    JsonObject getData();

    /**
     * List of errors contained in this response.
     */
    List<GraphQLError> getErrors();

    /**
     * Transform the contents of the `rootField` from this response into a list of objects
     * of the requested type.
     */
    <T> List<T> getList(Class<T> dataType, String rootField);

    /**
     * Transform the contents of the `rootField` from this response into an object
     * of the requested type.
     */
    <T> T getObject(Class<T> dataType, String rootField);

    /**
     * If this response contains any data, this returns `true`; `false` otherwise.
     */
    boolean hasData();

    /**
     * If this response contains at least one error, this returns `true`; `false` otherwise.
     */
    boolean hasError();

    /**
     * Get transport-specific metadata that came from the server with this response.
     */
    Map<String, List<String>> getTransportMeta();
}
