package at.neonartworks.jgagv2.core.post;

/**
 * Section. Holds information of a section.
 * 
 * 
 * @author Florian Wagner
 *
 */
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

	/**
	 * Returns the name of the section.
	 * 
	 * @return the section name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the url of the section.
	 * 
	 * @return the url of the section
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Returns the image-url of the section image.
	 * 
	 * @return the image-url of the section image
	 */
	public String getImageURL()
	{
		return imageURL;
	}

	@Override
	public String toString()
	{
		return "Section [name=" + name + ", url=" + url + ", imageURL=" + imageURL + "]";
	}

}
