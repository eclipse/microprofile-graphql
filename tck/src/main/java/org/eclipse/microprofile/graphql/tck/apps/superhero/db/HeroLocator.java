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
package org.eclipse.microprofile.graphql.tck.apps.superhero.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class HeroLocator {
    private final Map<String,String> heroLocations = new HashMap<>();

    protected void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        heroLocations.put("Iron Man", "Wachovia");
        heroLocations.put("Spider Man", "Brooklyn");
        heroLocations.put("Starlord", "Xandar");
        heroLocations.put("Wolverine", "New Orleans");
    }

    public Optional<String> getHeroLocation(String name) {
        return Optional.ofNullable(heroLocations.get(name));
    }
}
