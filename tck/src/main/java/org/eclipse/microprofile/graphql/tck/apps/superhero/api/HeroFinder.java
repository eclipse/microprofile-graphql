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
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.Argument;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.GraphQLException;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.DuplicateSuperHeroException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroDatabase;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroLocator;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.SidekickDatabase;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.UnknownHeroException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.UnknownSidekickException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.UnknownTeamException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Character;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Item;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Sidekick;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHero;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Team;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.UnknownCharacterException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@GraphQLApi
public class HeroFinder {
    private static final Logger LOG = Logger.getLogger(HeroFinder.class.getName());

    @Inject
    private HeroDatabase heroDB;

    @Inject
    private SidekickDatabase sidekickDB;

    @Inject
    private HeroLocator heroLocator;

    @Query
    public Character character(@Argument("name") String name) throws UnknownCharacterException {
        LOG.info("character invoked");

        try {
            SuperHero superHero = heroDB.getHero(name);
            return superHero;
        } catch (UnknownHeroException e) {
            try {
                Sidekick sidekick = sidekickDB.getSidekick(name);
                return sidekick;
            } catch (UnknownSidekickException ex) {
                throw new UnknownCharacterException(name);
            }
        }
    }

    @Query
    public SuperHero superHero(@Argument("name") String name) throws UnknownHeroException {
        LOG.info("superHero invoked " + name);
        return Optional.ofNullable(heroDB.getHero(name)).orElseThrow(() -> new UnknownHeroException(name));
    }

    @Query
    public Collection<SuperHero> allHeroes() {
        LOG.info("allHeroes invoked");
        return heroDB.getAllHeroes();
    }

    @Query
    public Collection<SuperHero> allHeroesIn(@DefaultValue("New York, NY") @Argument("city") String city) {
        LOG.info("allHeroesIn " + city + " invoked");
        return allHeroesByFilter(hero -> {
            return city.equals(hero.getPrimaryLocation());
        });
    }

    @Query
    public Collection<SuperHero> allHeroesWithPower(@Argument("power") String power) {
        LOG.info("allHeroesWithPower invoked");
        return allHeroesByFilter(hero -> {
            return hero.getSuperPowers().contains(power);
        });
    }

    @Query
    public Collection<SuperHero> allHeroesInTeam(@Argument("team") String teamName) throws UnknownTeamException {
        LOG.info("allHeroesInTeam invoked");
        return heroDB.getTeam(teamName).getMembers();
    }

    @Query
    public Collection<Team> allTeams() {
        LOG.info("allTeams invoked");
        return heroDB.getAllTeams();
    }

    @Mutation
    public SuperHero createNewHero(@Argument("hero") SuperHero newHero) throws DuplicateSuperHeroException, UnknownHeroException {
        LOG.info("createNewHero invoked");
        heroDB.addHero(newHero);
        return heroDB.getHero(newHero.getName());
    }

    @Mutation(description = "Adds a hero to the specified team and returns the updated team.")
    public Team addHeroToTeam(@Argument("hero") String heroName,
                              @Argument("team") String teamName)
            throws UnknownTeamException, UnknownHeroException {

        LOG.info("addHeroToTeam invoked");
        return heroDB.getTeam(teamName)
                .addMembers(heroDB.getHero(heroName));
    }

    @Mutation(description = "Removes a hero to the specified team and returns the updated team.")
    public Team removeHeroFromTeam(@Argument("hero") String heroName,
                                   @Argument("team") String teamName)
            throws UnknownTeamException, UnknownHeroException {
        LOG.info("removeHeroFromTeam invoked");
        return heroDB.getTeam(teamName)
                .removeMembers(heroDB.getHero(heroName));
    }

    @Mutation(description = "Removes a hero... permanently...")
    public Collection<SuperHero> removeHero(@Argument("hero") String heroName) throws UnknownHeroException {
        LOG.info("removeHero invoked");
        if (heroDB.removeHero(heroName) == null) {
            throw new UnknownHeroException(heroName);
        }
        return allHeroes();
    }

    @Mutation(description = "Gives a hero new equipment")
    public SuperHero provisionHero(@Argument("hero") String heroName,
                                   @DefaultValue(Item.CAPE) @Argument("item") Item item)
            throws UnknownHeroException {
        LOG.info("provisionHero invoked");
        SuperHero hero = heroDB.getHero(heroName);
        if (hero == null) {
            throw new UnknownHeroException(heroName);
        }
        hero.getEquipment().add(item);
        return hero;
    }

    @Mutation(description = "Removes equipment from a hero")
    public SuperHero removeItemFromHero(@Argument("hero") String heroName,
                                        @Argument("itemID") long itemID)
            throws UnknownHeroException {
        LOG.info("removeItemFromHero invoked");
        SuperHero hero = heroDB.getHero(heroName);
        if (hero == null) {
            throw new UnknownHeroException(heroName);
        }
        hero.getEquipment().removeIf(i -> {
            return i.getId() == itemID;
        });
        return hero;
    }

    @Mutation(description = "Update an item's powerLevel")
    public Item updateItemPowerLevel(@Argument("itemID") long itemID,
                                     @DefaultValue("5") @Argument("powerLevel") int newLevel) {

        Item item = null;
        for (SuperHero hero : allHeroes()) {
            for (Item i : hero.getEquipment()) {
                if (i.getId() == itemID) {
                    item = i;
                    item.setPowerLevel(newLevel);
                }
            }
        }
        return item;
    }

    @Query
    public String currentLocation(@Source SuperHero hero) throws GraphQLException {
        final String heroName = hero.getName();
        LOG.info("checking current location for: " + heroName);
        return heroLocator.getHeroLocation(heroName)
                          .orElseThrow(()-> { 
                              return new GraphQLException("Cannot find location for " + heroName, 
                                                          GraphQLException.ExceptionType.DataFetchingException);});
    }

    private Collection<SuperHero> allHeroesByFilter(Predicate<SuperHero> predicate) {
        return heroDB.getAllHeroes()
                .stream()
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Mutation
    public Team setRivalTeam(@Argument("teamName") String teamName, @Argument("rivalTeam") Team rivalTeam)
            throws UnknownTeamException {

        LOG.info("setRivalTeam: " + teamName + "'s new rival is: " + (rivalTeam == null ? "null" : rivalTeam.getName()));
        Team team = heroDB.getTeam(teamName);
        team.setRivalTeam(rivalTeam);
        return team;
    }
}
