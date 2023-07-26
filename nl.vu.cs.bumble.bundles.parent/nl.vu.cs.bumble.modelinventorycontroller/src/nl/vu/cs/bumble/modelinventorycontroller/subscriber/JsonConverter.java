package nl.vu.cs.bumble.modelinventorycontroller.subscriber;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emfcloud.modelserver.client.ModelServerClient;
import org.eclipse.emfcloud.modelserver.client.Response;
import org.eclipse.emfcloud.modelserver.common.codecs.EncodingException;
import org.eclipse.emfcloud.modelserver.jsonpatch.JsonPatch;
import org.eclipse.emfcloud.modelserver.jsonpatch.Operation;
import org.eclipse.emfcloud.modelserver.jsonpatch.Value;
import org.eclipse.emfcloud.modelserver.jsonpatch.impl.AddImpl;
import org.eclipse.emfcloud.modelserver.jsonpatch.impl.ObjectValueImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	private ModelServerClient clientForAPI;
	private HashMap<Integer, String> map;
	private String modelName;
	
	JsonConverter() throws MalformedURLException, JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException{
		this.clientForAPI = new ModelServerClient("http://localhost:8081/api/v2/");
		this.map = new HashMap<>();
		initMap();
	}
	
	public String run(JsonPatch patch) throws InterruptedException, ExecutionException, EncodingException, JsonMappingException, JsonProcessingException {
		
		if(getOp(patch).equals("add")) {
			if(getPath(patch).contains("collaborationSessions")) {
				//1. get model name
				String modelName = getAddValue(patch, "collaborateOn");
				this.modelName = modelName;
				// update map
				map.put(map.size(), modelName);
				//2. get model location in Model using name 
				String location = getLocationByAPI(modelName);
				
				return location;
			}
		}
		else if(getOp(patch).equals("remove")) {
			if(getPath(patch).contains("collaborationSessions")) {
				int id = getID(patch);
				String collaborateOn = id == -1 ? map.get(map.size() - 1) : map.get(id);
				this.modelName = collaborateOn;
				String location = getLocationByAPI(collaborateOn);
				updateMap(id);
				
				return location;
			}
		}
		return null;
	}
	
	public String getModelName() {
		return this.modelName;
	}
	
	public String getOp(JsonPatch patch) {
		Operation pat = patch.getPatch().get(0);
		String op = pat.getOp().toString();
		
		return op;
	}
	
	private String getPath(JsonPatch patch) {
		Operation pat = patch.getPatch().get(0);
		String path = pat.getPath().toString();
		
		return path;
	}
	
	private int getID(JsonPatch patch) {		
		int id = getPath(patch).charAt(23) == '-' ? -1 : Integer.parseInt(getPath(patch).charAt(23) + "");
		if(isSinglePatch(patch)) {
			return id;
		}
		else {
			return id - 1;
		}
	}
	
	private boolean isSinglePatch(JsonPatch patch) {
		return getPatchNum(patch) == 1 ? true : false;
	}
	
	private int getPatchNum(JsonPatch patch) {
		return patch.getPatch().size();
	}
	
	private void updateMap(int id) {
		if(id == map.size() - 1) {
			map.remove(id);
		}
		else {
			for(int i = 0; i < map.size(); i++) {
				if(i <= id) {
					continue;
				}
				else {
					map.put(i - 1, map.get(i));
				}
			}
			
			map.remove(map.size() - 1);
		}
	}
	//
	private String getAddValue(JsonPatch patch, String featureName) {
		if(getOp(patch).equals("add")){
			AddImpl add = (AddImpl)patch.getPatch().get(0);
			Value v = add.getValue();
			ObjectValueImpl ov = (ObjectValueImpl)v;
			DynamicEObjectImpl de = (DynamicEObjectImpl)(ov.getValue());
			EStructuralFeature feature = de.eClass().getEStructuralFeature(featureName);
			
			return de.eGet(feature).toString();
		}
		//TODO: throw NotAddException
		else {
			return null;
		}
	}
	//
	private String getLocationByAPI(String modelName) throws InterruptedException, ExecutionException, EncodingException, JsonMappingException, JsonProcessingException {
		JsonNode modelInventory = getResponseBody();
		JsonNode models = modelInventory.findValue("model");
		Iterator<JsonNode> temp = models.elements();
		
		while(temp.hasNext()) {
			JsonNode tempNode = temp.next();
			if(tempNode.findValue("uri").asText().equals(modelName)) {
				return tempNode.findValue("location").asText();
		    }
		}
		return null;
	}
	
	private void initMap() throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {	
		JsonNode modelInventory = getResponseBody();	
		JsonNode sessions = modelInventory.findValue("collaborationSessions");
		Iterator<JsonNode> temp = sessions.elements();
		
		int counter = 0;
		while(temp.hasNext()) {
			JsonNode tempSession = temp.next();
			String collaborateOn = tempSession.findValue("collaborateOn") != null ? tempSession.findValue("collaborateOn").asText() : ""; 
			map.put(counter, collaborateOn);
			counter++;
		}
	}
	
	private JsonNode getResponseBody() throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {
		CompletableFuture<Response<String>> future = clientForAPI.get("ModelInventory.xmi");
		String body = future.get().body();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode modelInventory = mapper.readTree(body);
		
		return modelInventory;
	}
}
