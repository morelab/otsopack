package otsopack.full.java.network.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Component;
import org.restlet.data.Protocol;

import otsopack.full.java.network.communication.resources.graphs.GraphsResource;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesResource;

public class RestServer {
	public static final int DEFAULT_PORT = 8182;
	
	private final int port;
	private final Component component;
	
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(PrefixesResource.getRoots());
		addPaths(GraphsResource.getRoots());
	}
	
	private static RestServer server = null;
	
	public static RestServer getCurrent(){
		return server;
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public RestServer(int port) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    for(String pattern : RestServer.PATHS.keySet())
	    	this.component.getDefaultHost().attach(pattern, RestServer.PATHS.get(pattern));
	    
		server = this;
	}
	
	public RestServer(){
		this(DEFAULT_PORT);
	}
	
	public ConcurrentMap<String, Object> getAttributes(){
		return this.attributes;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void attach(String pattern, Class<?> targetClass){
		this.component.getDefaultHost().attach(pattern, targetClass);
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
		PrefixesResource.clear();
	}
}
