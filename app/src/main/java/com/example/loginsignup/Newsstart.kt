package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.databinding.ActivityNewsstartBinding

class Newsstart : AppCompatActivity() {
    private lateinit var binding: ActivityNewsstartBinding
    private val userArrayList = ArrayList<Usernews>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityNewsstartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle edge-to-edge system insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mail = intent.getStringExtra("mail")
        // Data for ListView
        val names = arrayOf("Allnews", "Bookmark", "Sports", "politcs")
        val imageIds = intArrayOf(
            R.drawable.whatsapp_image_2024_12_07_at_21_19_14_cbe4c351,
            R.drawable.bookmark,
            R.drawable.sport,
            R.drawable.politics


        )

        // Populate ArrayList with Usernews objects
        for (i in names.indices) {
            val user = Usernews(names[i], imageIds[i])
            userArrayList.add(user)
        }

        // Set up ListView and adapter
        val listView: ListView = binding.listview
        listView.adapter = MyAdapter(this, userArrayList)

        // Set OnItemClickListener to handle list item clicks
        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    val intent = Intent(this, Breezenews::class.java)
                    intent.putExtra("topic", "General")
                    intent.putExtra("mail", mail)
                    startActivity(intent)
                }

                1 -> {
                    val intent = Intent(this, Bookmark::class.java)
                    intent.putExtra("mail", mail)
                    startActivity(intent)

                }

                2 -> {
                    val intent = Intent(this, Breezenews::class.java)
                    intent.putExtra("topic", "Sports")
                    intent.putExtra("mail", mail)
                    startActivity(intent)
                }

                3 -> {
                    val intent = Intent(this, Breezenews::class.java)
                    intent.putExtra("topic", "Politics")
                    intent.putExtra("mail", mail)
                    startActivity(intent)
                }

            }
        }
    }
}

