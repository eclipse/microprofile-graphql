/*
 * Copyright 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.graphql.tck.dynamic.execution;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Print the Test data to output
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class PrintUtil {
    private static final Logger LOG = Logger.getLogger(PrintUtil.class.getName());   
    
    private PrintUtil(){
    }
    
    public static void toDisk(String httpMethod, TestData testData, String output, Throwable throwable){
        try{
            String log = toString(httpMethod, testData, output, throwable);
            writeTestFile(httpMethod + "_" + testData.getName(),log);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not save " + httpMethod + " data to target folder " + testData.getName(), ex);
        }
    }
    
    private static String toString(String httpMethod, TestData testData, String output, Throwable throwable){
        try(StringWriter sw = new StringWriter()){
            sw.write("============= " + httpMethod + " :: " + testData.getName() + " =============");
            sw.write("\n\n");
            if(throwable!=null){
                sw.write("errorMessage = " + throwable.getMessage());
            }else{
                sw.write("errorMessage = ");
            }
            sw.write("\n\n");
            sw.write("given input = " + testData.getInput());
            sw.write("\n\n");
            sw.write("variables input = " + prettyJson(testData.getVariables()));
            sw.write("\n\n");
            sw.write("http headers input = " + testData.getHttpHeaders());
            sw.write("\n\n");

            if (testData.getOutput().size() == 1) {
                sw.write("expected output = " + prettyJson(testData.getOutput().iterator().next()));
            } else {
                sw.write("expected output was either of the following = " + String.join("\nOR\n", testData.getOutput()));
            }

            sw.write("\n\n");
            sw.write("received output = " +  prettyJson(output));
            sw.write("\n\n");
            return sw.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static void writeTestFile(String fileName, String data) throws IOException{
        if(data!=null && !data.isEmpty()){
            Path file = Paths.get("target" + FS + fileName + ".log");
            Path createFile = Files.createFile(file);
            try(BufferedWriter writer = Files.newBufferedWriter(createFile, UTF_8)){
                writer.write(data);
            }
        }
    }
    
    private static String prettyJson(String text) {
        if (text == null) {
            return null;
        }
        if(isJson(text)) {
            JsonReader jr = Json.createReader(new StringReader(text));
            JsonObject jobj = jr.readObject();
            return prettyJson(jobj);
        } else {
            return text;
        }
    }

    private static boolean isJson(String text) {
        return text.startsWith("{") || text.startsWith("[");
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
    
    private static final String FS = System.getProperty("file.separator");
}
