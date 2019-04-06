/*
 * Copyright (C) 2014 The Android Open Source Project
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

@file:Suppress("DEPRECATION")

package com.huang.homan.youtubetv.View.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.common.io.BaseEncoding
import com.huang.homan.youtubetv.Helper.BaseActivityVP
import com.huang.homan.youtubetv.Model.YouTubeSearch
import com.huang.homan.youtubetv.Presenter.MessagePresenter
import com.huang.homan.youtubetv.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/*
 * Main Activity class that loads {@link MainFragment}.
 */
class MainActivity : Activity(), BaseActivityVP.View {

    lateinit var apiTest: TextView

    private var youTube: YouTube? = null
    private var presenter: MessagePresenter? = null

    private var task: YouTubeSearch? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiTest = findViewById(R.id.target)

        presenter = MessagePresenter(this)

        ltag("Error Status: false")
        // YouTube
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()

        val appName = getApplicationName(this)
        ltag("Application Name: $appName")

        val pkgName = this.packageName
        ltag("Package Name: $pkgName")

        youTube = YouTube.Builder(
                transport,
                jsonFactory
        ) { request ->
            val SHA1 = getSHA1(pkgName)
            request.headers.set("X-Android-Package", pkgName)
            request.headers.set("X-Android-Cert", SHA1)
        }.setApplicationName(appName).build()

        task = YouTubeSearch(youTube, presenter)
        task!!.execute("dog")
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun getSHA1(packageName: String): String? {
        try {
            val signatures = this.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES).signatures
            for (signature in signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())
                return BaseEncoding.base16().encode(md.digest())
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }


    override fun showMessage(msg: String) {
        ltag("showMessage()")
        apiTest.text = msg
    }

    override fun onResume() {
        super.onResume()
        ltag("onResume()")
    }

    override fun onPause() {
        super.onPause()
        ltag("onPause()")
    }

    override fun onStop() {
        super.onStop()
        ltag("onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        ltag("onRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        ltag("onDestroy()")
    }

    companion object {

        /* Log tag and shortcut */
        private val TAG = "MYLOG " + MainActivity::class.java.simpleName

        fun ltag(message: String) {
            Log.i(TAG, message)
        }

        fun getApplicationName(context: Context): String {
            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
        }
    }
}
