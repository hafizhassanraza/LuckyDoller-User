package com.enfotrix.luckydoller.UI

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityMainBinding
    private lateinit var modelUser: ModelUser
    private lateinit var phoneNumber:String
    private lateinit var emailAddress:String
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager

    private var announcementListener: ListenerRegistration? = null
    private var socialLinksListener: ListenerRegistration? = null
    private var db= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        supportActionBar?.hide()
        mContext=this@MainActivity
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)
        listenForAnnouncements()



        val textView1: TextView = findViewById(R.id.tvMarqee)
        val marqueeText = "ANNOUNCEMENT !!"
        textView1.text = marqueeText




        val text: String =
            "// LIVE Lucky Dollar.PK //// LIVE Lucky Dollar.PK //// LIVE LUCKY DOLLAR.PK//"
        val textView: TextView = findViewById(R.id.tvAnnouncement)
        textView.text = text
        textView.isSelected = true


        //listenForAnnouncements()
        listenForSocialLinks()


        binding.cdLogout.setOnClickListener {


                showLogoutDialog()

        }

        binding.cardResults.setOnClickListener{
            startActivity(Intent(mContext, ActivityResult::class.java))
        }
        binding.cardBids.setOnClickListener{
            startActivity(Intent(mContext, ActivityBid::class.java))
        }
        binding.cardNewBid.setOnClickListener{
            startActivity(Intent(mContext, ActivityNewBid::class.java))
        }
        binding.cardLiveStream.setOnClickListener{
            startActivity(Intent(mContext, ActivityLiveStream::class.java))
        }


        binding.whatsapp.setOnClickListener {
                      openWhatsApp()
                   }

        binding.mail.setOnClickListener {
                      openEmail()
                    }

    }


    ///////////////////// FUNCTION FOR LIVE ANNOUNCEMENT /////////////////
    private fun listenForAnnouncements() {
        val adminAnnouncementRef = db.collection(constants.ADMIN_COLLECTION)
            .document("Dg33Yix08jocNtRCPF2D")
        announcementListener = adminAnnouncementRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val announcement = documentSnapshot.getString("announcement")
                if (!announcement.isNullOrEmpty()) {
                    binding.tvAnnouncement.setText(announcement)
                } else {
                    binding.tvAnnouncement.setText("")
                }
            } else {
                binding.tvAnnouncement.setText("")
            }
        }
    }
    private fun stopListeningForAnnouncements() {
        announcementListener?.remove()
    }


    ///////////////// FUNCTION FOR LIVE LINKS //////////////////////////

    private fun listenForSocialLinks() {
        val socialLinksRef = db.collection(constants.SOCIAL_LINKS_COLLECTION)
            .document("4o7GvF2Fyaf33gljZAqf")
        socialLinksListener = socialLinksRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val fbLink = documentSnapshot.getString("fb")
                val ytLink = documentSnapshot.getString("yt")
                val twLink = documentSnapshot.getString("tw")
                phoneNumber = documentSnapshot.getString("whatsapp").toString()
                emailAddress = documentSnapshot.getString("mail").toString()
                val instaLink = documentSnapshot.getString("ig")
                if (!fbLink.isNullOrEmpty()) {
                    binding.facebook.setOnClickListener {
                        openLink(fbLink)
                    }
                }
                if (!ytLink.isNullOrEmpty()) {
                    binding.youtube.setOnClickListener {
                        openLink(ytLink)
                    }
                }
                if (!twLink.isNullOrEmpty()) {
                    binding.twitter.setOnClickListener {
                        openLink(twLink)
                    }
                }
//                if (!whatsappLink.isNullOrEmpty()) {
//                    binding.whatsapp.setOnClickListener {
//                        openLink(whatsappLink)
//                    }
             //   }
//                if (!mailLink.isNullOrEmpty()) {
//                    binding.mail.setOnClickListener {
//                        openLink(mailLink)
//                    }
//                }
                if (!instaLink.isNullOrEmpty()) {
                    binding.instagram.setOnClickListener {
                        openLink(instaLink)
                    }
                }
            }
        }
    }
    private fun stopListeningForSocialLinks() {
        socialLinksListener?.remove()
    }
    private fun openLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(mContext, "Error While Opening Link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }




                ///FOR OPENING OF WHATSAPP ////
    private fun openWhatsApp() {
//        val phoneNumber = "+923036307725" // Replace with the phone number you want to chat with
        val message = "Hello, this is a custom message" // Replace with the message you want to send

        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    ///FOR OPENING OF Email ////

    private fun openEmail() {
        val subject = "Hello, this is the email subject" // Replace with the email subject
        val message = "This is the email message body" // Replace with the email message body

        val uri = Uri.parse("mailto:$emailAddress?subject=$subject&body=$message")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }

    private fun showLogoutDialog()
    {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.dialog_for_logout)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textTitle = dialog.findViewById<TextView>(R.id.textTitle)
        val buttonYes = dialog.findViewById<Button>(R.id.buttonYes)
        val buttonNo = dialog.findViewById<Button>(R.id.buttonNo)
        buttonYes.setOnClickListener {

            sharedPrefManager.logOut(isLoggedOut = false)
            startActivity(Intent(mContext, ActivityLogin::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }

        buttonNo.setOnClickListener {
            // Handle the "No" button click (cancel logout)
            dialog.dismiss()
        }
        dialog.show()

    }




}