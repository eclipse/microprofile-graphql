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

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class HeroLocator {
    final Map<String,String> heroLocations = new HashMap<>();

    protected void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        heroLocations.put("Iron Man", "Wachovia");
        heroLocations.put("Spider Man", "Brooklyn");
        heroLocations.put("Starlord", "Xandar");
        heroLocations.put("Wolverine", "New Orleans");
    }

    public String getHeroLocation(String name) {
        return heroLocations.getOrDefault(name, "Whereabouts unknown...");
    }
}
