package at.neonartworks.jgagv2.api;

public enum APIuser
{
	USERID("userId"), ACCOUNTID("accountId"), LOGINNAME("loginName"), FULLNAME("fullName"), EMOJISTATUS("emojiStatus"),
	EMAIL("email"), PROFILECOLOR("profileColor"), HASPASSWORD("hasPassword"), FBUSERID("fbUserId"),
	FBDISPLAYNAME("fbDisplayName"), GPLUSUSERID("gplusUserId"), GPLUSACCOUNTNAME("gplusAccountName"),
	CANPOSTTOFB("canPostToFB"), FBPUBLISH("fbPublish"), FBTIMELINE("fbTimeline"), FBLIKEACTION("fbLikeAction"),
	FBCREATEACTION("fbCreateAction"), FBCOMMENTACTION("fbCommentAction"), SAFEMODE("safeMode"), ABOUT("about"),
	LANG("lang"), LOCATION("location"), TIMEZONEGMTOFFSET("timezoneGmtOffset"), WEBSITE("website"),
	PROFILEURL("profileUrl"), AVATARURLMEDIUM("avatarUrlMedium"), AVATARURLSMALL("avatarUrlSmall"),
	AVATARURLTINY("avatarUrlTiny"), AVATARURLLARGE("avatarUrlLarge"), GENDER("gender"), BIRTHDAY("birthday"),
	HIDEUPVOTE("hideUpvote"), PERMISSIONS("permissions");

	private String userString;

	APIuser(String string)
	{
		this.userString = string;
	}

	public String getString()
	{
		return this.userString;
	}
}
