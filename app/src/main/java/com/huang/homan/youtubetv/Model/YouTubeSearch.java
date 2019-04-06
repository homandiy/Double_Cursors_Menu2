package com.huang.homan.youtubetv.Model;

import android.os.AsyncTask;
import android.util.Log;


import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.Gson;
import com.huang.homan.youtubetv.Model.error.Response;
import com.huang.homan.youtubetv.Presenter.MessagePresenter;

import java.util.ArrayList;
import java.util.List;

public class YouTubeSearch extends AsyncTask<String, Void, List<SearchResult>> {
    /* Log tag and shortcut */
    private final static String TAG = "MYLOG "+YouTubeSearch.class.getSimpleName();
    private static void ltag(String message) { Log.i(TAG, message); }

    private Exception exception = null;
    private YouTube youTube = null;
    private String YOUR_API_KEY;
    private long maxResult;
    private MessagePresenter presenter;

    public YouTubeSearch(YouTube youTube, MessagePresenter presenter) {
        this.youTube = youTube;
        YouTubeConfig youTubeConfig = new YouTubeConfig();
        this.YOUR_API_KEY = youTubeConfig.getApiKey();
        this.maxResult = youTubeConfig.getMaxResult();
        this.presenter = presenter;
    }

    protected List<SearchResult> doInBackground(String... keywords) {
        YouTube.Search.List query;
        if (isCancelled()) return null;
        try {
            String keyword = keywords[0];

            query = youTube.search().list("id, snippet");
            query.setPart("snippet");
            query.setKey(YOUR_API_KEY);
            query.setType("video");
            query.setRelatedToVideoId("Ks-_Mh1QhMc");
            query.setQ(keyword);
            query.setMaxResults(maxResult);

            SearchListResponse response = query.execute();

            return response.getItems();
        } catch (Exception e) {
            this.exception = e;
            cancel(true);
            return null;
        }
    }

    protected void onPostExecute(List<SearchResult> results) {

        if (results != null) {
            presenter.sendMessage("YouTube API Test:\nWork!");
        } else {
            presenter.sendMessage("YouTube API Test:\nFailed!");
        }

        ltag("Result 0: "+results.get(0));
    }

    @Override
    protected void onCancelled(List<SearchResult> results) {
        ltag("onCancelled()");

        this.cancel(true);

        String s = exception.getMessage();

        // remove 1st line to get JSON
        s = s.substring(s.indexOf('\n')+1);

        // convert json into objects
        Gson gson = new Gson();
        Response response = gson.fromJson(s, Response.class);

        String errMsg = "HTTP Error: "+response.getCode()+"\n"+
                "Message: "+response.getMessage()+"\n"+
                "Reason: "+response.getErrors().get(0).getReason();

        ltag (errMsg);
        presenter.sendMessage(errMsg);
        super.onCancelled(results);
    }
}
