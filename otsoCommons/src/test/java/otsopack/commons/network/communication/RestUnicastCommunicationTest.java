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
package otsopack.commons.network.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.Arguments;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;

public class RestUnicastCommunicationTest extends AbstractRestServerTesting {
	final private String spaceURI = "http://space1/";
	private final Arguments arguments = new Arguments().setOutputFormat(SemanticFormat.NTRIPLES).setTimeout(3000);
	
	private RestUnicastCommunication ruc;
	private String[] graphuri;
	
	@Before
	public void setUp() throws Exception {
		//super.testingPort = 8183;
		super.setUp();
		this.graphuri = new String[2];
		SemanticFactory.initialize( new MicrojenaFactory() );
		
		this.controller.getDataAccessService().startup();
		this.controller.getDataAccessService().joinSpace(this.spaceURI);
		
		Graph graph = new Graph(
				"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gómez-Goiri\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gómez-Goiri\" . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
				"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/depiction> <http://aitor.gomezgoiri.net/profile.jpg> . \n",
				SemanticFormat.NTRIPLES);
		this.graphuri[0] = this.controller.getDataAccessService().write(this.spaceURI, graph);
		
		graph = new Graph(
				"<http://facebook.com/user/yoda> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/name> \"Yoda\" . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/title> \"Jedi\" . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/givenname> \"Yoda\" . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com> . \n" +
				"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/depiction> <http://upload.wikimedia.org/wikipedia/en/9/96/CGIYoda.jpg> . \n",
				SemanticFormat.NTRIPLES);
		this.graphuri[1] = this.controller.getDataAccessService().write(this.spaceURI, graph);
		
		this.ruc = new RestUnicastCommunication(getBaseURL());
		this.ruc.startup();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		this.ruc.shutdown();
	}
	
	/**
	 * Test method for {@link otsopack.commons.network.communication.RestUnicastCommunication#query(java.lang.String, otsopack.commons.data.Template, otsopack.commons.data.SemanticFormat, long)}.
	 * @throws Exception 
	 */
	@Test
	public void testQuery() throws Exception {
		final Graph [] graphs = this.ruc.query(this.spaceURI,
								WildcardTemplate.createWithNull(null,"http://xmlns.com/foaf/0.1/title"),
								this.arguments);
		assertEquals(1, graphs.length);
		final Graph graph = graphs[0];
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("http://xmlns.com/foaf/0.1/title") );
	}

	/**
	 * Test method for {@link otsopack.commons.network.communication.RestUnicastCommunication#read(java.lang.String, otsopack.commons.data.Template, otsopack.commons.data.SemanticFormat, long)}.
	 * @throws Exception 
	 */
	@Test
	public void testReadStringTemplateSemanticFormatLong() throws Exception {
		final Graph graph = this.ruc.read(this.spaceURI,
				WildcardTemplate.createWithURI("http://facebook.com/user/yoda","http://xmlns.com/foaf/0.1/homepage","http://yodaknowsit.com"),
				this.arguments);
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com>") );
	}
		
	/**
	 * Test method for {@link otsopack.commons.network.communication.RestUnicastCommunication#read(java.lang.String, java.lang.String, otsopack.commons.data.SemanticFormat, long)}.
	 * @throws Exception 
	 */
	@Test
	public void testReadStringStringSemanticFormatLong() throws Exception {
		final Graph graph = this.ruc.read(this.spaceURI,
											this.graphuri[0],
											this.arguments);
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person>") );
	}
	
	/**
	 * Test method for {@link otsopack.commons.network.communication.RestUnicastCommunication#take(java.lang.String, otsopack.commons.data.Template, otsopack.commons.data.SemanticFormat, long)}.
	 */
	@Test
	public void testTakeStringTemplateSemanticFormatLong() throws Exception {
		Graph graph = this.ruc.take(this.spaceURI,
									WildcardTemplate.createWithURI("http://facebook.com/user/yoda","http://xmlns.com/foaf/0.1/homepage","http://yodaknowsit.com"),
									this.arguments);
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com>") );
		
		
		//TODO decide what to do in this case
		graph = this.ruc.take(this.spaceURI,
									WildcardTemplate.createWithURI("http://facebook.com/user/yoda","http://xmlns.com/foaf/0.1/homepage","http://yodaknowsit.com"),
									this.arguments);
		assertNull( graph );
		
		graph = this.ruc.take(this.spaceURI,
				WildcardTemplate.createWithURI("http://aitor.gomezgoiri.net/me","http://xmlns.com/foaf/0.1/homepage","http://aitor.gomezgoiri.net"),
				this.arguments);
		
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net>") );
	}

	/**
	 * Test method for {@link otsopack.commons.network.communication.RestUnicastCommunication#take(java.lang.String, java.lang.String, otsopack.commons.data.SemanticFormat, long)}.
	 * @throws Exception 
	 */
	@Test
	public void testTakeStringStringSemanticFormatLong() throws Exception {
		Graph graph = this.ruc.take(this.spaceURI,
									this.graphuri[1],
									this.arguments);
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com>") );
		
		
		//TODO decide what to do in this case
		graph = this.ruc.take(this.spaceURI,
								this.graphuri[1],
								this.arguments);
		assertNull( graph );
		
		
		graph = this.ruc.take(this.spaceURI,
								this.graphuri[0],
								this.arguments);
		
		assertEquals( SemanticFormat.NTRIPLES, graph.getFormat() );
		assertTrue( graph.getData().contains("<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net>") );
	}
}