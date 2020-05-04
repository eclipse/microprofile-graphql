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
package org.eclipse.microprofile.graphql.tck.dynamic.schema;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CsvParserTest {

    @Test
    public void shouldHandleDoubleEscape() throws IOException {
        String content = "1| union X | ´´ | Should Handle DoubleEscape\n";

        final List<TestData> testDate = CsvParser.parse("shouldHandleDoubleEscape", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(0), "´");

    }
    @Test
    public void shouldHandleTripleEscape() throws IOException {
        String content = "1| union X | ´Test ´´´ | Should Handle TripleEscape\n";

        final List<TestData> testDate = CsvParser.parse("shouldHandleTripleEscape", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(0), "Test ´");

    }

    @Test
    public void shouldHandleEscaping() throws IOException {
        String content = "1| union X | ´Y | Z´ | Should Handle Escaping\n";

        final List<TestData> testDate = CsvParser.parse("shouldHandleEscaping", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(0), "Y | Z");
        Assert.assertEquals(testDate.get(0).getErrorMessage(), "(1) - Should Handle Escaping");
    }

    @Test
    public void shouldHandleOr() throws IOException {
        String content = "49| type Mutation           |   "
                + "scalarHolder(arg0: ScalarHolderInput): ScalarHolder 'OR' scalarHolder(scalarHolder: ScalarHolderInput): ScalarHolder   "
                + "|   Expecting a Mutation with the set removed from the name, scalarHolder.\n";

        final List<TestData> testDate = CsvParser.parse("shouldHandleEscaping", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().size(), 2);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(0), "scalarHolder(arg0: ScalarHolderInput): ScalarHolder");
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(1), "scalarHolder(scalarHolder: ScalarHolderInput): ScalarHolder");
    }

    @Test
    public void shouldHandleMultilineEscaping() throws IOException {
        String content = "5| type SourceType         |   ´dateInput(\n"
                + "    \"yyyy-MM-dd\"\n"
                + "    input: Date\n"
                + "  ): String´                                  |   Expecting field dateInput with parameter input in SourceType\n";

        final List<TestData> testDate = CsvParser.parse("shouldHandleEscaping", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(0), "dateInput(\n"
                + "    \"yyyy-MM-dd\"\n"
                + "    input: Date\n"
                + "  ): String");
    }

    @Test
    public void shouldIgnoreCommentsInLine() throws IOException {
        String content = "59| type ScalarHolder | \"#0.0 en-GB\" | Missing Number Format description on Output Type\n";

        final List<TestData> testDate = CsvParser.parse("shouldIgnoreCommentsInLine", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getContainsAnyOfString().get(0), "\"#0.0 en-GB\"");
    }

    @Test
    public void shouldHandleComments() throws IOException {
        String content = "# Basic Scalar Types\n"
                + "1| type ScalarHolder | bigDecimalObject: BigDecimal | Expecting a BigDecimal Scalar Type in type ScalarHolder\n";

        final List<TestData> testDate = CsvParser.parse("shouldHandleComments", new StringReader(content));

        Assert.assertEquals(testDate.size(), 1);
        Assert.assertEquals(testDate.get(0).getHeader(), "Basic Scalar Types");
        Assert.assertEquals(testDate.get(0).getCount(), 1);
    }
}
