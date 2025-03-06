package com.example.loginsignup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val homeFragment = Breezenews()
        val secondFragment = Bookmark()
        val thirdFragment = Profile()

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    setCurrentFragment(homeFragment)
                    val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
                    val iconView = bottomNavigationView.findViewById<View>(item.itemId)
                    iconView.startAnimation(animation)
                    true
                }
                R.id.navigation_Bookmark -> {
                    setCurrentFragment(secondFragment)
                    val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
                    val iconView = bottomNavigationView.findViewById<View>(item.itemId)
                    iconView.startAnimation(animation)
                    true
                }
                R.id.navigation_profile -> {
                    setCurrentFragment(thirdFragment)
                    val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
                    val iconView = bottomNavigationView.findViewById<View>(item.itemId)
                    iconView.startAnimation(animation)
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
