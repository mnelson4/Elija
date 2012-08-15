<%-- 
    Document   : callback
    Created on : 6-Aug-2012, 5:08:42 PM
    Author     : mnelson4
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Authenticated</h1>
        <p><a href="test.htm">Get my Family Tree from New Family Search!</a></p>
        <i>We just received another oauth token and an oauth token verifier in the URL. In the background, we just used them in another request
            to New Family Search in order to receive a session Id. With this session Id, we are now authenticated and can now make family tree requests!
        </i>
    </body>
</html>
