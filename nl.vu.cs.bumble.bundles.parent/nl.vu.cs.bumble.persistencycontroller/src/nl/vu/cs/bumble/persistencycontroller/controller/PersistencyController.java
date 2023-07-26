package nl.vu.cs.bumble.persistencycontroller.controller;

import nl.vu.cs.bumble.persistencycontroller.drivers.*;

public class PersistencyController{
	private PersistencyDriver driver = null;
	private DriverType type;
	private String url;
	private String modelName;
	
	public PersistencyController() {
		
	}
	
	public PersistencyController(String url, String modelName){
		this.url = url;
		this.modelName = modelName;
		setType(url);
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public void setType(String url) {
		this.url = url;
		if(url.contains("Git"))
		{
			this.type = DriverType.Git;
		}
		else if(url.contains("MongoDB"))
		{
			this.type = DriverType.MongoDB;
		}
		else
		{
			this.type = DriverType.Local;
		}
		getDriver();
	}
	
	private void getDriver() {
		switch(type) {
			case Git: driver = new GitPersistencyDriver(url, modelName); break;
			case MongoDB: driver = new MongoDBPersistencyDriver(url, modelName); break;
			case Local: driver = new LocalPersistencyDriver(url, modelName); break;
			// TODO: throw a DriverNotFound Exception
			default: return;
		}
		
	}
	
	public boolean fetch() {
		return driver.fetch();
	}

	public boolean fetchAll() {
		return driver.fetchAll();
	}
	
	public boolean save() {
		return driver.save();
	}
	
	public boolean saveAll() {
		return driver.saveAll();
	}
	
}