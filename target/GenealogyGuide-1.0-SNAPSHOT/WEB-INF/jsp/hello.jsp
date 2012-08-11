<%-- 
    Document   : helloView
    Created on : 18-Jul-2012, 10:28:35 PM
    Author     : mnelson4
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hello</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
    <script>
        var nfsKey="WCQY-7J1Q-GKVV-7DNM-SQ5M-9Q5H-JX3H-CMJK";
        var server = "https://sandbox.familysearch.org";
        
        var requestUrl = "/identity/v2/request_token"; 
        var authorizeUrl = "/identity/v2/authorize";
        var accessUrl = "/identity/v2/access_token";
        //check session for nfs session id. if not present, redirect to login page
        (function($){
            
            var nfsSessionId=readCookie('nfsSessionId');
            if(nfsSessionId==null){
                window.location=
            }
            //alert("nfs session id!");
            //createCookie('nfsSessionId','value1',10);
        })(jQuery);
        //if valid nfs session present, create a NFS requestor object
        
        //if if valid, start requesting
        
        
        
        
        /**
         * cookie management
         */
        function createCookie(name,value,days) {
            if (days) {
                var date = new Date();
                date.setTime(date.getTime()+(days*24*60*60*1000));
                var expires = "; expires="+date.toGMTString();
            }
            else var expires = "";
            document.cookie = name+"="+value+expires+"; path=/";
        }
        function readCookie(name) {
            var nameEQ = name + "=";
            var ca = document.cookie.split(';');
            for(var i=0;i < ca.length;i++) {
                var c = ca[i];
                while (c.charAt(0)==' ') c = c.substring(1,c.length);
                if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
            }
            return null;
        }
        function eraseCookie(name) {
            createCookie(name,"",-1);
        }
        
    </script>
</head>
<body>
    <h1>${helloMessage}</h1>
</body>
