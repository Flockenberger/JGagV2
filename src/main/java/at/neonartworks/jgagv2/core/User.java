package at.neonartworks.jgagv2.core;

public class User
{

	private final String USER_URL = "https://9gag.com/u/";
	private String userID;
	private String avatarURL;
	private String displayName;
	private String emojiStatus;
	private String accountID;
	private String hashedAccountID;
	private String profileURL;

	public User(String userID, String avatarURL, String displayName, String emojiStatus, String accountID,
			String hashedAccountID)
	{
		super();
		this.userID = userID;
		this.avatarURL = avatarURL;
		this.displayName = displayName;
		this.emojiStatus = emojiStatus;
		this.accountID = accountID;
		this.hashedAccountID = hashedAccountID;
		this.profileURL = USER_URL + this.displayName;
	}

	public String getUserID()
	{
		return userID;
	}

	public String getAvatarURL()
	{
		return avatarURL;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getEmojiStatus()
	{
		return emojiStatus;
	}

	public String getAccountID()
	{
		return accountID;
	}

	public String getHashedAccountID()
	{
		return hashedAccountID;
	}

	public String getProfileURL()
	{
		return profileURL;
	}

	@Override
	public String toString()
	{
		return "User [USER_URL=" + USER_URL + ", userID=" + userID + ", avatarURL=" + avatarURL + ", displayName="
				+ displayName + ", emojiStatus=" + emojiStatus + ", accountID=" + accountID + ", hashedAccountID="
				+ hashedAccountID + ", profileURL=" + profileURL + "]";
	}

}
