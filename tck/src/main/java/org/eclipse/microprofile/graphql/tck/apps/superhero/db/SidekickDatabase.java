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

import org.eclipse.microprofile.graphql.tck.apps.superhero.model.Sidekick;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SidekickDatabase {
    private final Map<String, Sidekick> allSidekicks = new HashMap<>();

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        try {
            Jsonb jsonb = JsonbBuilder.create();
            String mapJson = getInitalJson();
            addSidekicks(jsonb.fromJson(mapJson,
                      new ArrayList<Sidekick>(){}.getClass().getGenericSuperclass()));
        } catch (JsonbException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Sidekick getSidekick(String name) throws UnknownSidekickException {
        Sidekick sidekick = allSidekicks.get(name);

        if (sidekick == null) {
            throw new UnknownSidekickException(name);
        }

        return sidekick;
    }

    public Collection<Sidekick> getAllSidekicks() {
        return allSidekicks.values();
    }

    public int addSidekicks(Collection<Sidekick> sidekicks) {
        int count = 0;
        for (Sidekick sidekick : sidekicks) {
            try {
                addSidekick(sidekick);
                count++;
            } catch (DuplicateSidekickException ex) {
                System.out.println("Already added : " + sidekick.getName());
            }
        }
        return count;
    }

    public void addSidekick(Sidekick sidekick) throws DuplicateSidekickException {
        allSidekicks.put(sidekick.getName(), sidekick);
    }

    public Sidekick removeSidekick(String sidekickName) throws UnknownSidekickException {
        Sidekick sidekick = allSidekicks.remove(sidekickName);
        if (sidekick == null) {
            throw new UnknownSidekickException(sidekickName);
        }

        return sidekick;
    }

    private static String getInitalJson() {
        return "[" +
            "{" +
             "\"superHeroSidekicked\":" +
                "{" +
                    "\"name\":\"Iron Man\"" +
                "}," +
             "\"name\":\"James Rhodes\"" +
            "}," +
            "{" +
             "\"superHeroSidekicked\":" +
                "{" +
                "\"name\":\"Spider Man\"" +
                "}," +
             "\"name\":\"Andy Maguire\"" +
            "}" +
           "]";
    }
}
