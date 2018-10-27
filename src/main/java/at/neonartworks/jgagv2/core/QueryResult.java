package at.neonartworks.jgagv2.core;

import java.util.ArrayList;
import java.util.List;

public class QueryResult
{

	private List<Post> posts = new ArrayList<Post>();
	private List<Tag> relatedTags = new ArrayList<Tag>();
	private Tag searchedTag;

	public QueryResult(List<Post> posts, List<Tag> relatedTags, Tag searchedTag)
	{
		super();
		this.posts = posts;
		this.relatedTags = relatedTags;
		this.searchedTag = searchedTag;
	}

	@Override
	public String toString()
	{
		return "QueryResult [posts=" + posts + ", relatedTags=" + relatedTags + ", searchedTag=" + searchedTag + "]";
	}

	public List<Post> getPosts()
	{
		return posts;
	}

	public List<Tag> getRelatedTags()
	{
		return relatedTags;
	}

	public Tag getSearchedTag()
	{
		return searchedTag;
	}

}
