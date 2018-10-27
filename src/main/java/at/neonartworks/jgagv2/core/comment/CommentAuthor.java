package at.neonartworks.jgagv2.core.comment;

/**
 * User. This class holds information of a user. THis class is primarily used
 * for the {@link Comment} class.
 * 
 * @author Florian Wagner
 *
 */
public class CommentAuthor
{

	private final String USER_URL = "https://9gag.com/u/";
	private String userID;
	private String avatarURL;
	private String displayName;
	private String emojiStatus;
	private String accountID;
	private String hashedAccountID;
	private String profileURL;

	public CommentAuthor(String userID, String avatarURL, String displayName, String emojiStatus, String accountID,
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

	/**
	 * Returns the user id.
	 * 
	 * @return the user id
	 */
	public String getUserID()
	{
		return userID;
	}

	/**
	 * Returns the user avatar url.
	 * 
	 * @return the avatar url
	 */
	public String getAvatarURL()
	{
		return avatarURL;
	}

	/**
	 * Returns the DisplayName of the user.
	 * 
	 * @return the display name of the user
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Returns the emoji status of the user.
	 * 
	 * @return the emoji status
	 */
	public String getEmojiStatus()
	{
		return emojiStatus;
	}

	/**
	 * Returns the account id of the user.
	 * 
	 * @return the account id
	 */
	public String getAccountID()
	{
		return accountID;
	}

	/**
	 * Returns the hashed account id of the user.
	 * 
	 * @return the hashed account id
	 */
	public String getHashedAccountID()
	{
		return hashedAccountID;
	}

	/**
	 * Returns the profile url of the user.
	 * 
	 * @return the profile url
	 */
	public String getProfileURL()
	{
		return profileURL;
	}

	@Override
	public String toString()
	{
		return "CommentAuthor [USER_URL=" + USER_URL + ", userID=" + userID + ", avatarURL=" + avatarURL
				+ ", displayName=" + displayName + ", emojiStatus=" + emojiStatus + ", accountID=" + accountID
				+ ", hashedAccountID=" + hashedAccountID + ", profileURL=" + profileURL + "]";
	}

}
