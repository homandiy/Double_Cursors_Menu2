package com.huang.homan.youtubetv.Presenter

import com.huang.homan.youtubetv.Helper.BaseActivityVP
import com.huang.homan.youtubetv.View.Activity.MainActivity

class MessagePresenter(private val activity: MainActivity) : BaseActivityVP.Presenter {

    override fun sendMessage(msg: String) {
        activity.showMessage(msg)
    }
}
