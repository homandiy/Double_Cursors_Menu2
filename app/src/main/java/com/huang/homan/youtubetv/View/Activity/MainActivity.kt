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
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.api.services.youtube.YouTube
import com.google.common.io.BaseEncoding

import com.huang.homan.youtubetv.Helper.BaseActivityVP
import com.huang.homan.youtubetv.Model.YouTubeSearch
import com.huang.homan.youtubetv.Presenter.MessagePresenter
import com.huang.homan.youtubetv.R
import okhttp3.OkHttpClient
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import okhttp3.logging.HttpLoggingInterceptor.Level

import com.huang.homan.youtubetv.BuildConfig
import com.huang.homan.youtubetv.Model.search.Item
import com.huang.homan.youtubetv.Model.search.SearchResult
import com.huang.homan.youtubetv.View.Fragment.SearchFragment
import com.huang.homan.youtubetv.View.widget.CursorMenu
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.logging.HttpLoggingInterceptor
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import io.reactivex.subjects.PublishSubject
import java.lang.StringBuilder


/*
 * Main Activity class that loads {@link MainFragment}.
 */
class MainActivity : FragmentActivity(), BaseActivityVP.View {

    lateinit var apiTest: TextView

    private var youTube: YouTube? = null
    private var presenter: MessagePresenter? = null

    private var mResult : List<Item>? = null

    private lateinit var searchResult : Single<SearchResult>
    
    private var task: YouTubeSearch? = null

    private lateinit var mSHA1: String

    // RxJava for focus subject
    // When subject is in hasFocus state, the fragment containers will switch to responsible fragment
    private val focusSubject = PublishSubject.create<String>()

    val fm : FragmentManager? = this.supportFragmentManager

    @SuppressLint("CheckResult")
    public override fun onCreate(savedInstanceState: Bundle?) {
        ltag("onCreate()")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiTest = findViewById(R.id.target)

        presenter = MessagePresenter(this)
/*
        val pkgName = this.packageName
        mSHA1 = RequestSHA1(pkgName)!!

        // Http Client
        val youTubeClient = getHttpClient(pkgName, mSHA1)

        // RxJava
        searchResult = YouTubeRxSearch("cat", youTubeClient).getResult()
        searchResult.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    mResult = result.items
                }, {
                    error ->
                    ltag("This error: ${error.message}")
                })
*/
        menuBar.mDataList = arrayListOf("Text 1", "Text 2", "Text 3", "Text 4", "Text 5", "Text 6", "Text 7", "Text 8")
        menuBar.cursorListener = mCursorListener // it will call search fragment

        menuBar.mCursorViewTop = cursorViewTop
        menuBar.mCursorViewBase = cursorViewBase

        menuBar.requestFocus()
    }

    fun loadFragment(pos: Int) {
        var mPos = pos
        if (mPos < 0) mPos = 0

        val item = menuBar.mDataList.get(mPos)
        ltag("menu option: $item")

        // update info target
        val info = "Page: $item"
        target.text = info

        var fragment =  fm!!.findFragmentByTag(item)

        if (fragment == null) {
            ltag("New fragment created: $item")

            val ft: FragmentTransaction = fm.beginTransaction()
            fragment = SearchFragment.newInstance(item)
            ft.replace(R.id.fragmentContainer, fragment, item)
            ft.addToBackStack(null)
            ft.commit()

        } else {
            ltag("Existed fragment tag: $item")

            val ft: FragmentTransaction = fm.beginTransaction()
            ft.replace(R.id.fragmentContainer, fragment)
            ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    fun getHttpClient(pkgName: String, fgpt: String) : OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()

        // Http Log
        httpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        })

        // Http Authentication
        httpClientBuilder.addInterceptor { chain ->
            val original = chain.request()

            // Add authentication
            val requestBuilder = original.newBuilder()
                    .header("X-Android-Package", pkgName)
                    .header("X-Android-Cert", fgpt)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClientBuilder.build()
    }


    private val mCursorListener = object : CursorMenu.CursorListener {
        override fun onCursorChange(position: Int, keyCode: Int) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (position > -1) loadFragment(position)
                }
                KeyEvent.KEYCODE_DPAD_UP -> {

                }
                KeyEvent.KEYCODE_DPAD_DOWN -> {

                }
                KeyEvent.KEYCODE_MENU -> {
                    menuBar.mDataList = arrayListOf("Sub1", "Sub2", "Sub3", "Sub4", "Sub5", "Sub6", "Sub7", "Sub8")
                }
            }
        }
    }

    @SuppressLint("PackageManagerGetSignatures")
    fun RequestSHA1(packageName: String): String? {
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

        fun ltag(message: String) { Log.i(TAG, message) }

        fun getApplicationName(context: Context): String {
            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
        }
    }



}

