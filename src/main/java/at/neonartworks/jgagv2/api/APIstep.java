package at.neonartworks.jgagv2.api;

public enum APIstep
{

	META_DATA("metaData"), IMAGE_DATA("imageData"), URL_DATA("urlData"), TRIGGER_CREATION("triggerCreation");

	private String step;

	APIstep(String step)
	{
		this.step = step;
	}

	public String getStep()
	{
		return this.step;
	}

}
