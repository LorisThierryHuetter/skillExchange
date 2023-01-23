package ch.lorishuetter.skillexchange

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

data class User(val identifierKey: String, val fullName: String, val occupation: String, val skills: List<String>, val interests: List<String>)
data class MatchedUsers(val users: List<User>)

class MainViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        val sharedPref = getSharedPreferences("identifierKey", Context.MODE_PRIVATE)
        val userJson = sharedPref.getString("identiferKey", null)

        if (userJson != null) {
            val user = Gson().fromJson(userJson, User::class.java)
            displayUserProfile(user)
            getMatchedUsers(user.skills, user.interests)
        } /*else {
            // Error, user profile not found, redirect to CreateProfileActivity
            val createUserIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createUserIntent)
            finish()
        }*/
    }

    private fun displayUserProfile(user: User) {
        // Display user profile information
        val skillsTextView = findViewById<TextView>(R.id.skills_input)
        val interestsTextView = findViewById<TextView>(R.id.interests_input)
        skillsTextView.text = user.skills.joinToString(", ")
        interestsTextView.text = user.interests.joinToString(", ")
    }

    private fun getMatchedUsers(skills: List<String>, interests: List<String>) {
        // Build request URL
        val url =
            "https://skill-exchange.loris-huetter.ch/userHandler.php?skills=${skills.joinToString(",")}&interests=${
                interests.joinToString(",")
            }"

        // Send request
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainViewActivity,
                        "Failed to get matched users",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val matchedUsers = Gson().fromJson(json, MatchedUsers::class.java)
                // val matchedUsers: List<User>? = Gson().fromJson(json, MatchedUsers::class.java) // fetch matched users from the server
                val matchesContainer: LinearLayout = findViewById(R.id.matches_container)

                runOnUiThread {
                    if (matchedUsers != null) {
                        // Display matched users
                        for (user in matchedUsers.users) {
                            val cardView =
                                layoutInflater.inflate(R.layout.user_card, matchesContainer, false)
                            val fullNameTextView =
                                cardView.findViewById<TextView>(R.id.full_name_text_view)
                            val occupationTextView =
                                cardView.findViewById<TextView>(R.id.occupation_text_view)
                            val skillsTextView =
                                cardView.findViewById<TextView>(R.id.skills_text_view)
                            val interestsTextView =
                                cardView.findViewById<TextView>(R.id.interests_text_view)

                            fullNameTextView.text = user.fullName
                            occupationTextView.text = user.occupation
                            skillsTextView.text = user.skills.joinToString(", ")
                            interestsTextView.text = user.interests.joinToString(", ")

                            matchesContainer.addView(cardView)
                        }
                    } else {
                        // Show "no matches" message
                        val noMatchesTextView = TextView(this@MainViewActivity)
                        noMatchesTextView.text = "No matches"
                        noMatchesTextView.textSize = 24f
                        noMatchesTextView.setTextColor(Color.BLACK)
                        noMatchesTextView.gravity = Gravity.CENTER
                        matchesContainer.addView(noMatchesTextView)
                    }
                }
            }
        })
    }
}
