package com.leoleo.datacommunication

import android.widget.Toast
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.leoleo.config.Const

/**
 * データ通信機能
 */
class DataListenerService : WearableListenerService() {
    // データ更新イベントを取得する.
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path ==  Const.MsgEvent.PATH) {
            val message = String(messageEvent.data)
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "Wear#Service"
    }
}