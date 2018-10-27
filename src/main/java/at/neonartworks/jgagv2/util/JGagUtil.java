package at.neonartworks.jgagv2.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * 
 * 
 * @author Florian Wagner
 *
 */
public class JGagUtil
{

	public static String getRandomUUID()
	{
		UUID uuidID = UUID.randomUUID();
		String uuid = uuidID.toString();

		uuid = uuid.replaceAll("-", "");
		return uuid;
	}

	public static String getRandomSha1()
	{
		String s1 = String.valueOf(getTimeStamp());

		String s2 = null;
		try
		{
			s2 = new String(s1.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s1 = DigestUtils.sha1Hex(s2.getBytes());
		return s1;

	}

	public static long getTimeStamp()
	{
		return System.currentTimeMillis();
	}

	public static String md5(String s1)
	{
		String s2 = null;
		try
		{
			s2 = new String(s1.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DigestUtils.md5Hex(s2.getBytes());
	}

	public static String sign(String timestamp, String app_id, String device_uuid)
	{
		String s1 = "*" + timestamp + "_._" + app_id + "._." + device_uuid + "9GAG";

		String s2 = null;
		try
		{
			s2 = new String(s1.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DigestUtils.sha1Hex(s2);

	}

}
