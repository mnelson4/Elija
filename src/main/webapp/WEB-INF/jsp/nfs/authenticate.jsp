<%-- 
    Document   : index.jsp
    Created on : 28-Jul-2012, 2:28:08 PM
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
        <h1>You are required to authenticate with New Family Search</h1>
        <a href="${authUrl}">Click here to authentication with New Family Search</a>
        <i>Note: in the background, we just acquired an oauth access token and oauth token secret from New Family Search. 
            We put the oauth access token in the URL you are about to click on, and store the oauth token secret. We also told New Family Search where to send
        the user after a successful login (the callback url).</i>
    </body>
</html>
