package com.example.qrcode

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.data
import android.R.attr.text
import android.widget.Toast
import com.google.zxing.integration.android.IntentResult
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.AsyncTask
import android.widget.TextView
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder


public class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_scan.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                    GetJsonWithOkHttpClient(this.text).execute();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }

        }
    }

    open class GetJsonWithOkHttpClient(textView: TextView):AsyncTask<Unit, Unit, String>(){

        val mInnerTextView = textView

        override fun doInBackground(vararg params: Unit?):String?{
            val networkClient = NetworkClient()
            val stream = BufferedInputStream(
                networkClient.get(urlN))
            return readStream(stream)
        }

        override fun onPostExecute(result: String?){
            super.onPostExecute(result)
            mInnerTextView.text = result
        }

        fun readStream(inputStream: BufferedInputStream):String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }
    }
    companion object {
        @JvmField
        val urlN = "https://changemomentum.herokuapp.com/participants/registration/4"
    }
}
