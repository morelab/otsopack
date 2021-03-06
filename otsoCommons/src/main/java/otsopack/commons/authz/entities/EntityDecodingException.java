/*
 * Copyright (C) 2008 onwards University of Deusto
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
 *
 */
package otsopack.commons.authz.entities;

import otsopack.commons.authz.AuthzException;

public class EntityDecodingException extends AuthzException {

	private static final long serialVersionUID = 5064494550950291257L;

	public EntityDecodingException(String message) {
		super(message);
	}
}
