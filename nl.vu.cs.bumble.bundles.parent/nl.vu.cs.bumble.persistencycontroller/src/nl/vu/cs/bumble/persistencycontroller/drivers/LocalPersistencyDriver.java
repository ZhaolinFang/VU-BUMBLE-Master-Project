package nl.vu.cs.bumble.persistencycontroller.drivers;

public class LocalPersistencyDriver extends PersistencyDriver {
	public LocalPersistencyDriver(String url, String modelName) {
		super(url, modelName);
	}

	public boolean fetch() {
		System.out.println("The model " + modelName + " at " + url + " is fetched!");
		return true;
	}
	
	public boolean save() {
		System.out.println("The model " + modelName + " at " + url + " is saved!");
		return true;
	}
	
}