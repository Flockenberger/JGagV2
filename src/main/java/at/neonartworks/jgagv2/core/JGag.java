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
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import at.neonartworks.jgagv2.util.APIPath;
import at.neonartworks.jgagv2.util.AppID;
import at.neonartworks.jgagv2.util.Argument;
import at.neonartworks.jgagv2.util.DeviceType;
import at.neonartworks.jgagv2.util.HeaderType;
import at.neonartworks.jgagv2.util.JGagUtil;
import at.neonartworks.jgagv2.util.RESTType;
import at.neonartworks.jgagv2.util.Services;

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

	private String app_id;
	private String token;
	private String device_uuid;
	private final String LANG = "en_US";
	private CloseableHttpClient client;
	private String olderThan;

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
		boolean succ = validateResponse(response);
		if (succ)
			this.token = response.getJsonObject("data").getString("userToken");
		if (!succ)
		{
			System.err.println("Error while login-attempt! Are your credentials correct?");
		}
		return succ;
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
	 * @param group  the Section you want to fetch posts from
	 * @param type   from where you want to grab the posts form, HOT, TRENDING or
	 *               FRESH
	 * @param count  the amount of posts you want to fetch
	 * @param offset the offset of how many posts to ignore before fetching
	 * @return a List filled with posts or null
	 */
	public List<Post> getPosts(PostSection group, PostFrom type, int count, int offset)
	{

		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("group", String.valueOf(group.getId())));
		arg.add(new Argument<String, String>("type", type.getFrom()));
		arg.add(new Argument<String, String>("itemCount", String.valueOf(count)));
		arg.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		arg.add(new Argument<String, String>("offset", String.valueOf(offset)));
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

	// posting stuff to 9gag research
	// post_tags
	// upload_type 2 = gif, 5 =mp4, else jpg!
	// source
	// upload_id
	// progress
	// section
	// submit_ts System.currentTimeMillis()
	// title
	// public static final int IMAGE_TYPE_GIF = 2;
	// public static final int IMAGE_TYPE_JPG = 1;
	// public static final int IMAGE_TYPE_MP4 = 5;
	// public static final int IMAGE_TYPE_RAW = 4;
	public boolean uploadImage(String caption, ImageType imgType, PostSection section, List<Tag> tags,
			BufferedImage image)
	{

		return true;
	}

	public boolean uploadYoutubeVideo(String caption, ImageType imgType, PostSection section, List<Tag> tags,
			URL youtubeURL)
	{
		return true;
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
}
