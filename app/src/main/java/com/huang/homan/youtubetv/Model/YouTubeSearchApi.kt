package com.huang.homan.youtubetv.Model


import com.huang.homan.youtubetv.Model.search.SearchResult
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Provide YouTube Data Api V3 search service
 * by Retrofit and RxJava
 *
 * Example: val ytService = YouTubeSearchApi.Factory.create(httpClient)
 *          ytService.search(/* search params go in here */)
 */
interface YouTubeSearchApi {

    /**
     * YouTube Query:
     * query = youTube.search().list("id, snippet");
     * query.setPart("snippet");
     * query.setKey(YOUR_API_KEY);
     * query.setType("video");
     * query.setRelatedToVideoId("Ks-_Mh1QhMc");
     * query.setQ(keyword);
     * query.setMaxResults(maxResult);
     */
    @GET("/youtube/v3/search")
    fun search(
            @Query("part") part: String,
            @Query("key") apiKey: String,
            @Query("type") type: String,
            @Query("q") keyword: String,
            @Query("maxResults") maxResult: Int
    ): Single<SearchResult>

    /**
     * Factory class for convenient creation of the Api Service interface
     */
    companion object Factory {
        //val DevUrl = "https://developers.google.com"
        val BaseUrl = "https://www.googleapis.com"

        // Inject Http client for authentication
        fun create(okClient: OkHttpClient): YouTubeSearchApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BaseUrl)
                    .client(okClient)
                    .build()
            return retrofit.create(YouTubeSearchApi::class.java)
        }
    }
}
