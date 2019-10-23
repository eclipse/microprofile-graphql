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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

/**
 * Scalar testing.
 * This allows basic testing of the scalar mapping as defined in GraphQL Entities-Scalars section.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@GraphQLApi
public class ScalarTestApi {
    @Query
    public ScalarHolder getScalarHolder() {
        return new ScalarHolder(Short.MIN_VALUE, Short.MIN_VALUE, 
                0, Integer.BYTES, 
                0, Long.MIN_VALUE, 
                0, Float.NaN, 
                0, Double.MAX_VALUE, 
                true, Boolean.FALSE, 
                Character.MAX_VALUE, Character.MAX_VALUE, 
                "stringPrimitive".toCharArray(), "stringObject", 
                Byte.MIN_VALUE, Byte.MIN_VALUE, 
                BigInteger.ZERO, BigDecimal.ZERO, 
                LocalDate.MAX, LocalTime.MIDNIGHT, LocalDateTime.MIN, 
                "123");
    }

    class ScalarHolder {
        private short shortPrimitive;
        private Short shortObject;
        
        private int intPrimitive;
        private Integer intObject;
        
        private long longPrimitive;
        private Long longObject;

        private float floatPrimitive;
        private Float floatObject;
        
        private double doublePrimitive;
        private Double doubleObject;
    
        private boolean booleanPrimitive;
        private Boolean booleanObject;
        
        private char charPrimitive;
        private Character charObject;

        private char[] stringPrimitive;
        private String stringObject;
        
        private byte bytePrimitive;
        private Byte byteObject;
    
        private BigInteger bigIntegerObject;
        private BigDecimal bigDecimalObject;
    
        private LocalDate dateObject;
        private LocalTime timeObject;
        private LocalDateTime dateTimeObject;
    
        private String id;
        
        public ScalarHolder() {
            
        }

        public ScalarHolder(short shortPrimitive, Short shortObject, 
                int intPrimitive, Integer intObject, 
                long longPrimitive, Long longObject, 
                float floatPrimitive, Float floatObject, 
                double doublePrimitive, Double doubleObject, 
                boolean booleanPrimitive, Boolean booleanObject, 
                char charPrimitive, Character charObject, 
                char[] stringPrimitive, String stringObject, 
                byte bytePrimitive, Byte byteObject, 
                BigInteger bigIntegerObject, BigDecimal bigDecimalObject, 
                LocalDate dateObject, LocalTime timeObject, LocalDateTime dateTimeObject, 
                String id) {
            this.shortPrimitive = shortPrimitive;
            this.shortObject = shortObject;
            this.intPrimitive = intPrimitive;
            this.intObject = intObject;
            this.longPrimitive = longPrimitive;
            this.longObject = longObject;
            this.floatPrimitive = floatPrimitive;
            this.floatObject = floatObject;
            this.doublePrimitive = doublePrimitive;
            this.doubleObject = doubleObject;
            this.booleanPrimitive = booleanPrimitive;
            this.booleanObject = booleanObject;
            this.charPrimitive = charPrimitive;
            this.charObject = charObject;
            this.stringPrimitive = stringPrimitive;
            this.stringObject = stringObject;
            this.bytePrimitive = bytePrimitive;
            this.byteObject = byteObject;
            this.bigIntegerObject = bigIntegerObject;
            this.bigDecimalObject = bigDecimalObject;
            this.dateObject = dateObject;
            this.timeObject = timeObject;
            this.dateTimeObject = dateTimeObject;
            this.id = id;
        }

        public short getShortPrimitive() {
            return shortPrimitive;
        }

        public void setShortPrimitive(short shortPrimitive) {
            this.shortPrimitive = shortPrimitive;
        }

        public Short getShortObject() {
            return shortObject;
        }

        public void setShortObject(Short shortObject) {
            this.shortObject = shortObject;
        }

        public int getIntPrimitive() {
            return intPrimitive;
        }

        public void setIntPrimitive(int intPrimitive) {
            this.intPrimitive = intPrimitive;
        }

        public Integer getIntObject() {
            return intObject;
        }

        public void setIntObject(Integer intObject) {
            this.intObject = intObject;
        }

        public long getLongPrimitive() {
            return longPrimitive;
        }

        public void setLongPrimitive(long longPrimitive) {
            this.longPrimitive = longPrimitive;
        }

        public Long getLongObject() {
            return longObject;
        }

        public void setLongObject(Long longObject) {
            this.longObject = longObject;
        }

        public float getFloatPrimitive() {
            return floatPrimitive;
        }

        public void setFloatPrimitive(float floatPrimitive) {
            this.floatPrimitive = floatPrimitive;
        }

        public Float getFloatObject() {
            return floatObject;
        }

        public void setFloatObject(Float floatObject) {
            this.floatObject = floatObject;
        }

        public double getDoublePrimitive() {
            return doublePrimitive;
        }

        public void setDoublePrimitive(double doublePrimitive) {
            this.doublePrimitive = doublePrimitive;
        }

        public Double getDoubleObject() {
            return doubleObject;
        }

        public void setDoubleObject(Double doubleObject) {
            this.doubleObject = doubleObject;
        }

        public boolean isBooleanPrimitive() {
            return booleanPrimitive;
        }

        public void setBooleanPrimitive(boolean booleanPrimitive) {
            this.booleanPrimitive = booleanPrimitive;
        }

        public Boolean getBooleanObject() {
            return booleanObject;
        }

        public void setBooleanObject(Boolean booleanObject) {
            this.booleanObject = booleanObject;
        }

        public char getCharPrimitive() {
            return charPrimitive;
        }

        public void setCharPrimitive(char charPrimitive) {
            this.charPrimitive = charPrimitive;
        }

        public Character getCharObject() {
            return charObject;
        }

        public void setCharObject(Character charObject) {
            this.charObject = charObject;
        }

        public char[] getStringPrimitive() {
            return stringPrimitive;
        }

        public void setStringPrimitive(char[] stringPrimitive) {
            this.stringPrimitive = stringPrimitive;
        }

        public String getStringObject() {
            return stringObject;
        }

        public void setStringObject(String stringObject) {
            this.stringObject = stringObject;
        }

        public byte getBytePrimitive() {
            return bytePrimitive;
        }

        public void setBytePrimitive(byte bytePrimitive) {
            this.bytePrimitive = bytePrimitive;
        }

        public Byte getByteObject() {
            return byteObject;
        }

        public void setByteObject(Byte byteObject) {
            this.byteObject = byteObject;
        }

        public BigInteger getBigIntegerObject() {
            return bigIntegerObject;
        }

        public void setBigIntegerObject(BigInteger bigIntegerObject) {
            this.bigIntegerObject = bigIntegerObject;
        }

        public BigDecimal getBigDecimalObject() {
            return bigDecimalObject;
        }

        public void setBigDecimalObject(BigDecimal bigDecimalObject) {
            this.bigDecimalObject = bigDecimalObject;
        }

        public LocalDate getDateObject() {
            return dateObject;
        }

        public void setDateObject(LocalDate dateObject) {
            this.dateObject = dateObject;
        }

        public LocalTime getTimeObject() {
            return timeObject;
        }

        public void setTimeObject(LocalTime timeObject) {
            this.timeObject = timeObject;
        }

        public LocalDateTime getDateTimeObject() {
            return dateTimeObject;
        }

        public void setDateTimeObject(LocalDateTime dateTimeObject) {
            this.dateTimeObject = dateTimeObject;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    
}
