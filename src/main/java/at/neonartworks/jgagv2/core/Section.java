package at.neonartworks.jgagv2.core;

import java.util.List;

import at.neonartworks.jgagv2.core.post.Tag;

public class Section
{
	private int id;
	private String url;
	private String name;
	private String desccription;
	private boolean userUploadEnabled;
	private String listType;
	private String listType2;
	private String ogImageUrl;
	private List<Tag> featuredTags;
	private boolean isSensitive;

	public Section(int id, String url, String name, String desccription, boolean userUploadEnabled, String listType,
			String listType2, String ogImageUrl, List<Tag> featuredTags, boolean isSensitive)
	{
		super();
		this.id = id;
		this.url = url;
		this.name = name;
		this.desccription = desccription;
		this.userUploadEnabled = userUploadEnabled;
		this.listType = listType;
		this.listType2 = listType2;
		this.ogImageUrl = ogImageUrl;
		this.featuredTags = featuredTags;
		this.isSensitive = isSensitive;
	}

	public int getId()
	{
		return id;
	}

	public String getUrl()
	{
		return url;
	}

	public String getName()
	{
		return name;
	}

	public String getDesccription()
	{
		return desccription;
	}

	public boolean getUserUploadEnabled()
	{
		return userUploadEnabled;
	}

	public String getListType()
	{
		return listType;
	}

	public String getListType2()
	{
		return listType2;
	}

	public String getOgImageUrl()
	{
		return ogImageUrl;
	}

	public List<Tag> getFeaturedTags()
	{
		return featuredTags;
	}

	public boolean isSensitive()
	{
		return isSensitive;
	}

	@Override
	public String toString()
	{
		return "Section [id=" + id + ", url=" + url + ", name=" + name + ", desccription=" + desccription
				+ ", userUploadEnabled=" + userUploadEnabled + ", listType=" + listType + ", listType2=" + listType2
				+ ", ogImageUrl=" + ogImageUrl + ", featuredTags=" + featuredTags + ", isSensitive=" + isSensitive
				+ "]";
	}

}
