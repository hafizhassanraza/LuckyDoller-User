package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isEmpty
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.Admin
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ActivityLogin : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding: ActivityLoginBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var dialog: Dialog
    private lateinit var CNIc: String
    private lateinit var number: String
    private lateinit var dialog1: Dialog
    private lateinit var dialog2: Dialog
    private lateinit var sharedPrefManager: SharedPrefManager

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityLogin
        supportActionBar?.hide()
        utils = Utils(mContext)
        constants = Constants()


        sharedPrefManager = SharedPrefManager(mContext)


        binding.btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(binding.etCNIC.text.toString())) {
                binding.etCNIC.setError("Enter CNIC")
            } else if (binding.etCNIC.text.toString().length < 13) {
                binding.etCNIC.setError("Invalid CNIC")
            } else if (TextUtils.isEmpty(binding.etPassword.text.toString())) {
                binding.etPassword.setError("Enter PIN")
            } else {
                login(
                    utils.cnicFormate(binding.etCNIC.text.toString()),
                    binding.etPassword.text.toString()
                )
            }
        }

        binding.tvSignUp.setOnClickListener {

            startActivity(Intent(mContext, ActivitySignup::class.java))
        }


        binding.tvForgetPassword.setOnClickListener {
            showCnicDialog()

        }


    }

    fun login(cnic: String, pin: String) {

        utils.startLoadingAnimation()
        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC, cnic)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utils.endLoadingAnimation()
                    if (task.result.size() > 0) {


                        var modelUser: ModelUser? = null
                        for (document in task.result) {
                            modelUser = document.toObject(ModelUser::class.java)
                            modelUser.id = document.id
                        }

                        //Toast.makeText(mContext, pin+" "+modelUser?.pin, Toast.LENGTH_SHORT).show()
                        if (modelUser?.pin.equals(pin)) {


                            if (modelUser != null) {
                                sharedPrefManager.saveLoginAuth(modelUser, modelUser.id, true)
                            }

                            //Toast.makeText(mContext, "Login Successfull", Toast.LENGTH_SHORT).show()

                            startActivity(
                                Intent(
                                    mContext,
                                    MainActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        } else Toast.makeText(mContext, "Incorrect PIN", Toast.LENGTH_SHORT).show()

                    } else Toast.makeText(mContext, "CNIC Incorrect", Toast.LENGTH_LONG).show()

                }

            }
    }

    private fun showCnicDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_cnic_forget)

        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set dialog dimensions
        val params = WindowManager.LayoutParams()

        dialog.window?.attributes = params

        val editTextCNIC = dialog.findViewById<EditText>(R.id.editTextCNIC)
        val buttonSubmit = dialog.findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            var cnic = editTextCNIC.text.toString()
            CNIc = formatCnicWithDashes(cnic)

            if (checkCnicValidation(cnic)) {
                showPhoneNumbberDialog()
                dialog.dismiss()
            } else Toast.makeText(this, "you have entered invalid CNIC", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }


