/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
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

package org.eclipse.microprofile.graphql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps of the annotated Java class to a GraphQL type.
 * <br><br>
 * For example, a user might annotate a class as such:
 * <pre>
 * {@literal @}Type(value = "Starship", description = "A starship in StarWars")
 * public class Starship {
 *     private String id;
 *     private String name;
 *     private float length;
 *
 *     // getters/setters...
 * }
 * </pre>
 *
 * Schema generation of this would result in a stanza such as:
 * <pre>
 * # A starship in StarWars
 * type Starship {
 *   id: String
 *   name: String
 *   length: Float
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Type {

    /**
     * @return the name of the GraphQL type.
     */
    String value() default "";

    /**
     * @return the textual description of the GraphQL type to be included as a comment in the schema.
     */
    String description() default "";
}
