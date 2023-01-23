package ch.lorishuetter.skillexchange

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.telecom.Call
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        // Set background color
        window.decorView.setBackgroundColor(Color.parseColor("#37E1FD"))

        // Set icon and text
        val icon = findViewById<ImageView>(R.id.splash_icon)
        icon.setImageResource(R.drawable.fullLogo)
        val text = findViewById<TextView>(R.id.splash_text)
        text.text = "Skill Exchange"

        // Check for saved identifier key
        val prefs = getSharedPreferences("skillExchange", Context.MODE_PRIVATE)
        val identifierKey = prefs.getString("identifierKey", null)
        if (identifierKey == null) {
            // Show create user screen
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Try to get user profile using saved identifier key
            val url = "https://skill-exchange.loris-huetter.ch/userHandler.php?identifierKey=$identifierKey"
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Show create user screen
                    val intent = Intent(this@SplashActivity, CreateUserActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()?.string()
                    val user = Gson().fromJson(json, User::class.java)
                    if (user == null) {
                        // Show create user screen
                        val intent = Intent(this@SplashActivity, CreateUserActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Store user profile and show main screen
                        prefs.edit().putString("user", json).apply()
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }
    }
}
