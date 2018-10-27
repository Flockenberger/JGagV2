# JGAG V2

This project is an unofficial Java 9GAG api

It uses 9gag's api

This Java library is a port of [this](https://github.com/and3rson/nineapi)  python client library by [and3rson](https://github.com/and3rson)

 ## Features
 ► Retrieve posts from every section.
 
 ► It is possible to get posts from HOT, TRENDING and FRESH.
 
 ► Search for posts.
 
 ► Posts have all comments.
 
## Currently Working On
 ► Uploading posts to 9gag.
 
 ► Writing comments.

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
		List<Post> posts = jgag.getPosts(PostGroup.FUNNY, PostFrom.TRENDING, 10);
		
		//of search for tags/strings
		QueryResult result = jgag.searchPosts("Gandalf", 10, SortBy.ASCENDING);
		//a QueryResult has all found Posts, and related Tags for the search
		
		//getPosts returns an empty List if no posts where found or null if an error occurred!
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
# Sharing Is Caring!
For more info on the headers and how the signing is made take a look at [this](https://github.com/and3rson/nineapi)  python client library by [and3rson](https://github.com/and3rson)
This is the information I gathered so far for the API itself.
The POST and GET request that I could find so far.
```
ApiService Requests... 
POST 
post-submit/step/articleData
post-submit/step/createMedia ->uploadID RequestBody, MultipartBody
post-submit/step/createMedia ->uploadID RequestBody, urlMedia RequestBody
	  
GET 
group-list ->entryTypes String, locale String,
user-notifications/locale/{locale} ->locale String, refKey String
post-list/group/{group}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}/olderThan/{olderThan}
post-list/group/{group}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}
post -> entryIds String entryTypes String
search/query/{query}/fromIndex/{fromIndex}/itemCount/{itemCount}/entryTypes/{entryTypes}/sortBy/{sortBy}
tag-search->query String, fromIndex String, itemCount String, entryTypes String, sortBy String
post-list/userId/{userId}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}
post-list/userId/{userId}/type/{type}/itemCount/{itemCount}/entryTypes/{entryTypes}/olderThan/{olderThan}
tags ->type String url-info ->urls String user-info
```

