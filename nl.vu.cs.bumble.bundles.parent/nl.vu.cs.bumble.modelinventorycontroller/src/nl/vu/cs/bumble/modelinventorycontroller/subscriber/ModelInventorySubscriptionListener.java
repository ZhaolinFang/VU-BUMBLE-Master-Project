package nl.vu.cs.bumble.modelinventorycontroller.subscriber;

import java.net.MalformedURLException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emfcloud.modelserver.client.EObjectSubscriptionListener;
import org.eclipse.emfcloud.modelserver.client.ModelServerNotification;
import org.eclipse.emfcloud.modelserver.client.Response;
import org.eclipse.emfcloud.modelserver.emf.common.codecs.JsonCodecV2;
import org.eclipse.emfcloud.modelserver.jsonpatch.JsonPatch;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.vu.cs.bumble.persistencycontroller.controller.PersistencyController;


public class ModelInventorySubscriptionListener extends EObjectSubscriptionListener {
	
	private JsonConverter converter;
	private PersistencyController persistency;
	
	public ModelInventorySubscriptionListener() throws MalformedURLException, JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
		super(new JsonCodecV2());
		this.converter = new JsonConverter();
	} 

	@Override
	public void onOpen(final Response<String> response) {
	    System.out.println("Connected: " + response.getMessage());
	  }

	  @Override
	  public void onSuccess(final Optional<String> message) {
	    System.out.println("Success: " + message.get());
	  }

	  @Override
	  public void onIncrementalUpdate(final JsonPatch patch) {
		 try {
		  
		  String location = this.converter.run(patch);
		  String modelName = this.converter.getModelName();
		  String op = this.converter.getOp(patch);
		  this.persistency = new PersistencyController(location, modelName);
		  
		  if(op.equals("add"))
		  {
			  persistency.fetch();
		  }
		  
		  if(op.equals("remove"))
		  {
			  persistency.save();
		  }
		  
		 }catch(Exception e){
			 e.printStackTrace();
		 };

	     System.out.println("Patch update from model server received: " + patch.toString());
	  }

	  @Override
	  public void onDirtyChange(final boolean isDirty) {
	    System.out.println("Dirty State: " + isDirty);
	  }

	  @Override
	  public void onUnknown(final ModelServerNotification notification) {
	    System.out.println("Unknown notification of type " + notification.getType() + ": " + notification.getData());
	  }

	  @Override
	  public void onFullUpdate(final EObject fullUpdate) {
	    System.out.println("Full <EObject> update from model server received: " + fullUpdate.toString());
	  }

	  @Override
	  public void onError(final Optional<String> message) {
	    System.out.println("Error from model server received: " + message.get());
	  }

	  @Override
	  public void onFailure(final Throwable t, final Response<String> response) {
	    System.out.println("Failure: " + response.getMessage());
	    t.printStackTrace();
	  }

	  @Override
	  public void onFailure(final Throwable t) {
	    System.out.println("Failure: ");
	    t.printStackTrace();
	  }

	  @Override
	  public void onClosing(final int code, final String reason) {
	    System.out.println("Closing connection to model server, reason: " + reason);
	  }

	  @Override
	  public void onClosed(final int code, final String reason) {
	    System.out.println("Closed connection to model server, reason: " + reason);
	  }
	
}
