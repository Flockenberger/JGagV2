package at.neonartworks.jgagv2.core.post;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import at.neonartworks.jgagv2.api.APIpath;
import at.neonartworks.jgagv2.api.AppID;
import at.neonartworks.jgagv2.api.APIservice;
import at.neonartworks.jgagv2.core.JGag;
import at.neonartworks.jgagv2.core.comment.Comment;
import at.neonartworks.jgagv2.core.comment.CommentAuthor;
import at.neonartworks.jgagv2.util.Argument;
import at.neonartworks.jgagv2.util.RESTType;

/**
 * Post. Holds every necessary data from a 9GAG post, including comments.
 * 
 * @author Florian Wagner
 *
 */
public class Post
{

	private String id;
	private String title;
	private String url;
	private String type;
	private String mediaURL;
	private int reportedStatus;
	private String status;
	private int fbShres;
	private int tweetCount;
	private String sourceDomain;
	private String sourceURL;
	private String externalURL;
	private String channel;
	private String isVoted;
	private int userScore;
	private boolean hasLongPostCover;
	private boolean isVoteMasked;
	private String albumWebUrl;
	private int score;
	private int viewsCount;
	private int version;
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
		// System.out.println(obj);
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

		this.reportedStatus = obj.getInt("reportedStatus");
		this.status = obj.getString("status");
		this.fbShres = obj.getInt("fbShares");
		this.tweetCount = obj.getInt("tweetCount");
		this.sourceDomain = obj.getString("sourceDomain");
		this.sourceURL = obj.getString("sourceUrl");
		this.externalURL = obj.getString("externalUrl");
		this.channel = obj.getString("channel");
		this.isVoted = obj.getString("isVoted");
		this.userScore = obj.getInt("userScore");
		int _hasLongPostCover = obj.getInt("hasLongPostCover");
		if (_hasLongPostCover == 1)
			this.hasLongPostCover = true;
		else
			this.hasLongPostCover = false;
		int _isVoteMasked = obj.getInt("isVoteMasked");
		if (_isVoteMasked == 1)
			this.isVoteMasked = true;
		else
			this.isVoteMasked = false;
		this.albumWebUrl = obj.getString("albumWebUrl");
		this.score = obj.getInt("score");
		this.viewsCount = obj.getInt("viewsCount");
		this.version = obj.getInt("version");

