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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.full.java.network.coordination.discovery;

import static org.junit.Assert.*;
import org.junit.Test;

import otsopack.full.java.network.coordination.SpaceManager;


public class MultiplexerDiscoveryTest {
	private final SimpleDiscovery discovery1 = new SimpleDiscovery(new SpaceManager("http://ts.alimerka.es/discovery/sample01/"), new SpaceManager("http://ts.alimerka.es/discovery/sample02/"));
	private final SimpleDiscovery discovery2 = new SimpleDiscovery(new SpaceManager("http://sample01.morelab.deusto.es"), new SpaceManager("http://sample02.morelab.deusto.es"));
	
	@Test
	public void testSingle() throws DiscoveryException{
		final MultiplexerDiscovery md = new MultiplexerDiscovery(this.discovery1);
		final SpaceManager [] managers = md.getSpaceManagers("");
		assertArrayEquals(new SpaceManager[]{ 
				new SpaceManager("http://ts.alimerka.es/discovery/sample01/"),
				new SpaceManager("http://ts.alimerka.es/discovery/sample02/"),
		}, managers);
	}
	
	@Test
	public void testMultiple() throws DiscoveryException{
		final MultiplexerDiscovery md = new MultiplexerDiscovery(this.discovery1, this.discovery2);
		final SpaceManager [] managers = md.getSpaceManagers("");
		assertArrayEquals(new SpaceManager[]{ 
				new SpaceManager("http://ts.alimerka.es/discovery/sample01/"),
				new SpaceManager("http://ts.alimerka.es/discovery/sample02/"),
				new SpaceManager("http://sample01.morelab.deusto.es"),
				new SpaceManager("http://sample02.morelab.deusto.es"),
		}, managers);
	}
}
