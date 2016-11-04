package org.grails.plugins

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(IsomorphicRenderingService)
class IsomorphicRenderingServiceSpec extends Specification {

    void "test server-side rendering"() {
        setup:
        def javascript = """
            global.addition = function(a, b) { 
                return a + b;
            };  
            
            global.renderServer = function(data) { 
                var json = JSON.parse(data); 
                return addition(json.a, json.b); 
            };"""

        def data = [a: 1, b: 2]

        when: "Javascript is rendered"
        def result = service.render((InputStream) new ByteArrayInputStream(javascript.getBytes()), data)

        then: "the expected result is rendered"
        result == "3.0"
    }
}
