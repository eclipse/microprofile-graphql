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
        ScalarHolder sh = new ScalarHolder();
        sh.setBigDecimalObject(new BigDecimal(123.123));
        sh.setBigIntegerObject(new BigInteger("123"));
        sh.setBooleanObject(new Boolean(false));
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
        sh.setStringPrimitive("123".toCharArray());
        sh.setTimeObject(LocalTime.parse("11:46:34.263"));
        
        return sh;
    }
    
}
