package at.neonartworks.jgagv2.core.post;

/**
 * PostType.
 * 
 * @author Florian Wagner
 *
 */
public enum PostType
{

	PHOTO("Photo"), ANIMATED("Animated");

	private String type;

	PostType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return this.type;
	}
}
