package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityMainBinding
import com.enfotrix.luckydoller.databinding.ActivitySignupBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivitySignup : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding: ActivitySignupBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mContext = this@ActivitySignup
        utils = Utils(mContext)
        constants = Constants()
        sharedPrefManager = SharedPrefManager(this)


        binding.tvLogin.setOnClickListener {
            startActivity(
                Intent(mContext, ActivityLogin::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        binding.imgBack.setOnClickListener {
            startActivity(
                Intent(
                    mContext,
                    ActivityLogin::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        binding.btnProfileRegister.setOnClickListener {

            val phone = binding.etPhone.text.toString()
            val Cphone = phone.substring(0, 2)
            if (TextUtils.isEmpty(binding.etFirstName.text.toString())) {
                binding.etFirstName.setError("Enter Your Name")
            } else if (!isNameValid(binding.etFirstName.text.toString())) {
                binding.etFirstName.setError("Enter valid name ")
            } else if (TextUtils.isEmpty(binding.etLastName.text.toString())) {
                binding.etLastName.setError("Enter Father Name")
            } else if (!isNameValid(binding.etLastName.text.toString())) {
                binding.etLastName.setError("Enter valid name ")
            } else if (TextUtils.isEmpty(binding.etCNIC.text.toString())) {
                binding.etCNIC.setError("Enter CNIC")
            } else if (binding.etCNIC.text.toString().length < 13) {
                binding.etCNIC.setError("Invalid CNIC")
            } else if (!isCnicValid(binding.etCNIC.text.toString())) {
                binding.etCNIC.setError("Invalid CNIC")
            } else if (TextUtils.isEmpty(binding.etPhone.text.toString())) {
                binding.etPhone.setError("Enter Phone Number")
            } else if (binding.etPhone.text.toString().length < 11) {
                binding.etPhone.setError("Invalid Phone Number")
            } else if (Cphone != "03") {
                binding.etPhone.setError("Invalid Phone Number")

            } else if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                binding.etPassword.setError("Enter Your Pin")
            } else if (!isPasswordValid(binding.etPassword.text.toString())) {
                binding.etPassword.setError("Passeord should be of 6 digits")
            } else {
                modelUser = ModelUser(
                    utils.cnicFormate(binding.etCNIC.text.toString()),
                    binding.etFirstName.text.toString(),//fullname
                    binding.etLastName.text.toString(),//Father Name
                    binding.etPhone.text.toString(),
                    binding.etPassword.text.toString()
                )
                Save(modelUser)
            }


        }


    }

    fun Save(modelUser: ModelUser) {


        utils.startLoadingAnimation()
        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC, modelUser.cnic)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.size() > 0) {
                        utils.endLoadingAnimation()
                        Toast.makeText(mContext, "CNIC already exists", Toast.LENGTH_SHORT).show()
                    } else {

                        val docRef: DocumentReference =
                            db.collection(constants.USERS_COLLECTION).document()
                        modelUser.id = docRef.id
                        Toast.makeText(mContext, "" + docRef.id, Toast.LENGTH_SHORT).show()
                        docRef.set(modelUser)
                            .addOnCompleteListener {
                                utils.endLoadingAnimation()
                                if (it.isSuccessful) {
                                    modelUser.id = docRef.id
                                    Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                                    sharedPrefManager.saveLoginAuth(modelUser, modelUser.id, true)
                                    startActivity(
                                        Intent(
                                            mContext,
                                            MainActivity::class.java
                                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                    finish()
                                }

                            }
                    }
                }
            }
    }

    fun isPasswordValid(password: String): Boolean {
        var flag = false
        if (password.length == 6)
            flag = true
        return flag
    }

    fun isNameValid(name: String): Boolean {
        if (name.length < 3 && name.length < 17) {
            return false
        }

        // Rule 2: Name should contain only English alphabets (a-z or A-Z)
        val alphabetsRegex = Regex("^[a-zA-Z ]+$")
        if (!name.matches(alphabetsRegex)) {
            return false
        }


        /////// If all rules are satisfied, the name is valid
        return true
    }

    fun isCnicValid(cnic: String): Boolean {
        var flag = true
        if (cnic.all { it == '0' }) {
            flag = false
        }
        return flag
    }


}