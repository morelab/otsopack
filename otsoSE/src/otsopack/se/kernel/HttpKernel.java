/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: FILLME
 *
 */
package otsopack.se.kernel;

import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.kernel.AbstractKernel;
import otsopack.full.java.network.RestNetwork;
import otsopack.full.java.network.coordination.IRegistry;
import otsopack.full.java.network.coordination.bulletinboard.BulletinBoardsManager;

public class HttpKernel extends AbstractKernel {

	private final int port;
	private final IEntity signer;
	private final IRegistry registry;
	private RestNetwork restNetwork;
	
	public HttpKernel(int port, IEntity signer, IRegistry registry){
		this.port     = port;
		this.signer   = signer;
		this.registry = registry;
	}
	
	protected void buildKernel(){
		super.buildKernel();
		
		if (this.networkService == null) {
			// TODO define by default IBulletinBoard
			final BulletinBoardsManager bbMngr = new BulletinBoardsManager();
			//LocalBulletinBoard lbb = new LocalBulletinBoard(registry);
			this.restNetwork = new RestNetwork(getController(), port, signer, registry, bbMngr);
			this.setNetworkService(this.restNetwork);
		}
	}
	
	public RestNetwork getRestNetworkService() {
		return this.restNetwork;
	}
	
}
