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
package otsopack.commons.dataaccess.simplestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.dataaccess.AbstractDataAccess;
import otsopack.commons.dataaccess.authz.IAuthorizationChecker;
import otsopack.commons.dataaccess.memory.space.GraphMem;
import otsopack.commons.dataaccess.memory.space.MemoryFactory;
import otsopack.commons.dataaccess.memory.space.SpaceMem;
import otsopack.commons.exceptions.PersistenceException;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.util.Util;

/**
 * This class defines a really simple persistent data access which stores graphs.
 * 
 * It should be only used when other more complex DataAccess such as Sesame
 * or Jena based ones cannot be used.
 */
public class SimplePersistentDataAccess extends AbstractDataAccess {
	
	public static enum OpenMode {PRELOAD, LOAD_ON_JOIN, CLEAR_OLD_CONTENT};  
	
	ConcurrentHashMap<String,SpaceMem> spaces = new ConcurrentHashMap<String,SpaceMem>();
	ConcurrentHashMap<String,SpaceMem> preloadedSpaces = new ConcurrentHashMap<String,SpaceMem>();
	ISimpleStore dao;
	final OpenMode selectedMode;
	
	public static boolean DEBUG = false;
	
	public SimplePersistentDataAccess(ISimpleStore simple, OpenMode open) {
		this.dao = simple;
		this.selectedMode = open;
	}
	
	@Override
	public void startup() throws TSException {
		this.dao.startup();
		if (this.selectedMode==OpenMode.CLEAR_OLD_CONTENT) {
			this.dao.clear();
		} else if (this.selectedMode==OpenMode.PRELOAD) {
			final Set<DatabaseTuple> tuples = this.dao.getGraphs();
			for(DatabaseTuple tuple: tuples) {
				this.preloadedSpaces.putIfAbsent(tuple.getSpaceuri(),MemoryFactory.createSpace(tuple.getSpaceuri()));
				final SpaceMem space = this.preloadedSpaces.get(tuple.getSpaceuri());
				space.write(new ModelImpl(tuple.getGraph()), tuple.getGraphuri());
			}
		}
	}
	
	@Override
	public void shutdown() throws TSException {
		this.dao.shutdown();
	}
	
