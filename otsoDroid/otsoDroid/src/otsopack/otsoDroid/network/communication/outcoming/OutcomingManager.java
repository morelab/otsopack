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
package otsopack.otsoDroid.network.communication.outcoming;

import net.jxta.endpoint.Message;

import org.apache.log4j.Logger;

import otsopack.otsoDroid.network.communication.IMessageSender;
import otsopack.otsoDroid.network.communication.incoming.IncomingList;
import otsopack.otsoDroid.network.communication.incoming.response.LockModelResponse;
import otsopack.otsoDroid.network.communication.incoming.response.ModelResponse;
import otsopack.otsoDroid.network.communication.incoming.response.URIResponse;
import otsopack.otsoDroid.network.communication.util.MessageParser;
import otsopack.otsoCommons.configuration.TscMEConfiguration;
import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.ITemplate;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import otsopack.otsoCommons.network.communication.event.listener.INotificationListener;
import otsopack.otsoCommons.network.coordination.IPeerInformationHolder;
import otsopack.otsoCommons.stats.Statistics;

public class OutcomingManager implements IDemandSender {
	private final static Logger log = Logger.getLogger(OutcomingManager.class.getName());
	IPeerInformationHolder peerInfo;
	IMessageSender space;
	IncomingList inbox;
	
	public OutcomingManager(IMessageSender pipe, IncomingList inbox, IPeerInformationHolder c) {
		this.space = pipe;
		this.peerInfo = c;
		this.inbox = inbox;
	}
	
	private IGraph sendMessageWaitingResponse(Message m, Object responseKey) {
		space.send(m);
		
		LockModelResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new LockModelResponse(responseKey, lock, 1);
			addExpectedResponse(ru);
			//wait until the first response is received!
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}

		return ru.getGraph();
	}
	
	private IGraph sendMessageWaitingNResponses(Message m, Object responseKey, int numberOfResponsesExpected) {
		space.send(m);
		
		LockModelResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new LockModelResponse(responseKey, lock, numberOfResponsesExpected);
			addExpectedResponse(ru);
			//wait until the first response is received!
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}

		return ru.getGraph();
	}
	
	/**
	 * 
	 * @param space
	 * @param m
	 * @param sel
	 * @return the uri of the un/advertised / un/subscribed template.
	 */
	private String sendMessageWaitingToURI(Message m, ITemplate sel) {
		space.send(m);
		
		URIResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new URIResponse(sel, lock);
			addExpectedResponse(ru);
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}

		return ru.getURI();
	}
	
	private IGraph sendMessageWaitingTimeout(Message m, Object responseKey, long timeout) {
		space.send(m);
		
		/*ModelResponse ru = new ModelResponse(responseKey);
		space.getInbox().add(ru);
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		space.getInbox().remove(ru);*/
		
		LockModelResponse ru = null;
		final Object lock = new Object();
		synchronized(lock) {
			ru = new LockModelResponse(responseKey, lock, 1);
			addExpectedResponse(ru);
			//wait until the first response is received or the timeout is reached!
			try {
				lock.wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeExpectedResponse(ru);
		}
		
		return ru.getGraph();
	}
	
	public IGraph query(ITemplate template, long timeout) {
		Message m = MessageParser.createQueryMessage(peerInfo.getPeerName(), template);
		IGraph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, template, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
//			Statistics.setMeasure("query", timeneeded, ret.size());
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, template, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, template);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Query performed in "+ timeneeded + " ms");
			}
		}
		return ret;
	}
	
	public IGraph read(ITemplate template, long timeout) {
		Message m = MessageParser.createReadMessage(peerInfo.getPeerName(), template);
		IGraph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, template, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
//			Statistics.setMeasure("read", timeneeded, ret.size());
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, template, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, template);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Read performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public IGraph read(String graphuri, long timeout) {
		Message m = MessageParser.createReadMessage(peerInfo.getPeerName(), graphuri);
		IGraph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, graphuri, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
//			Statistics.setMeasure("read", timeneeded, ret.size());
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, graphuri, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, graphuri);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Read performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public IGraph take(ITemplate template, long timeout) {
		Message m = MessageParser.createTakeMessage(peerInfo.getPeerName(), template);
		IGraph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, template, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
//			Statistics.setMeasure("take", timeneeded, ret.size());
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, template, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, template);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Take performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public IGraph take(String graphuri, long timeout) {
		Message m = MessageParser.createTakeMessage(peerInfo.getPeerName(), graphuri);
		IGraph ret = null;
		if(TscMEConfiguration.getConfiguration().isEvaluationMode()) {
			long start = System.currentTimeMillis();
			ret = sendMessageWaitingNResponses(m, graphuri, Statistics.getNumberOfResponses());
			long timeneeded = System.currentTimeMillis() - start;
//			Statistics.setMeasure("take", timeneeded, ret.size());
		} else {
			if(timeout>0) ret = sendMessageWaitingTimeout(m, graphuri, timeout);
			else {
				long start = System.currentTimeMillis();
				ret = sendMessageWaitingResponse(m, graphuri);
				long timeneeded = System.currentTimeMillis() - start;
				log.debug("Take performed in "+timeneeded+"ms");
			}
		}
		return ret;
	}
	
	public String advertise(ITemplate template) {
		Message m = MessageParser.createAdvertiseMessage(peerInfo.getPeerName(), template);
		return sendMessageWaitingToURI(m, template);
	}
	
	public void unadvertise(String advertisementURI) {
		Message m = MessageParser.createUnadvertiseMessage(peerInfo.getPeerName(), advertisementURI);
		space.send(m);
	}
	
	public String subscribe(ITemplate template, INotificationListener listener) {
		Message m = MessageParser.createSubscribeMessage(peerInfo.getPeerName(), template);
		return sendMessageWaitingToURI(m, template);
	}
	
	public void unsubscribe(String subscriptionURI) {
		Message m = MessageParser.createUnsubscribeMessage(peerInfo.getPeerName(), subscriptionURI);
		space.send(m);
	}
	
	public void demand(ITemplate template,long leaseTime) {
		Message m = MessageParser.createDemandMessage(peerInfo.getPeerName(),template,leaseTime);
		space.send(m);
	}
	
	public void suggest(IGraph graph) {
		Message m = MessageParser.createSuggestMessage(
				peerInfo.getPeerName(), new SemanticFactory().createModelForGraph(graph));
		space.send(m);
	}
	
	public void obtainDemands() {
		Message m = MessageParser.createObtainDemandsMessage(peerInfo.getPeerName());
		space.send(m);
	}
	
	protected void addExpectedResponse(URIResponse response) {
		inbox.add(response);
	}
	
	protected void addExpectedResponse(ModelResponse response) {
		inbox.add(response);
	}
	
	protected void removeExpectedResponse(URIResponse response) {
		inbox.remove(response);
	}
	
	protected void removeExpectedResponse(ModelResponse response) {
		inbox.remove(response);
	}
}