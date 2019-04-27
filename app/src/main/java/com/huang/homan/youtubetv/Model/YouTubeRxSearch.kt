package com.huang.homan.youtubetv.Model

import android.util.Log
import com.huang.homan.youtubetv.Model.search.SearchResult
import io.reactivex.Single
import okhttp3.OkHttpClient

class YouTubeRxSearch(
        private val keyword: String,
        private val httpClient: OkHttpClient) {
    /* Log tag and shortcut */
    private val TAG = "MYLOG " + YouTubeRxSearch::class.java.simpleName

    private fun ltag(message: String) {
        Log.i(TAG, message)
    }

    private val part = QueryParam.part
    private val apiKey = YouTubeConfig().apiKey
    private val type = QueryParam.Type.Video.toString()
    private val maxResult = YouTubeConfig().maxResults

    fun getResult() : Single<SearchResult> {
        ltag("searchResult: $keyword")

        val apiService = YouTubeSearchApi.create(httpClient)
        /*
            fun search(
                @Query("Part") part: String,
                @Query("key") apiKey: String,
                @Query("type") type: String,
                @Query("q") keyword: String,
                @Query("maxResult") maxResult: Long
            ): Single<SearchResult>
         */
        return apiService.search(part, apiKey, type, keyword, maxResult)
    }
}
