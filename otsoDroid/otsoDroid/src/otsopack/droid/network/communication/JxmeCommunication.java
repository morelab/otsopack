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
package otsopack.droid.network.communication;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.droid.network.coordination.JxmeCoordination;

public class JxmeCommunication implements ICommunication {
	IController controller;
	
	public JxmeCommunication(IController c) {
		controller = c;
	}
	
	public void startup() {}
	
	public void shutdown() {}
	
	private SpaceManager getSpace(String spaceURI) throws SpaceNotExistsException {
		return ((JxmeCoordination)controller.getNetworkService().getCoordination()).getSpace(spaceURI);
	}

	public IGraph read(String spaceURI, String graphURI, long timeout)
			throws SpaceNotExistsException {
		return getSpace(spaceURI).read(graphURI,timeout);
	}

	public IGraph read(String spaceURI, ITemplate template, long timeout)
			throws SpaceNotExistsException {
		return getSpace(spaceURI).read(template,timeout);
	}

	public IGraph take(String spaceURI, String graphURI, long timeout)
			throws SpaceNotExistsException {
		return getSpace(spaceURI).take(graphURI,timeout);
	}

	public IGraph take(String spaceURI, ITemplate template, long timeout)
			throws SpaceNotExistsException {
		return getSpace(spaceURI).take(template,timeout);
	}

	public IGraph query(String spaceURI, ITemplate template, long timeout)
			throws SpaceNotExistsException {
		return getSpace(spaceURI).query(template,timeout);
	}

	public String subscribe(String spaceURI, ITemplate template,
			INotificationListener listener) throws SpaceNotExistsException {
		return getSpace(spaceURI).subscribe(template,listener);
	}

	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		getSpace(spaceURI).unsubscribe(subscriptionURI);
	}

	public String advertise(String spaceURI, ITemplate template)
			throws SpaceNotExistsException {
		return getSpace(spaceURI).advertise(template);
	}

	public void unadvertise(String spaceURI, String advertisementURI)
			throws SpaceNotExistsException {
		getSpace(spaceURI).unadvertise(advertisementURI);
	}
	
	public void demand(String spaceURI, ITemplate template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		getSpace(spaceURI).demand(template, leaseTime, callback);
	}

	public void suggest(String spaceURI, IGraph graph) throws TSException {
		getSpace(spaceURI).suggest(graph);
	}

	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(String spaceURI,
			IGraph triples) throws TSException {
		return getSpace(spaceURI).callbackIfIHaveResponsabilityOverThisKnowlege(triples);
	}

	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			IGraph triples) throws TSException {
		return getSpace(spaceURI).hasAnyPeerResponsabilityOverThisKnowlege(triples);
	}
}