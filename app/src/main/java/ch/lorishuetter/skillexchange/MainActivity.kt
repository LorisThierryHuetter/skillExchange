package ch.lorishuetter.skillexchange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
            val createProfileIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createProfileIntent)
            finish()
        }
    }
}
/*
class MainViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userJson = sharedPref.getString("user", null)

        if (userJson != null) {
            val user = Gson().fromJson(userJson, User::class.java)
            displayUserProfile(user)
            getMatchedUsers(user.skills, user.interests)
        } else {
            // Error, user profile not found, redirect to CreateProfileActivity
            val createUserIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createUserIntent)
            finish()
        }
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
        val url = "https://skill-exchange.loris-huetter.ch/userHandler.php?skills=${skills.joinToString(",")}&interests=${interests.joinToString(",")}"

        // Send request
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainViewActivity, "Failed to get matched users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val matchedUsers = Gson().fromJson(json, MatchedUsers::class.java)

                runOnUiThread {
                        if (matchedUsers != null) {
                            // Display matched users
                            for (user in matchedUsers) {
                                val cardView = layoutInflater.inflate(R.layout.user_card, null)
                                val fullNameTextView = cardView.findViewById<TextView>(R.id.full_name_text_view)
                                val occupationTextView = cardView.findViewById<TextView>(R.id.occupation_text_view)
                                val skillsTextView = cardView.findViewById<TextView>(R.id.skills_text_view)
                                val interestsTextView = cardView.findViewById<TextView>(R.id.interests_text_view)

                                fullNameTextView.text = user.fullName
                                occupationTextView.text = user.occupation
                                skillsTextView.text = user.skills.joinToString(", ")
                                interestsTextView.text = user.interests.joinToString(", ")

                                matchesContainer.addView(cardView)
                            }
                        } else {
                            // Show "no matches" message
                            val noMatchesTextView = TextView(this)
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
*/
