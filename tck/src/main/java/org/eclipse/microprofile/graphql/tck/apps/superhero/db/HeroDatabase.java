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
package org.eclipse.microprofile.graphql.tck.apps.superhero.db;

import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHero;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Team;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@ApplicationScoped
public class HeroDatabase {
    private final Map<String,SuperHero> allHeroes = new HashMap<>();
    private final Map<String,Team> allTeams = new HashMap<>();

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String mapJson = getInitalJson();
            addHeroes(jsonb.fromJson(mapJson,
                      new ArrayList<SuperHero>(){}.getClass().getGenericSuperclass()));
        } catch (JsonbException ex) {
            throw new RuntimeException(ex);
        }
    }

    public SuperHero getHero(String name) throws UnknownHeroException {
        SuperHero superHero = allHeroes.get(name);

        if (superHero == null) {
            throw new UnknownHeroException(name);
        }

        return superHero;
    }

    public Team getTeam(String name) throws UnknownTeamException {
        Team team = allTeams.get(name);
        if (team == null) {
            throw new UnknownTeamException(name);
        }
        return team;
    }

    public Collection<SuperHero> getAllHeroes() {
        return allHeroes.values();
    }

    public Collection<Team> getAllTeams() {
        return allTeams.values();
    }

    public int addHeroes(Collection<SuperHero> heroes) {
        int count = 0;
        for (SuperHero hero : heroes) {
            try {
                addHero(hero);
                count++;
            } catch (DuplicateSuperHeroException ex) {
                System.out.println("Already added : " + hero.getName());
            }
        }
        return count;
    }

    public void addHero(SuperHero hero) throws DuplicateSuperHeroException {
        allHeroes.put(hero.getName(), hero);
        List<Team> teams = hero.getTeamAffiliations();
        if (teams != null) {
            ListIterator<Team> iter = teams.listIterator();
            while (iter.hasNext()) {
                Team team = iter.next();
                Team existingTeam = allTeams.get(team.getName());
                if (existingTeam == null) {
                    existingTeam = new Team();
                    existingTeam.setName(team.getName());
                    allTeams.put(team.getName(), existingTeam);
                }
                iter.set(existingTeam);
                List<SuperHero> members = existingTeam.getMembers();
                if (members == null) {
                    members = new ArrayList<>();
                    existingTeam.setMembers(members);
                }
                members.add(hero);
            }
        }
    }

    public SuperHero removeHero(String heroName) {
        SuperHero hero = allHeroes.remove(heroName);
        if (hero == null) {
            return null;
        }
        for (Team team : getAllTeams()) {
            team.removeMembers(hero);
        }
        return hero;
    }

    private static String getInitalJson() {
        return "[" +
            "{" +
             "\"name\":\"Iron Man\"," +
             "\"realName\":\"Tony Stark\"," +
             "\"primaryLocation\":\"Los Angeles, CA\"," +
             "\"superPowers\":[\"wealth\",\"engineering\"]," +
             "\"teamAffiliations\":[{\"name\":\"Avengers\"}]" +
            "}," +
            "{" +
             "\"name\":\"Spider Man\"," +
             "\"realName\":\"Peter Parker\"," +
             "\"primaryLocation\":\"New York, NY\"," +
             "\"superPowers\":[\"Spidey Sense\",\"Wall-Crawling\",\"Super Strength\",\"Web-shooting\"]," +
             "\"teamAffiliations\":[{\"name\":\"Avengers\"}]" +
            "}," +
            "{" +
             "\"name\":\"Starlord\"," +
             "\"realName\":\"Peter Quill\"," +
             "\"primaryLocation\":\"Outer Space\"," +
             "\"superPowers\":[\"Ingenuity\",\"Humor\",\"Dance moves\"]," +
             "\"teamAffiliations\":[{\"name\":\"Guardians of the Galaxy\"}]" +
            "}," +
            "{" +
             "\"name\":\"Wolverine\"," +
             "\"realName\":\"James Howlett, aka Logan\"," +
             "\"primaryLocation\":\"Unknown\"," +
             "\"superPowers\":[\"Regeneritive Healing\",\"Enhanced Reflexes\",\"Adamantium-infused skeleton\",\"Retractable claws\"]," +
             "\"teamAffiliations\":[{\"name\":\"Avengers\"},{\"name\":\"X-Men\"}]" +
            "}" +
           "]";
    }
}
