package at.neonartworks.jgagv2.core;

public class Section
{

	private String name;
	private String url;
	private String imageURL;

	public Section(String name, String url, String imageURL)
	{
		super();
		this.name = name;
		this.url = url;
		this.imageURL = imageURL;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getImageURL()
	{
		return imageURL;
	}

	public void setImageURL(String imageURL)
	{
		this.imageURL = imageURL;
	}

	@Override
	public String toString()
	{
		return "Section [name=" + name + ", url=" + url + ", imageURL=" + imageURL + "]";
	}

}
