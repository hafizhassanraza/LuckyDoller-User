package com.enfotrix.luckydoller.UI


import android.R
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelResult
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityLiveStreamBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CountDownLatch


class ActivityLiveStream : AppCompatActivity() {


    private  var first:String=""
    private  var second:String=""
    private  var third:String=""
    private  var fourth:String=""

    private val playWhenReady = true
    private val playbackPosition: Long = 0
    private val currentWindow = 0
    var urlStream: String? = null




    private val playerView: PlayerView? = null
    private var player: ExoPlayer? = null






    private var isHelloToastShown = true


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    // Define a Handler to manage the runnable execution
    val handler = Handler()


    private var continuousRunnable: Runnable? = null

    private var socialLinksListener: ListenerRegistration? = null


    private var db= Firebase.firestore
    private lateinit var binding : ActivityLiveStreamBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLiveStreamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@ActivityLiveStream
        utils = Utils(mContext)
        constants = Constants()
        sharedPrefManager = SharedPrefManager(mContext)
        listenForSocialLinks()

        /////get results here firstly    / /


        val colorFrom = resources.getColor(R.color.system_primary_fixed)
        val colorTo = resources.getColor(R.color.holo_blue_dark)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.addUpdateListener { animator -> binding.tv1.setTextColor(animator.animatedValue as Int) }
        colorAnimation.start()



        val stramLink="https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8"

        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            //.createMediaSource(MediaItem.fromUri("https://live.relentlessinnovations.net:1936/afghannobel/afghannobel/playlist.m3u8"))
            .createMediaSource(MediaItem.fromUri(stramLink))

        player = ExoPlayer.Builder(mContext).build()
        binding.palyerView.player= player
        player?.setMediaSource(hlsMediaSource)
        player?.prepare()
        player?.play()



        var toastShown = false // Add this flag as a class member

        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D")
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Toast.makeText(
                        this@ActivityLiveStream,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addSnapshotListener
                }

