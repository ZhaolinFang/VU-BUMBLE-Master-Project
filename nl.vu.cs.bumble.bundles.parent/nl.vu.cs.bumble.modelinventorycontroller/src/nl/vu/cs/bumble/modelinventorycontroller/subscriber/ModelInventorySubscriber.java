package nl.vu.cs.bumble.modelinventorycontroller.subscriber;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import org.eclipse.emfcloud.modelserver.client.ModelServerClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ModelInventorySubscriber{
	
	private ModelServerClient ct;
	
	public ModelInventorySubscriber() throws MalformedURLException, JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
		this.ct = new ModelServerClient("http://localhost:8081/api/v2/");
		String subscriptionId = "ModelInventory.xmi";
		this.ct.subscribe(subscriptionId, new ModelInventorySubscriptionListener());
//		System.out.println("running");
	}

}


