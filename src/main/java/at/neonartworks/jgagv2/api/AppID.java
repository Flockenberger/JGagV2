package at.neonartworks.jgagv2.api;

public enum AppID
{
	APP_ID("com.ninegag.android.app"), COMMENT_CDN("a_dd8f2b7d304a10edaf6f29517ea0ca4100a43d1b");
	
	private String id;

	AppID(String id)
	{
		this.id = id;
	}

	public String getID()
	{
		return this.id;
	}
}
