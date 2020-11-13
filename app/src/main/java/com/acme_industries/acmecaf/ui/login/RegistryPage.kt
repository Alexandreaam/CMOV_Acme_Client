package com.acme_industries.acmecaf.ui.login

import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acme_industries.acmecaf.MainActivityPage
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.Constants.Companion.serverUrl
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_register_data.*
import org.json.JSONObject
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.security.auth.x500.X500Principal


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
        val nif = findViewById<EditText>(R.id.editTextNIF).text.toString()

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
            nif == "" -> {
                editTextNIF.error = "This field can't be empty"
            }
            !password.matches(pattern) -> {
                editTextPass2.error = "Must contain at least 8 characters with one number, one lowercase, one uppercase and a special character"
            }
            password != passwordConfirm -> {
                editTextPass2.error = "Password does not match"
                editTextPassCheck.error = "Password does not match"
            }
            else -> {
                createUser(username, password, realName, creditDebit, nif)
            }
        }
    }

    private fun createUser(username: String, password: String, realName: String, creditDebit: String, nif: String) {
        val url = serverUrl + "users"
        val registerMessage = JSONObject()
        registerMessage.put("username", username)
        registerMessage.put("password", password)
        registerMessage.put("fullname", realName)
        registerMessage.put("creditcard", creditDebit)
        registerMessage.put("nif", nif)

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, registerMessage,
                { response ->
                    println("Response is: $response")

                    if (response.has("usernameTaken") && response.get("usernameTaken") == "True") {
                        editTextUser2.error = "Username already taken!"
                    } else if (response.has("username") && (response.get("username") == username)) {
                        sendCertificate()
                    }
                },
                { error ->
                    println("That didn't work: $error")
                    Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show()
                })

        queue.add(jsonObjectRequest)
    }

    private fun keyGen(): KeyPair? {

        val start = GregorianCalendar()
        val end = GregorianCalendar()
        end.add(Calendar.YEAR, 30)
        val keyAlias = "keyPair"

        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                "AndroidKeyStore"
        )
        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).run {
            setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            setCertificateNotBefore(start.time)
            setCertificateNotAfter(end.time)
            setCertificateSerialNumber(BigInteger.valueOf(12345678))
            setKeySize(512)
            setCertificateSubject(X500Principal("CN=$keyAlias"))
            build()
        }

        kpg.initialize(parameterSpec)

        return kpg.generateKeyPair()
    }

    private fun sendCertificate(){
        val kp = keyGen()

        if(kp == null) {
            println("Error making certificate")
            Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show()
        }

        else {
            val url = serverUrl + "users/cert"
            val certificateMessage = JSONObject()

            val cert: X509Certificate

            // Load Keystore
            val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore")
            ks.load(null)

            // Get public
            val entry: KeyStore.Entry = ks.getEntry("keyPair", null)

            cert = (entry as KeyStore.PrivateKeyEntry).certificate as X509Certificate
            val b64Cert: String = Base64.encodeToString(cert.encoded, Base64.NO_WRAP) // transform into Base64 string (PEM format without the header and footer)

            val payload = JSONObject.quote(b64Cert) // JSON requires enclosing quotes

            certificateMessage.put("payload", payload.toByteArray(Charset.forName("ISO-8859-1")))

            //TODO Send Certificate and store it
            /*
            val queue = Volley.newRequestQueue(this)

            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, certificateMessage,
                    { response ->
                        println("Response is: $response")
                        val intent = Intent(this, MainActivityPage::class.java).apply { }
                        startActivity(intent)
                    },
                    { error ->
                        println("That didn't work: $error")
                        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show()
                    })

            queue.add(jsonObjectRequest)
            */
            val intent = Intent(this, MainActivityPage::class.java).apply { }
            startActivity(intent)

        }
    }
}