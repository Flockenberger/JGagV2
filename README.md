# JGAG V2

This project is an unofficial Java 9GAG api

It uses 9gag's api.

This Java library is a port and extension of [this](https://github.com/and3rson/nineapi)  python client library by [and3rson](https://github.com/and3rson)

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
		SearchResult result = jgag.searchPosts("Gandalf", 10, SortBy.ASCENDING);
		//a SearchResult has all found Posts, and related Tags for the search
		
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

9GAG does user GSON internally, so their responses etc. are Object based. Why didn't I do it this way? I only noticed this after I was halfway done with writing all my code. I will probably change my stuff to be GSON based. So it mirrors the 9GAG api better.
## Uploading to 9GAG

Upload URL: ```http://api.9gag.com/v2/post-submit/```

There are three steps to uploading an image to 9GAG through their api.

### Step 1: Sending the image to the 9GAG Server
The image-data Body consists of the following parts:
```
imageData ->FileBody Value: so far I only tested this with .jpg
step ->String: Value: imageData
uploadId ->String: Value: calculation of uploadId down below!
```
A successfull response looks as follows:
```
{"meta":{"timestamp":1540733039,"status":"Success","sid":":cHWBRXTUNWeMRVRywEVRNDTUlVY"},"data":{"dummyField":"dummyValue","imageStatus":1,"mediaStatus":1,"metaStatus":0,"entryId":null}}
```
Before sending the meta-data, make sure that imageStatus and mediaStatus are both 1!

### Step 2: Sending meta-data to the 9GAG Server
The meta-data Body consists of the following parts:
```
isNSFW ->Boolean: Value: 0/1
step ->String: Value: metaData
section ->String: Value: [9GAG Section in lower case!]
title ->String: Value: Title/caption of the post
tags ->String: Value: maximum of 3 Tags separated trough ','
uploadId ->String: Value: calculation of uploadId down below!
```
A successfull response looks as follows:
```
{"meta":{"timestamp":1540733040,"status":"Success","sid":"9gVQ01EVjlHTUVkMMRVT6xEVJlnT31TY"},"data":{"dummyField":"dummyValue","imageStatus":1,"mediaStatus":1,"metaStatus":1,"entryId":null}}
```
Before sending the last step, make sure that imageStatus, mediaStatus and metaStatus are returned as 1!

### Step3: TriggerCreation
Once we successfully uploaded the image/meta-data to the server we need to trigger the creation.
The Body consits of the following parts:
```
step ->String: Value: triggerCreation
uploadId ->String: Value: calculation of uploadId down below!
```
A successfull response looks as follows:
```
{"meta":{"timestamp":1540733040,"status":"Success","sid":"9gVQ01EVjlHTUVkMMRVS3xEVFlnT31TY"},"data":{"dummyField":"dummyValue","imageStatus":1,"mediaStatus":1,"metaStatus":1,"entryId":"a73yy7e"}}
```
Note the entryId is the id of the post.

## Calculating the uploadId

The upload id is a String value which consits of the accountId of the logged in User and a timestamp with an _ in between.
I noticed that 9GAG uses the timestamp which counts since 1970.
Example uploadId: ```50222134_1540733568```
