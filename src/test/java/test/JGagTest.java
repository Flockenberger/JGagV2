package test;

import java.util.List;

import at.neonartworks.jgagv2.core.JGag;
import at.neonartworks.jgagv2.core.Post;

public class JGagTest
{

	public static void main(String[] args)
	{
		JGag jgag = new JGag();

		boolean login = jgag.login("USERNAME", "CLIENT_SECRET");

		if (login)
		{

			List<Post> posts = jgag.getPosts(1, "hot", 10);

			for (Post p : posts)
			{
				System.out.println(p.getTitle() + " ;; " + p.getMediaURL());
			}

		}
	}

}
