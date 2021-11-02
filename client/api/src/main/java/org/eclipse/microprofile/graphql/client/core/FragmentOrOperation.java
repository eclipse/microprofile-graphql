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

/**
 * Represents a node in a GraphQL document that can contain either an operation (query/mutation/subscription),
 * or a definition of a named fragment. On the top level, a GraphQL document basically consists of a list
 * of these nodes.
 */
public interface FragmentOrOperation extends Buildable {
}
