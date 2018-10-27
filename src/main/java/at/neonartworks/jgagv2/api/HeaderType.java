package at.neonartworks.jgagv2.api;

public enum HeaderType
{
	GAG_GAG_TOKEN("9GAG-9GAG_TOKEN"), GAG_TIMESTAMP("9GAG-TIMESTAMP"), GAG_APP_ID("9GAG-APP_ID"),
	X_Package_ID("X-Package-ID"), GAG_DEVICE_UUID("9GAG-DEVICE_UUID"), X_Device_UUID("X-Device-UUID"),
	GAG_DEVICE_TYPE("9GAG-DEVICE_TYPE"), GAG_BUCKET_NAME("9GAG-BUCKET_NAME"),
	GAG_REQUEST_SIGNATURE("9GAG-REQUEST-SIGNATURE");
	private String id;

	HeaderType(String id)
	{
		this.id = id;
	}

	public String getHeader()
	{
		return this.id;
	}
}
