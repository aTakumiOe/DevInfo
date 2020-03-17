package com.access_company.android.devinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.iid.FirebaseInstanceId
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
            loadFirebaseInstanceId()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reload -> {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
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

    private fun loadFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                val token = task.result?.token
                editTextFirebaseInstanceId.setText(token)
                buttonLogcatFirebaseInstanceId.setOnClickListener {
                    Log.e(TAG, "FirebaseInstanceId=$token")
                }
                buttonCopyFirebaseInstanceId.setOnClickListener {
                    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
                        setPrimaryClip(ClipData.newPlainText("", token))
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
