package de.axishotels.booking.client;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class MyPasswordCallbackHandler implements CallbackHandler {

    /**
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {	    	
	    	if (callbacks[i] instanceof WSPasswordCallback) {
	            WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];

	            // store password in the WSPasswordCallback instance
	            if (pc.getUsage()==WSPasswordCallback.SIGNATURE) {	            
	            	// return password for private key of the client 
	            	// this is needed to sign messages	            
	            	if ("bob".equals(pc.getIdentifier())) 
	            		pc.setPassword("secret");
	            }
	        } else {
	            throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
	        }
	    }
    }
}
