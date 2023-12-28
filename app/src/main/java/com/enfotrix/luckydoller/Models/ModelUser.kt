package com.enfotrix.luckydoller.Models

import com.google.firebase.Timestamp


data class ModelUser @JvmOverloads constructor(

    var cnic: String = "",
    var fulName: String = "",
    var fatherName: String = "",
    var phone: String = "",
    var pin: String = "",
    var balance: String = "",
    var status: String = "",
    var id: String = "",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)

