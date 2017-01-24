# BVCache
Easy Image Caching for andorid

BVCache is a light weight image caching library that demonstrates the usage of LRUCache in android. 

## Usage instructions:
1) Download **AsyncImageDownloader.java** and **BVCache.java** files and include them in your Android Project.

2) To set image to an ImageView from a Url String is as simple as follows:

```

BVCache.setImageFromURL("Url for image goes here", imageView);
```

3) You can also use BVCache to cache other files types using -

```

BVCache.getFileFromURL("URL String goes here");
```
