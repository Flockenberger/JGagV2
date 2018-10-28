package at.neonartworks.jgagv2.core.exception;

public class GagApiResponseException extends GagApiException
{

	private static final long serialVersionUID = -430241287996726393L;
	private String errorCode;
	private String errorMessage;

	public GagApiResponseException(String errorCode, String errorMessage)
	{
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

}
