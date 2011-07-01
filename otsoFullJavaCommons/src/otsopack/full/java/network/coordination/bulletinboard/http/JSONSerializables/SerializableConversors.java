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
package otsopack.full.java.network.coordination.bulletinboard.http.JSONSerializables;

import otsopack.commons.data.TripleLiteralObject;
import otsopack.commons.data.TripleURIObject;
import otsopack.commons.data.WildcardTemplate;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;

public class SerializableConversors {
	
	public static Advertisement convertFromSerializable(AdvertiseJSON adv) {
		final WildcardTemplate tpl;
		if (adv.tpl.object==null) {
			tpl = WildcardTemplate.createWithNull(adv.tpl.subject, adv.tpl.predicate);
		} else if (adv.tpl.object.startsWith("http://")){
			tpl = WildcardTemplate.createWithURI(adv.tpl.subject, adv.tpl.predicate, adv.tpl.object);
		} else {
			tpl = WildcardTemplate.createWithLiteral(adv.tpl.subject, adv.tpl.predicate, adv.tpl.object);
		}
		return new Advertisement(adv.id, adv.expiration, tpl);
	}
	
	public static AdvertiseJSON convertToSerializable(Advertisement adv) {
		final WildcardTemplate wtpl = (WildcardTemplate) adv.getTemplate();
		final String obj;
		if (wtpl.getObject()==null) {
			obj = null;
		} else if (wtpl.getObject() instanceof TripleLiteralObject ) {
			obj = ((TripleLiteralObject)wtpl.getObject()).getValue().toString();
		} else {
			obj = ((TripleURIObject)wtpl.getObject()).getURI();
		}
		TemplateJSON tpl = new TemplateJSON(wtpl.getSubject(), wtpl.getPredicate(), obj);
		return new AdvertiseJSON(adv.getID(), tpl, adv.getExpiration());
	}
	
	public static Advertisement[] convertFromSerializable(AdvertiseJSON[] advs) {
		final Advertisement[] ret = new Advertisement[advs.length];
		int i=0;
		for(AdvertiseJSON adv: advs) {
			ret[i++] = convertFromSerializable(adv);
		}
		return ret;
	}
	
	public static AdvertiseJSON[] convertToSerializable(Advertisement[] advs) {
		final AdvertiseJSON[] ret = new AdvertiseJSON[advs.length];
		int i=0;
		for(Advertisement adv: advs) {
			ret[i++] = convertToSerializable(adv);
		}
		return ret;
	}
}