	private void dbg(String message) {
		if(DEBUG) {
			System.err.print(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS ").format(new Date()));
			System.err.print(SimplePersistentDataAccess.class.getSimpleName());
			System.err.print(": ");
			System.err.println(message);
		}
	}

	protected SpaceMem getSpace(String spaceURI) throws SpaceNotExistsException {
		dbg("Get space " + spaceURI + " instance: " + this);
		
		final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
		final SpaceMem ret = this.spaces.get(normalizedURI);
		if (ret==null) throw new SpaceNotExistsException("The space \"" + spaceURI + "\" could not be found"); 
		return ret;
	}
	
	private void createSpace(String spaceURI) throws SpaceAlreadyExistsException {
		dbg("Create space " + spaceURI + " instance: " + this);
		
		final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
		if (this.spaces.containsKey(normalizedURI)) throw new SpaceAlreadyExistsException();
		
		if (this.selectedMode==OpenMode.PRELOAD) {
			if(this.preloadedSpaces.containsKey(normalizedURI)) {
				this.spaces.putIfAbsent(normalizedURI,this.preloadedSpaces.get(normalizedURI));
			}
		}
		// "putIfAbsent" ensures that it's just going to be created if it has not been preloaded...
		this.spaces.putIfAbsent(normalizedURI,MemoryFactory.createSpace(normalizedURI));
	}
	
	@Override
	public void joinSpace(String spaceURI) throws SpaceAlreadyExistsException, PersistenceException {
		createSpace(spaceURI);
		
		dbg("Join space " + spaceURI + " instance: " + this);
		
		final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
		
		if (this.selectedMode==OpenMode.LOAD_ON_JOIN) {
			final Set<DatabaseTuple> tuples = this.dao.getGraphsFromSpace(normalizedURI);
			for(DatabaseTuple tuple: tuples) {
				final SpaceMem space = this.spaces.get(tuple.getSpaceuri());
				space.write(new ModelImpl(tuple.getGraph()), tuple.getGraphuri());
			}
		}
	}
	
	@Override
	public Set<String> getJoinedSpaces() {
		return this.spaces.keySet();
	}
	
	@Override
	public void leaveSpace(String spaceURI) throws SpaceNotExistsException {
		dbg("Leave space " + spaceURI + " instance: " + this);
		
		final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
		
		if (!this.spaces.containsKey(normalizedURI)) throw new SpaceNotExistsException("The space \"" + spaceURI + "\" could not be found");
		this.spaces.remove(spaceURI);
	}
	
	@Override
	public String[] getLocalGraphs(String spaceURI)	throws SpaceNotExistsException {
		final SpaceMem espacio = getSpace(spaceURI);
		return espacio.getLocalGraphs();
	}
	
	protected String generateUniqueURIbasedOnContent(String spaceuri, byte[] bytes) {
		final String normalizedURI = Util.normalizeSpaceURI(spaceuri, "");
		return Util.normalizeSpaceURI(normalizedURI + UUID.nameUUIDFromBytes(bytes).toString(), "");
	}
	
	@Override
	public String write(String spaceURI, Graph triples)
			throws SpaceNotExistsException, UnsupportedSemanticFormatException,
			PersistenceException {
		final SpaceMem space = getSpace(spaceURI);
		final String graphuri = generateUniqueURIbasedOnContent(spaceURI,triples.getData().getBytes());
		
		//If this exact graph was already stored in the past, why we should write it again?
		if (!space.containsGraph(graphuri)) {
			final String normalizedURI = Util.normalizeSpaceURI(spaceURI, "");
			this.dao.insertGraph(normalizedURI, graphuri, triples);
			// consistency kept, if the exception does not do the following write in memory
			space.write(new ModelImpl(triples), graphuri);
		}
		
		return graphuri;
	}
	
	@Override
	public Graph concreteQuery(String spaceURI, Template template,
			SemanticFormat outputFormat, IAuthorizationChecker checker)
			throws SpaceNotExistsException, UnsupportedTemplateException {
		final SpaceMem space = getSpace(spaceURI);
		final ModelImpl ret = space.query(template,checker);
		return (ret==null)? null: ret.write(outputFormat);
	}
	
	protected Graph convertToGraph(GraphMem graphmem, SemanticFormat outputFormat) {
		if( graphmem==null ) return null;
		return graphmem.getModel().write(outputFormat);
	}
	
	@Override
	public Graph concreteRead(String spaceURI, Template template,
			SemanticFormat outputFormat, IAuthorizationChecker checker)
			throws SpaceNotExistsException, UnsupportedTemplateException {
		final SpaceMem space = getSpace(spaceURI);
		return convertToGraph(space.read(template,checker),outputFormat);
	}

	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteRead(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException {
		final SpaceMem space = getSpace(spaceURI);
		return convertToGraph(space.read(graphURI),outputFormat);
	}
	
	public Graph concreteTake(String spaceURI, Template template, SemanticFormat outputFormat, IAuthorizationChecker checker) throws SpaceNotExistsException, UnsupportedTemplateException, PersistenceException {
		final SpaceMem space = getSpace(spaceURI);
		final GraphMem graph = space.take(template, checker);
		if (graph!=null) {
			try {
				final String normalizedgraphuri = Util.normalizeSpaceURI(graph.getUri(), "");
				final String normalizedspaceuri = Util.normalizeSpaceURI(spaceURI, "");
				this.dao.deleteGraph(normalizedspaceuri, normalizedgraphuri);
			} catch(PersistenceException e) {
				// writing if sth has gone wrong, we ensure the space consistency
				space.write(graph.getModel(), graph.getUri());
				throw e;
			}
		}
		return convertToGraph(graph,outputFormat);
	}
	
	/**
	 * Already authorized in AbstractDataAccess
	 */
	public Graph concreteTake(String spaceURI, String graphURI, SemanticFormat outputFormat) throws SpaceNotExistsException, PersistenceException {
		final SpaceMem space = getSpace(spaceURI);
		final String normalizedgraphuri = Util.normalizeSpaceURI(graphURI, "");
		
		final String normalizedspaceuri = Util.normalizeSpaceURI(spaceURI, "");
		this.dao.deleteGraph(normalizedspaceuri, normalizedgraphuri);
		// space consistency kept, if this.dao throws an exception does not reach here
		return convertToGraph(space.take(normalizedgraphuri),outputFormat);
	}
}