package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class welcomeactivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcomeactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val name1=intent.getStringExtra("name")
        val mail=intent.getStringExtra("mail")

        val welcometext=findViewById<TextView>(R.id.welcome)
        welcometext.text="welcome $name1"
        val nametext=findViewById<TextView>(R.id.name)
        nametext.text="your mail is $mail"

        lifecycleScope.launch{
            delay(1000)

            val intent = Intent(this@welcomeactivity,Newsstart::class.java)
            intent.putExtra("mail",mail)
            startActivity(intent)
            finish()
        }
    }
}