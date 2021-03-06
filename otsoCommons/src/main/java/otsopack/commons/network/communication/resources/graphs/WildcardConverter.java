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

package otsopack.commons.network.communication.resources.graphs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import otsopack.commons.data.Template;
import otsopack.commons.data.TripleLiteralObject;
import otsopack.commons.data.TripleURIObject;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.network.communication.resources.prefixes.PrefixesStorage;

public class WildcardConverter {
	
	final static Map<Class<?>, String> datatypes = new HashMap<Class<?>, String>();
	static {
		datatypes.put(Float.class,	"xsd:float");
		datatypes.put(Double.class,	"xsd:double");
		datatypes.put(Integer.class,"xsd:int");
		datatypes.put(Long.class,	"xsd:long");
		datatypes.put(Boolean.class,"xsd:boolean");
		datatypes.put(String.class,	"xsd:string");
	}
	
	public static Template createTemplateFromURL(String subject, String predicate, String objectUri, String objectValue, String objectType, PrefixesStorage prefixes) throws Exception {
		if(objectUri != null && objectValue != null)
			throw new IllegalArgumentException("objectUri or objectValue (or both) must be null");
		
		if(objectUri != null && !objectUri.equals("*"))
			return createTemplateFromURLwithURI(subject, predicate, objectUri, prefixes);
		
		if(objectValue != null){
			if(objectType == null)
				throw new IllegalArgumentException("if objectValue is provided, then objectType must be provided!");

			for(Class<?> klass : datatypes.keySet()){
			
				if(datatypes.get(klass).equals(objectType)){
					final Object o;
					try {
						o = klass.getConstructor(String.class).newInstance(objectValue);
					} catch (Exception e) {
						throw new IllegalArgumentException("Could not build object value " + objectValue + " for datatype: " + objectType, e);
					}
					return createTemplateFromURLwithValue(subject, predicate, o, prefixes);
				}
			}
			throw new IllegalArgumentException("Datatype: " + objectType + " not supported");
		}
		
		return createTemplateFromURLwithNull(subject, predicate, prefixes);
	}
	
	public static Template createTemplateFromURLwithNull(String subject, String predicate, PrefixesStorage prefixes) throws Exception {
		return WildcardTemplate.createWithNull(
					adaptFieldFormat(subject,prefixes),
					adaptFieldFormat(predicate,prefixes)
				);
	}
	
	public static Template createTemplateFromURLwithValue(String subject, String predicate, Object object, PrefixesStorage prefixes) throws Exception {
		return WildcardTemplate.createWithLiteral(
					adaptFieldFormat(subject,prefixes),
					adaptFieldFormat(predicate,prefixes),
					object
				);
	}
	
	public static Template createTemplateFromURLwithURI(String subject, String predicate, String object, PrefixesStorage prefixes) throws Exception {
		return WildcardTemplate.createWithURI(
					adaptFieldFormat(subject,prefixes),
					adaptFieldFormat(predicate,prefixes),
					adaptFieldFormat(object,prefixes)
				);
	}
	
	protected static String adaptFieldFormat(String field, PrefixesStorage prefixesStorage) throws Exception {
		if( field.equals("*") ) {
			return null;
		} else if( field.startsWith("http://") ) {
			return field;
		} else {
			final String[] split = field.split(":");
			String uri = prefixesStorage.getPrefixByName(split[0]);
			if(uri==null) {
				throw new Exception("This prefix does not exist.");
			}
			if( split.length>1 ) {
				uri = uri + split[1];
			}
			return uri;
		}
	}
	
	/**
	 * @return
	 * 		/{subjecturi}/{predicateuri}/* <br/>
	 * 		/{subjecturi}/{predicateuri}/{object-uri} <br />
	 *		/{subjecturi}/{predicateuri}/{object-literal-type}/{object-literal-value} <br />
	 * 		e.g.: /{subjecturi}/{predicateuri}/xsd:int/5<br />
	 * 		And it uses HTMLEncode...
	 * @throws UnsupportedEncodingException 
	 */
	public static String createURLFromTemplate(WildcardTemplate wtpl) throws UnsupportedEncodingException {
		String ret;
		if( wtpl.getSubject()==null ) ret = "*";
		else ret = URLEncoder.encode( wtpl.getSubject(), "UTF-8" );
		
		ret += "/";
		
		if( wtpl.getPredicate()==null ) ret += "*";
		else ret += URLEncoder.encode( wtpl.getPredicate(), "UTF-8" );
		
		ret += "/";
		
		if( wtpl.getObject()==null ) ret += "*";
		else if( wtpl.getObject() instanceof TripleURIObject )
			ret += URLEncoder.encode( ((TripleURIObject)wtpl.getObject()).getURI(), "UTF-8" );
		else if( wtpl.getObject() instanceof TripleLiteralObject ) {
			ret += getLastPart( ((TripleLiteralObject)wtpl.getObject()).getValue() );
		}
	
		return ret;
	}
	
	private static String getLastPart(Object obj) throws UnsupportedEncodingException {
		return getDataType(obj) + "/" + URLEncoder.encode( obj.toString(), "UTF-8"  );
	}
	
	/**
	 * @return
	 * 		Float.class => xsd:float
     *		Double.class => xsd:double
     *		Integer.class => xsd:int
     *			(xsd:integer also exists, but we are not going to use it)
     *		Long.class => xsd:long
     *		Boolean.class => xsd:boolean
     * 		String.class => xsd:string
     */
	protected static String getDataType(Object obj) {
		for(Class<?> klass : datatypes.keySet())
			if(klass.isInstance(obj))
				return datatypes.get(klass);
		
		throw new IllegalArgumentException("Object type " + obj.getClass().getName() + " not supported");
	}
}