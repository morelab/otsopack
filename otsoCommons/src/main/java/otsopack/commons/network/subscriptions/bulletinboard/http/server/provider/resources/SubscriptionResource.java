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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import otsopack.commons.network.communication.resources.AbstractServerResource;
import otsopack.commons.network.communication.util.JSONDecoder;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoardOuterFacade;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.serializables.SubscribeJSON;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class SubscriptionResource extends AbstractServerResource implements ISubscriptionResource {
	public static final String ROOT = SubscriptionsResource.ROOT + "/{subscribe}";
	
	public static Map<String, Class<? extends ServerResource>> getRoots() {
		final Map<String, Class<? extends ServerResource>> roots = new HashMap<String, Class<? extends ServerResource>>();
		roots.put(ROOT, SubscriptionResource.class);
		return roots;
	}
	
	@Override
	public Representation viewSubscription(Representation rep) {
		final String subID = this.getArgument("subscribe");
		final IBulletinBoardOuterFacade bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		return new JacksonRepresentation<SubscribeJSON>( bulletinBoard.getJsonSubscription(subID) );
	}
	
	@Override
	public Representation modifySubscription(Representation rep) {
		try {
			final String subID = this.getArgument("subscribe");
			final IBulletinBoardOuterFacade bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
			final String provided = rep.getText();
			final SubscribeJSON subjson = JSONDecoder.decode(provided, SubscribeJSON.class);
			
			bulletinBoard.updateSubscription(JSONSerializableConversors.convertFromSerializable(subjson), subjson.getNodesWhichAlreadyKnowTheSubscription()); // not exception thrown
			return new StringRepresentation(subID);
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
		}
	}
	
	@Override
	public Representation removeSubscription() {
		final String subID = getArgument("subscribe");
		final IBulletinBoardOuterFacade bulletinBoard = ((OtsopackHttpBulletinBoardProviderApplication)getApplication()).getController().getBulletinBoard();
		bulletinBoard.remoteUnsubscribe(subID); // not exception thrown
		return new StringRepresentation(subID);
	}
}