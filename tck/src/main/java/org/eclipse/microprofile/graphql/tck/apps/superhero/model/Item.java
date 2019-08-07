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
package org.eclipse.microprofile.graphql.tck.apps.superhero.model;

import org.eclipse.microprofile.graphql.Id;

public class Item {

    @Id
    private long id;
    private String name;
    private int powerLevel;
    private double height;
    private float weight;
    private boolean supernatural;

    public final static String CAPE =
        "{" +
        "   \"id\": 1000," +
        "   \"name\": \"Cape\","+
        "   \"powerLevel\": 3," +
        "   \"height\": 1.2," +
        "   \"weight\": 0.3," +
        "   \"supernatural\": false" +
        "}";

    public Item(){
    }
    
    public Item(long id, String name, int powerLevel, double height, float weight, boolean supernatural) {
        this.id = id;
        this.name = name;
        this.powerLevel = powerLevel;
        this.height = height;
        this.weight = weight;
        this.supernatural = supernatural;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isSupernatural() {
        return supernatural;
    }

    public void setSupernatural(boolean supernatural) {
        this.supernatural = supernatural;
    }
}