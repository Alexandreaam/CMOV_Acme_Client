package com.acme_industries.acmecaf.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acme_industries.acmecaf.MainActivityPage
import com.acme_industries.acmecaf.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_register_data.*
import org.json.JSONObject

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_login_page)
    }

    fun fullRegistry(view: View) {
        val username = findViewById<EditText>(R.id.editTextUser).text.toString()
        val password = findViewById<EditText>(R.id.editTextPass).text.toString()

        val intent = Intent(this, RegistryPage::class.java).apply {
            putExtra("user", username)
            putExtra("pass", password)
        }
        startActivity(intent)
    }

    fun loginFunction(view: View) {
        val username = findViewById<EditText>(R.id.editTextUser).text.toString()
        val password = findViewById<EditText>(R.id.editTextPass).text.toString()

        val url = "http://10.0.2.2:3000/users/login"
        val registerMessage = JSONObject()
        registerMessage.put("username", username)
        registerMessage.put("password", password)

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, registerMessage,
                { response ->
                    // Display the first 500 characters of the response string.
                    println("Response is: $response")
                    if (response.has("result")) {
                        when {
                            response.get("result") == "Confirmed" -> {

                                //TODO Save session
                                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, MainActivityPage::class.java).apply {}
                                startActivity(intent)
                            }
                            response.get("result") == "Wrong" -> {
                                Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
                            }
                            response.get("result") == "Nonexistent" -> {
                                Toast.makeText(this, "Username does not exist", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                { error ->
                    println("That didn't work: $error")
                    Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show()
                })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
}