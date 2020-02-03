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
package org.eclipse.microprofile.graphql.tck.apps.superhero.model;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import javax.json.bind.annotation.JsonbDateFormat;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Ignore;
import org.eclipse.microprofile.graphql.NonNull;

@Description("Something of use to a super hero")
public class Item {

    @Id
    private long id;
    @NonNull
    private String name;
    private String description;
    private int powerLevel;
    private double height;
    private float weight;
    @DefaultValue("false")
    private boolean supernatural;
    @Ignore
    private boolean invisible;
    private Collection<SuperHero> canWield;
    private boolean artificialIntelligenceRating;

    @JsonbDateFormat(value = "dd MMMM yyyy 'at' HH:mm 'in' VV",locale = "en_ZA")
    private ZonedDateTime dateCreated;
    
    @JsonbDateFormat(value = "dd MMM yyyy 'at' HH:mm 'in zone' Z",locale = "en_ZA")
    private OffsetDateTime dateLastUsed;
    
    public final static String CAPE =
        "{" +
        "   \"id\": 1000," +
        "   \"name\": \"Cape\","+
        "   \"powerLevel\": 3," +
        "   \"height\": 1.2," +
        "   \"weight\": 0.3," +
        "   \"supernatural\": false," +
        "   \"dateCreated\": \"19 February 1900 at 12:00 in Africa/Johannesburg\"," +
        "   \"dateLastUsed\": \"29 Jan 2020 at 09:45 in zone +0200\"" +
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

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Ignore
    public Collection<SuperHero> getCanWield() {
        return canWield;
    }

    public void setCanWield(Collection<SuperHero> canWield) {
        this.canWield = canWield;
    }

    public boolean isArtificialIntelligenceRating() {
        return artificialIntelligenceRating;
    }

    @Ignore
    public void setArtificialIntelligenceRating(boolean artificialIntelligenceRating) {
        this.artificialIntelligenceRating = artificialIntelligenceRating;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @DefaultValue("An unidentified item")
    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getDateLastUsed() {
        return dateLastUsed;
    }

    public void setDateLastUsed(OffsetDateTime dateLastUsed) {
        this.dateLastUsed = dateLastUsed;
    }

    @Override
    public String toString() {
        return "Item{" 
                + "id=" + id 
                + ", name=" + name 
                + ", description=" + description 
                + ", powerLevel=" + powerLevel 
                + ", height=" + height 
                + ", weight=" + weight 
                + ", supernatural=" + supernatural 
                + ", invisible=" + invisible 
                + ", canWield=" + canWield 
                + ", artificialIntelligenceRating=" + artificialIntelligenceRating + '}';
    }
}