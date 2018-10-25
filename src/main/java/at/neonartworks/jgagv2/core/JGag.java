package at.neonartworks.jgagv2.core;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import at.neonartworks.jgagv2.util.APIPath;
import at.neonartworks.jgagv2.util.AppID;
import at.neonartworks.jgagv2.util.Argument;
import at.neonartworks.jgagv2.util.DeviceType;
import at.neonartworks.jgagv2.util.HeaderType;
import at.neonartworks.jgagv2.util.JGagUtil;
import at.neonartworks.jgagv2.util.RESTType;
import at.neonartworks.jgagv2.util.Services;

public class JGag
{

	private String app_id;
	private String token;
	private String device_uuid;
	private final String LANG = "en_US";
	private CloseableHttpClient client;

	public JGag()
	{

		this.app_id = AppID.APP_ID.getID();
		this.token = JGagUtil.getRandomSha1();
		this.device_uuid = JGagUtil.getRandomUUID();
	}

	public boolean login(String ua, String pw)
	{
		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("password", JGagUtil.md5(pw)));
		arg.add(new Argument<String, String>("pushToken", this.token));
		arg.add(new Argument<String, String>("loginMethod", "9gag"));
		arg.add(new Argument<String, String>("loginName", ua));
		arg.add(new Argument<String, String>("language", LANG));

		JsonObject response = makeRequest(RESTType.GET, APIPath.USER_TOKEN, Services.API, arg, null, null);

		this.token = response.getJsonObject("data").getString("userToken");
		// this.userData = response.getJsonString("data");
		
		return validateResponse(response);
	}

	private boolean validateResponse(JsonObject obj)
	{

		if (obj.getJsonObject("meta").getString("status").equals("Success"))
			return true;
		else
			return false;
	}

	public List<Post> getPosts(int group, String type, int count)
	{

		List<Argument<String, String>> arg = new ArrayList<Argument<String, String>>();
		arg.add(new Argument<String, String>("group", String.valueOf(group)));
		arg.add(new Argument<String, String>("type", type));
		arg.add(new Argument<String, String>("itemCount", "9gag"));
		arg.add(new Argument<String, String>("entryTypes", "animated,photo,video,album"));
		arg.add(new Argument<String, String>("offset", String.valueOf(10)));

		JsonObject response = makeRequest(RESTType.GET, APIPath.POST_LIST, Services.API, arg, null, null);
		List<Post> retPosts = new ArrayList<Post>();

		JsonArray posts = response.getJsonObject("data").getJsonArray("posts");
		for (int i = 0; i < posts.size(); i++)
		{
			retPosts.add(new Post(posts.getJsonObject(i)));
		}

		return retPosts;
	}

	private JsonObject makeRequest(RESTType method, APIPath path, Services service, List<Argument<String, String>> args,
			List<Argument<String, String>> params, List<String> body)
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

		HttpGet get = new HttpGet();
		HttpResponse response = null;

		Header[] headers = new Header[hheaders.size()];
		hheaders.toArray(headers);
		get.setHeaders(headers);

		client = HttpClientBuilder.create().build();
		try
		{
			get.setURI(new URI(url));
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			response = client.execute(get);
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
		for (Argument<String, String> arg : args)
		{
			sb.append("/" + arg.a + "/" + arg.b);
		}
		String url = service.getService() + path.getPath() + sb.toString();
		return url;
	}
}
