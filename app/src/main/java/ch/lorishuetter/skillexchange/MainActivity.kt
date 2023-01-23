package ch.lorishuetter.skillexchange

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val identifierKey = sharedPref.getString("identifier_key", null)

        if (identifierKey != null) {
            // User has a saved identifier key, launch MainViewActivity
            val mainViewIntent = Intent(this, MainViewActivity::class.java)
            startActivity(mainViewIntent)
            finish()
        } else {
            // User does not have a saved identifier key, launch CreateProfileActivity
            val createProfileIntent = Intent(this, CreateProfileActivity::class.java)
            startActivity(createProfileIntent)
            finish()
        }
    }
}

class MainViewActivity {

}
