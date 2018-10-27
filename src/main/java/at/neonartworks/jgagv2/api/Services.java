package at.neonartworks.jgagv2.api;

public enum Services
{
	API("http://api.9gag.com"), COMMENT_CDN("http://comment-cdn.9gag.com"), COMMENT("http://comment.9gag.com"),
	NOTIFY("http://notify.9gag.com"), AD("http://ad.9gag.com"), ADMIN("http://admin.9gag.com");

	private String service;

	Services(String service)
	{
		this.service = service;
	}

	public String getService()
	{
		return this.service;
	}
}
