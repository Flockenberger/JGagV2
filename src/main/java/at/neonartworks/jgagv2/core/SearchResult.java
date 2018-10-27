package at.neonartworks.jgagv2.core;

import java.util.ArrayList;
import java.util.List;

import at.neonartworks.jgagv2.core.post.Post;
import at.neonartworks.jgagv2.core.post.Tag;

public class SearchResult
{

	private List<Post> posts = new ArrayList<Post>();
	private List<Tag> relatedTags = new ArrayList<Tag>();
	private Tag searchedTag;

	public SearchResult(List<Post> posts, List<Tag> relatedTags, Tag searchedTag)
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
