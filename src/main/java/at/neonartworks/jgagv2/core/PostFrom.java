package at.neonartworks.jgagv2.core;

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
