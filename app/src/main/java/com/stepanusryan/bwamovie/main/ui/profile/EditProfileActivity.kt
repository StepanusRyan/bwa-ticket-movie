package com.stepanusryan.bwamovie.main.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.MainActivity
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    lateinit var preferences: Preferences
    lateinit var mDatabase:DatabaseReference

    private var username = ""
    private var password = ""
    private var email = ""
    private var name = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        preferences = Preferences(this)
        etUsername.setText(preferences.getValues("username"))
        etPassword.setText(preferences.getValues("password"))
        etEmail.setText(preferences.getValues("email"))
        etName.setText(preferences.getValues("name"))
        Glide.with(this)
                .load(preferences.getValues("url"))
                .into(circleImageView2)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        btnNewSave.setOnClickListener {
            username = etUsername.text.toString()
            password = etPassword.text.toString()
            email = etEmail.text.toString()
            name = etName.text.toString()


            checkUpdate(username,password,email,name)
            Toast.makeText(this,"Data berhasil diubah",Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))

        }
    }

    private fun checkUpdate(username: String, password: String, email: String, name: String) {
        mDatabase.child(username).child("username").setValue(username)
        mDatabase.child(username).child("password").setValue(password)
        mDatabase.child(username).child("email").setValue(email)
        mDatabase.child(username).child("nama").setValue(name)
        preferences.setValues("username",username)
        preferences.setValues("password",password)
        preferences.setValues("email",email)
        preferences.setValues("name",name)
    }
}