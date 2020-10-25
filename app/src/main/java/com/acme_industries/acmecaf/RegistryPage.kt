package com.acme_industries.acmecaf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class RegistryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_registry_page)
        fillFields();
    }

    private fun fillFields() {
        val editName = findViewById<EditText>(R.id.editTextUser2)
        val editPassword = findViewById<EditText>(R.id.editTextPass2)
        val prevUser = intent.getStringExtra("user")
        val prevPass = intent.getStringExtra("pass")
        editName.setText(prevUser)
        editPassword.setText(prevPass)
    }

    fun loginFunction(view: View) {
        val username = findViewById<EditText>(R.id.editTextUser2).text.toString()
        val password = findViewById<EditText>(R.id.editTextPass2).text.toString()

        val intent = Intent(this, MainActivityPage::class.java).apply {
            putExtra("user", username)
            putExtra("pass", password)
        }
        startActivity(intent)
    }
}