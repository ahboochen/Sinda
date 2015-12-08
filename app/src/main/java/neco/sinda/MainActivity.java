package neco.sinda;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new YoutubeLiveShow().execute("HD");
        new HttpGetTest().execute(Config.YOUTUBE_PLAYLIST);

        //我想爬山
    }

    /**
        * Get Youtube LiveShow Stream key & Play
        * @execute quality HD/SD
        *
        **/
    private class YoutubeLiveShow extends AsyncTask<String, Integer , String>{
        @Override
        protected String doInBackground(String... params) {
            String quality = params[0];
            String line;
            String streamKey = null;
            StringBuffer sb = new StringBuffer();

            try {
                URL url = new URL(Config.STREAM_KEY);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
                String data = sb.toString();

                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    if (!jsonObj.has("quality") || !jsonObj.has("key")) { continue; }
                    if (jsonObj.get("quality").toString().equals(quality)) {
                        streamKey = jsonObj.get("key").toString();
                    }
                }
            } catch (Exception  e) {
                e.printStackTrace();
            }

            return streamKey;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String streamKey) {
            super.onPostExecute(streamKey);
            Bundle bundle = new Bundle();
            bundle.putString("streamKey", streamKey);

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, YoutubeActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    /*
     ==================================================
     Get Youtube Video Items & ShowList
     params = url
     ==================================================
     */
    private class HttpGetTest extends AsyncTask<String, Integer , ArrayList> {

        @Override
        protected ArrayList<VideoInfo> doInBackground(String... params) {
            String urlStr = params[0];
            HttpClient client = new DefaultHttpClient();
            ArrayList<VideoInfo> videoItems = new ArrayList<>();

            try {
                HttpGet get = new HttpGet(urlStr);
                HttpResponse response = client.execute(get);
                HttpEntity resEntity = response.getEntity();
                String result = EntityUtils.toString(resEntity);

                JSONObject resultObj = new JSONObject(result);
                if (resultObj.has("nextPageToken")) {
                    String nextPageToken = resultObj.getString("nextPageToken");
                    System.out.println("nextPageToken: " + nextPageToken);
                }
                JSONArray itemsArray = resultObj.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.getJSONObject(i);
                    if (!itemObj.has("id")) { continue; }
                    String videoId = itemObj.getString("id");

                    if (!itemObj.has("snippet")) { continue; }
                    JSONObject snippetObj = itemObj.getJSONObject("snippet");

                    if (!snippetObj.has("title")) { continue; }
                    String videoTitle = snippetObj.getString("title");

                    if (!snippetObj.has("thumbnails")) { continue; }
                    JSONObject thumbnailsObj = snippetObj.getJSONObject("thumbnails");

                    if (!thumbnailsObj.has("high")) { continue; }
                    JSONObject thumbnailsHeihObj = thumbnailsObj.getJSONObject("high");

                    if (!thumbnailsHeihObj.has("url")) { continue; }
                    String imageUrl = thumbnailsHeihObj.getString("url");

                    if (!videoTitle.matches("\\d{4}年\\d{2}月\\d{2}日")) { continue; }
                    videoItems.add(new VideoInfo(videoId, videoTitle, imageUrl));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                client.getConnectionManager().shutdown();
            }
            return videoItems;
        }
    }

    class VideoInfo {
        public String id, title, img;
        public  VideoInfo(String id, String title, String img) {
            this.id = id;
            this.title = title;
            this.img = img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
