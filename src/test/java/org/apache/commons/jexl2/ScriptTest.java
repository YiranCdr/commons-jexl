/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jexl2;

import java.io.File;
import java.net.URL;

/**
 * Tests for Script
 * @since 1.1
 */
public class ScriptTest extends JexlTestCase {
    static final String TEST1 =  "src/test/scripts/test1.jexl";

    // test class for testScriptUpdatesContext
    // making this class private static will cause the test to fail.
    // this is due to unusual code in ClassMap.getAccessibleMethods(Class)
    // that treats non-public classes in a specific way. Why getAccessibleMethods
    // does this is not known yet.
    public static class Tester {
        private String code;
        public String getCode () { 
            return code; 
        }
        public void setCode(String c) {
            code = c;
        }
    }
    /**
     * Create a new test case.
     * @param name case name
     */
    public ScriptTest(String name) {
        super(name);
    }

    /**
     * Test creating a script from a string.
     */
    public void testSimpleScript() throws Exception {
        simpleScript(true);
    }
    public void testLegacySimpleScript() throws Exception {
        simpleScript(false);
    }
    private void simpleScript(boolean jexl) throws Exception {
        String code = "while (x < 10) x = x + 1;";
        Script s = jexl? JEXL.createScript(code) : ScriptFactory.createScript(code);
        JexlContext jc = new JexlContext.Mapped();
        jc.setJexlVariable("x", new Integer(1));
    
        Object o = s.execute(jc);
        assertEquals("Result is wrong", new Integer(10), o);
        assertEquals("getText is wrong", code, s.getText());
    }

    public void testScriptFromFile() throws Exception {
        scriptFromFile(true);
    }
    public void testLegacyScriptFromFile() throws Exception {
        scriptFromFile(false);
    }
    private void scriptFromFile(boolean jexl) throws Exception {
        File testScript = new File(TEST1);
        Script s = jexl? JEXL.createScript(testScript) : ScriptFactory.createScript(testScript);
        JexlContext jc = new JexlContext.Mapped();
        jc.setJexlVariable("out", System.out);
        Object result = s.execute(jc);
        assertNotNull("No result", result);
        assertEquals("Wrong result", new Integer(7), result);
    }

    public void testScriptFromURL() throws Exception {
        scriptFromURL(true);
    }
    public void testLegacyScriptFromURL() throws Exception {
        scriptFromURL(false);
    }
    private void scriptFromURL(boolean jexl) throws Exception {
        URL testUrl = new File("src/test/scripts/test1.jexl").toURI().toURL();
        Script s = jexl? JEXL.createScript(testUrl) : ScriptFactory.createScript(testUrl);
        JexlContext jc = new JexlContext.Mapped();
        jc.setJexlVariable("out", System.out);
        Object result = s.execute(jc);
        assertNotNull("No result", result);
        assertEquals("Wrong result", new Integer(7), result);
    }

    public void testScriptUpdatesContext() throws Exception {
        scriptUpdatesContext(true);
    }
    public void testLegacyScriptUpdatesContext() throws Exception {
        scriptUpdatesContext(false);
    }
    private void scriptUpdatesContext(boolean jexl) throws Exception {
        String jexlCode = "resultat.setCode('OK')";
        Expression e = jexl? JEXL.createExpression(jexlCode) : ExpressionFactory.createExpression(jexlCode);
        Script s = jexl? JEXL.createScript(jexlCode) : ScriptFactory.createScript(jexlCode);

        Tester resultatJexl = new Tester();
        JexlContext jc = new JexlContext.Mapped();
        jc.setJexlVariable("resultat", resultatJexl);

        resultatJexl.setCode("");
        e.evaluate(jc);
        assertEquals("OK", resultatJexl.getCode());
        resultatJexl.setCode("");
        s.execute(jc);
        assertEquals("OK", resultatJexl.getCode());
    }

}