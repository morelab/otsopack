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
package otsopack.commons.authz.entities;

/**
 * This class represents an authorisation entity to a certain graph.
 * 
 * More specifically: individual users.
 */
public class User implements IEntity {
	final String id;
	
	public User(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if( this==o ) return true;
		if( o!=null && o instanceof User ) {
			return this.id==((User)o).id;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.id.hashCode();
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#check(java.lang.Object)
	 */
	public boolean check(Object o) {
		return equals(o);
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.authz.entities.IEntity#isAnonymous()
	 */
	public boolean isAnonymous() {
		return false;
	}
}