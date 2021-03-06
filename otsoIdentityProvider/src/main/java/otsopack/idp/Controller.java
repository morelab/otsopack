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
package otsopack.idp;

import otsopack.idp.authn.ICredentialsChecker;
import otsopack.restlet.commons.sessions.ISessionManager;
import otsopack.restlet.commons.sessions.memory.MemorySessionManager;

public class Controller implements IIdpController {
	
	private ICredentialsChecker credentialsChecker;
	private ISessionManager<IdpSession> sessionManager;

	public Controller(ICredentialsChecker credentialsChecker, ISessionManager<IdpSession> sessionManager){
		this.credentialsChecker = credentialsChecker;
		this.sessionManager = sessionManager;
	}
	
	public Controller(ICredentialsChecker credentialsChecker){
		this(credentialsChecker, new MemorySessionManager<IdpSession>());
	}
	
	@Override
	public ICredentialsChecker getCredentialsChecker() {
		return this.credentialsChecker;
	}
	
	@Override
	public ISessionManager<IdpSession> getSessionManager(){
		return this.sessionManager;
	}
}
