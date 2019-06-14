/*
 * Copyright 2019 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.microprofile.graphql.tck.dynamic.init;

import java.util.Properties;
import javax.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple Holder for Test Data sets
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class TestData {
    private String name;
    private String input;
    private Properties httpHeaders;
    private String output;
    private JsonObject variables;
    private String prepare;
    private String cleanup;
    private Properties properties;
    
    public TestData(String name){
        this.name = name;
    }
    
    public boolean shouldIgnore(){
        if(properties!=null && !properties.isEmpty() && properties.containsKey("ignore")){
            String ignore = properties.getProperty("ignore");
            if(ignore!=null && !ignore.isEmpty() && ignore.equalsIgnoreCase("true")){
                return true;
            }
        }
        return false; // default   
    }
    
    
    public Integer getPriority(){
        if(properties!=null && !properties.isEmpty() && properties.containsKey("priority")){
            return Integer.valueOf(properties.getProperty("priority"));
        }
        return 999; // default
    }
}
