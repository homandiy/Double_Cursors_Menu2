/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.huang.homan.youtubetv.Model

/*
 * Search Param
 */
object QueryParam {
    const val part = "snippet"

    enum class ChannelType(private val text: String) {
        Any("any"),
        Show("show");

        override fun toString(): String {
            return text
        }
    }

    enum class EventType(private val text: String) {
        COMPLETED("completed"),
        LIVE("live"),
        UPCOMING("upcoming");

        override fun toString(): String {
            return text
        }
    }

    enum class Order(private val text: String) {
        Date("date"),
        Rating("rating"),
        Relevance("relevance"),
        Title("title"),
        VideoCount("videoCount"),
        ViewCount("viewCount");

        override fun toString(): String {
            return text
        }
    }

    enum class SafeSearch(private val text: String) {
        Moderate("moderate"),
        None("none"),
        Strict("strict");

        override fun toString(): String {
            return text
        }
    }

    enum class Type(private val text: String) {
        Channel("channel"),
        Playlist("playlist"),
        Video("video");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoCaption(private val text: String) {
        Any("any"),
        ClosedCaption("closedCaption"),
        None("none");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoDefinition(private val text: String) {
        Any("any"),
        High("high"),
        Standard("standard");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoDuration(private val text: String) {
        Any("any"),
        Long("long"),
        Medium("medium"),
        Short("short");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoEmbeddable(private val text: String) {
        Any("any"),
        True("true");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoLicense(private val text: String) {
        Any("any"),
        CreativeCommon("creativeCommon "),
        Youtube("youtube");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoSyndicated(private val text: String) {
        Any("any"),
        True("true");

        override fun toString(): String {
            return text
        }
    }

    enum class VideoType(private val text: String) {
        Any("any"),
        Episode("episode"),
        Movie("movie");

        override fun toString(): String {
            return text
        }
    }
}
