package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signuplogin1 = findViewById<Button>(R.id.signuplogin)
        val nam = findViewById<TextView>(R.id.name)
        val gmail = findViewById<TextView>(R.id.gmail)
        val password = findViewById<TextView>(R.id.password)

        signuplogin1.setOnClickListener {
            val mail = gmail.text.toString()
            val name = nam.text.toString()
            val pass = password.text.toString()
            val user = User(name, mail, pass)

            database = FirebaseDatabase.getInstance().getReference("User")
            database.child(mail).setValue(user).addOnSuccessListener {

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class User(val name: String, val mail: String, val pass: String)
}


