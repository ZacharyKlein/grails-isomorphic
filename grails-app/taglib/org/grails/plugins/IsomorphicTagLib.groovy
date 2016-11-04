package org.grails.plugins


class IsomorphicTagLib {

    static namespace = "iso"
    static defaultEncodeAs = [taglib:'none']

    def isomorphicRenderingService

    /*
    * bundle - tag to render JS bundle server-side and load the result + client-side JS bundle
    * @attrs.path (required) = path to server-side bundle
    * @attrs.data = map to be converted to JSON for initial state
    * @attrs.element = div id for app
    * @attrs.publicPath = path to client-side bundle (defaults to /static/${path})
    * @attrs.clientRenderFunction = name of render function for client bundle (defaults to 'renderClient')
    * */
    def bundle = { attrs ->
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
