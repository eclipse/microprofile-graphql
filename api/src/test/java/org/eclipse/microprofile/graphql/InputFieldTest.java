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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Simple test mainly as a placeholder for now.
 */
public class InputFieldTest {

    @InputType()
    private static class Starship {
        private String id;
        @InputField("theName")
        @Description("Name of the starship in StarshipInput type")
        private String name;
        private float length;
    }

    @Test
    public void testInputFieldAnnotationOnNameField() throws Exception {
        InputField inputField = Starship.class.getDeclaredField("name").getAnnotationsByType(InputField.class)[0];
        Description description = Starship.class.getDeclaredField("name").getAnnotationsByType(Description.class)[0];
        assertEquals(inputField.value(),"theName");
        assertEquals(description.value(),"Name of the starship in StarshipInput type");
    }
}
