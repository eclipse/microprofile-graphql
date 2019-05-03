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
package org.eclipse.microprofile.graphql.tck.apps.superhero.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.Argument;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import org.eclipse.microprofile.graphql.tck.apps.superhero.db.DuplicateSuperHeroException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroDatabase;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.UnknownTeamException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHero;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Team;

@GraphQLApi
public class HeroFinder {
    @Inject
    HeroDatabase heroDB;

    @Query
    public Collection<SuperHero> allHeroes() {
        return heroDB.getAllHeroes();
    }

    @Query
    public Collection<SuperHero> allHeroesIn(@Argument("city") String city) {
        return allHeroesByFilter(hero -> {
            return city.equals(hero.getPrimaryLocation());});
    }

    @Query
    public Collection<SuperHero> allHeroesWithPower(@Argument("power") String power) {
        return allHeroesByFilter(hero -> {
            return hero.getSuperPowers().contains(power);});
    }

    @Query
    public Collection<SuperHero> allHeroesInTeam(@Argument("team") String teamName) throws UnknownTeamException {
        return heroDB.getTeam(teamName).getMembers();
    }

    @Query
    public Collection<Team> allTeams() {
        return heroDB.getAllTeams();
    }
    @Mutation
    public SuperHero createNewHero(@Argument("hero") SuperHero newHero) throws DuplicateSuperHeroException {
        heroDB.addHero(newHero);
        return heroDB.getHero(newHero.getName());
    }

    @Mutation(description="Adds a hero to the specified team and returns the updated team.")
    public Team addHeroToTeam(@Argument("hero") String heroName,
                              @Argument("team") String teamName)
                              throws UnknownTeamException {

        return heroDB.getTeam(teamName)
                     .addMembers( heroDB.getHero(heroName) );
    }

    @Mutation(description="Removes a hero to the specified team and returns the updated team.")
    public Team removeHeroFromTeam(@Argument("hero") String heroName,
                                   @Argument("team") String teamName)
                                   throws UnknownTeamException {

        return heroDB.getTeam(teamName)
                     .removeMembers( heroDB.getHero(heroName) );
    }

    private Collection<SuperHero> allHeroesByFilter(Predicate<SuperHero> predicate) {
        return heroDB.getAllHeroes()
                     .stream()
                     .filter(predicate)
                     .collect(Collectors.toCollection(ArrayList::new));
    }
}
