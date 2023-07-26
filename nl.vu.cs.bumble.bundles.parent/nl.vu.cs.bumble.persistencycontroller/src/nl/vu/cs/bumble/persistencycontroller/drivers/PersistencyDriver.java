package nl.vu.cs.bumble.persistencycontroller.drivers;

public abstract class PersistencyDriver {
	
	protected DriverType type;  
	protected String url;
	protected String modelName;
	
	PersistencyDriver(String url, String modelName)
	{
		this.url = url;
		this.modelName = modelName;
	}
	
	public abstract boolean save() throws Exception;
	
	public boolean saveAll() {
		System.out.println("All models are saved!");
		return true;
	};
	
	public abstract boolean fetch();
	
	public boolean fetchAll() {
		System.out.println("All models are fetched!");
		return true;
	};
}