package com.acme_industries.acmecaf

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_register_data.*
import kotlinx.android.synthetic.main.fragment_simple_login.*

class RegistryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registry_page)
        fillFields()
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
        val passwordConfirm = findViewById<EditText>(R.id.editTextPassCheck).text.toString()
        val realName = findViewById<EditText>(R.id.editTextRealName).text.toString()
        val creditDebit = findViewById<EditText>(R.id.editTextCreditDebit).text.toString()
        val NIF = findViewById<EditText>(R.id.editTextNIF).text.toString()

        val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$".toRegex()

        when {
            username == "" -> {
                editTextUser2.error = "This field can't be empty"
            }
            password == "" -> {
                editTextPass2.error = "This field can't be empty"
            }
            passwordConfirm == "" -> {
                editTextPassCheck.error = "This field can't be empty"
            }
            realName == "" -> {
                editTextRealName.error = "This field can't be empty"
            }
            creditDebit == "" -> {
                editTextCreditDebit.error = "This field can't be empty"
            }
            NIF == "" -> {
                editTextNIF.error = "This field can't be empty"
            }
            !password.matches(pattern) -> {
                editTextPass2.error = "Must contain at least 8 characters with one number, one lowercase, one uppercase and a special character"
            }
            password != passwordConfirm -> {
                // pass_warning.visibility = View.VISIBLE
                editTextPass2.error = "Password does not match"
                editTextPassCheck.error = "Password does not match"
            }
            else -> {
                val intent = Intent(this, MainActivityPage::class.java).apply {
                    putExtra("user", username)
                    putExtra("pass", password)
                }
                startActivity(intent)
            }
        }




    }
}