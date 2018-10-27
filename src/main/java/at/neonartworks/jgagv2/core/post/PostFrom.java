package at.neonartworks.jgagv2.core.post;

/**
 * PostFrom. Helper-Enum for distinguishing from where to fetch the posts from.
 * 
 * @author Florian Wagner
 *
 */
public enum PostFrom
{

	HOT("hot"), FRESH("vote"), TRENDING("trending");

	private String from;

	PostFrom(String from)
	{
		this.from = from;
	}

	public String getFrom()
	{
		return this.from;
	}
}
