package com.huang.homan.youtubetv.View.Activity

import android.util.Log
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.huang.homan.youtubetv.Model.YouTubeConfig
import com.huang.homan.youtubetv.Model.YouTubeRxSearch
import com.huang.homan.youtubetv.Model.search.SearchResult
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val TAG = "MYLOG MainActivityTest"
    private fun ltag(message: String) { Log.i(TAG, message) }

    private lateinit var mainActivity: MainActivity
    private val httpResponseDelay : Long = 5000

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mainActivity = activityTestRule.activity
    }

    @Test
    fun testMaxResult() {
        ltag("Test")
        assertNotNull(mainActivity)

        val testSHA1 = mainActivity.RequestSHA1(mainActivity.packageName)
        val client = testSHA1?.let { mainActivity.getHttpClient(mainActivity.packageName, it) }

        // Observable: Single
        val testResult = YouTubeRxSearch("dog", client!!).getResult()
        // TestObserver
        val testObserver = TestObserver<SearchResult>()
        // TestScheduler
        val testScheduler = TestScheduler()
        // Process the observer
        testResult.subscribe(testObserver)
        // Delay
        testScheduler.advanceTimeBy(httpResponseDelay, TimeUnit.MILLISECONDS)
        // Test Observer
        val content = testObserver.values()
        assertNotNull(content)
        assertTrue("Download incomplete.", content.size >= 1)

        // Collect item from the data
        val testItems = content.get(0).items
        // Test items count with user setting
        val userSetting = YouTubeConfig().maxResults
        assertTrue(
                "Failed: downloaded items = ${testItems.size}, < required = $userSetting .",
                testItems.size >= userSetting
        )

        testObserver.assertComplete()


/*
        // Test Fragments added
        val fm : FragmentManager? = mainActivity.getManager()
        val fgList: List<Fragment> = fm?.fragments as List<Fragment>
        ltag("Added fragments: ${fgList.size}")
*/

    }


}

