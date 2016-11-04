package org.grails.plugins

import grails.plugins.*

class IsomorphicGrailsPlugin extends Plugin {

    def grailsVersion = "3.1.0 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Grails Isomorphic Rendering Plugin" // Headline display name of the plugin
    def author = "Zachary Klein"
    def authorEmail = "zak@silver-chalice.com"
    def description = '''\
        Provides simple server-side rendering of Javascript with Nashorn 
        '''
    def profiles = ['web']
    def documentation = "https://github.com/ZacharyKlein/grails-isomorphic/blob/master/README.md"
    def license = "APACHE"
    def issueManagement = [ system: "GITHUB", url: "https://github.com/ZacharyKlein/grails-isomorphic/issues" ]
    def scm = [ url: "https://github.com/ZacharyKlein/grails-isomorphic" ]

}
