package com.acme_industries.acmecaf

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

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

        val intent = Intent(this, MainActivityPage::class.java).apply {
            putExtra("user", username)
            putExtra("pass", password)
        }
        startActivity(intent)
    }
}