package at.neonartworks.jgagv2.core;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import at.neonartworks.jgagv2.util.APIPath;
import at.neonartworks.jgagv2.util.AppID;
import at.neonartworks.jgagv2.util.Argument;
import at.neonartworks.jgagv2.util.RESTType;
import at.neonartworks.jgagv2.util.Services;

public class Post
{

	private String id;
	private String title;
	private String url;
	private String type;
	private String mediaURL;
	private boolean nsfw;
	private int upvotes;
	private int downvotes;
	private int totalVotes;
	private boolean promotedPost;
	private int commentsCount;
	private int orderID;
	private Section section;
	private JGag api;
	private List<Tag> tags = new ArrayList<Tag>();
	private List<Comment> comments = new ArrayList<Comment>();
	private JsonObject json;

	public Post(JGag jgag, JsonObject obj)
	{
		this.json = obj;
		this.api = jgag;
		init(this.json);
	}

	private void init(JsonObject obj)
	{
		this.id = obj.getString("id");
		this.title = obj.getString("title");
		this.url = obj.getString("url");
		this.type = obj.getString("type");
		int _nsfw = obj.getInt("nsfw");
		if (_nsfw == 1)
			this.nsfw = true;
		else
			this.nsfw = false;
		int _promotedPost = obj.getInt("promoted");
		if (_promotedPost == 1)
			this.promotedPost = true;
		else
			this.promotedPost = false;

		this.upvotes = obj.getInt("upVoteCount");
		this.downvotes = obj.getInt("downVoteCount");
		this.totalVotes = obj.getInt("totalVoteCount");

		this.orderID = obj.getInt("orderId");
		this.commentsCount = obj.getInt("commentsCount");
		JsonArray arr = obj.getJsonArray("tags");
		if (arr != null)
			for (int i = 0; i < arr.size(); i++)
			{
				String s1 = (arr.getJsonObject(i).getString("key"));
				String s2 = (arr.getJsonObject(i).getString("url"));
				this.tags.add(new Tag(s1, s2));

			}
		String secName = obj.getJsonObject("postSection").getString("name");
		String securl = obj.getJsonObject("postSection").getString("url");
		String secimageUrl = obj.getJsonObject("postSection").getString("imageUrl");

		this.section = new Section(secName, securl, secimageUrl);
		this.mediaURL = fetchMediaURL();
		this.comments = getComments(this.url);
	}

	public List<Comment> getComments()
	{
		return comments;
	}

	private List<Comment> getComments(String url)
	{
		List<Comment> top_comment = new ArrayList<Comment>();
		List<Argument<String, String>> params = new ArrayList<Argument<String, String>>();

		params.add(new Argument<String, String>("appId", AppID.COMMENT_CDN.getID()));
		params.add(new Argument<String, String>("urls", url));
		params.add(new Argument<String, String>("commentL1", String.valueOf("10")));
		params.add(new Argument<String, String>("commentL2", String.valueOf("10")));
		params.add(new Argument<String, String>("pretty", String.valueOf(1)));

		JsonObject response = api.makeRequest(RESTType.GET, APIPath.TOP_COMMENTS, Services.COMMENT_CDN,
				new ArrayList<Argument<String, String>>(), params, null);

		// top_comment.add()
		// System.out.println(response.toString());
		//System.out.println(response);
		JsonArray commentsArray = response.getJsonObject("payload").getJsonArray("data").getJsonObject(0)
				.getJsonArray("comments");
		for (int i = 0; i < commentsArray.size(); i++)
		{
			JsonObject cmt = commentsArray.getJsonObject(i);
		//	System.out.println(cmt);
			String commentID = cmt.getString("commentId");
			String text = cmt.getString("text");
			String parentID = cmt.getString("parent");
			String permalink = cmt.getString("permalink");
			int likeCount = cmt.getInt("likeCount");
			int dislikeCount = cmt.getInt("dislikeCount");
			int coinCount = cmt.getInt("coinCount");
			int _isURL = cmt.getInt("isUrl");
			int childrenCount = cmt.getInt("childrenTotal");
			boolean isURL = false;
			if (_isURL == 1)
				isURL = true;
			JsonObject user = cmt.getJsonObject("user");
			String userID = user.getString("userId");
			String avatarUrl = user.getString("avatarUrl");
			String displayName = user.getString("displayName");
			String emojiStatus = user.getString("emojiStatus");
			String accountId = user.getString("accountId");
			String hashedAccountId = user.getString("hashedAccountId");

			User u = new User(userID, avatarUrl, displayName, emojiStatus, accountId, hashedAccountId);
			top_comment.add(new Comment(commentID, text, parentID, permalink, likeCount, dislikeCount, coinCount, isURL,
					childrenCount, u, cmt));

		}

		// System.out.println(response.toString());
		return top_comment;
	}

	private String fetchMediaURL()
	{
		if (this.type.equals("Photo"))
		{
			return this.json.getJsonObject("images").getJsonObject("image700").getString("url");
		} else if (this.type.equals("Animated"))
			return this.json.getJsonObject("images").getJsonObject("image460sv").getString("url");
		return "NULL";
	}

	@Override
	public String toString()
	{
		return "Post [id=" + id + ", title=" + title + ", url=" + url + ", type=" + type + ", mediaURL=" + mediaURL
				+ ", nsfw=" + nsfw + ", upvotes=" + upvotes + ", downvotes=" + downvotes + ", totalVotes=" + totalVotes
				+ ", promotedPost=" + promotedPost + ", commentsCount=" + commentsCount + ", orderID=" + orderID
				+ ", section=" + section + ", tags=" + tags + "]";
	}

	public String getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getUrl()
	{
		return url;
	}

	public String getType()
	{
		return type;
	}

	public String getMediaURL()
	{
		return mediaURL;
	}

	public boolean isNsfw()
	{
		return nsfw;
	}

	public int getUpvotes()
	{
		return upvotes;
	}

	public int getDownvotes()
	{
		return downvotes;
	}

	public int getTotalVotes()
	{
		return totalVotes;
	}

	public boolean isPromotedPost()
	{
		return promotedPost;
	}

	public int getCommentsCount()
	{
		return commentsCount;
	}

	public int getOrderID()
	{
		return orderID;
	}

	public Section getSection()
	{
		return section;
	}

	public List<Tag> getTags()
	{
		return tags;
	}

	public JsonObject getJson()
	{
		return json;
	}

	private String millisToDate(long millis)
	{

		return DateFormat.getDateInstance(DateFormat.SHORT).format(millis);
		// You can use DateFormat.LONG instead of SHORT

	}
}
