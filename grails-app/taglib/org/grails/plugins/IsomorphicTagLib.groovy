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


class IsomorphicTagLib {

    static namespace = "iso"
    static defaultEncodeAs = [taglib:'none']

    def isomorphicRenderingService

    /*
    * javascript - tag to render a JavaScript file server-side and output the result
    * @attrs.path (required) = path to JavaScript file
    * @attrs.data = map to be converted to JSON for initial state
    * @attrs.element = div id for app
    * @attrs.publicPath = path to client-side JavaScript file (defaults to /static/${path})
    * @attrs.clientRenderFunction = name of render function for client JavaScript (defaults to 'renderClient')
    * */
    def javascript = { attrs ->
        if(attrs.path) {

            String content = isomorphicRenderingService.render(
                    (InputStream) session.servletContext.getResourceAsStream("${attrs.path}"),
                    (Map) attrs.data,
                    attrs.serverRenderFunction)

            if(content) {
                out << "<div id='${attrs.element ?: 'app'}'>"
                out <<  content
                out << "</div>"
            }

            out << render(template: '/templates/isomorphicTemplate',
                    model: [path: attrs.path,
                            publicPath: attrs.publicPath,
                            data: attrs.data,
                            clientRenderFunction: attrs.clientRenderFunction])
        }
    }

}
