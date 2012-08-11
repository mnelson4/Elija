/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elija.nfs.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.springframework.validation.BindException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.elija.nfs.services.NFSAPIClient;
import org.elija.nfs.services.NFSOAuthenticator;
import org.familysearch.ws.client.familytree.v2.schema.FamilyTree;
import org.familysearch.ws.client.familytree.v2.schema.Person;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author mnelson4
 */
public class NfsController extends MultiActionController {

    NFSAPIClient nfsClient;
    NFSOAuthenticator nfsAuthenticator;

    public void setNFSAuthenticator(NFSOAuthenticator nfsAuthenticator) {
        this.nfsAuthenticator = nfsAuthenticator;
    }
    public void setNFSAPIClient(NFSAPIClient nfsClient) {
        this.nfsClient = nfsClient;
    }

    
    public NfsController() {
        //Initialize controller properties here or 
        //in the Web Application Context
        //setCommandClass(Name.class);
//        setCommandName("name");
//        setSuccessView("hello");
//        setFormView("name");
    }

    public ModelAndView index(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
        ModelAndView model = new ModelAndView("nfs/index");
        return model;
    }
    
    public ModelAndView getPedigree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
        
       
       
       
        HashMap<String, Object> model = new HashMap<String, Object>();
        //model.put("me",me);
        ModelAndView modelAndView = new ModelAndView("nfs/test", model);
        return modelAndView;
    }

    public ModelAndView authenticate(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        //String nfsDeveloperKey = "FAKE-7J1Q-GKEV-7DNM-SQ5M-9Q5H-JX3H-CMJK";//getServletContext().getInitParameter("NFSdeveloperKey");
      
        //NFSOAuthenticator nfsAuth = new NFSOAuthenticator(nfsDeveloperKey, true);
        //this.nfsAuthenticator=nfsAuth;
        this.nfsAuthenticator.getRequestToken("http://localhost:8080/GenealogyGuide/nfs/authenticate_callback.htm");

        URI authUrl = nfsAuthenticator.getAuthenticationUrl();
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("authUrl", authUrl);
        ModelAndView modelAndView = new ModelAndView("nfs/index", model);
        return modelAndView;
    }

    public ModelAndView authenticate_callback(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String oauthVerifier=hsr.getParameter("oauth_verifier");
        String oauthToken=hsr.getParameter("oauth_token");
        
        String sessionId=this.nfsAuthenticator.getSessionId(oauthVerifier,oauthToken);
        this.nfsClient.setSessionId(sessionId);
        HashMap<String, Object> model = new HashMap<String, Object>();
        ModelAndView modelAndView = new ModelAndView("nfs/callback", model);
        return modelAndView;
    }
    
    public ModelAndView test(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        FamilyTree tree=this.nfsClient.getMe();
        Person me=tree.getPersons().get(0);
        String id=me.getId();
        
        FamilyTree family=this.nfsClient.getPedigree(id,3); 
        FamilyTree properties=this.nfsClient.getProperties();
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("me",me);
        ModelAndView modelAndView = new ModelAndView("nfs/test", model);
        return modelAndView;
    }
    
    
}