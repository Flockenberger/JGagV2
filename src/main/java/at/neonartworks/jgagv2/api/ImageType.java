package at.neonartworks.jgagv2.api;

/**
 * 
 * @author Florian Wagner
 *
 */
public enum ImageType
{

	JPEG("jpeg", 1), GIF("gif", 2), MP4("mp4", 5), RAW("raw", 4);

	private String name;
	private int id;

	ImageType(String name, int id)
	{
		this.name = name;
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public int getID()
	{
		return this.id;
	}

}
