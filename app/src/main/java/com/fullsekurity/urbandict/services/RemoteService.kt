package com.fullsekurity.urbandict.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Chronometer
import com.fullsekurity.urbandict.logger.LogUtils
import java.lang.ref.WeakReference


class RemoteService : Service() {

    companion object {
        const val MSG_GET_TIMESTAMP = 1000
    }

    private val TAG = LongRunningService::class.java.simpleName
    private lateinit var mChronometer: Chronometer
    private val messenger: Messenger = Messenger(RemoteServiceHandler(this))

    internal class RemoteServiceHandler(service: RemoteService) : Handler() {
        private val TAG = RemoteServiceHandler::class.java.simpleName
        private val service = WeakReference(service)
        override fun handleMessage(msg: Message) {
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteServiceHandler: message received 1"))
            when (msg.what) {
                MSG_GET_TIMESTAMP -> {
                    service.get()?. let { service_get ->
                        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteServiceHandler: message received 2"))
                        val elapsedMillis: Long = (SystemClock.elapsedRealtime() - service_get.mChronometer.getBase())
                        val hours = (elapsedMillis / 3600000).toInt()
                        val minutes = (elapsedMillis - hours * 3600000).toInt() / 60000
                        val seconds = (elapsedMillis - hours * 3600000 - minutes * 60000).toInt() / 1000
                        val millis = (elapsedMillis - hours * 3600000 - (minutes * 60000) - seconds * 1000).toInt()
                        val activityMessenger: Messenger = msg.replyTo
                        val bundle = Bundle()
                        bundle.putString("timestamp", "$hours:$minutes:$seconds:$millis")
                        val replyMsg: Message = Message.obtain(null, MSG_GET_TIMESTAMP)
                        replyMsg.data = bundle
                        try {
                            activityMessenger.send(replyMsg)
                        } catch (e: RemoteException) {
                            LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), e)
                        }
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteService: onCreate()"))
        mChronometer = Chronometer(this)
        mChronometer.base = SystemClock.elapsedRealtime()
        mChronometer.start()
    }

    override fun onBind(intent: Intent?): IBinder {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteService: onCreate()"))
        return messenger.binder
    }

    override fun onRebind(intent: Intent?) {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteService: onCreate()"))
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteService: onCreate()"))
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("RemoteService: onCreate()"))
        mChronometer.stop()
    }
}