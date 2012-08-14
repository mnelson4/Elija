/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elija.nfs.services.exceptions;

/**
 *
 * @author mnelson4
 */
public class NFSNoSessionException extends Exception{
    public NFSNoSessionException(String msg){
        super("There is no sessionID. The user must log in again."+msg);
    }
    public NFSNoSessionException(){
        super("There is no SessionID. The user must log in again.");
    }

}
