package test;

import java.util.List;

import at.neonartworks.jgagv2.core.JGag;
import at.neonartworks.jgagv2.core.Post;
import at.neonartworks.jgagv2.core.PostFrom;
import at.neonartworks.jgagv2.core.PostSection;

public class JGagTest
{
	public static void main(String[] args)
	{
		JGag jgag = new JGag(); // create a new JGag instance

		// login to 9GAG with your credentials
		boolean login = jgag.login("USERNAME", "USER_TOKEN");

		if (login)
		{
			// grab Posts
			List<Post> posts = jgag.getPosts(PostSection.DARKHUMOR, PostFrom.HOT, 5, 0);

			// getPosts returns null if no posts where found or if an error occurred!

			if (posts != null && posts.size() >= 1)
			{
				for (Post p : posts)
				{
					System.out.println(p.getMediaURL());
				}
			}
		}
	}

}
