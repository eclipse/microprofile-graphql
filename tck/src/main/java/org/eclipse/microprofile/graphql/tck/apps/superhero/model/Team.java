/*
 * Copyright (c) 2020, 2021 Contributors to the Eclipse Foundation
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

import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.graphql.NonNull;

import jakarta.json.bind.annotation.JsonbDateFormat;

public class Team {

    private String name;
    private List<SuperHero> members;
    private Team rivalTeam;

    @JsonbDateFormat(value = "HH:mm 'offset' Z", locale = "en-ZA")
    private OffsetTime dailyStandupMeeting;

    public Team() {

    }

    public Team(String name, List<SuperHero> members) {
        this.name = name;
        this.members = members;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public List<@NonNull SuperHero> getMembers() {
        return members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<SuperHero> members) {
        this.members = members;
    }

    public Team addMembers(SuperHero... heroes) {
        if (members == null) {
            members = new ArrayList<>();
        }
        for (SuperHero hero : heroes) {
            members.add(hero);
        }
        return this;
    }
    public Team removeMembers(SuperHero... heroes) {
        if (members != null) {
            for (SuperHero hero : heroes) {
                members.remove(hero);
            }
        }
        return this;
    }

    public Team getRivalTeam() {
        return rivalTeam;
    }

    public void setRivalTeam(Team rivalTeam) {
        this.rivalTeam = rivalTeam;
    }

    public OffsetTime getDailyStandupMeeting() {
        return dailyStandupMeeting;
    }

    public void setDailyStandupMeeting(OffsetTime dailyStandupMeeting) {
        this.dailyStandupMeeting = dailyStandupMeeting;
    }

    @Override
    public String toString() {
        return "Team{"
                + "name=" + name
                + ", members=" + members;
    }
}
