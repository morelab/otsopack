package otsopack.full.java.network.communication.resources.graphs;

import otsopack.full.java.network.communication.resources.AbstractServerResource;


public class WildcardGraphResource extends AbstractServerResource implements IWildcardGraphResource {

	public static final String ROOT = WildcardsGraphResource.ROOT + "/{subject}/{predicate}/{object}";

	@Override
	public String toJson(){
		final String subject   = getArgument("subject");
		final String predicate = getArgument("predicate");
		final String object    = getArgument("object");
		
		System.out.println(getContext());
		
		
		return "bar";
	}
}