//    fun login(cnic: String) {
//
//        utils.startLoadingAnimation()
//        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC,cnic)
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    utils.endLoadingAnimation()
//                    if (task.result.size() > 0) {
//
//
//                        var modelUser: ModelUser? = null
//                        for (document in task.result) {
//                            modelUser = document.toObject(ModelUser::class.java)
//                            modelUser.id = document.id
//                        }
//
//                        //Toast.makeText(mContext, pin+" "+modelUser?.pin, Toast.LENGTH_SHORT).show()
//                        if (modelUser?.pin.equals(pin)) {
//
//
//                            if (modelUser != null) {
//                                sharedPrefManager.saveLoginAuth(modelUser, modelUser.id, true)
//                            }
//
//                            //Toast.makeText(mContext, "Login Successfull", Toast.LENGTH_SHORT).show()
//
//                            startActivity(
//                                Intent(
//                                    mContext,
//                                    MainActivity::class.java
//                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            )
//                            finish()
//
//                        } else Toast.makeText(mContext, "Incorrect PIN", Toast.LENGTH_SHORT).show()
//
//                    } else Toast.makeText(mContext, "CNIC Incorrect", Toast.LENGTH_LONG).show()
//
//                }
//
//            }
//    }

    private fun checkCnicValidation(CNIC: String): Boolean {

        var Cnic = formatCnicWithDashes(CNIC)
        Toast.makeText(mContext, Cnic, Toast.LENGTH_LONG).show()
        var flag = false
        utils.startLoadingAnimation()
        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC, Cnic)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    utils.endLoadingAnimation()
                    if (task.result.size() > 0) {
                        flag = true

                        showPhoneNumbberDialog()

                    }

                }
            }
        return flag


    }

    private fun showPhoneNumbberDialog() {
        dialog1 = Dialog(this) // Create a new Dialog instance
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.setContentView(R.layout.dialog_phone_confirm)

        dialog1.setCancelable(true)
        dialog1.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set dialog dimensions
        val params = WindowManager.LayoutParams()

        dialog1.window?.attributes = params

        val editTextphone = dialog1.findViewById<EditText>(R.id.editTextPhone)
        val buttonSubmit = dialog1.findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val phone = editTextphone.text.toString()
            number = phone
            checkPhoneValidation(phone)


        }

        dialog1.show()
    }


    private fun checkPhoneValidation(phone: String) {

        utils.startLoadingAnimation()

        db.collection(constants.USERS_COLLECTION).whereEqualTo("phone", phone)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utils.endLoadingAnimation()
                    if (task.result.size() > 0) {

                        dialog1.dismiss()

                        showNewPasswordDialog()
                    } else {
                        Toast.makeText(
                            this,
                            "You have entered incorrect phone number",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog1.dismiss()
                        showPhoneNumbberDialog()
                    }

                }
            }.addOnFailureListener {

                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()

            }


    }


    private fun showNewPasswordDialog() {

        dialog2 = Dialog(this) // Create a new Dialog instance
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog2.setContentView(R.layout.dialog_new_password)

        dialog2.setCancelable(true)
        dialog2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set dialog dimensions
        val params = WindowManager.LayoutParams()
        dialog2.window?.attributes = params

        val editTextPassword = dialog2.findViewById<EditText>(R.id.editTextPassword)
        val buttonSubmit = dialog2.findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val newPassword = editTextPassword.text.toString()

          //  updatePassword(newPassword)
            update(newPassword)
            dialog2.dismiss()

            startActivity(Intent(this, ActivityLogin::class.java))
            finish() // Close the current activity after navigating back to login
        }

        dialog2.show()
    }


    private fun formatCnicWithDashes(cnic: String): String {
        if (cnic.length != 13) {
            // CNIC should be exactly 13 digits long
            return cnic
        }

        val formattedCnic = StringBuilder()
        formattedCnic.append(cnic.substring(0, 5))
        formattedCnic.append("-")
        formattedCnic.append(cnic.substring(5, 12))
        formattedCnic.append("-")
        formattedCnic.append(cnic[12])

        return formattedCnic.toString()
    }


    private fun update(password: String) {
        utils.startLoadingAnimation()
        var modelUser: ModelUser? = null
        db.collection(constants.USERS_COLLECTION).whereEqualTo(constants.USER_CNIC, CNIc)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utils.endLoadingAnimation()
                    if (task.result.size() > 0) {
                        for (document in task.result) {
                            modelUser = document.toObject(ModelUser::class.java)
                            modelUser!!.id = document.id
                        }

                        //Toast.makeText(mContext, pin+" "+modelUser?.pin, Toast.LENGTH_SHORT).show()
                        modelUser?.let { sharedPrefManager.saveLoginAuth(it, modelUser!!.id, true) }
                        if(modelUser!=null){
                            modelUser?.pin=password
                            db.collection(constants.USERS_COLLECTION).document(sharedPrefManager.getToken()).set(
                                modelUser!!)
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        Toast.makeText(this, "password updated", Toast.LENGTH_SHORT).show()


                                    }


                                }

                        }




                    }

                }

            }
    }


//    private fun updatePassword(password: String) {
//        var user = sharedPrefManager.getUser()
//        user.pin = password
//        utils.startLoadingAnimation()
//        db.collection("Users")
//            .whereEqualTo("cnic", CNIc)
//            .whereEqualTo("phone", number)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                Toast.makeText(this, "debug0", Toast.LENGTH_SHORT).show()
//                for (document in querySnapshot.documents) {
//                    Toast.makeText(this, "debug1", Toast.LENGTH_SHORT).show()
//
//                    var documentId = document.id
//                    utils.endLoadingAnimation()
//                    Toast.makeText(this, "document id$documentId", Toast.LENGTH_SHORT).show()
//                    updatePasswordInDocument(documentId, password)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(
//                    mContext,
//                    "Error retrieving documents: ${exception.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//    }

//    private fun updatePasswordInDocument(documentId: String, newPassword: String) {
//        db.collection("Users")
//            .document(documentId)
//            .update("pin", newPassword)
//            .addOnSuccessListener {
//                Toast.makeText(mContext, "Password Updated successfully", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(
//                    mContext,
//                    "Error updating password: ${exception.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//    }


    }

