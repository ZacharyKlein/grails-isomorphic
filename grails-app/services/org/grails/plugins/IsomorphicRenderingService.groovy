package org.grails.plugins

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
        log.info "Rendering React with data: $data"
        try {
            nashorn.get().eval(new InputStreamReader(inputStream))

            Object html = nashorn.get().invokeFunction(function ?: 'renderServer', new JsonBuilder(data).toString())
            return String.valueOf(html)
        }
        catch (Exception e) {
            throw new IllegalStateException("unable to render React component", e)
        }
    }

    private Reader readResource(String path) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)
        return new InputStreamReader(inputStream)
    }

}
