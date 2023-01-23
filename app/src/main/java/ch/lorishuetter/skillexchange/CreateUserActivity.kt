package ch.lorishuetter.skillexchange

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.random.Random



class CreateUserActivity : AppCompatActivity() {

    private lateinit var fullNameInput: EditText
    private lateinit var occupationInput: EditText
    private lateinit var skillsInput: EditText
    private lateinit var interestsInput: EditText
    private lateinit var createProfileButton: MaterialButton
    // private var prefs: SharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    // private val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private var prefs: SharedPreferences? = null

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_activity)
        prefs = getSharedPreferences("identiferKey", Context.MODE_PRIVATE)
        // initializePrefs()

        // Bind views
        fullNameInput = findViewById(R.id.full_name_input)
        occupationInput = findViewById(R.id.occupation_input)
        skillsInput = findViewById(R.id.skills_input)
        interestsInput = findViewById(R.id.interests_input)
        createProfileButton = findViewById(R.id.create_profile_button)

        // Disable create profile button by default
        createProfileButton.isEnabled = false

        // Listen for input changes
        fullNameInput.addTextChangedListener(createProfileButtonEnabler)
        occupationInput.addTextChangedListener(createProfileButtonEnabler)
        skillsInput.addTextChangedListener(createProfileButtonEnabler)
        interestsInput.addTextChangedListener(createProfileButtonEnabler)

        // Listen for create profile button click
        createProfileButton.setOnClickListener {
            createProfile()
        }
    }

    /*
    private fun initializePrefs() {
        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
    */


    private val createProfileButtonEnabler = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val fullName = fullNameInput.text.toString().trim()
            val occupation = occupationInput.text.toString().trim()
            val skills = skillsInput.text.toString().trim()
            val interests = interestsInput.text.toString().trim()

            createProfileButton.isEnabled =
                fullName.isNotEmpty() && occupation.isNotEmpty() && skills.isNotEmpty() && interests.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


    private fun createProfile() {
        // Get user input
        val fullName = fullNameInput.text.toString()
        val occupation = occupationInput.text.toString()
        val skills = skillsInput.text.toString().split(",").toMutableList()
        val interests = interestsInput.text.toString().split(",").toMutableList()

        // Validate input
        if (fullName.isEmpty() || occupation.isEmpty() || skills.isEmpty() || interests.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Remove whitespace from skills and interests
        for (i in 0 until skills.size) {
            skills[i] = skills[i].trim()
        }
        for (i in 0 until interests.size) {
            interests[i] = interests[i].trim()
        }

        // Create a new identifier key
        var identifierKey = ""
        val characters = "abcdefghijklmnopqrstuvwxyz0123456789"
        for (i in 0 until 10) {
            identifierKey += characters[Random.nextInt(characters.length)]
        }

        // Create user profile object
        val user = User(identifierKey, fullName, occupation, skills, interests)

        // Convert user object to JSON
        val json = Gson().toJson(user)

// Send POST request to create profile
        val url =
            "https://skill-exchange.loris-huetter.ch/userHandler.php?identifierKey=$identifierKey"
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder().url(url).post(body).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@CreateUserActivity,
                        "Failed to create profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val statusCode = response.code
                if (statusCode == 409) {
                    // Identifier key already exists, try again
                    createProfile()
                } else if (statusCode == 201) {
                    // Profile created successfully
                    runOnUiThread {
                        Toast.makeText(
                            this@CreateUserActivity,
                            "Profile created",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Store identifier key and launch main activity
                        prefs?.edit()?.apply {
                            putString("identifierKey", identifierKey)
                            apply()
                        }
                        // prefs?.edit()?.putString("identifierKey", identifierKey)?.commit()
                        val intent = Intent(this@CreateUserActivity, MainViewActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Failed to create profile
                    runOnUiThread {
                        Toast.makeText(
                            this@CreateUserActivity,
                            "Failed to create profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}

