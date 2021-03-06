/*
 * Copyright (C) 2011 onwards University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.authn;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

import otsopack.authn.resources.SessionRequestResource;
import otsopack.authn.resources.ValidatedSessionResource;
import otsopack.restlet.commons.AbstractOtsopackApplication;

public class OtsoAuthnApplication extends AbstractOtsopackApplication<IController> {
	
	public static final String AUTHN_ROOT_PATH = "/authn";
	
	private static final Map<String, Class<? extends ServerResource>> PATHS = new HashMap<String, Class<? extends ServerResource>>();
	private static final ClientResourceFactory defaultClientResourceFactory = new ClientResourceFactory();
	
	private IClientResourceFactory clientResourceFactory;
	
	static {
		addPaths(SessionRequestResource.getRoots());
		addPaths(ValidatedSessionResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<? extends ServerResource>> roots) {
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public OtsoAuthnApplication(){
		super(PATHS);
	}
	
	public OtsoAuthnApplication(IController controller) {
		this();
		setController(controller);
	}
	
	public OtsoAuthnApplication(IAuthenticatedUserHandler authenticatedUserHandler) {
		this(new Controller(authenticatedUserHandler));
	}
	
	// For testing purposes
	private IClientResourceFactory getClientResourceFactory() {
		if(this.clientResourceFactory == null)
			return defaultClientResourceFactory;
		
		return this.clientResourceFactory;
	}
	
	public ClientResource createResource(String url){
		return getClientResourceFactory().createResource(url);
	}

	public void setClientResourceFactory(IClientResourceFactory clientResourceFactory) {
		this.clientResourceFactory = clientResourceFactory;
	}
}
