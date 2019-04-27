package com.huang.homan.youtubetv.View.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.huang.homan.youtubetv.View.Activity.MainActivity

/**
 * Created by Mr.T on 2018/3/29. Editor's Link: https://www.jianshu.com/u/5b14282c8054
 */
class CursorView
        @JvmOverloads
        constructor(context: Context,
                    attrs: AttributeSet? = null,
                    defStyleAttr: Int = 0)
        : ImageView(context, attrs, defStyleAttr) {

    init {
        visibility = View.INVISIBLE
    }

    private var mDuration = 0L
    private var mLastLocation = 0
    private val set = AnimatorSet()

    /**
     * cursor jump
     */
    fun fsatJumpTo(location: Int) {
        mDuration = 1L
        jumpTo(location)
        mDuration = 200L
    }

    /**
     * Cursor animation
     */
    fun jumpTo(location: Int) {
        val realLocation = location - width / 2
        ltag("mLastLocation: $mLastLocation     realLocation: $realLocation")

        if (mLastLocation == realLocation) return
        if (set.isRunning) set.cancel()
        createAnimator(realLocation).start()
        mLastLocation = realLocation
    }

    fun createAnimator(location: Int): AnimatorSet {

        val translationX = ObjectAnimator.ofFloat(this, "translationX", mLastLocation.toFloat(), location.toFloat())
        val rotationY = ObjectAnimator.ofFloat(this, "rotationY", 0.0f, if (mLastLocation > location) -180.0f else 180.0f, 0.0f)
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.2f, 1f)
        set.duration = mDuration
        set.playTogether(translationX, rotationY, scaleX)
        return set
    }

    companion object {

        /* Log tag and shortcut */
        private val TAG = "MYLOG " + CursorView::class.java.simpleName
        fun ltag(message: String) { Log.i(TAG, message) }

    }
}