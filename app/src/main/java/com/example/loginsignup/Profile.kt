package com.example.loginsignup


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Profile : Fragment(R.layout.activity_welcomeactivity) {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation", "WrongViewCast", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val mail = currentUser?.email.toString().replace(".",",")
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("User")
            databaseReference.child(mail).get().addOnSuccessListener { it: DataSnapshot ->
                if (it.exists()) {
                    val name1 = it.child("name").value
                    Toast.makeText(requireContext(), "Welcome ${name1}", Toast.LENGTH_SHORT).show()
                    val mail = it.child("mail").value
                    val name = view.findViewById<TextView>(R.id.editTextText)
                    val gmail = view.findViewById<TextView>(R.id.mail)
                    name.text = "   ${name1}"
                    gmail.text = "   ${mail}"
                }
            }

            val updatePasswordEditText = view.findViewById<EditText>(R.id.newpasswordtxt)
            val updatePasswordButton = view.findViewById<Button>(R.id.updatepasswordbtn)

            updatePasswordButton.setOnClickListener {
                val newPassword = updatePasswordEditText.text.toString()
                if (newPassword.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter a new password", Toast.LENGTH_SHORT).show()
                } else {
                    updatePassword(newPassword)
                }
            }

            val signOutButton: ImageButton = view.findViewById(R.id.signout)
            signOutButton.setOnClickListener { signOut() }
        } else {
            val name1 = "Google user"
            val mail = "aayush@gmail.com"
            val name = view.findViewById<TextView>(R.id.editTextText)
            val gmail = view.findViewById<TextView>(R.id.mail)
            name.text = "   ${name1}"
            gmail.text = "   ${mail}"
        }
    }

    private fun updatePassword(newPassword: String) {
        auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error updating password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(requireContext(), Googleauth::class.java)
        Toast.makeText(requireContext(), "Sign out successful", Toast.LENGTH_SHORT).show()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
