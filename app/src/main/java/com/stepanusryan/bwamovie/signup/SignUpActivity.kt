package com.stepanusryan.bwamovie.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.model.User
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var username:String
    lateinit var password:String
    lateinit var name:String
    lateinit var email:String

    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mFirebaseDatabaseReference: DatabaseReference
    lateinit var preference:Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference("User")
        preference = Preferences(this)

        btnRegisterNext.setOnClickListener {
            username = etUsername.text.toString()
            password = etPassword.text.toString()
            name = etName.text.toString()
            email = etEmail.text.toString()

            when {
                username == "" -> {
                    etUsername.error = "Silahkan mengisi username Anda"
                    etUsername.requestFocus()
                }
                password == "" -> {
                    etPassword.error = "Silahkan mengisi password Anda"
                    etPassword.requestFocus()
                }
                name == "" -> {
                    etName.error = "Silahkan mengisi nama Anda"
                    etName.requestFocus()
                }
                email == "" -> {
                    etEmail.error = "Silahkan mengisi email Anda"
                    etEmail.requestFocus()
                }
                else -> {
                    saveNewUser(username,password,name,email)
                }
            }
        }
    }

    private fun saveNewUser(username: String, password: String, name: String, email: String) {
        val user = User()
        user.username = username
        user.password = password
        user.nama = name
        user.email = email
        user.saldo = "0"

        checkUsername(username,user)

    }

    private fun checkUsername(username: String, user: User) {
        mFirebaseDatabaseReference.child(username).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = dataSnapshot.getValue(User::class.java)

                if (users == null)
                {
                    mFirebaseDatabaseReference.child(username).setValue(user)
                    preference.setValues("name",user.nama.toString())
                    preference.setValues("username",user.username.toString())
                    preference.setValues("password",user.password.toString())
                    preference.setValues("email",user.email.toString())
                    preference.setValues("saldo","0")
                    preference.setValues("status","1")
                    startActivity(Intent(this@SignUpActivity, PhotoScreenActivity::class.java).putExtra("name",user.nama).putExtra("username",user.username))
                }
                else
                {
                    Log.d("d","")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, databaseError.message,Toast.LENGTH_LONG).show()
            }

        })
    }
}