package test;

import java.util.List;

import at.neonartworks.jgagv2.core.JGag;
import at.neonartworks.jgagv2.core.Post;
import at.neonartworks.jgagv2.core.PostFrom;
import at.neonartworks.jgagv2.core.PostGroup;

public class JGagTest
{

	public static void main(String[] args)
	{
		JGag jgag = new JGag();

		boolean login = jgag.login("USERNAME", "USER_TOKEN");

		if (login)
		{
			List<Post> posts = jgag.getPosts(PostGroup.FUNNY, PostFrom.TRENDING, 10, 0);
			if (posts != null && posts.size() >= 1)
			{
				for (Post p : posts)
				{
					System.out.println(p.toString());
				}
			}
		}
	}

}
