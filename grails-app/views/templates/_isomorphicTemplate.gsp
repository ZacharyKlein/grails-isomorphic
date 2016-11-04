<%@ page import="grails.converters.JSON" %>
<script type="text/javascript" src="${publicPath ?: '/static/' + path}"></script>
<script type="text/javascript">${clientRenderFunction ?: 'renderClient'}(<%= data as JSON %>);</script>