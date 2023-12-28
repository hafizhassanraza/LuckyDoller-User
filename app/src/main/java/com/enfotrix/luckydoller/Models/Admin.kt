package com.enfotrix.luckydoller.Models

import com.google.firebase.Timestamp

class Admin  @JvmOverloads constructor(

    var docID: String = "",
    var bidStatus: String = "",//Active,Stop
    var announcement: String = "",//Active,Stop
    var resultAt: Timestamp? = null,//Active,Stop

)