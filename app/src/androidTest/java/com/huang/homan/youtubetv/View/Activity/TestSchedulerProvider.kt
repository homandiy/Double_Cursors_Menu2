package com.huang.homan.youtubetv.View.Activity

import com.huang.homan.youtubetv.Helper.BaseSchedulerProvider
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val scheduler: TestScheduler) : BaseSchedulerProvider {
    override fun computation() = scheduler
    override fun ui() = scheduler
    override fun io() = scheduler
}