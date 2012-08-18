/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elija.nfs.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.elija.nfs.services.exceptions.NFSOAuthException;

/**
 * The NFSAUthenticator is for authenticating with New Family Search, so that a user may authorize your application to make requests to New Family Search
 * on their behalf.
 * The main output of the NFSOAuthenticator is a New Family Search Session Id, which can be sent along with requests to New Family Search in order to authenticate the request.
 * Example:
   public class NfsController extends MultiActionController{
 *  public ModelAndView authenticate(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String nfsDeveloperKey = "FAKE-7J1Q-GKEV-7DNM-SQ5M-9Q5H-JX3H-CMJK";//getServletContext().getInitParameter("NFSdeveloperKey");
      
        NFSOAuthenticator nfsAuth = new NFSOAuthenticator(nfsDeveloperKey, true);
        this.nfsAuthenticator=nfsAuth;
        this.nfsAuthenticator.getRequestToken("http://localhost:8080/Elija/nfs/authenticate_callback.htm");

        URI authUrl = nfsAuthenticator.getAuthenticationUrl();
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("authUrl", authUrl);
        ModelAndView modelAndView = new ModelAndView("nfs/index", model);//shows a basic page with a <a href="${authUrl}">login to NFS!</a>
        return modelAndView;
    }

*   //after the user logs in on new family search, we've specified they should be sent to this page, where we get their session ID
    public ModelAndView authenticate_callback(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String oauthVerifier=hsr.getParameter("oauth_verifier");
        String oauthToken=hsr.getParameter("oauth_token");
        
        String sessionId=this.nfsAuthenticator.getSessionId(oauthVerifier,oauthToken);
        HashMap<String, Object> model = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView("nfs/index", model);
        return modelAndView;
    }
   }
 * @author Mike Nelson
 * 
 */
public class NFSOAuthenticator {

    private String developerKey;
    private String server;
    private String oauthToken;
    private String oauthSecret;

    public NFSOAuthenticator() {
    }

    public NFSOAuthenticator(String developerKey, Boolean useSandbox) {
        this.developerKey = developerKey;

        if (useSandbox) {
            this.server = "sandbox.familysearch.org/";
        } else {
            this.server = "api.familysearch.org/";
        }
    }

    public void setDeveloperKey(String developerKey) {
        this.developerKey = developerKey;
    }

    public void setUseSandbox(boolean useSandbox) {
        if (useSandbox) {
            this.server = "sandbox.familysearch.org/";
        } else {
            this.server = "api.familysearch.org/";
        }
    }

    /*
     * Get a request_token
     */
    public void getRequestToken(String oauth_callback) throws NFSOAuthException {
        Long timeStamp = (new Date()).getTime() / 1000;//getTime() is timestamp in milliseconds, but we want seconds
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(this.server)
                .setPath("identity/v2/request_token")
                .setParameter("oauth_callback", oauth_callback)
                .setParameter("oauth_consumer_key", this.developerKey)
                .setParameter("oauth_signature_method", "PLAINTEXT")
                .setParameter("oauth_nonce", "99806503068046")
                .setParameter("oauth_version", "1.0")
                .setParameter("oauth_timestamp", "" + timeStamp)
                .setParameter("oauth_signature", "&");
        Map<String, String> parsedResponse = buildRequestAndGetParsedResponse(builder);
        this.oauthToken = parsedResponse.get("oauth_token");
        this.oauthSecret = parsedResponse.get("oauth_token_secret");
    }

    /**
     * After you've called getRequestToken, sent the user to the URL returned by getAuthenticationUrl, call this function
     * to get the final session ID used in makign requests to new family search.
     * Eg, send a request to /familytree/v2/user/?sessionId={resultOfGetSessionId}
     * @param oauthVerifier in querystring added to callback URL
     * @param oauthToken in querystring added ot callback URL
     * @return String sessionId
     * @throws NFSOAuthException 
     */
    public String getSessionId(String oauthVerifier, String oauthToken) throws NFSOAuthException {
        Long timeStamp = (new Date()).getTime() / 1000;//getTime() is timestamp in milliseconds, but we want seconds
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(this.server).setPath("identity/v2/access_token").setParameter("oauth_consumer_key", this.developerKey).setParameter("oauth_signature_method", "PLAINTEXT").setParameter("oauth_nonce", "99806503068046").setParameter("oauth_version", "1.0").setParameter("oauth_timestamp", "" + timeStamp).setParameter("oauth_verifier", oauthVerifier).setParameter("oauth_token", oauthToken).setParameter("oauth_signature", "&" + this.oauthSecret);
        Map<String, String> parsedResponse = buildRequestAndGetParsedResponse(builder);
        return parsedResponse.get("oauth_token");
    }
    private Map<String, String> buildRequestAndGetParsedResponse(URIBuilder uriBuilder) throws NFSOAuthException {
        URI uri;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException ex) {
            Logger.getLogger(NFSOAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
            throw new NFSOAuthException("Problem creating the OAuth request. Somehow the uri we were trying to build had baSd syntax." + ex);
        }
        HttpEntity responseEntity = httpRequest(uri, null);
        if (responseEntity != null) {
            try {
                String output = EntityUtils.toString(responseEntity);
                Map<String, String> outputParsed = getUrlParameters(output);
                return outputParsed;
            } catch (IOException ex) {
                Logger.getLogger(NFSOAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
                throw new NFSOAuthException("Could not parse response from New Family Search." + ex.getMessage());
            }
        }
        throw new NFSOAuthException("Received an empty response from New Family Search!");
    }

    private HttpEntity httpRequest(URI url, HashMap<String, String> postData) {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpContext localContext = new BasicHttpContext();
        HttpGet httpget = new HttpGet(url);
        String uriString = httpget.getURI().toString();
        HttpResponse response;
        try {
            response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            return entity;

        } catch (ClientProtocolException ex) {
            Logger.getLogger(NFSOAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NFSOAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public URI getAuthenticationUrl() throws NFSOAuthException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost(this.server).setPath("identity/v2/authorize").setParameter("oauth_token", this.oauthToken);
        URI uri;
        try {
            uri = builder.build();
            return uri;
        } catch (URISyntaxException ex) {
            Logger.getLogger(NFSOAuthenticator.class.getName()).log(Level.SEVERE, null, ex);
            throw new NFSOAuthException("Problem creating the OAuth authorization URL. Somehow the uri we were trying to build had bad syntax." + ex);
        }
    }

    private static Map<String, String> getUrlParameters(String query) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            String key = URLDecoder.decode(pair[0], "UTF-8");
            String value = "";
            if (pair.length > 1) {
                value = URLDecoder.decode(pair[1], "UTF-8");
            }
            params.put(key, value);
        }
        return params;
    }
}