                snapshot?.let { document ->
                    val modelResult = document.toObject<ModelResult>()
                    val time: Timestamp? = modelResult?.createdAt

                    if (time != null) {


                        val firstNumber = document.getString("numberFirst")?.toString() ?: "0"
                        val secondNumber = document.getString("numberSecond")?.toString() ?: "0"
                        val thirdNumber = document.getString("numberThird")?.toString() ?: "0"
                        val fourthNumber = document.getString("numberFourth")?.toString() ?: "0"

                        // Log the values for debugging


                        // Now you can use the value of the fields as strings
                        first = firstNumber
                        second = secondNumber
                        third = thirdNumber
                        fourth = fourthNumber


                        // Get the current time as a Timestamp
                        val currentTime = Timestamp.now()

                        // Calculate the time difference in seconds
                        val timeDifference = Math.abs(currentTime.seconds - time.seconds)

                        if (timeDifference <= 180 && currentTime.seconds > time.seconds) {
                            if (!toastShown) {
                                toastShown = true
                                // Show the toast message
                                Toast.makeText(
                                    mContext,
                                    "Time from Firestore matches system time or is within a 3-minute window!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ShowResult()
                            }
                        } else if (currentTime.seconds < time.seconds) {
                            // Calculate the delay until the time matches
                            val delayMillis = (time.seconds - currentTime.seconds) * 1000

                            if (!toastShown) {
                                Handler().postDelayed({
                                    toastShown = true
                                    // Show the toast message
                                    Toast.makeText(
                                        mContext,
                                        "Toast message",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    ShowResult()
                                }, delayMillis)
                            }
                        }
                    }
                }
            }






//        db.collection("tempResult").document("D
    //
    //Yix08jocNtRCPF2D")
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val documentSnapshot = task.result
//
//                    if (documentSnapshot != null && documentSnapshot.exists()) {
//                        // Retrieve the value of the numeric fields
//                        val firstNumber = documentSnapshot.getString("numberFirst")?.toString() ?: "0"
//                        val secondNumber = documentSnapshot.getString("numberSecond")?.toString() ?: "0"
//                        val thirdNumber = documentSnapshot.getString("numberThird")?.toString() ?: "0"
//                        val fourthNumber = documentSnapshot.getString("numberFourth")?.toString() ?: "0"
//
//                        // Log the values for debugging
//
//
//                        // Now you can use the value of the fields as strings
//                        first = firstNumber
//                        second = secondNumber
//                        third = thirdNumber
//                        fourth = fourthNumber
//
//
//
//
//                        val text: String = "/// LIVE Lucky Dollar.PK ///"
//                        val textView: TextView = binding.tvInfo
//                        textView.text = text
//                        textView.isSelected = true
//
//                        val modelResult = documentSnapshot.toObject<ModelResult>()
//                        val time: Timestamp? = modelResult?.createdAt
//
//                        if (time != null) {
//                            // Stop previous Runnable if it exists
//                            continuousRunnable?.let { handler.removeCallbacks(it) }
//
//                            // Start the new Runnable
//                            checkTimeContinuously(time)
//                        }
//                        listenForSocialLinks()
//
//
//
//
//                        //val path = "android.resource://"+packageName+"/"+R.raw.test_video
//
//                        //////////////////HERE IS SUBSTRING CONVERSION//////////////////////
//
//
//
//                        /////***************************************************************/////
//
//                        //val resourceId1 = resources.getIdentifier("video1", "raw", packageName)
//                        //val path = "android.resource://" + packageName + "/" + resourceId1
//                       // binding.videView.setVideoURI(Uri.parse(path))
//                        //binding.videView.visibility=View.GONE
//
//
//                      /*if(s1=="8"){
//
//
//                          if(s2=="9")
//                          {
//                              val resourceId1 = resources.getIdentifier("video1", "raw", packageName)
//                              val path = "android.resource://" + packageName + "/" + resourceId1
//                              binding.videView.setVideoURI(Uri.parse(path))
//                              binding.videView.visibility=View.GONE
//                          }
//                      }*/
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                        /* binding.btnSwitch.setOnClickListener {
//                             player?.pause()
//                             binding.palyerView.visibility= View.GONE
//                             binding.videView.visibility=View.VISIBLE
//                             binding.videView.start()
//
//                         }
//                         binding.btnContinue.setOnClickListener {
//                             player?.play()
//                             binding.palyerView.visibility= View.VISIBLE
//                             binding.videView.visibility=View.GONE
//                             binding.videView.start()
//
//                         }*/
//
//
//                        val colorFrom = resources.getColor(R.color.system_primary_fixed)
//                        val colorTo = resources.getColor(R.color.holo_blue_dark)
//                        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
//                        colorAnimation.addUpdateListener { animator -> binding.tv1.setTextColor(animator.animatedValue as Int) }
//                        colorAnimation.start()
//
//
//
//                        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
//                        val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
//                            .createMediaSource(MediaItem.fromUri("https://live.relentlessinnovations.net:1936/afghannobel/afghannobel/playlist.m3u8"))
//
//                        player = ExoPlayer.Builder(mContext).build()
//                        binding.palyerView.player= player
//                        player?.setMediaSource(hlsMediaSource)
//                        player?.prepare()
//                        player?.play()
//
//
//
//
//
//
//
//
//
//
//                        // Display the values using Toast
//                        //Toast.makeText(this, "First number: $firstNumber", Toast.LENGTH_SHORT).show()
//                        //Toast.makeText(this, "Second number: $secondNumber", Toast.LENGTH_SHORT).show()
//                        //Toast.makeText(this, "Third number: $thirdNumber", Toast.LENGTH_SHORT).show()
//                        //Toast.makeText(this, "Fourth number: $fourthNumber", Toast.LENGTH_SHORT).show()
//                    } else {
//                        // Document doesn't exist
//                        Toast.makeText(this, "Document doesn't exist", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    // Task was not successful
//                    Toast.makeText(this, "Task failed", Toast.LENGTH_SHORT).show()
//                }
//            }



//        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D")
//            .addSnapshotListener { snapshot, firebaseFirestoreException ->
//                firebaseFirestoreException?.let {
//                    Toast.makeText(
//                        this@ActivityLiveStream,
//                        it.message.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@addSnapshotListener
//                }
//
//                snapshot?.let { document ->
//                    val modelResult = document.toObject<ModelResult>()
//                    val time: Timestamp? = modelResult?.createdAt
//
//                    if (time != null) {
//                        // Stop previous Runnable if it exists
//                        continuousRunnable?.let { handler.removeCallbacks(it) }
//
//                        Toast.makeText(mContext, time.toString(), Toast.LENGTH_SHORT).show()
//                        // Start the new Runnable
//                        checkTimeContinuously(time)
//                    }
//                }
//            }


    }


