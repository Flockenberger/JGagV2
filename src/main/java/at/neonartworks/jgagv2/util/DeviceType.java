package at.neonartworks.jgagv2.util;

public enum DeviceType
{
	ANDROID("android");

	private String id;

	DeviceType(String id)
	{
		this.id = id;
	}

	public String GetDevice()
	{
		return this.id;
	}
}
