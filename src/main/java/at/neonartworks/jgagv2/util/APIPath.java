package at.neonartworks.jgagv2.util;

public enum APIPath
{
	USER_TOKEN("/v2/user-token"), POST_LIST("/v2/post-list"), TOP_COMMENTS("/v1/topComments.json"),
	COMMENTS("/v1/comments.json"), GROUP_LIST("/v2/group-list"), POST_SUBMIT("/v2/post-submit");
	
	private String id;

	APIPath(String id)
	{
		this.id = id;
	}

	public String getPath()
	{
		return this.id;
	}
}
