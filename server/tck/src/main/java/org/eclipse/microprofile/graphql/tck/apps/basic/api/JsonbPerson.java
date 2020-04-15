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

import javax.json.bind.annotation.JsonbProperty;

/**
 * Type to test {@code @JsonbProperty} on field, getter & setter.
 */
public class JsonbPerson {

    @JsonbProperty("lastName")
    private String surname;

    private String forename;

    private LocalDate birth;


    public JsonbPerson() {
    }

    public JsonbPerson(final String surname, final String forename, final LocalDate birth) {
        this.surname = surname;
        this.forename = forename;
        this.birth = birth;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }


    @JsonbProperty("firstName")
    public String getForename() {
        return forename;
    }

    public void setForename(final String forename) {
        this.forename = forename;
    }


    public LocalDate getBirth() {
        return birth;
    }

    @JsonbProperty("birthDay")
    public void setBirth(final LocalDate birth) {
        this.birth = birth;
    }

}
