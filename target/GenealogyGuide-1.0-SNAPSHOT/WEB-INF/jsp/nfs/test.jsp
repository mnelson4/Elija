<%-- 
    Document   : test
    Created on : 6-Aug-2012, 5:10:10 PM
    Author     : mnelson4
--%> 

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Test</h1>
        My New Family Search Person ID: ${me.id}
       
        Persons we found in your tree:
        <ol>
        <c:forEach var="person" items="${persons}" >
            <li>Person:${person} ${person.id}</li>
        </c:forEach>    
        </ol>
        
        Properties available from New Family Search:
        <ol>
        <c:forEach var="property" items="${properties}" >
            <li>${property.name}: ${property.value}</li>
        </c:forEach>    
        </ol>
    </body>
</html>
