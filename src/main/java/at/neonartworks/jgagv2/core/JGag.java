package at.neonartworks.jgagv2.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import at.neonartworks.jgagv2.api.APIpath;
import at.neonartworks.jgagv2.api.APIservice;
import at.neonartworks.jgagv2.api.APIstatus;
import at.neonartworks.jgagv2.api.APIstep;
import at.neonartworks.jgagv2.api.APIuser;
import at.neonartworks.jgagv2.api.AppID;
import at.neonartworks.jgagv2.api.DeviceType;
import at.neonartworks.jgagv2.api.HeaderType;
import at.neonartworks.jgagv2.core.exception.GagApiException;
import at.neonartworks.jgagv2.core.exception.GagApiResponseException;
import at.neonartworks.jgagv2.core.exception.GagApiTagLengthException;
import at.neonartworks.jgagv2.core.post.Post;
import at.neonartworks.jgagv2.core.post.PostFrom;
import at.neonartworks.jgagv2.core.post.PostSection;
import at.neonartworks.jgagv2.core.post.Tag;
import at.neonartworks.jgagv2.util.Argument;
import at.neonartworks.jgagv2.util.JGagUtil;
import at.neonartworks.jgagv2.util.RESTType;
import at.neonartworks.jgagv2.util.SortBy;

/**
 * <h1>JGAG V2</h1>
 * <p>
 * This project is an unofficial Java wrapper of 9Gags internal API.
 * </p>
 * <h2>Features</h2>
 * <p>
 * ► Retrieve posts from every section. ► Uploading posts to 9gag. ► Search for
 * posts.
 * </p>
 * <h2>Currently Working On</h2>
 * <p>
 * ► Writing comments to 9gag posts ► Uploading Youtube videos
 * </p>
 * <h2>Getting Started</h2>
 * <h3>Loggin into 9GAG</h3>
 * <p>
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	JGag jgag = new JGag();
 * 	boolean login = jgag.login("USERNAME", "USER_TOKEN");
 * }
 * </pre>
 * </p>
 * <h3>Getting Posts from section</h3>
 * 
 * <pre>
 * &#64;code
 * if(login) 
 * { 
 * 	List<Post> posts = jgag.getPosts(PostSection.DARKHUMOR, PostFrom.HOT, 10); 
 * 	if (posts != null && posts.size() > 1) 
 * 	{ 
 * 		for (Post post : posts)
 * 		System.out.println(post.toString()); 
 * 	} 
 * }
 * 
 * </pre>
 * 
 * <h3>Uploading a Post</h3>
 * 
 * <pre>
 * &#64;code
 * if(login) 
 * { 
 * 	File file = new File("PATH-TO-JPG"); 
 * 	try { 
 * 		Post p = jgag.uploadImage(file, 0.5f, "Caption This!", PostSection.FUNNY, false, "tag1", "tag2", "tag3");
 * 		System.out.println(p.toString()); 
 * 	} catch (GagApiException e) {
 * 		e.printStackTrace(); 
 * 	} 
 * }
 * 
 * </pre>
 * 
 * @author Florian Wagner
 *
 */
public class JGag
{

	private String app_id;
	private String token;
	private String device_uuid;
	private final String LANG = "en_US";
	private CloseableHttpClient client;
	private String olderThan;
	private LoggedInUser loggedInUser;
	private Calendar cl = Calendar.getInstance();

	public JGag()
	{

		this.app_id = AppID.APP_ID.getID();
		this.token = JGagUtil.getRandomSha1();
		this.device_uuid = JGagUtil.getRandomUUID();
	}

	/**
	 * Tries to log into the 9gag account associated with the given username and
	 * password. This method returns true if the login attempt was successful and
	 * otherwise false.
	 * 
	 * @param ua your 9gag username
	 * @param pw your 9gag password
	 * @return true or false depending whether the login attempt was successful or
	 *         not
	 */
	public boolean login(String ua, String pw)
	{
		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("password", JGagUtil.md5(pw)));
		arg.add(new Argument<String, String>("pushToken", this.token));
		arg.add(new Argument<String, String>("loginMethod", "9gag"));
		arg.add(new Argument<String, String>("loginName", ua));
		arg.add(new Argument<String, String>("language", LANG));

