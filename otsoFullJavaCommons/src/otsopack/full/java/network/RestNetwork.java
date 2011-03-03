package otsopack.full.java.network;

import otsopack.commons.IController;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.ICoordination;
import otsopack.commons.network.INetwork;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.util.collections.Set;
import otsopack.full.java.network.communication.RestServer;

public class RestNetwork implements INetwork {
	
	RestServer rs;
	
	public RestNetwork(IController controller) {
		this.rs = new RestServer();
		this.rs.getAttributes().put("controller", controller);
	}

	@Override
	public void startup() throws TSException {
		try {
			this.rs.startup();
		} catch (Exception e) {
			throw new TSException("Rest server could not be started. " + e.getMessage());
		}
	}

	@Override
	public void shutdown() throws TSException {
		try {
			this.rs.shutdown();
		} catch (Exception e) {
			throw new TSException("Rest server could not be restarted. " + e.getMessage());
		}
	}
	
	@Override
	public IGraph read(String spaceURI, String graphURI, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph read(String spaceURI, ITemplate template, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph take(String spaceURI, String graphURI, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph take(String spaceURI, ITemplate template, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGraph query(String spaceURI, ITemplate template, long timeout)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribe(String spaceURI, ITemplate template,
			INotificationListener listener) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public String advertise(String spaceURI, ITemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unadvertise(String spaceURI, String advertisementURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void demand(String spaceURI, ITemplate template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void suggest(String spaceURI, IGraph graph) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(
			String spaceURI, IGraph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			IGraph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set getSpaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getJoinedSpaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSpace(String spaceURI) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinSpace(String spaceURI) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void leaveSpace(String spaceURI) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getPeerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommunication getCommunication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICoordination getCoordination() {
		// TODO Auto-generated method stub
		return null;
	}

}