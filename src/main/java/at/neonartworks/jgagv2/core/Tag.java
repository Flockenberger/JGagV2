package at.neonartworks.jgagv2.core;

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

	public String getName()
	{
		return name;
	}

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
