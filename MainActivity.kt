package com.tuapp.navegadorstreaming

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import android.content.SharedPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var geckoView: GeckoView
    private lateinit var geckoSession: GeckoSession
    private lateinit var runtime: GeckoRuntime
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlBar = findViewById<EditText>(R.id.urlBar)
        val goButton = findViewById<Button>(R.id.goButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val reloadButton = findViewById<Button>(R.id.reloadButton)
        geckoView = findViewById(R.id.geckoView)

        runtime = GeckoRuntime.create(this)
        geckoSession = GeckoSession()
        geckoSession.open(runtime)
        geckoView.setSession(geckoSession)

        prefs = getSharedPreferences("Historial", MODE_PRIVATE)

        goButton.setOnClickListener {
            val input = urlBar.text.toString()
            val url = if (input.startsWith("http")) input else "https://duckduckgo.com/?q=$input"
            geckoSession.loadUri(url)
            guardarHistorial(url)
        }

        backButton.setOnClickListener {
            geckoSession.goBack()
        }

        reloadButton.setOnClickListener {
            geckoSession.reload()
        }
    }

    private fun guardarHistorial(url: String) {
        val editor = prefs.edit()
        val historial = prefs.getString("urls", "")
        editor.putString("urls", historial + "\n" + url)
        editor.apply()
    }
}