		this.section = new Section(secName, securl, secimageUrl);
		this.mediaURL = fetchMediaURL();
		this.comments = getComments(this.url);
	}

	/**
	 * Returns the top-level comments of the Post. Every response comment can be
	 * retrieved with the {@link Comment#getChildren()} method.
	 * 
	 * @return all top-level comments.
	 */
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

		JsonObject response = api.makeRequest(RESTType.GET, APIpath.TOP_COMMENTS, APIservice.COMMENT_CDN,
				new ArrayList<Argument<String, String>>(), params, null);

		// top_comment.add()
		// System.out.println(response.toString());
		// System.out.println(response);
		JsonArray commentsArray = response.getJsonObject("payload").getJsonArray("data").getJsonObject(0)
				.getJsonArray("comments");
		for (int i = 0; i < commentsArray.size(); i++)
		{
			JsonObject cmt = commentsArray.getJsonObject(i);
			// System.out.println(cmt);
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

			CommentAuthor u = new CommentAuthor(userID, avatarUrl, displayName, emojiStatus, accountId,
					hashedAccountId);
			top_comment.add(new Comment(commentID, text, parentID, permalink, likeCount, dislikeCount, coinCount, isURL,
					childrenCount, u, cmt));

		}

		// System.out.println(response.toString());
		return top_comment;
	}

	private String fetchMediaURL()
	{
		if (this.type.equalsIgnoreCase(PostType.PHOTO.getType()))
		{
			return this.json.getJsonObject("images").getJsonObject("image700").getString("url");
		} else if (this.type.equalsIgnoreCase(PostType.ANIMATED.getType()))
			return this.json.getJsonObject("images").getJsonObject("image460sv").getString("url");
		return "NULL";
	}

	@Override
	public String toString()
	{
		return "Post [id=" + id + ", title=" + title + ", url=" + url + ", type=" + type + ", mediaURL=" + mediaURL
				+ ", reportedStatus=" + reportedStatus + ", status=" + status + ", fbShres=" + fbShres + ", tweetCount="
				+ tweetCount + ", sourceDomain=" + sourceDomain + ", sourceURL=" + sourceURL + ", externalURL="
				+ externalURL + ", channel=" + channel + ", isVoted=" + isVoted + ", userScore=" + userScore
				+ ", hasLongPostCover=" + hasLongPostCover + ", isVoteMasked=" + isVoteMasked + ", albumWebUrl="
				+ albumWebUrl + ", score=" + score + ", viewsCount=" + viewsCount + ", version=" + version + ", nsfw="
				+ nsfw + ", upvotes=" + upvotes + ", downvotes=" + downvotes + ", totalVotes=" + totalVotes
				+ ", promotedPost=" + promotedPost + ", commentsCount=" + commentsCount + ", orderID=" + orderID
				+ ", section=" + section + ", tags=" + tags + ", comments=" + comments + "]";
	}

	/**
	 * Returns the reported status of this post.
	 * 
	 * @return the reported status;
	 */
	public int getReportedStatus()
	{
		return reportedStatus;
	}

	/**
	 * Returns the status of this post.
	 * 
	 * @return the status of this post
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Returns the amount of Facebook shares this post has.
	 * 
	 * @return the Facebook shares
	 */
	public int getFbShres()
	{
		return fbShres;
	}

	/**
	 * Returns the amount of Tweets this post has.
	 * 
	 * @return the amount of Tweets.
	 */
	public int getTweetCount()
	{
		return tweetCount;
	}

	/**
	 * Returns the source domain. **I think for Youtube video, not sure!**
	 * 
	 * @return the source domain
	 */
	public String getSourceDomain()
	{
		return sourceDomain;
	}

	/**
	 * Returns the source URL. **I think for Youtube video, not sure!**
	 * 
	 * @return the source URL
	 */
	public String getSourceURL()
	{
		return sourceURL;
	}

	/**
	 * Returns the external URL. **I think for Youtube video, not sure!**
	 * 
	 * @return the external URL
	 */
	public String getExternalURL()
	{
		return externalURL;
	}

	/**
	 * Returns the channel of this post.
	 * 
	 * @return the channel
	 */
	public String getChannel()
	{
		return channel;
	}

	/**
	 * Returns the isVoted parameter of this post **Not idea what this is**
	 * 
	 * @return the isVoted parameter.
	 */
	public String getIsVoted()
	{
		return isVoted;
	}

	/**
	 * Returns the UserScore parameter of this post. **Not idea what this is**
	 * 
	 * @return the UserScore parameter
	 */
	public int getUserScore()
	{
		return userScore;
	}

	/**
	 * Returns whether this post has the "long post" cover in 9GAG.
	 * 
	 * @return exactly what I had written above
	 */
	public boolean isHasLongPostCover()
	{
		return hasLongPostCover;
	}

	/**
	 * Returns the isVotedMask parameter. **Not idea what this is**
	 * 
	 * @return the isVotedMask parameter.
	 */
	public boolean isVoteMasked()
	{
		return isVoteMasked;
	}

	/**
	 * Returns the albumWebUrl parameter. **Not idea what this is**
	 * 
	 * @return the albumWebUrl parameter
	 */
	public String getAlbumWebUrl()
	{
		return albumWebUrl;
	}

	/**
	 * The score of this post. **Not idea what this is**
	 * 
	 * @return the score of this post
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Returns the views count. This has so far always been 0 in my tests
	 * 
	 * @return the views count
	 */
	public int getViewsCount()
	{
		return viewsCount;
	}

	/**
	 * Returns the version parameter. **Again not quite sure what version**
	 * 
	 * @return the version parameter
	 */
	public int getVersion()
	{
		return version;
	}

	/**
	 * The id of the post.
	 * 
	 * @return the id of the post.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * The title /caption of the post.
	 * 
	 * @return the title of the post
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * The url of this post.
	 * 
	 * @return the url of this post
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * The type of the post.
	 * 
	 * @return
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * The media url of this post. The media url is the url which directly links to
	 * either the posts image of video(gif).
	 * 
	 * @return the media url
	 */
	public String getMediaURL()
	{
		return mediaURL;
	}

	/**
	 * Whether this post is tagged as Not Safe For Work or not.
	 * 
	 * @return true if this is a nfsw post otherwise false
	 */
	public boolean isNsfw()
	{
		return nsfw;
	}

	/**
	 * Gets the amount of upvotes.
	 * 
	 * @return the upvotes of the post
	 */
	public int getUpvotes()
	{
		return upvotes;
	}

	/**
	 * Gets the amount of downvotes.
	 * 
	 * @return the downvotes of the post
	 */
	public int getDownvotes()
	{
		return downvotes;
	}

	/**
	 * The total votes of the post. Upvotes-Downvotes
	 * 
	 * @return the total amount of votes
	 */
	public int getTotalVotes()
	{
		return totalVotes;
	}

	/**
	 * Checks whether this post is a promoted one or not.
	 *
	 * @return whether this post is promoted or not
	 */
	public boolean isPromotedPost()
	{
		return promotedPost;
	}

	/**
	 * Gets the amount of comments.
	 * 
	 * @return the amount of comments
	 */
	public int getCommentsCount()
	{
		return commentsCount;
	}

	/**
	 * The order id of this post.
	 * 
	 * @return order id of this post
	 */
	public int getOrderID()
	{
		return orderID;
	}

	/**
	 * The {@link Section} of this post.
	 * 
	 * @return the {@link Section}
	 */
	public Section getSection()
	{
		return section;
	}

	/**
	 * Returns a list containing all {@link Tag}s of the post
	 * 
	 * @return the tags of the post
	 */
	public List<Tag> getTags()
	{
		return tags;
	}

	/**
	 * The raw JSON object from the query.
	 * 
	 * @return the raw JSON object.
	 */
	public JsonObject getJson()
	{
		return json;
	}
}
