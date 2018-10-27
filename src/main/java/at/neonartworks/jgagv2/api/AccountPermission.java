package at.neonartworks.jgagv2.api;

public enum AccountPermission
{
	KEY_PERMISSION_9GAG_PRO("9GAG_Pro"), KEY_PERMISSION_9GAG_STAFF("9GAG_Staff");

	private String permission;

	AccountPermission(String perm)
	{
		this.permission = perm;
	}

	public String getPermission()
	{
		return this.permission;
	}
}
