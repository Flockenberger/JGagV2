package at.neonartworks.jgagv2.core.post;

/**
 * Tag. Holds information of a Tag.
 * 
 * @author Florian Wagner
 *
 */
public class Tag
{

	private String name;
	private String url;

	public Tag(String name, String url)
	{
		super();
		this.name = name;
		this.url = url;
	}

	/**
	 * Return the tags name.
	 * 
	 * @return the tags name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the tags url.
	 * 
	 * @return the tag url
	 */
	public String getUrl()
	{
		return url;
	}

	@Override
	public String toString()
	{
		return "Tag [name=" + name + ", url=" + url + "]";
	}

}
