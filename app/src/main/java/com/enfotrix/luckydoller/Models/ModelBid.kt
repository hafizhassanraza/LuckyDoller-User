package com.enfotrix.luckydoller.Models

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class ModelBid @JvmOverloads constructor(
    var userID: String = "",
    var gameCtg: String = "",
    var gameSubCtg: String = "",
    val number: String = "",
    var amount: String = "",
    var status: String = "",
    var transactionID: String = "",
    var approve: String = "", //Pending
    var result: String = "",
    var id: String = "",
    var createdAt: String = SimpleDateFormat("dd/MM/yyyy").format(Date())
)