		JsonObject response = makeRequest(RESTType.GET, APIpath.USER_TOKEN, APIservice.API, arg, null, null);
		// System.out.println(response);

		boolean succ = validateResponse(response);
		if (succ)
		{
			this.token = response.getJsonObject("data").getString("userToken");
			this.loggedInUser = getUserfromLoginResponse(response.getJsonObject("data").getJsonObject("user"));

		}
		if (!succ)
		{
			System.err.println("Error while login-attempt! Are your credentials correct?");
		}
		return succ;
	}

	/**
	 * Returns a list of all Sections available.
	 * 
	 * @return a list of all 9GAG sections.
	 */
	public List<Section> getSectionList()
	{

		List<Argument<String, String>> params = new ArrayList<Argument<String, String>>();
		params.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		params.add(new Argument<String, String>("locale", getLoggedInUser().getLang()));

		JsonObject response = makeRequest(RESTType.GET, APIpath.GROUP_LIST, APIservice.API, params, params, null);
		List<Section> sections = new ArrayList<Section>();

		if (validateResponse(response))
		{
			JsonObject data = response.getJsonObject("data");
			JsonArray groups = data.getJsonArray("groups");
			for (int i = 0; i < groups.size(); i++)
			{
				JsonObject tmp = groups.getJsonObject(i);
				List<Tag> tags = new ArrayList<Tag>();
				int id = Integer.valueOf(tmp.getString("id"));
				String url = tmp.getString("url");
				String name = tmp.getString("name");
				String description = tmp.getString("description");
				int _userUploadEnabled = tmp.getInt("userUploadEnabled");
				boolean userUploadEnabled = false;
				if (_userUploadEnabled == 1)
					userUploadEnabled = true;
				String listType = tmp.getString("listType");
				String listType2 = tmp.getString("listType2");
				String ogImageUrl = tmp.getString("ogImageUrl");
				int _isSensitive = tmp.getInt("isSensitive");
				boolean isSensitive = false;
				if (_isSensitive == 1)
					isSensitive = true;
				JsonArray arr = tmp.getJsonArray("featuredTags");
				if (arr != null)
					for (int ii = 0; ii < arr.size(); ii++)
					{
						String s1 = (arr.getJsonObject(ii).getString("key"));
						String s2 = (arr.getJsonObject(ii).getString("url"));
						tags.add(new Tag(s1, s2));

					}
				sections.add(new Section(id, url, name, description, userUploadEnabled, listType, listType2, ogImageUrl,
						tags, isSensitive));
			}
		}

		return sections;
	}

	public void writeComment()
	{
		JsonObject response = makeRequest(RESTType.GET, APIpath.COMMENTS, APIservice.API, null, null, null);
		System.out.println(response);
	}

	/**
	 * Searches 9GAG for a specific query. I am not sure whether 9gag searches
	 * through tags or the title string, maybe both. This method returns a
	 * {@link SearchResult}. This result contains all {@link Post}s and related
	 * {@link Tag}s aswell as the {@link Tag} which was used for the query.
	 * 
	 * @param query     the query to search for
	 * @param itemCount the amount of posts to fetch
	 * @param sort      sort either by ascending or descending
	 * @return the found posts or null in case of an error
	 */
	public SearchResult searchPosts(String query, int itemCount, SortBy sort)
	{
		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("query", query));
		arg.add(new Argument<String, String>("fromIndex", String.valueOf(0)));
		arg.add(new Argument<String, String>("itemCount", String.valueOf(itemCount)));
		arg.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		arg.add(new Argument<String, String>("sortBy", sort.getSortBy()));
		JsonObject response = makeRequest(RESTType.GET, APIpath.TAG_SEARCH, APIservice.API, null, arg, null);
		SearchResult result;
		if (validateResponse(response))
		{
			List<Post> retPosts = new ArrayList<Post>();
			List<Tag> relatedTags = new ArrayList<Tag>();
			JsonArray posts = response.getJsonObject("data").getJsonArray("posts");
			if (posts != null)
				for (int i = 0; i < posts.size(); i++)
				{
					Post p = new Post(this, posts.getJsonObject(i));
					this.olderThan = p.getId();
					retPosts.add(p);
				}
			JsonArray arr = response.getJsonObject("data").getJsonArray("relatedTags");
			if (arr != null)
				for (int i = 0; i < arr.size(); i++)
				{
					String s1 = (arr.getJsonObject(i).getString("key"));
					String s2 = (arr.getJsonObject(i).getString("url"));
					relatedTags.add(new Tag(s1, s2));

				}
			Tag searchedTag = new Tag(response.getJsonObject("data").getJsonObject("tag").getString("key"),
					response.getJsonObject("data").getJsonObject("tag").getString("url"));

			result = new SearchResult(retPosts, relatedTags, searchedTag);
			return result;
		} else
		{
			return null;
		}
	}

	private File compressJPG(File file, float compression)
	{
		File compressedImageFile = new File("upload.jpg");
		OutputStream os = null;
		BufferedImage image = null;
		try
		{
			os = new FileOutputStream(compressedImageFile);
			image = ImageIO.read(file);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();

		ImageOutputStream ios = null;
		try
		{
			ios = ImageIO.createImageOutputStream(os);
		} catch (IOException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(compression); // Change the quality value you prefer

		try
		{
			writer.write(null, new IIOImage(image, null, null), param);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try
		{
			os.close();
			ios.close();
			writer.dispose();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return compressedImageFile;
	}

	private String createTagList(String[] args) throws GagApiTagLengthException
	{
		if (args.length > 3)
		{
			throw new GagApiTagLengthException("Too many Tags!");
		}
		StringBuilder sb = new StringBuilder();
		int c = 0;
		for (String s : args)
		{
			sb.append(s);
			if (c < args.length - 1)
			{
				sb.append(",");
			}
			c++;
		}
		return sb.toString();
	}

	/**
	 * Uploads an Image to 9GAG. The Image has to be in .jpg format.
	 * 
	 * @param file        a file pointing to the image that should be uploaded
	 * @param compression typically the images are too large to upload. Thus you can
	 *                    compress the image with this value. This value has to be
	 *                    between 0 and 1. A compression quality setting of 0.0 is
	 *                    most generically interpreted as "high compression is
	 *                    important," while a setting of 1.0 is most generically
	 *                    interpreted as "high image quality is important."
	 * @param title       The title/caption of the image.
	 * @param section     The section you want the image to upload to
	 * @param isNSFW      whether the post is "not safe for work"
	 * @param tags        the tags of this post. Can be null but must be less than
	 *                    3!
	 * @return the newly created {@link Post} or null in case of an error
	 * @throws GagApiException
	 */
	public Post uploadImage(File file, float compression, String title, PostSection section, boolean isNSFW,
			String... tags) throws GagApiException
	{
		String tagList = "";
		if (tags != null)
			tagList = createTagList(tags);

		String timestamp = String.valueOf(cl.getTimeInMillis() / 1000l);

		file = compressJPG(file, compression);
		String uploadId = getLoggedInUser().getAccountId() + "_" + timestamp;

		HttpEntity metaEntity = MultipartEntityBuilder.create().addTextBody("isNSFW", String.valueOf(isNSFW))
				.addTextBody("step", APIstep.META_DATA.getStep())
				.addTextBody("section", section.getName().toLowerCase()).addTextBody("title", title)
				.addTextBody("tags", tagList).addTextBody("uploadId", uploadId).build();

		HttpEntity mpEntity = MultipartEntityBuilder.create().addPart("imageData", new FileBody(file))
				.addTextBody("step", APIstep.IMAGE_DATA.getStep()).addTextBody("uploadId", uploadId).build();

		HttpEntity triggerEntity = MultipartEntityBuilder.create()
				.addTextBody("step", APIstep.TRIGGER_CREATION.getStep()).addTextBody("uploadId", uploadId).build();

		JsonObject response = makeRequest(RESTType.POST, APIpath.POST_SUBMIT, APIservice.API, null, null, mpEntity);
		if (validateImageUpload(response))
		{

			response = makeRequest(RESTType.POST, APIpath.POST_SUBMIT, APIservice.API, null, null, metaEntity);
			if (validateMetaUpload(response))
			{
				response = makeRequest(RESTType.POST, APIpath.POST_SUBMIT, APIservice.API, null, null, triggerEntity);
				String entryId = validateUploadAndGetPostId(response);
				removeFileAfterUpload(file);
				return getPostById(entryId);

			}
		}
		return null;

	}

	/**
	 * This method can be used to upload images and videos to 9GAG. So far it is
	 * possible to upload Youtube videos as well as Instagram images and videos. I
	 * guess that is a thing now. Maybe even other website urls work, I have not
	 * tested this yet!
	 * 
	 * @param url     the Youtube video, Instagram video/image url
	 * @param title   the title/caption of the post
	 * @param section the section where you want to upload the video to
	 * @param isNSFW  whether if it is "not safe for work" or not
	 * @param tags    the tags of this post. Only a maximum of 3 Tags are supported!
	 * @return the newly created {@link Post}
	 * @throws GagApiException
	 */
	public Post uploadURL(String url, String title, PostSection section, boolean isNSFW, String... tags)
			throws GagApiException
	{
		String tagList = "";
		if (tags != null)
			tagList = createTagList(tags);

		String timestamp = String.valueOf(cl.getTimeInMillis() / 1000l);

		String uploadId = getLoggedInUser().getAccountId() + "_" + timestamp;

		HttpEntity metaEntity = MultipartEntityBuilder.create().addTextBody("isNSFW", String.valueOf(isNSFW))
				.addTextBody("step", APIstep.META_DATA.getStep())
				.addTextBody("section", section.getName().toLowerCase()).addTextBody("title", title)
				.addTextBody("tags", tagList).addTextBody("uploadId", uploadId).build();

		HttpEntity mpEntity = MultipartEntityBuilder.create().addTextBody("urlMedia", url)
				.addTextBody("step", APIstep.URL_DATA.getStep()).addTextBody("uploadId", uploadId).build();

		HttpEntity triggerEntity = MultipartEntityBuilder.create()
				.addTextBody("step", APIstep.TRIGGER_CREATION.getStep()).addTextBody("uploadId", uploadId).build();

		JsonObject response = makeRequest(RESTType.POST, APIpath.POST_SUBMIT, APIservice.API, null, null, mpEntity);
		// System.out.println(response);
		if (validateResponse(response))
		{

			response = makeRequest(RESTType.POST, APIpath.POST_SUBMIT, APIservice.API, null, null, metaEntity);

			// System.out.println(response);
			if (validateResponse(response))
			{
				response = makeRequest(RESTType.POST, APIpath.POST_SUBMIT, APIservice.API, null, null, triggerEntity);
				// System.out.println(response);
				String entryId = validateURLUploadAndGetPostId(response);
				// System.out.println(entryId);
				return getPostById(entryId);

			}
		}
		return null;
	}

	/**
	 * Returns a {@link Post} object containing all information about this 9GAG
	 * post.
	 * 
	 * @param id the id of the 9gag post. The post id is typically
	 *           https://9gag.com/gag/<b>THIS PART<b>
	 * @return a Post object for this id or null if the post could not be found
	 */
	public Post getPostById(String id)
	{
		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("entryIds", id));
		arg.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		JsonObject response = makeRequest(RESTType.GET, APIpath.POST, APIservice.API, null, arg, null);
		// System.out.println(response);
		if (validateResponse(response))
		{
			return new Post(this, response.getJsonObject("data").getJsonArray("posts").getJsonObject(0));
		}
		return null;
	}

	private void removeFileAfterUpload(File f)
	{
		if (f.exists())
		{
			f.delete();
		}
	}

	private boolean isMetaStatusOK(JsonObject obj) throws GagApiException
	{
		JsonObject meta = obj.getJsonObject("meta");
		String status = meta.getString("status");

		if (status.equals(APIstatus.FAILURE))
		{
			String errorCode = meta.getString("errorCode");
			String errorMessage = meta.getString("errorMessage");
			throw new GagApiResponseException(errorCode, errorMessage);
		}
		return true;
	}

	private String validateURLUploadAndGetPostId(JsonObject obj)
	{
		if (validateResponse(obj))
		{
			JsonObject data = obj.getJsonObject("data");
			String entryId = null;
			try
			{
				entryId = data.getString("entryId");

			} catch (Exception e)
			{
				System.err.println("EntryId null!");
			}
			if (entryId != null)
			{
				return entryId;
			}

		}
		return "null";
	}

	private String validateUploadAndGetPostId(JsonObject obj) throws GagApiException
	{
		isMetaStatusOK(obj);

		JsonObject data = obj.getJsonObject("data");
		if (validateMetaUpload(obj))
		{
			String entryId = null;
			try
			{
				entryId = data.getString("entryId");

			} catch (Exception e)
			{
				System.err.println("EntryId null!");
			}
			if (entryId != null)
			{
				return entryId;
			}

		}
		return "null";
	}

	private boolean validateImageUpload(JsonObject obj) throws GagApiException
	{
		isMetaStatusOK(obj);

		JsonObject data = obj.getJsonObject("data");
		int _imageStatus = data.getInt("imageStatus");
		int _mediaStatus = data.getInt("mediaStatus");
		if (_imageStatus == 1 && _mediaStatus == 1)
			return true;

		return false;
	}

	private boolean validateMetaUpload(JsonObject obj) throws GagApiException
	{
		isMetaStatusOK(obj);

		JsonObject data = obj.getJsonObject("data");
		if (validateImageUpload(obj))
		{
			int _metaStatus = data.getInt("metaStatus");
			if (_metaStatus == 1)
				return true;

		}
		return false;
	}

	/**
	 * Searches 9gag for a specific tag. This method returns a {@link SearchResult}.
	 * This result contains all {@link Post}s and related {@link Tag}s aswell as the
	 * {@link Tag} which was used for the query.
	 * 
	 * @param tag       the tag to search for
	 * @param itemCount the amount of posts to fetch with this tag
	 * @param sort      sort either by ascending or descending
	 * @return the found posts or null in case of an error
	 */
	public SearchResult searchTagPosts(String tag, int itemCount, SortBy sort)
	{
		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("query", tag));
		arg.add(new Argument<String, String>("fromIndex", String.valueOf(0)));
		arg.add(new Argument<String, String>("itemCount", String.valueOf(itemCount)));
		arg.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		arg.add(new Argument<String, String>("sortBy", sort.getSortBy()));
		JsonObject response = makeRequest(RESTType.GET, APIpath.TAG_SEARCH, APIservice.API, null, arg, null);
		SearchResult result;

		if (validateResponse(response))
		{
			List<Post> retPosts = new ArrayList<Post>();
			List<Tag> relatedTags = new ArrayList<Tag>();
			JsonArray posts = response.getJsonObject("data").getJsonArray("posts");
			if (posts != null)
				for (int i = 0; i < posts.size(); i++)
				{
					Post p = new Post(this, posts.getJsonObject(i));
					this.olderThan = p.getId();
					retPosts.add(p);
				}
			JsonArray arr = response.getJsonArray("relatedTags");
			if (arr != null)
				for (int i = 0; i < arr.size(); i++)
				{
					String s1 = (arr.getJsonObject(i).getString("key"));
					String s2 = (arr.getJsonObject(i).getString("url"));
					relatedTags.add(new Tag(s1, s2));

				}
			Tag searchedTag = new Tag(response.getJsonObject("tag").getString("key"),
					response.getJsonObject("tag").getString("url"));

			result = new SearchResult(retPosts, relatedTags, searchedTag);
			return result;
		} else
		{
			return null;
		}
	}

	private LoggedInUser getUserfromLoginResponse(JsonObject user)
	{
		String userId = user.getString(APIuser.USERID.getString());
		String accountId = user.getString(APIuser.ACCOUNTID.getString());
		String loginName = user.getString(APIuser.LOGINNAME.getString());
		String fullName = user.getString(APIuser.FULLNAME.getString());
		String emojiStatus = user.getString(APIuser.EMOJISTATUS.getString());
		String email = user.getString(APIuser.EMAIL.getString());
		String profileColor = user.getString(APIuser.PROFILECOLOR.getString());
		int tmp = user.getInt(APIuser.HASPASSWORD.getString());
		boolean hasPassword = false;
		if (tmp == 1)
			hasPassword = true;
		String fbUserId = user.getString(APIuser.FBUSERID.getString());
		String fbDisplayName = user.getString(APIuser.FBDISPLAYNAME.getString());
		String gplusUserId = user.getString(APIuser.GPLUSUSERID.getString());
		String gplusAccountName = user.getString(APIuser.GPLUSACCOUNTNAME.getString());
		tmp = user.getInt(APIuser.CANPOSTTOFB.getString());
		boolean canPostToFB = false;
		if (tmp == 1)
			canPostToFB = true;
		int fbPublish = user.getInt(APIuser.FBPUBLISH.getString());
		int fbTimeline = user.getInt(APIuser.FBTIMELINE.getString());
		int fbLikeAction = user.getInt(APIuser.FBLIKEACTION.getString());
		int fbCreateAction = user.getInt(APIuser.FBCREATEACTION.getString());
		int fbCommentAction = user.getInt(APIuser.FBCOMMENTACTION.getString());

		int safeMode = user.getInt(APIuser.SAFEMODE.getString());
		String about = user.getString(APIuser.ABOUT.getString());
		String lang = user.getString(APIuser.LANG.getString());
		String location = user.getString(APIuser.LOCATION.getString());
		int timezoneGmtOffset = user.getInt(APIuser.TIMEZONEGMTOFFSET.getString());
		String website = user.getString(APIuser.WEBSITE.getString());
		String profileUrl = user.getString(APIuser.PROFILEURL.getString());
		String avatarUrlMedium = user.getString(APIuser.AVATARURLMEDIUM.getString());
		String avatarUrlSmall = user.getString(APIuser.AVATARURLSMALL.getString());
		String avatarUrlTiny = user.getString(APIuser.AVATARURLTINY.getString());
		String avatarUrlLarge = user.getString(APIuser.AVATARURLLARGE.getString());
		String gender = user.getString(APIuser.GENDER.getString());
		String birthday = user.getString(APIuser.BIRTHDAY.getString());
		String hideUpvote = user.getString(APIuser.HIDEUPVOTE.getString());
		// String permissions = user.getString(APIuser.PERMISSIONS.getString());

		LoggedInUser u = new LoggedInUser(userId, accountId, loginName, fullName, emojiStatus, email, profileColor,
				hasPassword, fbUserId, fbDisplayName, gplusUserId, gplusAccountName, canPostToFB, fbPublish, fbTimeline,
				fbLikeAction, fbCreateAction, fbCommentAction, safeMode, about, lang, location, timezoneGmtOffset,
				website, profileUrl, avatarUrlMedium, avatarUrlSmall, avatarUrlTiny, avatarUrlLarge, gender, birthday,
				hideUpvote);

		// BiConsumer<String, JsonValue> biConsumer = (key, value) -> System.out
		// .println("String " + key + " = user.getString(" + "APIuser." +
		// key.toUpperCase() + ".getString());");

		// user.forEach(biConsumer);
		return u;

	}

	private boolean validateResponse(JsonObject obj)
	{

		if (obj.getJsonObject("meta").getString("status").equals("Success"))
			return true;
		else
			return false;
	}

	/**
	 * This method returns a {@link List} of {@link Post}s containing all fetched
	 * posts. If there was an error or an invalid response, this method will return
	 * null in such case.
	 * 
	 * @param group the Section you want to fetch posts from
	 * @param type  from where you want to grab the posts form, HOT, TRENDING or
	 *              FRESH
	 * @param count the amount of posts you want to fetch
	 * @return a List filled with posts or null
	 */
	public List<Post> getPosts(PostSection group, PostFrom type, int count)
	{

		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("group", String.valueOf(group.getId())));
		arg.add(new Argument<String, String>("type", type.getFrom()));
		arg.add(new Argument<String, String>("itemCount", String.valueOf(count)));
		arg.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		// arg.add(new Argument<String, String>("offset", String.valueOf(offset)));
		if (this.olderThan != null)
			arg.add(new Argument<String, String>("olderThan", String.valueOf(this.olderThan)));

		JsonObject response = makeRequest(RESTType.GET, APIpath.POST_LIST, APIservice.API, arg, null, null);
		if (validateResponse(response))
		{
			List<Post> retPosts = new ArrayList<Post>();

			JsonArray posts = response.getJsonObject("data").getJsonArray("posts");
			if (posts != null)
				for (int i = 0; i < posts.size(); i++)
				{
					Post p = new Post(this, posts.getJsonObject(i));
					this.olderThan = p.getId();
					retPosts.add(p);
				}

			return retPosts;
		} else
		{
			return null;
		}
	}

	/**
	 * Internal method. Which is public. Hurray. <br>
	 * Basically this method is used to send all requests.
	 * 
	 * @param method  the method GET/POST
	 * @param path    APIpath basically is url path for the thing you want to do
	 * @param service normally you would only want to use API
	 * @param args    arguments for GET/POST requests
	 * @param params  params for POST/GET requests
	 * @param ent     Entity for POST Body
	 * @return {@link JsonObject} the response
	 */
	public JsonObject makeRequest(RESTType method, APIpath path, APIservice service,
			List<Argument<String, String>> args, List<Argument<String, String>> params, HttpEntity ent)
	{

		String url = formatURL(service, path, args);

		List<Header> hheaders = new ArrayList<Header>();
		String timestamp = String.valueOf(JGagUtil.getTimeStamp());
		hheaders.add(new BasicHeader(HeaderType.GAG_GAG_TOKEN.getHeader(), this.token));
		hheaders.add(new BasicHeader(HeaderType.GAG_TIMESTAMP.getHeader(), timestamp));
		hheaders.add(new BasicHeader(HeaderType.GAG_APP_ID.getHeader(), this.app_id));
		hheaders.add(new BasicHeader(HeaderType.X_Package_ID.getHeader(), this.app_id));
		hheaders.add(new BasicHeader(HeaderType.GAG_DEVICE_UUID.getHeader(), this.device_uuid));
		hheaders.add(new BasicHeader(HeaderType.X_Device_UUID.getHeader(), this.device_uuid));
		hheaders.add(new BasicHeader(HeaderType.GAG_DEVICE_TYPE.getHeader(), DeviceType.ANDROID.GetDevice()));
		hheaders.add(new BasicHeader(HeaderType.GAG_BUCKET_NAME.getHeader(), "MAIN_RELEASE"));

		// Signing of the header
		hheaders.add(new BasicHeader(HeaderType.GAG_REQUEST_SIGNATURE.getHeader(),
				JGagUtil.sign(timestamp, this.app_id, this.device_uuid)));

		HttpGet get = new HttpGet(url);
		HttpPost post = new HttpPost(url);

		HttpResponse response = null;

		URIBuilder builder = new URIBuilder(get.getURI());
		List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
		if (params != null)
		{
			for (Argument<String, String> p : params)
			{

				parameterList.add(new BasicNameValuePair(p.a, p.b));
			}
			builder.addParameters(parameterList);
			try
			{
				if (method.equals(RESTType.GET))
					get.setURI(builder.build());
				else
					post.setURI(builder.build());
			} catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		Header[] headers = new Header[hheaders.size()];
		hheaders.toArray(headers);
		get.setHeaders(headers);
		post.setHeaders(headers);
		if (ent != null)
		{
			post.setEntity(ent);

		}
		client = HttpClientBuilder.create().build();
		// System.out.println(post.getRequestLine());
		try
		{
			if (method.equals(RESTType.GET))
				response = client.execute(get);
			else
				response = client.execute(post);
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		JsonReader jsonReader = null;
		try
		{
			jsonReader = Json.createReader(response.getEntity().getContent());
		} catch (UnsupportedOperationException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		JsonObject object = jsonReader.readObject();

		return object;
	}

	private String formatURL(APIservice service, APIpath path, List<Argument<String, String>> args)
	{
		StringBuilder sb = new StringBuilder();
		if (args != null)
			for (Argument<String, String> arg : args)
			{
				sb.append("/" + arg.a + "/" + arg.b);
			}
		String url = service.getService() + path.getPath() + sb.toString();
		return url;
	}

	/**
	 * Returns the currently logged in user.
	 * 
	 * @return the user which is currently logged in with JGag
	 */
	public LoggedInUser getLoggedInUser()
	{
		return loggedInUser;
	}
}
