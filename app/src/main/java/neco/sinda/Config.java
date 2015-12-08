package neco.sinda;

public class Config {
    public static final String GOOGLE_BROWSER_KEY = "AIzaSyAyZvPFtH_u59M5rU2huZLKEt__D9UIJD0";

    public static final String YOUTUBE_API_KEY = "AIzaSyAXOhl0MLE-4G5sLaq3f6-Ab0NGVoHciSc";
    public static final String YOUTUBE_CHANNEL_ID = "UCjy-ThZci2iWyignjL2fBug";
    public static final String YOUTUBE_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,id&order=title&maxResults=50&key=" + GOOGLE_BROWSER_KEY + "&channelId=" + YOUTUBE_CHANNEL_ID;
    public static final String YOUTUBE_PLAYLIST_ITEMS = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet,id&order=title&maxResults=50&key=" + GOOGLE_BROWSER_KEY + "&playlistId=";

    public static final String STREAM_KEY = "http://www.sindagmp.com.tw/data/youtube.txt";

}
