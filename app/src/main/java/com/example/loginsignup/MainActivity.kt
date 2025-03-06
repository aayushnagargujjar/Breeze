package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser
        val email = currentUser?.email
        if (currentUser != null) {

            lifecycleScope.launch {
                delay(3000)
                val intent = Intent(this@MainActivity, Home::class.java)
                Toast.makeText(this@MainActivity, "Welcome back $email", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        } else {

            lifecycleScope.launch {
                delay(3000)
                val intent = Intent(this@MainActivity, Googleauth::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
