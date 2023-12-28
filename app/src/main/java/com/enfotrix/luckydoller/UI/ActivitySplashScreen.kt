package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import java.util.Timer
import kotlin.concurrent.schedule

class ActivitySplashScreen : AppCompatActivity() {


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    private lateinit var dialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        mContext= this@ActivitySplashScreen;
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)

        Timer().schedule(1500){

            if(sharedPrefManager.isLoggedIn()==true){
                startActivity(Intent(mContext,MainActivity::class.java))
                finish()
            }
            else if(sharedPrefManager.isLoggedIn()==false) {
                startActivity(Intent(mContext,ActivityLogin::class.java))
                finish()
            }
            else if (sharedPrefManager.logOut().equals(false)){
                startActivity(Intent(mContext,ActivityLogin::class.java))
                finish()
            }

        }


    }
}