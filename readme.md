The following technologies were used in the project: Material Design, Retrofit, Gson, Pretty Time, Glide, 
and the MVVM pattern (ViewModel, LiveData, Room).
Configuration:
• compileSdkVersion 30
• buildToolsVersion "30.0.2"
• minSdkVersion 24
• targetSdkVersion 30
For convenience, classes were moved to different packages: models and interaction with the database to the 
“Models” package; interaction with the News API classes were placed in the “API” package; for interaction 
with RecyclerView, adapters were placed in the “Adapters”package.
Name of the app is“NEWS APP " (figure – 1). The app also has a Splash Screen.

![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/1.png)
![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/2.png)

For user convenience, 3 tabs were created: Top Headlines, Everything and “Read later".

1st tab, displays a list of news from the v2/top-headlines endpoint. The request to this endpoint 
is repeated every 5 seconds and updates the news if anything has changed. The user can also manually 
update the news list using SwipeToRefresh

2nd tab for all news on a specific topic from v2/everything. The user can update the news list 
using SwipeToRefresh. Was also implemented to search the key. By default, the search subject is “Trump".

Pagination is implemented and when you hover over the last page, the list of news items will be enlarged.

When you click on the heart, the news will be added to the database and automatically added to the 
Read later fragment when the user goes to 3 tab. When you click the heart in Read later, the news 
will be immediately deleted and when you click the “Delete all news” button, all news will be deleted.

![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/3.png)
![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/4.png)
![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/5.png)
![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/6.png)

When the user clicks on the news card, a new activity will automatically open, where the user can 
view the news in more detail. The news' page source will be loaded using WebView.

![alt text](https://raw.githubusercontent.com/ramapitecusment/news_api_android/master/images/7.png)