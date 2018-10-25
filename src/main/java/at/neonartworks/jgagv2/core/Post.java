package at.neonartworks.jgagv2.core;

import javax.json.JsonObject;

public class Post
{

	private String id;
	private String title;
	private String url;
	private String type;
	private String mediaURL;
	private JsonObject json;

	public Post(JsonObject obj)
	{
		this.json = obj;
		init(this.json);
	}

	private void init(JsonObject obj)
	{
		id = obj.getString("id");
		title = obj.getString("title");
		url = obj.getString("url");
		type = obj.getString("type");
		mediaURL = fetchMediaURL();
	}

	private String fetchMediaURL()
	{
		if (this.type.equals("Photo"))
		{
			return this.json.getJsonObject("images").getJsonObject("image700").getString("url");
		} else if (this.type.equals("Animated"))
			return this.json.getJsonObject("images").getJsonObject("image460sv").getString("url");
		return "NULL";
	}

	@Override
	public String toString()
	{
		return "Post [id=" + id + ", title=" + title + ", url=" + url + ", type=" + type + ", mediaURL=" + mediaURL
				+ ", json=" + json + "]";
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getMediaURL()
	{
		return mediaURL;
	}

	public void setMediaURL(String mediaURL)
	{
		this.mediaURL = mediaURL;
	}

	public JsonObject getJson()
	{
		return json;
	}

	public void setJson(JsonObject json)
	{
		this.json = json;
	}

}
