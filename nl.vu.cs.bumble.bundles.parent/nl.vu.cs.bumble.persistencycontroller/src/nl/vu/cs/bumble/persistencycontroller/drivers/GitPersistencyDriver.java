package nl.vu.cs.bumble.persistencycontroller.drivers;

import nl.vu.cs.bumble.gitdriver.*;


public class GitPersistencyDriver extends PersistencyDriver {
	
	public GitPersistencyDriver(String url, String modelName) {
		super(url, modelName);
	}

	public boolean fetch() {
		boolean isSuccess = GitDriver.fetch(modelName);
		System.out.println("The model " + modelName + " at " + url + " is fetched!");
		return isSuccess;
	}
	
	public boolean save() throws Exception {
		boolean isSuccess = GitDriver.save(modelName, url);
		System.out.println("The model " + modelName + " at " + url + " is saved!");
		return isSuccess;
	}

}