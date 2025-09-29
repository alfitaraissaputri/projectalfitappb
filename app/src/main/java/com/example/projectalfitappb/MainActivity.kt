package com.example.projectalfitappb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.projectalfitappb.databinding.ActivityMainBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager.create(this)
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerEvents()
    }

    private fun registerEvents() {
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                val request = prepareRequest()
                loginByGoogle(request)
            }
        }
    }

    private fun prepareRequest(): GetCredentialRequest {
        val serverClientId = "543113924999-0jkh3sh4vpqggh4t6gnjsf6n3jcbue6i.apps.googleusercontent.com"

        val googleOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()
    }

    private suspend fun loginByGoogle(request: GetCredentialRequest) {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = this
            )

            val credential = result.credential
            val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

            firebaseLogin(idToken)
        } catch (exc: NoCredentialException) {
            Toast.makeText(this, "Login gagal : " + exc.message, Toast.LENGTH_LONG).show()
        } catch (exc: Exception) {
            Toast.makeText(this, "Login gagal : " + exc.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseLogin(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Login gagal", Toast.LENGTH_LONG).show()
                }
            }
    }
}