package com.enfotrix.luckydoller.Models

import com.google.firebase.Timestamp

class ModelResult @JvmOverloads constructor(
    var gameCtg: String = "",
    var numberFirst: String = "",
    var numberSecond: String = "",
    var numberThird: String = "",
    var numberFourth: String = "",
    var result: String = "",
    var docID: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
