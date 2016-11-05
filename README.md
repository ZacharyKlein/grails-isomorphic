# grails-isomorphic
Grails Isomorphic Rendering Plugin

[![Build Status](https://travis-ci.org/ZacharyKlein/grails-isomorphic.svg?branch=master)](https://travis-ci.org/ZacharyKlein/grails-isomorphic)


This plugin provides a single GSP taglib, `<iso:bundle/>` which will allow rendering a Javascript file through Nashorn on the server, as well as loading the same Javascript on the client. This plugin was designed to be used with React, but is not limited to that library.

## Usage
The `<iso:bundle />` tag requires two attributes, `path` and `data`. `path` should be a resource path to a Javascript file. Since for true isomorphic behavior the Javascript needs to be loaded on the client in addition to the server, `/src/main/webapp` is an ideal location (however it will not work if you deploy your app as a JAR file). Otherwise, a `publicPath` attribute is supported to supply an alternate path for the client Javascript file.

Example: 

```
    <iso:bundle path='bundle.js' data={[some: data]}/> <!-- this will load /src/main/webapp/bundle.js for both server and client (because resources in src/main/webapp are made available by default, e.g., static/bundle.js  -->
    
    <iso:bundle path='bundle.js' publicPath='assets/bundle.js' data={[some: data]}/> <!-- this will load /src/main/resources/bundle.js for the server and grails-app/assets/javascripts/bundle.js for the client  -->
```

The `data` attribute takes a map of data to be converted into a JSON object, which can be parsed and used in your Javascript to supply initial data. 

Example: 

`<iso:bundle path='bundle.js' data={[a: 1, b: 2]}/>`

The actual Javascript bundle can contain any valid Javascript code, however it is important to note that Nashorn does not support DOM features, CSS, or other browser-specific APIs. React code using JSX will need to be transpiled via Babel/webpack or some other means in order to be correctly evaluated by Nashorn. 

In addition, the plugin expects that the Javascript bundle will include two top-level "render" functions, one for the browser and one for the server. These functions should take the initial JSON object as a single argument. By default, these functions are expected to be named `renderClient` and `renderServer` - you can customize these names via the `clientRenderFunction` and `serverRenderFunction` attrs on the `iso:bundle` tag.

Here is a simple Javascript bundle that can be evaluated on both the browser and the server, assuming that the `data` attribute was supplied with `[a:1, b:2]`:

```
//bundle.js

if (typeof window !== 'undefined' && typeof document !== 'undefined' && typeof document.createElement === 'function') {
  window.renderClient = (data) => {
    return data.a + data.b;  //will return 3.0
  }
}
else {
  global.renderServer = (data) => {
    let json = JSON.parse(data);
    return json.a + json.b;  //will return 3.0
  };
}
```

When writing isomorphic React, it is important to make sure that the client and server-side rendered code is identically, or React will be unable to reuse the server-side rendered code. For this reason, the plugin will always supply both the client and server render functions with the same data - this should evaluate your React code the same way on both platforms.

Please see this [sample project to see the plugin usage](https://github.com/ZacharyKlein/grails-isomorphic-react) in conjunction with webpack, using the [React profile](https://github.com/grails-profiles/react) for Grails. 