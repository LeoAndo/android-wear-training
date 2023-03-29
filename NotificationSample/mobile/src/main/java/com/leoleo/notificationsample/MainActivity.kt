package com.leoleo.notificationsample

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val CHANNEL_ID = "NOTI_CHANNEL_ID"
    }

    private val permissionCheck =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Log.d("MainActivity", "result: $it")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.buttonSimple).setOnClickListener(this)
        findViewById<View>(R.id.buttonAction).setOnClickListener(this)
        findViewById<View>(R.id.buttonBigPicture).setOnClickListener(this)
        findViewById<View>(R.id.buttonBigText).setOnClickListener(this)
        findViewById<View>(R.id.buttonRemoteInput).setOnClickListener(this)
        findViewById<View>(R.id.buttonRemoteInput2).setOnClickListener(this)
        findViewById<View>(R.id.buttonMultiPage).setOnClickListener(this)
        findViewById<View>(R.id.buttonMultiStack).setOnClickListener(this)

        // Create the NotificationChannel
        val name = "通知テスト"
        val descriptionText = "Wear連携の通知テスト"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(mChannel)

        // Check Runtime Permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionCheck.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onResume() {
        super.onResume()
        showToast(getMessageText(intent))
    }

    override fun onClick(v: View?) {
        val viewId = v?.id ?: return
        when (viewId) {
            R.id.buttonSimple -> showSimpleNotification()
            R.id.buttonAction -> showSimpleNotificationWithAction()
            R.id.buttonBigPicture -> showBigPictureNotification()
            R.id.buttonBigText -> showBigTextNotification()
            R.id.buttonRemoteInput -> voiceReplyFreeText()
            R.id.buttonRemoteInput2 -> voiceReplySelection()
            R.id.buttonMultiPage -> showMultiPage()
            R.id.buttonMultiStack -> showMultiStack()
        }
    }

    /**
     * シンプルな通知を表示します。
     */
    private fun showSimpleNotification() {
        // 通知IDの定義
        val notificationId = 1000
        // 通知生成用ビルダーのインスタンス生成
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // アイコン
            .setContentTitle("Bar Mobile") // タイトル
            .setContentText("洋食／卵料理") // 本文
        // 通知マネージャーのインスタンス生成
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        // 通知の発行
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(notificationId, builder.build())
    }

    /**
     * 背景画像を含む通知を表示します。
     * BigPictureスタイルのノティフィケーション
     */
    private fun showBigPictureNotification() {
        val MY_NOTIFICATION_ID = 1001
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Bar Mobile")
            .setContentText("洋食／卵料理")
        val style: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle(
            builder
        )
        style.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.food))
        builder.setStyle(style)
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(MY_NOTIFICATION_ID, builder.build())
    }

    /**
     * 長文を含む通知を表示します。
     * BigTextスタイルのノティフィケーションを表示します。
     */
    private fun showBigTextNotification() {
        val MY_NOTIFICATION_ID = 1002
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.mail_small)
            .setContentTitle("Kenからのお知らせ")
        val style: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle(
            builder
        )
        style.bigText(
            "ご無沙汰しています。新しい開発者イベントの件で、ご連絡しました。"
                    + "今週の金曜日に新宿で開催します。"
        )
        builder.setStyle(style)
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(MY_NOTIFICATION_ID, builder.build())
    }

    /**
     * アクション付きのノティフィケーションを表示します(アクションボタンのみ追加)。
     */
    private fun showSimpleNotificationWithAction() {
        val MY_NOTIFICATION_ID = 1003
        // 電話発信のための暗黙的インテントの生成
        val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0123456789"))
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // ペンディングインテントの生成
        val pi = PendingIntent.getActivity(
            this, 0, i,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_restaurant)
            .setContentTitle("Bar Mobile")
            .setContentText("洋食／卵料理") // 通知へのペンディングインテント設定
            .addAction(
                android.R.drawable.sym_action_call,
                "電話発信", pi
            )
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(MY_NOTIFICATION_ID, builder.build())
    }

    /**
     * 複数ページを表示します。
     */
    private fun showMultiPage() {
        val MY_NOTIFICATION_ID = 1100
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.mail_large)
            .setContentTitle("Summary")
            .setContentText("Kenからのメッセージ")
        val detailPageStyle = NotificationCompat.BigTextStyle()
        detailPageStyle.setBigContentTitle("Detail")
            .bigText("お久しぶりです。私は北海道に来月行く予定です。もし都合が合えば、一緒にご飯でも行きませんか？よろしくお願いします。")
        val detailBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(detailPageStyle)
        val extraBuilder: NotificationCompat.Builder = NotificationCompat.WearableExtender()
            .addPage(detailBuilder.build())
            .extend(builder)
        val manager: NotificationManagerCompat = NotificationManagerCompat
            .from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(MY_NOTIFICATION_ID, extraBuilder.build())
    }

    /**
     * 複数の通知を集約表示します。
     */
    private fun showMultiStack() {
        val MY_NOTIFICATION_ID = 1050
        val GROUP_KEY_EMAILS = "group_key_email"
        val builder1: NotificationCompat.Builder = NotificationCompat.Builder(
            this
        ).setContentTitle("健一からのメール")
            .setContentText("先日のイベントの件について。")
            .setSmallIcon(R.drawable.mail_small)
            .setGroup(GROUP_KEY_EMAILS)
        val builder2: NotificationCompat.Builder = NotificationCompat.Builder(
            this
        ).setContentTitle("花子からのメール")
            .setContentText("お元気ですか？")
            .setSmallIcon(R.drawable.mail_small)
            .setGroup(GROUP_KEY_EMAILS)
        val builder3: NotificationCompat.Builder = NotificationCompat.Builder(
            this
        ).setContentTitle("蒼からのメール")
            .setContentText("こんにちは。")
            .setSmallIcon(R.drawable.mail_small)
            .setGroup(GROUP_KEY_EMAILS)
        val manager: NotificationManagerCompat = NotificationManagerCompat
            .from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(MY_NOTIFICATION_ID, builder1.build())
        manager.notify(MY_NOTIFICATION_ID + 1, builder2.build())
        manager.notify(MY_NOTIFICATION_ID + 2, builder3.build())
    }

    /**
     * ウェアラブル端末上で音声入力を行います。
     */
    private fun voiceReplyFreeText() {
        val notificationId = 1010
        val EXTRA_VOICE_REPLY = "extra_voice_reply"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, CHANNEL_ID
        ).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("音声入力").setContentText("音声で入力します。")

        // 音声入力結果を渡すIntentの生成
        val replyIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            replyIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // 音声入力機能のインスタンス生成
        val remoteInput: RemoteInput = RemoteInput.Builder(EXTRA_VOICE_REPLY)
            .setLabel("あなたの趣味は何ですか？").build()
        // 音声入力のアクション生成
        val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_btn_speak_now, "音声入力", pendingIntent
        )
            .addRemoteInput(remoteInput).build()
        // ウェアラブル用拡張機能のインスタンス生成
        val wearableExtender: NotificationCompat.WearableExtender =
            NotificationCompat.WearableExtender()
        // 音声アクションの設定
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(
            notificationId, builder.extend(
                wearableExtender.addAction(replyAction)
            ).build()
        )
    }

    /**
     * 音声結果が格納されたIntentを用いて処理を実施
     */
    private fun getMessageText(intent: Intent): CharSequence? {
        val EXTRA_VOICE_REPLY = "extra_voice_reply"
        // 音声入力結果を含むBundleの取得
        val remoteInput: Bundle = RemoteInput.getResultsFromIntent(intent) ?: return null
        return remoteInput.getCharSequence(EXTRA_VOICE_REPLY)
    }

    /**
     * ウェアラブル端末上で音声入力を行います。候補を画面に表示します。
     */
    private fun voiceReplySelection() {
        val notificationId = 1010
        val EXTRA_VOICE_REPLY = "extra_voice_reply"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, CHANNEL_ID
        ).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("音声入力").setContentText("音声で入力します。")

        // 音声入力結果を渡すIntentの生成
        val replyIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            replyIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // (a) ここから
        val replyChoices = resources.getStringArray(
            R.array.reply_choices
        )
        // (a) ここまで

        // 音声入力機能のインスタンス生成
        val remoteInput: RemoteInput = RemoteInput.Builder(EXTRA_VOICE_REPLY)
            .setLabel("あなたの趣味は何ですか？") // (b) ここから
            .setChoices(replyChoices).build()
        // (b) ここまで
        // 音声入力のアクション生成
        val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_btn_speak_now, "音声入力", pendingIntent
        ).addRemoteInput(remoteInput).build()
        // ウェアラブル用拡張機能のインスタンス生成
        val wearableExtender: NotificationCompat.WearableExtender =
            NotificationCompat.WearableExtender()
        // 音声アクションの設定
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(
            notificationId, builder.extend(
                wearableExtender.addAction(replyAction)
            ).build()
        )
    }

    private fun showToast(text: CharSequence?) {
        Toast.makeText(this, text ?: "", Toast.LENGTH_SHORT).show()
    }
}