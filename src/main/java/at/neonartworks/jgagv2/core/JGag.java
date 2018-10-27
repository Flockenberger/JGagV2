package at.neonartworks.jgagv2.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import at.neonartworks.jgagv2.api.APIPath;
import at.neonartworks.jgagv2.api.APIuser;
import at.neonartworks.jgagv2.api.AppID;
import at.neonartworks.jgagv2.api.DeviceType;
import at.neonartworks.jgagv2.api.HeaderType;
import at.neonartworks.jgagv2.api.Services;
import at.neonartworks.jgagv2.core.post.Post;
import at.neonartworks.jgagv2.core.post.PostFrom;
import at.neonartworks.jgagv2.core.post.PostSection;
import at.neonartworks.jgagv2.core.post.Tag;
import at.neonartworks.jgagv2.util.Argument;
import at.neonartworks.jgagv2.util.JGagUtil;
import at.neonartworks.jgagv2.util.RESTType;
import at.neonartworks.jgagv2.util.SortBy;

/**
 * JGAG.
 * 
 * This class is the base of the Java library.
 * 
 * @author Florian Wagner
 *
 */
public class JGag
{
	/**
	 * ApiService Requests... POST post-submit/step/articleData
	 * post-submit/step/createMedia ->uploadID RequestBody, MultipartBody
	 * post-submit/step/createMedia ->uploadID RequestBody, urlMedia RequestBody
	 * 
	 * GET group-list ->entryTypes String, locale String,
	 * user-notifications/locale/{locale} ->locale String, refKey String
	 * post-list/group/{group}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}/olderThan/{olderThan}
	 * post-list/group/{group}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}
	 * post -> entryIds String entryTypes String
	 * search/query/{query}/fromIndex/{fromIndex}/itemCount/{itemCount}/entryTypes/{entryTypes}/sortBy/{sortBy}
	 * tag-search->query String, fromIndex String, itemCount String, entryTypes
	 * String, sortBy String
	 * post-list/userId/{userId}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}
	 * post-list/userId/{userId}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}/olderThan/{olderThan}
	 * tags ->type String url-info ->urls String user-info
	 * 
	 * // posting stuff to 9gag research // post_tags // upload_type 2 = gif, 5
	 * =mp4, else jpg! // source // upload_id // progress // section // submit_ts
	 * System.currentTimeMillis() // title // public static final int IMAGE_TYPE_GIF
	 * = 2; // public static final int IMAGE_TYPE_JPG = 1; // public static final
	 * int IMAGE_TYPE_MP4 = 5; // public static final int IMAGE_TYPE_RAW = 4;
	 */
	private String app_id;
	private String token;
	private String device_uuid;
	private final String LANG = "en_US";
	private CloseableHttpClient client;
	private String olderThan;
	private LoggedInUser loggedInUser;

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

		JsonObject response = makeRequest(RESTType.GET, APIPath.USER_TOKEN, Services.API, arg, null);
		// System.out.println(response);
		this.loggedInUser = getUserfromLoginResponse(response.getJsonObject("data").getJsonObject("user"));

		boolean succ = validateResponse(response);
		if (succ)
			this.token = response.getJsonObject("data").getString("userToken");
		if (!succ)
		{
			System.err.println("Error while login-attempt! Are your credentials correct?");
		}
		return succ;
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
		JsonObject response = makeRequest(RESTType.GET, APIPath.TAG_SEARCH, Services.API, null, arg);
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

	public void upload()
	{
		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("uploadId",
				getLoggedInUser().getAccountId() + "_" + System.currentTimeMillis()));
		arg.add(new Argument<String, String>("media_type", "5"));
		arg.add(new Argument<String, String>("upload_type", "5"));
		//arg.add(new Argument<String, String>("step_type", "1"));
		arg.add(new Argument<String, String>("media_meta", "1"));
		arg.add(new Argument<String, String>("group_id", String.valueOf(PostSection.FUNNY.getId())));
		arg.add(new Argument<String, String>("source", "https://www.youtube.com/watch?v=owu2oWX46Z0"));
		arg.add(new Argument<String, String>("urlMedia", "https://www.youtube.com/watch?v=owu2oWX46Z0"));

		JsonObject response = makeRequest(RESTType.POST, APIPath.POST_SUBMIT, Services.API, null, arg);
		System.out.println(response);
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
		JsonObject response = makeRequest(RESTType.GET, APIPath.TAG_SEARCH, Services.API, null, arg);
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

		LoggedInUser u = new LoggedInUser(userId, accountId, loginName, fullName, emojiStatus, email, profileColor, hasPassword,
				fbUserId, fbDisplayName, gplusUserId, gplusAccountName, canPostToFB, fbPublish, fbTimeline,
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

		JsonObject response = makeRequest(RESTType.GET, APIPath.POST_LIST, Services.API, arg, null);
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
	 * Originally an internal method used to make direct queries/requests to the
	 * 9gag servers.
	 * 
	 * @param method  the method of the request (GET or POST)
	 * @param path    the "path"-> API location
	 * @param service api service of 9gag
	 * @param args    arguments to send with the query
	 * @param params  parameters to send with the query
	 * @return returns an {@link JsonObject} containing the response
	 */
	public JsonObject makeRequest(RESTType method, APIPath path, Services service, List<Argument<String, String>> args,
			List<Argument<String, String>> params)
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Header[] headers = new Header[hheaders.size()];
		hheaders.toArray(headers);
		get.setHeaders(headers);
		post.setHeaders(headers);
		client = HttpClientBuilder.create().build();

		try
		{
			if (method.equals(RESTType.GET))
				response = client.execute(get);
			else
				response = client.execute(post);
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonReader jsonReader = null;
		try
		{
			jsonReader = Json.createReader(response.getEntity().getContent());
		} catch (UnsupportedOperationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObject object = jsonReader.readObject();

		return object;
	}

	private String formatURL(Services service, APIPath path, List<Argument<String, String>> args)
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

	public LoggedInUser getLoggedInUser()
	{
		return loggedInUser;
	}
}
