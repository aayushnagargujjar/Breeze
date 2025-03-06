package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        auth = FirebaseAuth.getInstance()

        val signuplogin1 = findViewById<Button>(R.id.signuplogin)
        val nam = findViewById<TextView>(R.id.name)
        val gmail = findViewById<TextView>(R.id.gmail)
        val password = findViewById<TextView>(R.id.password)
        val alreadyuser = findViewById<Button>(R.id.alreadyuser)
        alreadyuser.setOnClickListener {
            val intent = Intent(this, LoginActivity1::class.java)
            startActivity(intent)
        }

        signuplogin1.setOnClickListener {
            val mail = gmail.text.toString().trim()
            val name = nam.text.toString().trim()
            val pass = password.text.toString().trim()


            if (mail.isNotEmpty() && name.isNotEmpty() && pass.isNotEmpty()) {
                createAccount(mail, pass, name)
            } else {
                Toast.makeText(this, "Name, Email, or Password is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = User(name, email, password)
                    storeUserInDatabase(user)
                } else {

                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun storeUserInDatabase(user: User) {
        database = FirebaseDatabase.getInstance().getReference("User")


        val userEmail = user.mail.replace(".", ",")
        database.child(userEmail).setValue(user).addOnSuccessListener {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, Home::class.java)
            startActivity(intent)

        }.addOnFailureListener {
            Toast.makeText(this, "Failed to store user data", Toast.LENGTH_SHORT).show()
        }
    }

    // User data class to store user information
    data class User(
        val name: String,
        val mail: String,
        val pass: String
    )
}