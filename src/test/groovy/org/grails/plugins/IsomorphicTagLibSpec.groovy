package org.grails.plugins

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(IsomorphicTagLib)
class IsomorphicTagLibSpec extends Specification {

    void "testBundleTag"() {
        setup:
        tagLib.isomorphicRenderingService = Mock(IsomorphicRenderingService) {
            render(_, _, _) >> "3.0"
        }

        expect:
        applyTemplate('<iso:bundle path="${path}" data="${data}" />',
                [path: 'test.js', data: [a: 1, b: 2]]) == """<div id='app'>3.0</div>
<script type="text/javascript" src="/static/test.js"></script>
<script type="text/javascript">renderClient({"a":1,"b":2});</script>"""
    }
}
