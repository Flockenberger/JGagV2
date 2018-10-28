package at.neonartworks.jgagv2.api;

public enum APIstatus
{
	SUCCESS("Success"), FAILURE("Failure");

	private String permission;

	APIstatus(String perm)
	{
		this.permission = perm;
	}
	
	public String getStatus()
	{
		return this.permission;
	}
}
