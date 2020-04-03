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

import javax.json.bind.annotation.JsonbProperty;

import org.eclipse.microprofile.graphql.Name;

/**
 * Type to test {@code @Name}, example taken from server-spec.
 */
public class Widget {
    @Name("widgetName")
    private String name;
    private double weight;
    private int quantity;
    private float price;

    public Widget() {
    }

    public Widget(final String name, final double weight, final int quantity, final float price) {
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.price = price;
    }

    @JsonbProperty("shippingWeight")
    public double getWeight() {
        return weight;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }

    @Name("cost")
    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Name("qty")
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
