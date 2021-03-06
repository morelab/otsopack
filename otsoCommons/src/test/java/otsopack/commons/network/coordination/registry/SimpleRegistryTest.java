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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.commons.network.coordination.registry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import otsopack.commons.network.coordination.IDiscovery;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.discovery.SimpleDiscovery;
import otsopack.commons.network.coordination.registry.SimpleRegistry;
import otsopack.commons.network.coordination.spacemanager.HttpSpaceManager;
import otsopack.commons.network.coordination.spacemanager.http.SpaceManagerManager;

public class SimpleRegistryTest {
	
	private int port = 18087;
	private SpaceManagerManager manager;
	
	@Before
	public void setUp() throws Exception {
		this.manager = new SpaceManagerManager(this.port);
		this.manager.startSpaceManagerServer();
	}
	
	@After
	public void tearDown() throws Exception {
		this.manager.stopSpaceManagerServer();
	}
	
	@Test
	public void testRegistry() throws Exception {
		
		final String spaceURI = "http://space1/";
		
		final IDiscovery discovery = new SimpleDiscovery(new HttpSpaceManager(this.manager.createClientAddress()));
		final SimpleRegistry registry = new SimpleRegistry(discovery){
			@Override
			public int getInterval(){
				return 10;
			}
		};
		registry.joinSpace(spaceURI);
		registry.startup();
		
		final Set<Node> nodes = registry.getNodesBaseURLs(spaceURI);
		assertThat(nodes, hasItem(SpaceManagerManager.NODE1));
		assertThat(nodes, hasItem(SpaceManagerManager.NODE2));
		
		registry.shutdown();
		for(int i = 0; i < 100; ++i){
			if(!registry.isAlive())
				break;
			Thread.sleep(10);
		}
		assertFalse(registry.isAlive());
	}
}
