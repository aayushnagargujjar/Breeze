package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity1 : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signup = findViewById<Button>(R.id.signup)
        val login = findViewById<Button>(R.id.login)
        val mailid = findViewById<TextView>(R.id.mailid)
        val password =findViewById<TextView>(R.id.password)

        signup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val userid = mailid.text.toString().replace(".", ",")
            val pass = password.text.toString()
            if (userid.isNotEmpty() && pass.isNotEmpty()) {
                readdata(userid, pass)
            } else {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readdata(userid: String ,pass:String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        databaseReference.child(userid).get().addOnSuccessListener { it: DataSnapshot ->
            if (it.exists()) {
                if(pass == it.child("pass").value){
                val name = it.child("name").value
                val email = it.child("mail").value
                val password = it.child("pass").value

                Log.d("LoginActivity1", "Email: $email, Password: $password") // Debugging info

                val intent = Intent(this, welcomeactivity::class.java)
                intent.putExtra("name", name.toString())
                intent.putExtra("mail", email.toString())
                intent.putExtra("pass", password.toString())
                startActivity(intent)}
                else{
                    Toast.makeText(this, "password mismatch", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found ", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to read data", Toast.LENGTH_SHORT).show()
            Log.e("LoginActivity1", "Error fetching data", it) // Debugging info
        }
    }
}
