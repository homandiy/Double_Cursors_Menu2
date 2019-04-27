package com.huang.homan.youtubetv.Model.search

data class SearchResult(
        val etag: String,
        val items: List<Item>,
        val kind: String,
        val nextPageToken: String,
        val pageInfo: PageInfo,
        val regionCode: String
)