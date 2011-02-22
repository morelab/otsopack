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
package otsopack.otsoME.network.communication.demand.local;

import otsopack.otsoMobile.data.IGraph;

public interface ISuggestionCallbackManager {
	/**
	 * This method call back to locally specified demands if their
	 * template matches the given triples.
	 * @param triples
	 * @return
	 * 		has any of the registered demands' template match the triples?
	 * 		or (the same)
	 * 		has any class be called back?
	 */
	public abstract boolean callbackForMatchingTemplates(final IGraph triples);
}