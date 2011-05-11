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
package otsopack.full.java.network.coordination.spacemanager;

import otsopack.full.java.network.coordination.ISpaceManager;
import otsopack.full.java.network.coordination.SpaceManager;
import otsopack.full.java.network.coordination.spacemanager.http.HttpSpaceManagerClient;

public class HttpSpaceManager extends SpaceManager {

	private final String uri;
	
	public HttpSpaceManager(String uri) {
		this.uri = uri;
	}
	
	public ISpaceManager createClient(){
		return new HttpSpaceManagerClient(this);
	}
	
	public String getURI(){
		return this.uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.uri == null) ? 0 : this.uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpSpaceManager other = (HttpSpaceManager) obj;
		if (this.uri == null) {
			if (other.uri != null)
				return false;
		} else if (!this.uri.equals(other.uri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HttpSpaceManager [uri=" + this.uri + "]";
	}
}