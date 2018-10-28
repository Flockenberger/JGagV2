package at.neonartworks.jgagv2.core.exception;

public class GagApiTagLengthException extends GagApiException
{

	private static final long serialVersionUID = -7225710077802911189L;

	private String errorMessage;

	public GagApiTagLengthException(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage()
	{
		return this.errorMessage;
	}

}
