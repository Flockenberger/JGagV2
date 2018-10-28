package at.neonartworks.jgagv2.api;

public enum APIpath
{
	USER_TOKEN("/v2/user-token"), POST_LIST("/v2/post-list"), TOP_COMMENTS("/v1/topComments.json"),
	COMMENTS("/v1/comments.json"), GROUP_LIST("/v2/group-list"), POST_SUBMIT("/v2/post-submit/"),
	USER_INFO("/v2/user-info"), URL_INFO("/v2/url-info"), TAGS("/v2/tags"), TAG_SEARCH("/v2/tag-search"),
	SEARCH("/v2/search"), USER_NOTIFICATIONS("/v2/user-notifications"), POST("/v2/post"),
	POST_SUBMIT_ARTICLE("/v2/post-sbumit/step"), POST_SUBMIT_CREATE_MEDIA1("/v2/post-sbumit/step"),
	POST_SUBMIT_CREATE_MEDIA2("/v2/post-submit/step"), EMPTY("");

	private String id;

	APIpath(String id)
	{
		this.id = id;
	}

	public String getPath()
	{
		return this.id;
	}
}
