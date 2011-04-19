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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.authn;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class FakeClientResource extends ClientResource {
	
	public Object obtainedRepresentation;
	public Representation returnedRepresentation;
	
	@Override
	public Representation post(Object repr){
		this.obtainedRepresentation = repr;
		return this.returnedRepresentation;
	}
}