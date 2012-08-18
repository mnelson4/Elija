/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elija.nfs.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.elija.nfs.services.exceptions.NFSAPIException;
import org.elija.nfs.services.exceptions.NFSNoSessionException;
import org.elija.nfs.services.exceptions.NFSOAuthException;
import org.familysearch.ws.client.familytree.v2.schema.FamilyTree;

/**
 *
 * @author mnelson4
 */
public class NFSAPIClient {
    private String sessionId;
    private String server;
    private Unmarshaller xmlUnmarshaller;
    public NFSAPIClient() throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(FamilyTree.class);
        
        this.xmlUnmarshaller = jc.createUnmarshaller();
    }
    public void setSessionId(String sessionId){
        this.sessionId=sessionId;
    }
    
    public void setUseSandbox(boolean useSandbox) {
        if (useSandbox) {
            this.server = "sandbox.familysearch.org/";
        } else {
            this.server = "api.familysearch.org/";
        }
    }
    public FamilyTree getPedigree(String pid, Integer generations) throws NFSAPIException, NFSNoSessionException{
        if(this.sessionId==null)
            throw new NFSNoSessionException();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(this.server)
                .setPath("familytree/v2/pedigree/"+pid)
                .setParameter("ancestors",generations.toString())
                .setParameter("properties", "all")
                .setParameter("sessionId", this.sessionId);
        
        try{
            URI uri=builder.build();
            
//             HttpGet httpget = new HttpGet(uri);
//        String uriString = httpget.getURI().toString();
//        HttpResponse response;
//        DefaultHttpClient httpclient = new DefaultHttpClient();
//        HttpContext localContext = new BasicHttpContext();
//            response = httpclient.execute(httpget, localContext);
//            HttpEntity entity = response.getEntity();
//          String output = EntityUtils.toString(entity);
            
            FamilyTree ft=(FamilyTree)this.xmlUnmarshaller.unmarshal(uri.toURL());
            return ft;
        }catch(UnmarshalException e){
            throw new NFSAPIException("We seem to have received an incorrect response from New Family Search: "+e);
        }
        catch(Exception e){
            throw new NFSAPIException("Exception while grabbing me."+e);
        }
    }
    public FamilyTree getMe() throws NFSAPIException, NFSNoSessionException{
        if(this.sessionId==null)
            throw new NFSNoSessionException();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(this.server)
                .setPath("familytree/v2/person")
                .setParameter("sessionId", this.sessionId);
        
        try{
            URI uri=builder.build();
            String uriString=uri.toString();
            FamilyTree ft=(FamilyTree)this.xmlUnmarshaller.unmarshal(uri.toURL());
            return ft;
        }catch(Exception e){throw new NFSAPIException("Exception while grabbing me."+e);}
    }
    
    public FamilyTree getProperties() throws NFSAPIException, NFSNoSessionException{
        if(this.sessionId==null)
            throw new NFSNoSessionException();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(this.server)
                .setPath("familytree/v2/properties");            
        try{
            URI uri=builder.build();
            String uriString=uri.toString();
            FamilyTree ft=(FamilyTree)this.xmlUnmarshaller.unmarshal(uri.toURL());
            return ft;
        }catch(Exception e){throw new NFSAPIException("Exception while grabbing me."+e);}
    }    
}