    override fun onBackPressed() {
        try {
            player?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release() // Release the player to avoid resource leaks
    }


    private fun pausePlayer() {
        if (player != null && player!!.isPlaying) {
            player?.pause()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onResume() {
        super.onResume()
        if (player != null && !player!!.isPlaying) {
            player?.play()
        }
    }


    fun ShowResult() {
        player?.pause()
        binding.palyerView.visibility = View.GONE
        binding.videView.visibility = View.VISIBLE
        binding.videView.start()

        val substrings = listOf(
            first.substring(0, 1), first.substring(1, 2), first.substring(2, 3), first.substring(3, 4),
            second.substring(0, 1), second.substring(1, 2), second.substring(2, 3), second.substring(3, 4),
            third.substring(0, 1), third.substring(1, 2), third.substring(2, 3), third.substring(3, 4),
            fourth.substring(0, 1), fourth.substring(1, 2), fourth.substring(2, 3), fourth.substring(3, 4)
        )

        //Intro Start
        val resourceId = resources.getIdentifier("intro", "raw", packageName)
        val path = "android.resource://" + packageName + "/" + resourceId
        binding.videView.setVideoURI(Uri.parse(path))
        binding.videView.start()


        // after intro completion
        binding.videView.setOnCompletionListener {
            binding.layResult.visibility=View.VISIBLE

            val scope = CoroutineScope(Dispatchers.Main)

            scope.launch {


                var counter:Int=1
                for (s in substrings) {

                    binding.layResultScreen.visibility=View.GONE
                    val videoNumber = "_${s.toIntOrNull()?.toString() ?: "0"}"
                    val resourceId = resources.getIdentifier(videoNumber, "raw", packageName)
                    val path = "android.resource://" + packageName + "/" + resourceId

                    binding.videView.setVideoURI(Uri.parse(path))
                    binding.videView.start()

                    val completionDeferred = CompletableDeferred<Unit>()
                    binding.videView.setOnCompletionListener {



                        if(counter==1){ binding.r1.text= s.toString() }
                        else if(counter==2) binding.r2.text= s.toString()
                        else if(counter==3) binding.r3.text= s.toString()
                        else if(counter==4) binding.r4.text= s.toString()
                        else if(counter==5) binding.r5.text= s.toString()
                        else if(counter==6) binding.r6.text= s.toString()
                        else if(counter==7) binding.r7.text= s.toString()
                        else if(counter==8) binding.r8.text= s.toString()
                        else if(counter==9) binding.r9.text= s.toString()
                        else if(counter==10) binding.r10.text= s.toString()
                        else if(counter==11) binding.r11.text= s.toString()
                        else if(counter==12) binding.r12.text= s.toString()
                        else if(counter==13) binding.r13.text= s.toString()
                        else if(counter==14) binding.r14.text= s.toString()
                        else if(counter==15) binding.r15.text= s.toString()
                        else if(counter==16) binding.r16.text= s.toString()
                        binding.videView.stopPlayback()


                        if (counter == 4 || counter == 8 || counter == 12 || counter == 16) {
                            if (counter == 4) {
                                binding.tvResultScreenFist.text = binding.r1.text.toString() + binding.r2.text.toString() + binding.r3.text.toString() + binding.r4.text.toString()
                            } else if (counter == 8) {
                                binding.tvResultScreenScondF.text = binding.r5.text.toString() + binding.r6.text.toString() + binding.r7.text.toString() + binding.r8.text.toString()
                            } else if (counter == 12) {
                                binding.tvResultScreenScondS.text = binding.r9.text.toString() + binding.r10.text.toString() + binding.r11.text.toString() + binding.r12.text.toString()
                            } else if (counter == 16) {
                                binding.tvResultScreenScondT.text = binding.r13.text.toString() + binding.r14.text.toString() + binding.r15.text.toString() + binding.r16.text.toString()
                            }

                            // Hide the layout initially
                            binding.layResultScreen.visibility = View.GONE

                            // Use a Handler to show it with a delay on the UI thread
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.layResultScreen.visibility = View.VISIBLE
                            }, 5000)


                        } else {
                            binding.layResultScreen.visibility = View.GONE
                        }

                        completionDeferred.complete(Unit)
                        counter++



                    }

                    completionDeferred.await() // Pause the loop until video completes

                    // Continue with the next iteration
                }

                // All videos have been played
                binding.palyerView.visibility = View.VISIBLE
                binding.videView.visibility = View.GONE
                player?.play()

                //handler.removeCallbacks(this)
                isHelloToastShown = false
            }

        }
    }

    /*fun ShowResult() {
        player?.pause()
        binding.palyerView.visibility = View.GONE
        binding.videView.visibility = View.VISIBLE
        binding.videView.start()

        val substrings = listOf(
            first.substring(0, 1), first.substring(1, 2), first.substring(2, 3), first.substring(3, 4),
            second.substring(0, 1), second.substring(1, 2), second.substring(2, 3), second.substring(3, 4),
            third.substring(0, 1), third.substring(1, 2), third.substring(2, 3), third.substring(3, 4),
            fourth.substring(0, 1), fourth.substring(1, 2), fourth.substring(2, 3), fourth.substring(3, 4)
        )

        //Intro Start
        val resourceId = resources.getIdentifier("intro", "raw", packageName)
        val path = "android.resource://" + packageName + "/" + resourceId
        binding.videView.setVideoURI(Uri.parse(path))
        binding.videView.start()

        val latch = CountDownLatch(substrings.size)

        // after intro completion
        binding.videView.setOnCompletionListener {
            binding.layResult.visibility = View.VISIBLE

            var counter: Int = 1
            for (s in substrings) {
                // defining video path
                val videoNumber = "_${s.toIntOrNull()?.toString() ?: "0"}"
                val resourceId = resources.getIdentifier(videoNumber, "raw", packageName)
                val path = "android.resource://" + packageName + "/" + resourceId

                // play video (single digit)
                binding.videView.setVideoURI(Uri.parse(path))
                binding.videView.start()

                binding.videView.setOnCompletionListener {
                    if (counter == 1) binding.r1.text = s.toString()
                    else if (counter == 2) binding.r2.text = s.toString()
                    // ... (other cases)
                    else if (counter == 16) binding.r16.text = s.toString()

                    binding.videView.stopPlayback()
                    counter++
                    latch.countDown() // Decrease the latch count
                }
            }

            // Wait for the latch countdown to reach 0 (all videos have completed)
            latch.await()

            // All videos have been played
            // continue the live stream
            binding.palyerView.visibility = View.VISIBLE
            binding.videView.visibility = View.GONE
            player?.play()
        }
    }*/


    /*fun ShowResult(){


        player?.pause()
        binding.palyerView.visibility = View.GONE
        binding.videView.visibility = View.VISIBLE
        binding.videView.start()

        val substrings = listOf(
            first.substring(0, 1),first.substring(1, 2), first.substring(2, 3), first.substring(3, 4),
            second.substring(0, 1),second.substring(1, 2), second.substring(2, 3), second.substring(3, 4),
            third.substring(0, 1),third.substring(1, 2), third.substring(2, 3), third.substring(3, 4),
            fourth.substring(0, 1),fourth.substring(1, 2), fourth.substring(2, 3), fourth.substring(3, 4)
        )

        //Intro Start
        val resourceId = resources.getIdentifier("intro", "raw", packageName)
        val path = "android.resource://" + packageName + "/" + resourceId
        binding.videView.setVideoURI(Uri.parse(path))
        binding.videView.start()


        // after intro completion
        binding.videView.setOnCompletionListener {

            binding.layResult.visibility=View.VISIBLE

            var counter:Int=1
            for (s in substrings) {

                // defining video path
                val videoNumber = "_${s.toIntOrNull()?.toString() ?: "0"}"
                val resourceId = resources.getIdentifier(videoNumber, "raw", packageName)
                val path = "android.resource://" + packageName + "/" + resourceId

                // play video (single digit)
                binding.videView.setVideoURI(Uri.parse(path))
                binding.videView.start()


                binding.videView.setOnCompletionListener {



                    if(counter==1) binding.r1.text= s.toString()
                    else if(counter==2) binding.r2.text= s.toString()
                    else if(counter==3) binding.r3.text= s.toString()
                    else if(counter==4) binding.r4.text= s.toString()
                    else if(counter==5) binding.r5.text= s.toString()
                    else if(counter==6) binding.r6.text= s.toString()
                    else if(counter==7) binding.r7.text= s.toString()
                    else if(counter==8) binding.r8.text= s.toString()
                    else if(counter==9) binding.r9.text= s.toString()
                    else if(counter==10) binding.r10.text= s.toString()
                    else if(counter==11) binding.r11.text= s.toString()
                    else if(counter==12) binding.r12.text= s.toString()
                    else if(counter==13) binding.r13.text= s.toString()
                    else if(counter==14) binding.r14.text= s.toString()
                    else if(counter==15) binding.r15.text= s.toString()
                    else if(counter==16) binding.r16.text= s.toString()

                    binding.videView.stopPlayback()
                    counter++


                }


            }


            //final case
            // All videos have been played
            // continue the live stream
            binding.palyerView.visibility = View.VISIBLE
            binding.videView.visibility = View.GONE
            player?.play()



        }




    }*/


    fun checkTimeContinuously(time: Timestamp?) {
        if (time == null) {
            // Handle the case where the 'time' parameter is null or invalid
            return
        }

        val gmt5TimeZone = TimeZone.getTimeZone("GMT+5")
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm:ss a", Locale.ENGLISH)
        dateFormat.timeZone = gmt5TimeZone

        // Runnable to perform the check
        continuousRunnable = object : Runnable {
            override fun run() {
                val currentTime = Timestamp.now()
                val formattedCurrentTime = dateFormat.format(Date(currentTime.seconds * 1000))
                val formattedTime = dateFormat.format(Date(time.seconds * 1000))

                binding.tvServerResult.text = formattedTime.toString()
                binding.tvResult.text = formattedCurrentTime.toString()




                /*val resourceId1 = resources.getIdentifier(videoNumber, "raw", packageName)
                val path = "android.resource://" + packageName + "/" + resourceId1
                binding.videView.setVideoURI(Uri.parse(path))
                binding.videView.start()*/

                val formattedTimeMillis = time.seconds * 1000
                val currentTimeMillis = currentTime.seconds * 1000
                val timeDifference = currentTimeMillis - formattedTimeMillis
                val threeMinutesMillis = 3 * 60 * 1000

                // Here, you can compare the 'time' parameter with the current time ('formattedCurrentTime')
                // and perform any necessary actions based on the comparison.
                // For example:


                if (timeDifference in 1 until threeMinutesMillis && isHelloToastShown) {


                    player?.pause()
                    binding.palyerView.visibility = View.GONE
                    binding.videView.visibility = View.VISIBLE
                    binding.videView.start()

                    val substrings = listOf(
                        first.substring(0, 1),first.substring(1, 2), first.substring(2, 3), first.substring(3, 4),
                        second.substring(0, 1),second.substring(1, 2), second.substring(2, 3), second.substring(3, 4),
                        third.substring(0, 1),third.substring(1, 2), third.substring(2, 3), third.substring(3, 4),
                        fourth.substring(0, 1),fourth.substring(1, 2), fourth.substring(2, 3), fourth.substring(3, 4)
                    )

                    val resourceId = resources.getIdentifier("intro", "raw", packageName)
                    val path = "android.resource://" + packageName + "/" + resourceId
                    binding.videView.setVideoURI(Uri.parse(path))
                    binding.videView.start()



                    binding.videView.setOnCompletionListener {

                        /*for (s in substrings) {
                            val videoNumber = "_"+s.toIntOrNull()?.toString() ?: "0"

                            val resourceId = resources.getIdentifier(videoNumber, "raw", packageName)
                            val path = "android.resource://" + packageName + "/" + resourceId
                            binding.videView.setVideoURI(Uri.parse(path))
                            binding.videView.start()



                            binding.videView.setOnCompletionListener {
                                //binding.videView.stopPlayback()


                                if (s == substrings.last()) {
                                    binding.palyerView.visibility = View.VISIBLE
                                    binding.videView.visibility = View.GONE
                                    player?.play()

                                    handler.removeCallbacks(this)
                                    isHelloToastShown = false
                                }
                            }
                        }*/


                        binding.layResult.visibility=View.VISIBLE

                        val scope = CoroutineScope(Dispatchers.Main)

                        scope.launch {
                            var counter:Int=1
                            for (s in substrings) {
                                val videoNumber = "_${s.toIntOrNull()?.toString() ?: "0"}"
                                val resourceId = resources.getIdentifier(videoNumber, "raw", packageName)
                                val path = "android.resource://" + packageName + "/" + resourceId

                                binding.videView.setVideoURI(Uri.parse(path))
                                binding.videView.start()

                                val completionDeferred = CompletableDeferred<Unit>()
                                binding.videView.setOnCompletionListener {



                                    if(counter==1){
                                        binding.r1.text= s.toString()
                                    }
                                    else if(counter==2) binding.r2.text= s.toString()
                                    else if(counter==3) binding.r3.text= s.toString()
                                    else if(counter==4) binding.r4.text= s.toString()
                                    else if(counter==5) binding.r5.text= s.toString()
                                    else if(counter==6) binding.r6.text= s.toString()
                                    else if(counter==7) binding.r7.text= s.toString()
                                    else if(counter==8) binding.r8.text= s.toString()
                                    else if(counter==9) binding.r9.text= s.toString()
                                    else if(counter==10) binding.r10.text= s.toString()
                                    else if(counter==11) binding.r11.text= s.toString()
                                    else if(counter==12) binding.r12.text= s.toString()
                                    else if(counter==13) binding.r13.text= s.toString()
                                    else if(counter==14) binding.r14.text= s.toString()
                                    else if(counter==15) binding.r15.text= s.toString()
                                    else if(counter==16) binding.r16.text= s.toString()

                                    binding.videView.stopPlayback()
                                    completionDeferred.complete(Unit)
                                    counter++


                                    //set by list length counter
                                    /*if (s == substrings.last()) {
                                        binding.palyerView.visibility = View.VISIBLE
                                        binding.videView.visibility = View.GONE
                                        player?.play()

                                        binding.layResult.visibility=View.GONE
                                        //handler.removeCallbacks(this@ActivityLiveStream)
                                        isHelloToastShown = false
                                    }*/

                                }

                                completionDeferred.await() // Pause the loop until video completes

                                // Continue with the next iteration
                            }

                            // All videos have been played
                            binding.palyerView.visibility = View.VISIBLE
                            binding.videView.visibility = View.GONE
                            player?.play()

                            //handler.removeCallbacks(this)
                            isHelloToastShown = false
                        }

                        binding.tvResult.text = formattedCurrentTime.toString()






                    }


                }


                /*if (formattedTime == formattedCurrentTime ) {
                    // Perform the action when the current time matches the desired time.
                    // For example, show a toast or update the UI.
                    Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show()
                    binding.tvResult.text = formattedCurrentTime.toString()
                }*/

                // Schedule the next check after a specific interval (e.g., 1 second)
                //handler.postDelayed(this, 1000) // 1000 milliseconds = 1 second



            }
        }

        // Start the initial check immediately
        continuousRunnable?.run()
    }


    private fun listenForSocialLinks() {
        val socialLinksRef = db.collection(constants.SOCIAL_LINKS_COLLECTION)
            .document("4o7GvF2Fyaf33gljZAqf")
        socialLinksListener = socialLinksRef.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Toast.makeText(mContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val ytLink = documentSnapshot.getString("yt")
                val whatsappLink = documentSnapshot.getString("whatsapp")
                if (!ytLink.isNullOrEmpty()) {
                    binding.youtube.setOnClickListener {
                        openLink(ytLink)
                    }
                }
                if (!whatsappLink.isNullOrEmpty()) {
                    binding.whatsapp.setOnClickListener {
                        openWhatsApp(whatsappLink)
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

    private fun openWhatsApp(phoneNumber:String) {
//        val phoneNumber = "+923036307725" // Replace with the phone number you want to chat with
        val message = "Hello, this is a custom message" // Replace with the message you want to send

        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

//    private fun getResults() {
//        db.collection("tempResult").document("Dg33Yix08jocNtRCPF2D").get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val documentSnapshot = task.result
//
//                    if (documentSnapshot != null && documentSnapshot.exists()) {
//                        // Retrieve the value of the numeric fields
//                        val firstNumber = documentSnapshot.getString("numberFirst")?.toString() ?: "0"
//                        val secondNumber = documentSnapshot.getString("numberSecond")?.toString() ?: "0"
//                        val thirdNumber = documentSnapshot.getString("numberThird")?.toString() ?: "0"
//                        val fourthNumber = documentSnapshot.getString("numberFourth")?.toString() ?: "0"
//
//                        // Log the values for debugging
//
//
//                        // Now you can use the value of the fields as strings
//                        first = firstNumber
//                        second = secondNumber
//                        third = thirdNumber
//                        fourth = fourthNumber
//
//                        // Display the values using Toast
//                        Toast.makeText(this, "First number: $firstNumber", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Second number: $secondNumber", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Third number: $thirdNumber", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Fourth number: $fourthNumber", Toast.LENGTH_SHORT).show()
//                    } else {
//                        // Document doesn't exist
//                        Toast.makeText(this, "Document doesn't exist", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    // Task was not successful
//                    Toast.makeText(this, "Task failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }




}










