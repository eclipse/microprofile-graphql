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

package org.eclipse.microprofile.graphql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Extends a GraphQL type by adding an externally defined field, effectively enabling a graph to be assembled. <br>
 * <br>
 * The GraphQL type which is extended is the GraphQL type corresponding to the Java type of the annotated method
 * parameter. <br>
 * <br>
 * The added field type can be a scalar or another GraphQL type, it's inferred from the method return type. <br>
 * <br>
 * At runtime, injects the concerned source object (which type is the extended GraphQL type), thus allowing to use
 * fields from it to resolve the added field. <br>
 * <br>
 * Optionally, specifies the name of the added field in the extended GraphQL type. By default, the name of the added
 * field is the name of the method.
 *
 * <br>
 * <br>
 * For example, a user might annotate a method's parameter as such:
 * 
 * <pre>
 * public class CharacterService {
 *      {@literal @}Inject TwitterService twitterService;
 *
 *     {@literal @}Query(value = "tweets")
 *     public List{@literal <}Tweet{@literal >} tweets(
 *                          {@literal @}Source("tweetsForMe") Character character,
 *                          {@literal @}Name("last") int last) {
 *          return twitterService.search(character.getName(), last);
 *     }
 * }
 * </pre>
 * <p>
 * Schema generation of this would result in a stanza such as:
 * 
 * <pre>
 * type Character {
 *    # Other fields ... 
 *    tweetsForMe(last: Int): [Tweet]
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Source {
    /**
     * @return the name of the added type in the extended GraphQL type.
     * @deprecated use `value` instead
     */
    @Deprecated
    String name() default "";

    /**
     * @return the name of the added type in the extended GraphQL type.
     */
    String value() default "";
}