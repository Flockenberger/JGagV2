# JGAG V2

This project is an unofficial Java 9GAG api

It uses 9gag's api

This Java library is a port of [this](https://github.com/and3rson/nineapi)  python client library by [and3rson](https://github.com/and3rson)

 ## Features
 ► Retrieve posts from every section.
 
 ► It is possible to get posts from HOT, TRENDING and FRESH.
 
 ► Posts have:
 ```
	Title
	Type
	ID
	Image/Video URL
	Up/Downvotes and total votes
	whether if it is promoted or not
	whether if it is tagged with NSFW
	All Comments
	Section where it was uploaded to
	Tags (if available)
 ```	

## Getting Started

```java
public static void main(String[] args)
	{
		JGag jgag = new JGag(); //create a new JGag instance
		
		//login to 9GAG with your credentials
		boolean login = jgag.login("USERNAME", "USER_TOKEN");

		if (login)
		{
			//grab Posts
			List<Post> posts = jgag.getPosts(PostGroup.FUNNY, PostFrom.TRENDING, 10, 0);
			
			//getPosts returns null if no posts where found or if an error occurred!
			
			if (posts != null && posts.size() >= 1)
			{
				for (Post p : posts)
				{
					System.out.println(p.toString());
				}
			}
		}
	}
```
