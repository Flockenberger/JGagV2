package at.neonartworks.jgagv2.util;

public enum RESTType
{
	GET("GET"), POST("POST");

	private String id;

	RESTType(String id)
	{
		this.id = id;
	}

	public String getType()
	{
		return this.id;
	}
}
