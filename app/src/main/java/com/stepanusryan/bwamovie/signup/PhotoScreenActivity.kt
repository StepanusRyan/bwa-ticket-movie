package com.stepanusryan.bwamovie.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.main.MainActivity
import com.stepanusryan.bwamovie.model.User
import com.stepanusryan.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_photo_screen.*
import java.util.*

class PhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQ_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath:Uri
    lateinit var storage:FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences
    lateinit var user : User
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase

    private var username:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_screen)
        btnLogin.visibility = View.INVISIBLE

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        txtName.text = "Selamat datang\n" + intent.getStringExtra("name")
        username = intent.getStringExtra("username")

        btnChoosePhoto.setOnClickListener {
            if (statusAdd)
            {
                statusAdd = false
                btnLogin.visibility = View.INVISIBLE
                btnChoosePhoto.setImageResource(R.drawable.ic_baseline_add_24)
                imageProfile.setImageResource(R.drawable.ic_baseline_person_24)
            }
            else
            {
//            Dexter.withContext(this@PhotoScreenActivity)
//                    .withPermission(Manifest.permission.CAMERA)
//                    .withListener(this)
//                    .check()
                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }

        }
        btnSkip.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@PhotoScreenActivity,MainActivity::class.java))
        }
        btnLogin.setOnClickListener {
            if (filePath != null)
            {
                var progressBar = ProgressDialog(this)
                progressBar.setTitle("Uploading...")
                progressBar.show()

                var ref = storageReference.child("images/"+ UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressBar.dismiss()
                        Toast.makeText(this@PhotoScreenActivity, "Uploaded",Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url",it.toString())
                            saveToFirebase(it.toString())
                        }
                    }
                    .addOnFailureListener {
                        progressBar.dismiss()
                        Toast.makeText(this@PhotoScreenActivity, "Failed" + it.message,Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                            taskSnapshot -> var progres = 100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressBar.setMessage("Upload :" + progres.toInt() + " %")
                    }
            }

        }

    }
    private fun saveToFirebase(url: String) {

        mFirebaseDatabase.child(username!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                mFirebaseDatabase.child(username!!).child("url").setValue(url)
                preferences.setValues("url", url)

                finishAffinity()
                val intent = Intent(this@PhotoScreenActivity,
                        MainActivity::class.java)
                startActivity(intent)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PhotoScreenActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent,REQ_IMAGE_CAPTURE)
            }
        }
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == REQ_IMAGE_CAPTURE && requestCode == Activity.RESULT_OK)
//        {
//            var bitmap = data?.extras?.get("data") as Bitmap
//            statusAdd = true
//
//            filePath = data.data!!
//            Glide.with(this)
//                    .load(bitmap)
//                    .into(imageProfile)
//
//            btnLogin.visibility = View.VISIBLE
//            btnChoosePhoto.setImageResource(R.drawable.ic_delete_forever_24px)
//            imageProfile.setImageResource(R.drawable.ic_baseline_person_24)
//
//        }
//        //super.onActivityResult(requestCode, resultCode, data)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
//            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true
            filePath = data?.data!!
            Glide.with(this)
                    .load(filePath)
                    .into(imageProfile)

            btnLogin.visibility = View.VISIBLE
            btnChoosePhoto.setImageResource(R.drawable.ic_delete_forever_24px)
            imageProfile.setImageResource(R.drawable.ic_baseline_person_24)

        }
        else if (resultCode == ImagePicker.RESULT_ERROR)
        {
            Toast.makeText(this,ImagePicker.getError(data),Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this,"Task canceled",Toast.LENGTH_LONG).show()
        }

    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        Toast.makeText(this@PhotoScreenActivity, "Anda tidak bisa menambahkan photo profile",Toast.LENGTH_LONG).show()
    }

}