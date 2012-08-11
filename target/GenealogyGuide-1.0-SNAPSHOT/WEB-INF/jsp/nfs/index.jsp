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
        <h1>Hello World! This is in NFS folder! </h1>
        <a href="${authUrl}">Click here to authentication with New Family Search</a>
        <c:forEach var="entry" items="${authUrl}">
            Name:  ${entry.key} <br/>
            Value: ${entry.value} <br/>
        </c:forEach>
        
    </body>
</html>
