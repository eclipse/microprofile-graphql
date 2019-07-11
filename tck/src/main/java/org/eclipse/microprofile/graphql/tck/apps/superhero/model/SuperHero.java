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

import java.util.List;

public class SuperHero {
    private List<Team> teamAffiliations;
    private List<String> superPowers;
    private String primaryLocation;
    private String name;
    private String realName;

    public SuperHero(){
    }
    
    public SuperHero(List<Team> teamAffiliations, 
                        List<String> superPowers, 
                        String primaryLocation, 
                        String name, 
                        String realName) {
        
        this.teamAffiliations = teamAffiliations;
        this.superPowers = superPowers;
        this.primaryLocation = primaryLocation;
        this.name = name;
        this.realName = realName;
    }

    public List<Team> getTeamAffiliations() {
        return teamAffiliations;
    }

    public List<String> getSuperPowers() {
        return superPowers;
    }

    public String getPrimaryLocation() {
        return primaryLocation;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public void setTeamAffiliations(List<Team> teamAffiliations) {
        this.teamAffiliations = teamAffiliations;
    }

    public void setSuperPowers(List<String> superPowers) {
        this.superPowers = superPowers;
    }

    public void setPrimaryLocation(String primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
