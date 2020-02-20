package com.access_company.android.devinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    companion object {
        private const val TAG = "DevInfo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch {
            loadAdvertisingId()
        }
    }

    private fun loadAdvertisingId() = launch(Dispatchers.IO) {
        val advertisingId = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext).id

        runOnUiThread {
            editTextAdvertisingId.setText(advertisingId)
            buttonLogcatAdvertisingId.setOnClickListener {
                Log.e(TAG, "AdvertisingId=$advertisingId")
            }
            buttonCopyAdvertisingId.setOnClickListener {
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
                    setPrimaryClip(ClipData.newPlainText("", advertisingId))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
