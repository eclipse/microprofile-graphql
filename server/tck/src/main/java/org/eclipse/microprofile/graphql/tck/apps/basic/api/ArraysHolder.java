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
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.graphql.DateFormat;
import org.eclipse.microprofile.graphql.NumberFormat;

public class ArraysHolder {

    private Integer[] intObject = {1, 2, 3};

    private List<Integer> intObjectList = Arrays.asList(intObject);

    private int[] intPrimitive = {1, 2, 3};

    @NumberFormat(value = "¤00", locale = "en-ZA")
    private Integer[] formattedIntObject = intObject;

    @NumberFormat(value = "¤00", locale = "en-ZA")
    private List<Integer> formattedIntObjectList = Arrays.asList(intObject);

    @NumberFormat(value = "¤00", locale = "en-ZA")
    private int[] formattedIntPrimitive = intPrimitive;

    private LocalDate[] date = {LocalDate.parse("2006-02-01"), LocalDate.parse("2007-03-02")};

    private List<LocalDate> dateList = Arrays.asList(date);

    @DateFormat(value = "dd.MM.yyyy")
    private LocalDate[] formattedDate = date;

    @DateFormat(value = "dd.MM.yyyy")
    private List<LocalDate> formattedDateList = Arrays.asList(date);

    public Integer[] getIntObject() {
        return intObject;
    }

    public int[] getIntPrimitive() {
        return intPrimitive;
    }

    public Integer[] getFormattedIntObject() {
        return formattedIntObject;
    }

    public int[] getFormattedIntPrimitive() {
        return formattedIntPrimitive;
    }

    public List<Integer> getIntObjectList() {
        return intObjectList;
    }

    public List<Integer> getFormattedIntObjectList() {
        return formattedIntObjectList;
    }

    public LocalDate[] getDate() {
        return date;
    }

    public List<LocalDate> getDateList() {
        return dateList;
    }

    public LocalDate[] getFormattedDate() {
        return formattedDate;
    }

    public List<LocalDate> getFormattedDateList() {
        return formattedDateList;
    }
}
