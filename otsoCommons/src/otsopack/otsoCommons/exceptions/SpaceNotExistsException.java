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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.otsoCommons.exceptions;

public class SpaceNotExistsException extends SpaceException {
	private static final long serialVersionUID = 1L;

	public SpaceNotExistsException() {
		super("The space you are trying to access doesn't exist.");
	}
	
	
	public SpaceNotExistsException(String message) {
		super(message);
	}
}