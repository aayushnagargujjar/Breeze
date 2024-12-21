package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity1 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        auth = FirebaseAuth.getInstance()

        val signup = findViewById<Button>(R.id.signup)
        val login = findViewById<Button>(R.id.login)
        val mailid = findViewById<TextView>(R.id.mailid)
        val password = findViewById<TextView>(R.id.password)

        signup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val email = mailid.text.toString()
            val pass = password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginUser(email, pass)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val userid = currentUser.email!!.replace(".", ",")
                        checkUserInDatabase(userid)
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserInDatabase(userid: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        databaseReference.child(userid).get().addOnSuccessListener { it: DataSnapshot ->
            if (it.exists()) {
                val name = it.child("name").value
                val email = it.child("mail").value
                val password = it.child("pass").value

                Log.d("LoginActivity1", "Email: $email, Password: $password") // Debugging info

                val intent = Intent(this, welcomeactivity::class.java)
                intent.putExtra("name", name.toString())
                intent.putExtra("mail", email.toString())
                intent.putExtra("pass", password.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "User not found ", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to read data", Toast.LENGTH_SHORT).show()
            Log.e("LoginActivity1", "Error fetching data", it) // Debugging info
        }
    }
}