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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import lombok.extern.java.Log;

/**
 * Print the Test data to output
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
public class PrintUtil {
    
    private PrintUtil(){
    }
    
    public static void print(TestData testData,String output){
        StringWriter sw = new StringWriter();
        sw.write("============= " + testData.getName() + " =============");
        sw.write("\n\n");
        sw.write("given input = " + testData.getInput());
        sw.write("\n\n");
        sw.write("variables input = " + prettyJson(testData.getVariables()));
        sw.write("\n\n");
        sw.write("http headers input = " + testData.getHttpHeaders());
        sw.write("\n\n");
        sw.write("expected output = " + prettyJson(testData.getOutput()));
        sw.write("\n\n");
        sw.write("received output = " +  prettyJson(output));
        sw.write("\n\n");
        log.info(sw.toString());
    }
    
    private static String prettyJson(String json) {
        JsonReader jr = Json.createReader(new StringReader(json));
        JsonObject jobj = jr.readObject();
        return prettyJson(jobj);
    }
    
    private static String prettyJson(JsonObject jsonObject) {
        if(jsonObject!=null){
            StringWriter sw = new StringWriter();
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);

            JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
            try (JsonWriter jsonWriter = writerFactory.createWriter(sw)) {
                jsonWriter.writeObject(jsonObject);
            }
            return sw.toString();
        }
        return null;
    }
}
