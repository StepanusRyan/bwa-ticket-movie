package com.stepanusryan.bwamovie.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.*
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.MainActivity
import com.stepanusryan.bwamovie.model.User
import com.stepanusryan.bwamovie.signup.SignUpActivity
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences
    lateinit var username:String
    lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboard","1")
        if (preferences.getValues("status").equals("1"))
        {
            finishAffinity()
            startActivity(Intent(this@SignInActivity,MainActivity::class.java))
        }

        btnLogin.setOnClickListener {
            username = etUsername.text.toString()
            password = etPassword.text.toString()
            when {
                username == "" -> {
                    etUsername.error = "Silahkan mengisi username Anda"
                    etUsername.requestFocus()
                }
                password == "" -> {
                    etPassword.error = "Silahkan mengisi password Anda"
                    etPassword.requestFocus()
                }
                else -> {
                    pushLogin(username,password)
                }
            }
        }
        btnRegister.setOnClickListener {
            startActivity(Intent(this@SignInActivity,SignUpActivity::class.java))
        }
    }

    private fun pushLogin(username: String, password: String) {
        mDatabase.child(username).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user == null)
                {
                    Toast.makeText(this@SignInActivity,"User tidak ditemukan",Toast.LENGTH_LONG).show()
                }
                else
                {
                    if (user.password != password)
                    {
                        Toast.makeText(this@SignInActivity,"Password Anda Salah",Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        preferences.setValues("name",user.nama.toString())
                        preferences.setValues("username",user.username.toString())
                        preferences.setValues("password",user.password.toString())
                        preferences.setValues("url",user.url.toString())
                        preferences.setValues("email",user.email.toString())
                        preferences.setValues("saldo",user.saldo.toString())
                        preferences.setValues("status","1")
                        startActivity(Intent(this@SignInActivity,MainActivity::class.java))
                        finish()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignInActivity,databaseError.message,Toast.LENGTH_LONG).show()
            }
        })
    }

}
