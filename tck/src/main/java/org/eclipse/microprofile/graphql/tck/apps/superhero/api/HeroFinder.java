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

import org.eclipse.microprofile.graphql.Argument;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.DuplicateSuperHeroException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroDatabase;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.HeroLocator;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.UnknownHeroException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.db.UnknownTeamException;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Item;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHero;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHeroError;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.SuperHeroOrError;
import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Team;

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
    HeroLocator heroLocator;

    @Query
    public SuperHeroOrError superHero(@Argument("name") String name) {
        LOG.info("superHero invoked");

        try {
            return heroDB.getHero(name);
        } catch (UnknownHeroException e) {
            return new SuperHeroError(101);
        }
    }

    @Query
    public Collection<SuperHero> allHeroes() {
        LOG.info("allHeroes invoked");
        return heroDB.getAllHeroes();
    }

    @Query
    public Collection<SuperHero> allHeroesIn(@Argument("city") String city) {
        LOG.info("allHeroesIn invoked");
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
                                   @Argument("item") Item item)
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

    @Query
    public String currentLocation(@Source SuperHero hero) {
        LOG.info("checking current location for: " + hero.getName());
        return heroLocator.getHeroLocation(hero.getName());
    }

    private Collection<SuperHero> allHeroesByFilter(Predicate<SuperHero> predicate) {
        return heroDB.getAllHeroes()
                .stream()
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
