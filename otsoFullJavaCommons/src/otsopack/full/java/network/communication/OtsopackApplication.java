package otsopack.full.java.network.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;
import org.restlet.service.MetadataService;

import otsopack.commons.IController;
import otsopack.full.java.network.communication.representations.OtsopackConverter;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesResource;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;
import otsopack.full.java.network.communication.resources.spaces.SpacesResource;

public class OtsopackApplication extends Application {
	
	private final Map<String, Class<?>> resources;
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private final PrefixesStorage prefixesStorage = new PrefixesStorage();
	
	private static final String CONTROLLER_PROPERTY_NAME = "controller";

	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(PrefixesResource.getRoots());
		addPaths(SpacesResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	
	public OtsopackApplication(){
		this.resources = PATHS;
	}
	
	public static void registerExtensions(MetadataService metadataService){
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_TURTLE, MediaType.APPLICATION_RDF_TURTLE);
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_RDF_XML, MediaType.APPLICATION_RDF_XML);
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_ACROSS_MULTIPART, OtsopackConverter.ACROSS_MULTIPART_MEDIA_TYPE);
		// For some reason by default nt is registered in Restlet as TEXT_PLAIN
		metadataService.addExtension(OtsopackConverter.MEDIA_TYPE_NTRIPLES, MediaType.TEXT_RDF_NTRIPLES, true); 
		// n3 is already registered
	}
	
	@Override
	public Restlet createInboundRoot(){
		registerExtensions(getMetadataService());
        final Router router = new Router(getContext());
        
	    for(String pattern : this.resources.keySet())
	    	router.attach(pattern, this.resources.get(pattern));
        
        return router;
	}
	
	public ConcurrentMap<String, Object> getAttributes(){
		return this.attributes;
	}
	
	public IController getController(){
		return (IController)this.attributes.get(CONTROLLER_PROPERTY_NAME);
	}
	
	public void setController(IController controller){
		this.attributes.put(CONTROLLER_PROPERTY_NAME, controller);
	}
	
	public PrefixesStorage getPrefixesStorage(){
		return this.prefixesStorage;
	}
}
