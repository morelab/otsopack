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
package otsopack.full.java.network.communication;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.full.java.network.communication.resources.ClientResourceFactory;
import otsopack.full.java.network.communication.resources.cookies.CookieStore;
import otsopack.full.java.network.communication.util.JSONDecoder;

/**
 * This is the class used by a comet client to communicate with another network.
 */
public class RestCometCommunication implements ICommunication {
	private static final int DEFAULT_TIMEOUT_CONNECTION = 1000;
	
	private final String gatewayURL;
	private final CookieStore cookieStore = new CookieStore();
	private final ClientResourceFactory clientFactory = new ClientResourceFactory(this.cookieStore);
	
	private String sessionId = null;
	
	public RestCometCommunication(String gatewayUrl) {
		this.gatewayURL = gatewayUrl;
	}

	public String getBaseURI() {
		return this.gatewayURL + "sessions/" + this.sessionId + "/";
	}

	@Override
	public void startup() throws TSException {
		try {
			final ClientResource cr = this.clientFactory.createStatefulClientResource( this.gatewayURL + "sessions/", DEFAULT_TIMEOUT_CONNECTION );
			try {
				final Representation rep = cr.post(null);
				this.sessionId = JSONDecoder.decode(rep.getText(), String.class);
			} finally{
				cr.release();
			}
		} catch(IOException e) {
			throw new TSException("Session could not be started: " + e.getMessage());
		}
	}

	@Override
	public void shutdown() throws TSException {
		final ClientResource cr = this.clientFactory.createStatefulClientResource( getBaseURI(), DEFAULT_TIMEOUT_CONNECTION );
		try {
			// if everything goes ok, it actually returns "ok"
			/*final Representation rep = */cr.delete();
			// final String ok = JSONDecoder.decode(rep.getText(), String.class);
		} finally{
			cr.release();
		}
	}

	// http://restlet-code.1609877.n2.nabble.com/Push-data-from-server-using-a-live-HTTP-connection-td2906563.html
	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		final ClientResource cr = this.clientFactory.createStatefulClientResource( getBaseURI() + "events", DEFAULT_TIMEOUT_CONNECTION );
		Representation r = cr.get(); 
        /*r.setReadListener(new StringListener(r) { 
            public void onContent(String s) { 
                if ("\n".equals(s)) { 
                    System.out.println("Empty event detected"); 
                } else { 
                    System.out.print("Event received:"); 
                    System.out.println(s); 
                    System.out.flush(); 
                } 
            } 
        }); */
		return null;
	}

	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph[] query(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph[] query(String spaceURI, Template template,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribe(String spaceURI, NotificableTemplate template,
			INotificationListener listener) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String advertise(String spaceURI, NotificableTemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unadvertise(String spaceURI, String advertisementURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void demand(String spaceURI, Template template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suggest(String spaceURI, Graph graph) throws TSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(
			String spaceURI, Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}
}