package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Googleauth : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var database: DatabaseReference

    companion object {
        private const val RC_SIGN_IN = 20
        private const val TAG = "GoogleAuth"
    }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_googleauth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signin = findViewById<Button>(R.id.Signin)
        signin.setOnClickListener {
            val intent = Intent(this,LoginActivity1::class.java)
            startActivity(intent)
        }

        val Googleauthsignin = findViewById<ImageButton>(R.id.googlesignin)
        Googleauthsignin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                firebaseAuthWithGoogle(it.idToken!!)
            }
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign-in failed", e)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    user?.let {
                        saveUserToFirestore(it)
                        saveuserdatatorealtimedatabase(it)
                    }
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser) {
        val userData = hashMapOf(
            "uid" to user.uid,
            "name" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl?.toString()
        )

        db.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "User data saved to Firestore")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error saving user data to Firestore", e)
            }
    }
    private fun saveuserdatatorealtimedatabase(user: FirebaseUser) {
        val userData = hashMapOf(
            "uid" to user.uid,
            "name" to user.displayName,
            "mail" to user.email,
            "password" to "12345678",
            "photoUrl" to user.photoUrl?.toString())
        database = FirebaseDatabase.getInstance().getReference("User")
        database.child(user.email.toString().replace(".",",")).setValue(userData).addOnSuccessListener {
            Log.d(TAG, "User data saved to realtimedatabase")
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error saving user data to realtimedatabase", e)
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Signed in as ${user.displayName}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java) // Replace with your main activity
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}
