package at.neonartworks.jgagv2.util;

public enum APIPath
{
	USER_TOKEN("/v2/user-token"), POST_LIST("/v2/post-list"), TOP_COMMENTS("/v1/topComments.json"),
	COMMENTS("/v1/comments.json"), GROUP_LIST("/v2/group-list"), POST_SUBMIT("/v2/post-submit"),
	USER_INFO("/v2/user-info"), URL_INFO("/v2/url-info"), TAGS("/v2/tags"), TAG_SEARCH("/v2/tag-search"),
	SEARCH("/v2/search"), USER_NOTIFICATIONS("/v2/user-notifications");

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
