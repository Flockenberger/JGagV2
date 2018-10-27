package at.neonartworks.jgagv2.core;

public class LoggedInUser
{

	private String userId;
	private String accountId;
	private String loginName;
	private String fullName;
	private String emojiStatus;
	private String email;
	private String profileColor;
	private boolean hasPassword;
	private String fbUserId;
	private String fbDisplayName;
	private String gplusUserId;
	private String gplusAccountname;
	private boolean canPostToFB;
	private int fbPublish;
	private int fbTimeline;
	private int fbLikeAction;
	private int fbCreateAction;
	private int fbCommentAction;
	private int safeMode;
	public String about;
	private String lang;
	private String location;
	private int timezoneGmtOffset;
	private String website;
	private String profileUrl;
	private String avatarUrlMedium;
	private String avatarUrlSmall;
	private String avatarUrlTiny;
	private String avatarUrlLarge;
	private String gender;
	private String birthday;
	private String hideUpvote;

	public LoggedInUser(String userId, String accountId, String loginName, String fullName, String emojiStatus, String email,
			String profileColor, boolean hasPassword, String fbUserId, String fbDisplayName, String gplusUserId,
			String gplusAccountname, boolean canPostToFB, int fbPublish, int fbTimeline, int fbLikeAction,
			int fbCreateAction, int fbCommentAction, int safeMode, String about, String lang, String location,
			int timezoneGmtOffset, String website, String profileUrl, String avatarUrlMedium, String avatarUrlSmall,
			String avatarUrlTiny, String avatarUrlLarge, String gender, String birthday, String hideUpvote)
	{
		super();
		this.userId = userId;
		this.accountId = accountId;
		this.loginName = loginName;
		this.fullName = fullName;
		this.emojiStatus = emojiStatus;
		this.email = email;
		this.profileColor = profileColor;
		this.hasPassword = hasPassword;
		this.fbUserId = fbUserId;
		this.fbDisplayName = fbDisplayName;
		this.gplusUserId = gplusUserId;
		this.gplusAccountname = gplusAccountname;
		this.canPostToFB = canPostToFB;
		this.fbPublish = fbPublish;
		this.fbTimeline = fbTimeline;
		this.fbLikeAction = fbLikeAction;
		this.fbCreateAction = fbCreateAction;
		this.fbCommentAction = fbCommentAction;
		this.safeMode = safeMode;
		this.about = about;
		this.lang = lang;
		this.location = location;
		this.timezoneGmtOffset = timezoneGmtOffset;
		this.website = website;
		this.profileUrl = profileUrl;
		this.avatarUrlMedium = avatarUrlMedium;
		this.avatarUrlSmall = avatarUrlSmall;
		this.avatarUrlTiny = avatarUrlTiny;
		this.avatarUrlLarge = avatarUrlLarge;
		this.gender = gender;
		this.birthday = birthday;
		this.hideUpvote = hideUpvote;
	}

	@Override
	public String toString()
	{
		return "User [userId=" + userId + ", accountId=" + accountId + ", loginName=" + loginName + ", fullName="
				+ fullName + ", emojiStatus=" + emojiStatus + ", email=" + email + ", profileColor=" + profileColor
				+ ", hasPassword=" + hasPassword + ", fbUserId=" + fbUserId + ", fbDisplayName=" + fbDisplayName
				+ ", gplusUserId=" + gplusUserId + ", gplusAccountname=" + gplusAccountname + ", canPostToFB="
				+ canPostToFB + ", fbPublish=" + fbPublish + ", fbTimeline=" + fbTimeline + ", fbLikeAction="
				+ fbLikeAction + ", fbCreateAction=" + fbCreateAction + ", fbCommentAction=" + fbCommentAction
				+ ", safeMode=" + safeMode + ", about=" + about + ", lang=" + lang + ", location=" + location
				+ ", timezoneGmtOffset=" + timezoneGmtOffset + ", website=" + website + ", profileUrl=" + profileUrl
				+ ", avatarUrlMedium=" + avatarUrlMedium + ", avatarUrlSmall=" + avatarUrlSmall + ", avatarUrlTiny="
				+ avatarUrlTiny + ", avatarUrlLarge=" + avatarUrlLarge + ", gender=" + gender + ", birthday=" + birthday
				+ ", hideUpvote=" + hideUpvote + "]";
	}

	public String getUserId()
	{
		return userId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public String getEmojiStatus()
	{
		return emojiStatus;
	}

	public String getEmail()
	{
		return email;
	}

	public String getProfileColor()
	{
		return profileColor;
	}

	public boolean isHasPassword()
	{
		return hasPassword;
	}

	public String getFbUserId()
	{
		return fbUserId;
	}

	public String getFbDisplayName()
	{
		return fbDisplayName;
	}

	public String getGplusUserId()
	{
		return gplusUserId;
	}

	public String getGplusAccountname()
	{
		return gplusAccountname;
	}

	public boolean isCanPostToFB()
	{
		return canPostToFB;
	}

	public int getFbPublish()
	{
		return fbPublish;
	}

	public int getFbTimeline()
	{
		return fbTimeline;
	}

	public int getFbLikeAction()
	{
		return fbLikeAction;
	}

	public int getFbCreateAction()
	{
		return fbCreateAction;
	}

	public int getFbCommentAction()
	{
		return fbCommentAction;
	}

	public int getSafeMode()
	{
		return safeMode;
	}

	public String getAbout()
	{
		return about;
	}

	public String getLang()
	{
		return lang;
	}

	public String getLocation()
	{
		return location;
	}

	public int getTimezoneGmtOffset()
	{
		return timezoneGmtOffset;
	}

	public String getWebsite()
	{
		return website;
	}

	public String getProfileUrl()
	{
		return profileUrl;
	}

	public String getAvatarUrlMedium()
	{
		return avatarUrlMedium;
	}

	public String getAvatarUrlSmall()
	{
		return avatarUrlSmall;
	}

	public String getAvatarUrlTiny()
	{
		return avatarUrlTiny;
	}

	public String getAvatarUrlLarge()
	{
		return avatarUrlLarge;
	}

	public String getGender()
	{
		return gender;
	}

	public String getBirthday()
	{
		return birthday;
	}

	public String getHideUpvote()
	{
		return hideUpvote;
	}

}
