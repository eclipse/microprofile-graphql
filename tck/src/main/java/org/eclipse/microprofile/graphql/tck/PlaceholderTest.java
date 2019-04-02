/*
 * Copyright 2019 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.microprofile.graphql.tck;

import java.lang.reflect.Method;

import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.tck.apps.placeholder.Character;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class PlaceholderTest extends Arquillian {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, PlaceholderTest.class.getSimpleName()+".war")
            .addPackage(Character.class.getPackage());
    }

    @Test
    public void canFindQueryInCharacterClass() throws Exception {
        Query query = null;
        for (Method m : Character.class.getMethods()) {
            query = m.getAnnotation(Query.class);
            if (query != null) {
                break;
            }
        }
        assertNotNull(query);
    }
}
