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
package otsopack.otsoDroid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import otsopack.otsoDroid.configuration.JxmeConfiguration;
import otsopack.otsoDroid.kernel.Kernel;
import otsopack.otsoCommons.data.IGraph;
import otsopack.otsoCommons.data.impl.SemanticFactory;
import otsopack.otsoCommons.data.impl.microjena.MicrojenaFactory;
import otsopack.otsoCommons.exceptions.SpaceAlreadyExistsException;
import otsopack.otsoCommons.exceptions.TSException;
import otsopack.otsoCommons.exceptions.TripleParseException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Thread t = new Thread(){
			@Override
			public void run(){
				initialize();
				startup();
			}
		};
		t.start();
		setContentView(R.layout.main);
	}
	
	public static final  String space = "http://www.morelab.deusto.es/scenario/havoc";
	Kernel kernel;
	SemanticFactory factory;
	
	private void initialize(){
		kernel = new Kernel();
		SemanticFactory.initialize(new MicrojenaFactory());
		factory = new SemanticFactory();
		Log.e("MainActivity", "SemanticFactory created");
	}
	
	private void configureJxme() {
		JxmeConfiguration jxmeConfiguration = JxmeConfiguration.getConfiguration();
		
		jxmeConfiguration.setPeerName("mymobile");
		
		jxmeConfiguration.setUseDefaultSeeds(false);
		jxmeConfiguration.setRendezvousName("IsmedRendezvous");
		try {
			jxmeConfiguration.setRendezvousURI(new URI("tcp://192.168.59.6:9701"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		jxmeConfiguration.setTcpPort(48000);
		jxmeConfiguration.setTcpStartPort(48101);
		jxmeConfiguration.setTcpEndPort(48200);
		jxmeConfiguration.setRendezvousConnectionTimeout(0);
	}
	
	private void startup(){
		configureJxme();
		try {
			kernel.startup();
			try{
				kernel.createSpace(space);
			} catch (SpaceAlreadyExistsException e) {
				e.printStackTrace();
			}
			kernel.joinSpace(space);
		} catch (TSException e) {
			e.printStackTrace();
		}
		try {
			kernel.write(space, factory.createTriple("http://www.morelab.deusto.es/sub", "http://www.morelab.deusto.es/pred", "http://www.morelab.deusto.es/obj"));
			IGraph graph = kernel.query(space, factory.createTemplate("?s ?p ?o ."), 5000);
			final Enumeration<?> enume = graph.elements();
			while(enume.hasMoreElements())
				System.out.println(enume.nextElement());
		} catch (TripleParseException e) {
			e.printStackTrace();
		} catch (TSException e) {
			e.printStackTrace();
		}
	}
}