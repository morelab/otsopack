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
package otsopack.commons.network.subscriptions.bulletinboard.http.serializables;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SubscribeJSON implements Serializable {
	
	private static final long serialVersionUID = 4659020443077554065L;
	
	//instead of extends AdvertiseJSON :-S
	//JsonRepresentation seems not to serialize parents' attributes :-S
	protected String id;
	protected TemplateJSON tpl;
	protected long lifetime;
	protected String callbackURL;
	protected Set<String> nodesWhichAlreadyKnowTheSubscription = new HashSet<String>(); // their uuid's
	
	public SubscribeJSON() { // For the JSON serializer
	}
	
	private SubscribeJSON(String id, TemplateJSON tpl, String callbackURL) {
		this.id = id;
		this.tpl = tpl;
		this.callbackURL = callbackURL;
		this.lifetime = 0;
	}
	
	public static SubscribeJSON createSubscription(String subscriptionId, TemplateJSON tpl, String callbackURL, long extratime) {
		final SubscribeJSON ret = new SubscribeJSON(subscriptionId, tpl, callbackURL);
		ret.setLifetime(extratime);
		return ret;
	}
	
	public static SubscribeJSON createUpdatableSubscription(String subscriptionId, long extratime) {
		final SubscribeJSON ret = new SubscribeJSON(subscriptionId, null, null);
		ret.setLifetime(extratime);
		return ret;
	}
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TemplateJSON getTpl() {
		return this.tpl;
	}
	public void setTpl(TemplateJSON adv) {
		this.tpl = adv;
	}
	
	public long getLifetime() {
		return this.lifetime;
	}

	/**
	 * @param lifetime
	 * 		Time left until the expiration of this subscription.
	 */
	public void setLifetime(long lifetime) {
		this.lifetime = lifetime;
	}

	public String getCallbackURL() {
		return callbackURL;
	}
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	public Set<String> getNodesWhichAlreadyKnowTheSubscription() {
		return nodesWhichAlreadyKnowTheSubscription;
	}

	public void addNodeWhichAlreadyKnowTheSubscription(String nodesWhichAlreadyKnowTheSubscription) {
		this.nodesWhichAlreadyKnowTheSubscription.add(nodesWhichAlreadyKnowTheSubscription);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		SubscribeJSON other = (SubscribeJSON) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
}
