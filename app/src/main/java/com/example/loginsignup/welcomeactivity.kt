package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation", "WrongViewCast", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcomeactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val homeiconbtn =findViewById<ImageButton>(R.id.homebtn)
        homeiconbtn.setOnClickListener{
            val intent =Intent(this,Breezenews::class.java)
            startActivity(intent)
        }
        val name1 = intent.getStringExtra("name") ?: "Aayush Nagar"
        val mail = intent.getStringExtra("mail") ?: "Googleuser12345@gmail.com"
        val name = findViewById<EditText>(R.id.editTextText)
        val gmail = findViewById<EditText>(R.id.mail)
        name.setText("   ${name1}")
        gmail.setText("   ${mail}")

        auth = FirebaseAuth.getInstance()

        val updatePasswordEditText = findViewById<EditText>(R.id.newpasswordtxt)
        val updatePasswordButton = findViewById<Button>(R.id.updatepasswordbtn)

        updatePasswordButton.setOnClickListener {
            val newPassword = updatePasswordEditText.text.toString()
            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            } else {
                updatePassword(newPassword)
            }
        }

        val signOutButton: ImageButton = findViewById(R.id.signout)
        signOutButton.setOnClickListener { signOut() }
    }

    private fun updatePassword(newPassword: String) {
        auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error updating password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signOut() {
        auth.signOut()

        val intent = Intent(this, example::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
