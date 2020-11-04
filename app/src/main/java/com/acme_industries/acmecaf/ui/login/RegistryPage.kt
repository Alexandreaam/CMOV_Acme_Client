package com.acme_industries.acmecaf.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import android.widget.EditText
import com.acme_industries.acmecaf.MainActivityPage
import com.acme_industries.acmecaf.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_register_data.*
import org.json.JSONObject
import java.security.KeyPair
import java.security.KeyPairGenerator

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

    private fun keyGen(): KeyPair? {

        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                "AndroidKeyStore"
        )
        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                "keyPair",
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).run {
            setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            build()
        }

        kpg.initialize(parameterSpec)

        return kpg.generateKeyPair()
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

                createUser(username, password, realName, creditDebit, NIF)
            }
        }
    }
    private fun createUser(username: String, password: String, realName: String, creditDebit: String, NIF: String) {
        val kp = keyGen()
        val url = "http://10.0.2.2:3000/users"
        val registerMessage = JSONObject()
        registerMessage.put("username",username)
        registerMessage.put( "password",password)
        registerMessage.put("fullname",realName)
        registerMessage.put("creditcard",creditDebit)
        registerMessage.put("nif",NIF)

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, registerMessage,
                { response ->
                    // Display the first 500 characters of the response string.
                    println("Response is: $response")
                },
                { error ->
                    println("That didn't work: $error")
                })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

        val intent = Intent(this, MainActivityPage::class.java).apply { }
        startActivity(intent)
    }
}