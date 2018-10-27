package at.neonartworks.jgagv2.util;

public enum SortBy
{

	ASCENDING("asc"), DESCENDING("desc");

	private String sort;

	SortBy(String s)
	{
		this.sort = s;
	}

	public String getSortBy()
	{
		return this.sort;
	}

}
