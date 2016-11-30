/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.grails.plugins

import grails.converters.JSON
import groovy.json.JsonBuilder
import jdk.nashorn.api.scripting.NashornScriptEngine

import javax.script.ScriptEngineManager
import javax.script.ScriptException

class IsomorphicRenderingService {

    private ThreadLocal<NashornScriptEngine> nashorn = new ThreadLocal<NashornScriptEngine>() {
        @Override
        protected NashornScriptEngine initialValue() {
            NashornScriptEngine nashornScriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn")

            try {
                nashornScriptEngine.eval(readResource("nashorn-polyfill.js"))
            } catch (ScriptException e) {
                throw new RuntimeException(e)
            }
            return nashornScriptEngine
        }
    }

    String render(InputStream inputStream, Map data, String function) {
        log.info "Rendering server-side JavaScript with data: $data"
        try {
            nashorn.get().eval(new InputStreamReader(inputStream))

            def json = data as JSON

            Object html = nashorn.get().invokeFunction(function ?: 'renderServer', json)
            return String.valueOf(html)
        }
        catch (Exception e) {
            throw new IllegalStateException("unable to render server-side JavaScript", e)
        }
    }

    private Reader readResource(String path) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)
        return new InputStreamReader(inputStream)
    }

}
