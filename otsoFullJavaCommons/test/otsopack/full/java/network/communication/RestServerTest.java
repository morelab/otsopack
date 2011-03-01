package otsopack.full.java.network.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.resource.ClientResource;

public class RestServerTest {
	RestServer rs;
	
	@Before
	public void setUp() throws Exception {
		this.rs = new RestServer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartup() throws Exception {
		this.rs.startup();
		
		ClientResource cr = new ClientResource("http://localhost:8182/prefixes/rdf");
		IPrefixResource prefmng = cr.wrap(IPrefixResource.class);
		assertNull( prefmng );
		
		this.rs.shutdown();
	}

	@Test
	public void testCreatePrefix() throws Exception {
		this.rs.startup();
		
		ClientResource cr = new ClientResource("http://localhost:8182/prefixes");
		IPrefixesResource prefrsc = cr.wrap(IPrefixesResource.class);
		prefrsc.create( new Prefix("rdf", new URI("http://www.w3.org/1999/02/22-rdf-syntax-ns#")) );
		prefrsc.create( new Prefix("rdfs", new URI("http://www.w3.org/2000/01/rdf-schema#")) );
		prefrsc.create( new Prefix("xsd", new URI("http://www.w3.org/2001/XMLSchema#")) );
		prefrsc.create( new Prefix("rdf", new URI("http://www.w3.org/2002/07/owl#")) );
		
		Collection<Prefix> prefixes = prefrsc.retrieve();
		assertEquals(prefixes.size(), 4);
		
		this.rs.shutdown();
	}
}
