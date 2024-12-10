package com.example.loginsignup

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseReferenceHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var reference: DatabaseReference

    fun getInstance(): FirebaseDatabase {
        return database
    }

    fun child(name: String): DatabaseReference {
        reference = database.getReference(name)
        return reference
    }
}
