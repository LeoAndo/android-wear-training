package com.leoleo.datacommunication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable
import com.leoleo.config.Const

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    private lateinit var mGoogleApiClient: GoogleApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Wearable.API)
            .build()

        val editText = findViewById<EditText>(R.id.editText)
        findViewById<View>(R.id.sendButton).setOnClickListener {
            sendDataByUsingMessageApi(editText.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun onConnected(p0: Bundle?) {
        Log.i("MainActivity", "onConnected")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("MainActivity", "onConnectionSuspended $p0")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("MainActivity", "onConnectionFailed $p0")
    }

    /**
     * テキストを送信します。
     *
     * @param text テキスト
     */
    private fun sendDataByUsingMessageApi(text: String) {
        Thread {
            val nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await() // 接続済みのノード一覧を取得
            for (node in nodes.nodes) {
                Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, node.id, Const.MsgEvent.PATH, text.toByteArray()
                )
            }
        }.start()
    }
}