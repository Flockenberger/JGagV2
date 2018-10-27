package at.neonartworks.jgagv2.core.comment;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArray;

import javax.json.JsonValue;

/**
 * Comment. Holds a comment and it's children (response comments).
 * 
 * @author Florian Wagner
 *
 */
public class Comment
{
	private String commentID;
	private String text;
	private String parentID;
	private String permalink;
	private int likeCount;
	private int dislikeCount;
	private int coinCount;
	private boolean isURL;
	private int childrenCount;
	private CommentAuthor author;
	private List<Comment> children = new ArrayList<Comment>();
	private JsonObject json;

	public Comment(String commentID, String text, String parentID, String permalink, int likeCount, int dislikeCount,
			int coinCount, boolean isURL, int childrenCount, CommentAuthor author, JsonObject json)
	{
		super();
		this.commentID = commentID;
		this.text = text;
		this.parentID = parentID;
		this.permalink = permalink;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.coinCount = coinCount;
		this.isURL = isURL;
		this.childrenCount = childrenCount;
		this.author = author;
		this.json = json;
		getChildrenComments();
	}

	private void getChildrenComments()
	{
		JsonArray arr = json.getJsonArray("children");
		for (int i = 0; i < arr.size(); i++)
		{
			JsonObject cmt = arr.getJsonObject(i);
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

			CommentAuthor u = new CommentAuthor(userID, avatarUrl, displayName, emojiStatus, accountId, hashedAccountId);
			children.add(new Comment(commentID, text, parentID, permalink, likeCount, dislikeCount, coinCount, isURL,
					childrenCount, u, cmt));

		}
	}

	/**
	 * Returns the comment id
	 * 
	 * @return comment id
	 */
	public String getCommentID()
	{
		return commentID;
	}

	/**
	 * Returns the comment text, this can also be a URL (image/gif) check
	 * {@link #isURL}
	 * 
	 * @return the content of the comment
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Gets the id of the parent comment.
	 * 
	 * @return parent id
	 */
	public String getParentID()
	{
		return parentID;
	}

	/**
	 * Returns the permalink of this comment.
	 * 
	 * @return permalink of the comment.
	 */
	public String getPermalink()
	{
		return permalink;
	}

	/**
	 * Returns all children of the comment. Children are all sub/response comments
	 * which are typically underneath.
	 * 
	 * @return the comment children
	 */
	public List<Comment> getChildren()
	{
		return children;
	}

	@Override
	public String toString()
	{
		return "Comment [commentID=" + commentID + ", text=" + text + ", parentID=" + parentID + ", permalink="
				+ permalink + ", likeCount=" + likeCount + ", dislikeCount=" + dislikeCount + ", coinCount=" + coinCount
				+ ", isURL=" + isURL + ", childrenCount=" + childrenCount + ", author=" + author + ", children="
				+ children + "]";
	}

}
