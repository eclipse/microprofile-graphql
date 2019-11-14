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
import javax.json.bind.annotation.JsonbProperty;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

/**
 * Scalar testing.
 * This allows basic testing of the scalar mapping as defined in GraphQL Entities-Scalars section.
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@GraphQLApi
public class ScalarTestApi {
    @Query("testScalarsInPojo")
    public ScalarHolder getScalarHolder() {
        ScalarHolder sh = new ScalarHolder();
        sh.setBigDecimalObject(BigDecimal.valueOf(123.123));
        sh.setBigIntegerObject(BigInteger.valueOf(123));
        sh.setBooleanObject(Boolean.valueOf(false));
        sh.setBooleanPrimitive(false);
        sh.setByteObject(new Byte("123"));
        byte b = 123;
        sh.setBytePrimitive(b);
        char c = "c".charAt(0);
        sh.setCharObject(new Character(c));
        sh.setCharPrimitive(c);
        sh.setDateObject(LocalDate.parse("2019-10-23"));
        sh.setDateTimeObject(LocalDateTime.parse("2019-10-23T11:46:34.263"));
        double d = 123.123;
        sh.setDoubleObject(new Double(d));
        sh.setDoublePrimitive(d);
        float f = 123.123F;
        sh.setFloatObject(new Float(f));
        sh.setFloatPrimitive(f);
        sh.setId("123");
        int i = 123;
        sh.setIntObject(new Integer(i));
        sh.setIntPrimitive(i);
        long l = 123L;
        sh.setLongObject(new Long(l));
        sh.setLongPrimitive(l);
        short s = 123;
        sh.setShortObject(new Short(s));
        sh.setShortPrimitive(s);
        sh.setStringObject("123");
        sh.setCharArray("123".toCharArray());
        sh.setTimeObject(LocalTime.parse("11:46:34.263"));
        
        return sh;
    }
 
    
    @Query
    @JsonbProperty("testShortPrimitive")
    public short shortPrimitive(){
        return getScalarHolder().getShortPrimitive();
    }
    @Query
    @Name("testShortObject")
    public Short shortObject(){
        return getScalarHolder().getShortPrimitive();
    }

    @Query
    @JsonbProperty("testIntPrimitive")
    public int intPrimitive(){
        return getScalarHolder().getIntPrimitive();
    }
    
    @Query
    @Name("testIntObject")
    public Integer intObject(){
        return getScalarHolder().getIntObject();
    }

    @Query
    @JsonbProperty("testLongPrimitive")
    public long longPrimitive(){
        return getScalarHolder().getLongPrimitive();
    }
    
    @Query
    @Name("testLongObject")
    public Long longObject(){
        return getScalarHolder().getLongObject();
    }

    @Query
    @JsonbProperty("testFloatPrimitive")
    public float floatPrimitive(){
        return getScalarHolder().getFloatPrimitive();
    }
    
    @Query
    @Name("testFloatObject")
    public Float floatObject(){
        return getScalarHolder().getFloatObject();
    }

    @Query
    @JsonbProperty("testDoublePrimitive")
    public double doublePrimitive(){
        return getScalarHolder().getDoublePrimitive();
    }
    
    @Query
    @Name("testDoubleObject")
    public Double doubleObject(){
        return getScalarHolder().getDoubleObject();
    }

    @Query
    @JsonbProperty("testBooleanPrimitive")
    public boolean booleanPrimitive(){
        return getScalarHolder().isBooleanPrimitive();
    }
    @Query
    @Name("testBooleanObject")
    public Boolean booleanObject(){
        return getScalarHolder().getBooleanObject();
    }

    @Query
    @JsonbProperty("testCharPrimitive")
    public char charPrimitive(){
        return getScalarHolder().getCharPrimitive();
    }
    
    @Query
    @Name("testCharObject")
    public Character charObject(){
        return getScalarHolder().getCharObject();
    }

    @Query
    @JsonbProperty("testCharArray")
    public char[] charArray(){
        return getScalarHolder().getCharArray();
    }
    @Query
    @Name("testStringObject")
    public String stringObject(){
        return getScalarHolder().getStringObject();
    }

    @Query
    @JsonbProperty("testBytePrimitive")
    public byte bytePrimitive(){
        return getScalarHolder().getBytePrimitive();
    }
    @Query
    @Name("testByteObject")
    public Byte byteObject(){
        return getScalarHolder().getByteObject();
    }

    @Query
    @JsonbProperty("testBigIntegerObject")
    public BigInteger bigIntegerObject(){
        return getScalarHolder().getBigIntegerObject();
    }
    
    @Query
    @Name("testBigDecimalObject")
    public BigDecimal bigDecimalObject(){
        return getScalarHolder().getBigDecimalObject();
    }

    @Query
    @JsonbProperty("testDateObject")
    public LocalDate dateObject(){
        return getScalarHolder().getDateObject();
    }
    
    @Query
    @Name("testTimeObject")
    public LocalTime timeObject(){
        return getScalarHolder().getTimeObject();
    }
    
    @Query
    @JsonbProperty("testDateTimeObject")
    public LocalDateTime dateTimeObject(){
        return getScalarHolder().getDateTimeObject();
    }

    @Query @Id
    @Name("testId")
    public String id(){
        return getScalarHolder().getId();
    }   
}