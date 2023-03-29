package com.leoleo.datacommunication

import android.widget.Toast
import com.google.android.gms.wearable.*
import com.leoleo.config.Const
import com.leoleo.config.Const.DataEvent.DATA_PATH
import com.leoleo.config.Const.DataEvent.KEY_DATA

/**
 * データ通信機能
 */
class DataListenerService : WearableListenerService() {
    // Message APIを使った時に呼ばれる(データ更新イベントを取得する).
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == Const.MsgEvent.PATH) {
            val message = String(messageEvent.data)
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Data APIを使った通信時に呼ばれるコールバック
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem
                if (item.uri.path == DATA_PATH) {
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    Toast.makeText(
                        applicationContext,
                        dataMap.getString(KEY_DATA),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}