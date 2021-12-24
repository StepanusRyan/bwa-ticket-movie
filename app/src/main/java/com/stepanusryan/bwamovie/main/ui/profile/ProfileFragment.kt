package com.stepanusryan.bwamovie.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.stepanusryan.bwamovie.R
import com.stepanusryan.bwamovie.signin.SignInActivity
import com.stepanusryan.bwamovie.utils.Preferences
import com.stepanusryan.bwamovie.wallet.MyWalletActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    lateinit var preferences: Preferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferences = Preferences(requireContext())
        txtNama.text = preferences.getValues("name")
        txtEmail.text = preferences.getValues("email")
        Glide.with(this)
            .load(preferences.getValues("url"))
            .into(imageProfile)

        tv_my_wallet.setOnClickListener {
            startActivity(Intent(context, MyWalletActivity::class.java))
        }
        tv_edit_profile.setOnClickListener {
            startActivity(Intent(context,EditProfileActivity::class.java))
        }
        tv_change_language.setOnClickListener {
            Toast.makeText(context,"Fitur masih dalam pengembangan",Toast.LENGTH_LONG).show()
        }
        tv_help.setOnClickListener {
            Toast.makeText(context,"Fitur masih dalam pengembangan",Toast.LENGTH_LONG).show()
        }
        loguout.setOnClickListener {
            activity?.finishAffinity()
            preferences.toLogout(preferences.getValues("name").toString())
            startActivity(Intent(context,SignInActivity::class.java))
            Toast.makeText(context,"Logout Berhasil",Toast.LENGTH_LONG).show()
        }
    }
}