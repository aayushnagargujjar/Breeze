package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        var name1=intent.getStringExtra("name")
        var mail=intent.getStringExtra("mail")
             if (name1==null&&mail==null){
                 name1="User"
                 mail="Googleuser@gmail.com"
             }
        val welcometext=findViewById<TextView>(R.id.welcome)
        welcometext.text="welcome $name1"
        val nametext=findViewById<TextView>(R.id.name)
        nametext.text="your mail is $mail"
        val btn=findViewById<Button>(R.id.button4)
          btn.setOnClickListener {
            val intent = Intent(this@welcomeactivity,Newsstart::class.java)
            intent.putExtra("mail",mail)
            startActivity(intent)
            finish()
        }
    }
}