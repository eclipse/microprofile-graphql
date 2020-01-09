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
package org.eclipse.microprofile.graphql.tck.apps.basic.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbNumberFormat;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;

/**
 * Plain POJO with scalar fields
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class ScalarHolder {
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

    private char[] charArray;
    private String stringObject;

    private byte bytePrimitive;
    private Byte byteObject;

    private BigInteger bigIntegerObject;
    private BigDecimal bigDecimalObject;
    @JsonbNumberFormat("#0.00")
    private BigDecimal formattedBigDecimalObject;
    
    @DefaultValue("1978-07-03")
    private LocalDate dateObject;
    private LocalTime timeObject;
    private LocalDateTime dateTimeObject;
    
    @Description("This is another date")
    private LocalDate anotherDateObject;
    @Description("This is another time")
    private LocalTime anotherTimeObject;
    @Description("This is another datetime")
    private LocalDateTime anotherDateTimeObject;
    
    @JsonbDateFormat("MM dd yyyy")
    @Description("This is a formatted date")
    private LocalDate formattedDateObject;
    @JsonbDateFormat("hh:mm a")
    @Description("This is a formatted time")
    private LocalTime formattedTimeObject;
    @JsonbDateFormat("MM dd yyyy 'at' hh:mm a")
    @Description("This is a formatted datetime")
    private LocalDateTime formattedDateTimeObject;
    
    @Id
    private String id;
    @Id
    private long longPrimitiveId;
    @Id
    private int intPrimitiveId;
    @Id
    private Long longObjectId;
    @Id
    private Integer integerObjectId;
    @Id
    private UUID uuidId;

    public ScalarHolder() {

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

    public char[] getCharArray() {
        return charArray;
    }

    public void setCharArray(char[] charArray) {
        this.charArray = charArray;
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

    public BigDecimal getFormattedBigDecimalObject() {
        return formattedBigDecimalObject;
    }

    public void setFormattedBigDecimalObject(BigDecimal bigDecimalObject) {
        this.formattedBigDecimalObject = bigDecimalObject;
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

    public LocalDate getAnotherDateObject() {
        return anotherDateObject;
    }

    public void setAnotherDateObject(LocalDate dateObject) {
        this.anotherDateObject = dateObject;
    }

    public LocalTime getAnotherTimeObject() {
        return anotherTimeObject;
    }

    public void setAnotherTimeObject(LocalTime timeObject) {
        this.anotherTimeObject = timeObject;
    }

    public LocalDateTime getAnotherDateTimeObject() {
        return anotherDateTimeObject;
    }

    public void setAnotherDateTimeObject(LocalDateTime dateTimeObject) {
        this.anotherDateTimeObject = dateTimeObject;
    }
    
    public LocalDate getFormattedDateObject() {
        return formattedDateObject;
    }

    public void setFormattedDateObject(LocalDate dateObject) {
        this.formattedDateObject = dateObject;
    }

    public LocalTime getFormattedTimeObject() {
        return formattedTimeObject;
    }

    public void setFormattedTimeObject(LocalTime timeObject) {
        this.formattedTimeObject = timeObject;
    }

    public LocalDateTime getFormattedDateTimeObject() {
        return formattedDateTimeObject;
    }

    public void setFormattedDateTimeObject(LocalDateTime dateTimeObject) {
        this.formattedDateTimeObject = dateTimeObject;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLongPrimitiveId() {
        return longPrimitiveId;
    }

    public void setLongPrimitiveId(long longPrimitiveId) {
        this.longPrimitiveId = longPrimitiveId;
    }

    public int getIntPrimitiveId() {
        return intPrimitiveId;
    }

    public void setIntPrimitiveId(int intPrimitiveId) {
        this.intPrimitiveId = intPrimitiveId;
    }

    public Long getLongObjectId() {
        return longObjectId;
    }

    public void setLongObjectId(Long longObjectId) {
        this.longObjectId = longObjectId;
    }

    public Integer getIntegerObjectId() {
        return integerObjectId;
    }

    public void setIntegerObjectId(Integer integerObjectId) {
        this.integerObjectId = integerObjectId;
    }

    public UUID getUuidId() {
        return uuidId;
    }

    public void setUuidId(UUID uuidId) {
        this.uuidId = uuidId;
    }

}
