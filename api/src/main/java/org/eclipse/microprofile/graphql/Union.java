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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.util.Collections;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Union {

    String name();

    String description() default "";

    Class<?>[] possibleTypes() default {};

    Class<? extends PossibleTypeFactory> possibleTypeFactory() default Union.DummyPossibleTypeFactory.class;

    boolean possibleTypeAutoDiscovery() default false;

    String[] scanPackages() default {};

    interface PossibleTypeFactory {
        List<AnnotatedType> getPossibleTypes();
    }

    class DummyPossibleTypeFactory implements Union.PossibleTypeFactory {
        @Override
        public List<AnnotatedType> getPossibleTypes() {
            return Collections.emptyList();
        }
    }
}