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
package otsopack.otsoME.network.communication.outcoming;

import otsopack.otsoMobile.data.IGraph;
import otsopack.otsoMobile.data.ITemplate;

public interface IResponseSender {
	void response(ITemplate responseTo, IGraph triples);
	void response(String responseToGraphURI, IGraph triples);
	void responseToObtainDemands(byte[] recordsExported);
}