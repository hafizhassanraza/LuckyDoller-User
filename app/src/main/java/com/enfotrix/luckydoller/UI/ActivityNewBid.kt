package com.enfotrix.luckydoller.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.enfotrix.luckydoller.Adapter.BidAdapter
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.Admin
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelResult
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityBidBinding
import com.enfotrix.luckydoller.databinding.ActivityNewBidBinding
import com.enfotrix.luckydoller.databinding.ActivityResultBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.atomic.AtomicInteger

class ActivityNewBid : AppCompatActivity() {


    var gameCTG = ArrayList<String>()
    var gameFirstSubCTG = ArrayList<String>()
    var gameSecondSubCTG = ArrayList<String>()

    private lateinit var modelBid: ModelBid


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityNewBidBinding
    private lateinit var modelUser: ModelUser
    private lateinit var modelResult: ModelResult
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    var bidNumberList = ArrayList<String>()
    var bids = ArrayList<ModelBid>()
    private var db= Firebase.firestore

    var numberCounter:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityNewBid
        supportActionBar?.hide()
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        gameCTG.add("فرسٹ")
        gameCTG.add("سیکنڈ")

        gameFirstSubCTG.add("حرف")
        gameFirstSubCTG.add("آکرہ")
        gameFirstSubCTG.add("ٹنڈولا")
        gameFirstSubCTG.add("پنگہورا")

        gameSecondSubCTG.add("آکرہ")
        gameSecondSubCTG.add("ٹنڈولا")
        gameSecondSubCTG.add("پنگہورا")

        binding.rvBids.layoutManager = LinearLayoutManager(mContext)


        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameCTG)
        binding.spGameCtg.adapter= adapterGameCTG

        var amount=binding.ammount.text.toString()
        var Ammount=amount.substring(0,1)
        binding.btnBid.setOnClickListener {
            if (TextUtils.isEmpty(binding.etBidNumber.text.toString())) {
                binding.etBidNumber.setError("Enter Bid Number")
            }
            else if (TextUtils.isEmpty(binding.etBidTransactionID.text.toString())) {
                binding.etBidTransactionID.setError("Enter ID")
            }
            else if (TextUtils.isEmpty(binding.etBidAmount.text.toString())) {
                binding.etBidAmount.setError("Enter Amount")
            }
            else {
                val bidAmount = binding.etBidAmount.text.toString().toDouble()

                if (bidAmount == 0.0) {
                    binding.etBidAmount.setError("Bid Amount cannot be zero")
                }
                else if(Ammount=="0")
                {

                }
                else if (bidAmount % 5 != 0.0) {
                    binding.etBidAmount.setError("Bid Amount must be a multiple of 5")
                }
                else if (binding.etBidTransactionID.length()<4)
                {
                    binding.etBidTransactionID.setError("Enter correct transaction Id")
                }
                else if (Ammount=="0")
                {
                    binding.ammount.setError("Ammount should not start from 0")
                }

                else {
                    if(numberCounter==binding.etBidNumber.text.length) {
                        addBid()
                        binding.etBidNumber.text.clear()

                    }

                    else Toast.makeText(mContext, "Incorrect number", Toast.LENGTH_SHORT).show()

                }
            }

        }


        binding.btnSaveAll.setOnClickListener {
            if(bids.size>0){
                saveBid()
            }
            else{
                Toast.makeText(mContext, "Please add at least one bid!", Toast.LENGTH_SHORT).show()
            }
        }


        binding.spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position==0) {
                    binding.etBidNumber.setText("")
                    binding. etBidAmount.filters = arrayOf(InputFilter.LengthFilter(6))
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameFirstSubCTG)
                    binding.spGameSubCtg.adapter= adapterGameSubCTG

                    //////////////////////// VALIDATION CODE FOR BID NUMBER ////////////////////////////

                    binding.spGameSubCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                            if(position==0) {
                                binding.etBidNumber.setText("")
                                binding. etBidNumber.filters = arrayOf(InputFilter.LengthFilter(1))
                                numberCounter=1
                            }
                            else if(position==1) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(2))
                                numberCounter=2

                            }
                            else if(position==2) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(3))
                                numberCounter=3

                            }
                            else if(position==3) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(4))
                                numberCounter=4

                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    })

                }
                else if(position==1) {
                    binding.etBidNumber.setText("")
                    binding. etBidAmount.filters = arrayOf(InputFilter.LengthFilter(6))
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameSecondSubCTG)
                    binding.spGameSubCtg.adapter= adapterGameSubCTG

                    //////////////////////// VALIDATION CODE FOR BID NUMBER ////////////////////////////

                    binding.spGameSubCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            if(position==0) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(2))
                                numberCounter=2

                            }
                            else if(position==1) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(3))
                                numberCounter=3

                            }
                            else if(position==2) {
                                binding.etBidNumber.setText("")
                                binding.etBidNumber.filters = arrayOf(InputFilter.LengthFilter(4))
                                numberCounter=4

                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    })

                }



                /*//first,  we have to retrieve the item position as a string
                // then, we can change string value into integer
                val item_position = position.toString()
                val positonInt = Integer.valueOf(item_position)
                Toast.makeText(this@ActivityBid, "value is $positonInt", Toast.LENGTH_SHORT).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })


    }

    private fun addBid() {

        val token= sharedPrefManager.getToken()

        bids.add( ModelBid(

            token,
            binding.spGameCtg.selectedItem.toString(),
            binding.spGameSubCtg.selectedItem.toString(),
            binding.etBidNumber.text.toString(),

            binding.etBidAmount.text.toString(),
            "Active",
            binding.etBidTransactionID.text.toString(),
            "Pending"
            )
        )
        bids.sortByDescending { it.createdAt }
        binding.rvBids.adapter= BidAdapter( bids)

    }

    private fun setBidNumber() {


        /*bidNumberList.add(binding.etBidNumber.text.toString())

        val listOfNumbers = bidNumberList.joinToString(", ")
        binding.tvBidNumbers.text = listOfNumbers

        binding.etBidNumber.text.clear()*/

/*        bidNumberList.add(binding.etBidNumber.text.toString())
        for (number in bidNumberList.indices) {

            listOfNumbers +number.toString()+", "

        }
        binding.tvBidNumbers.text =listOfNumbers*/

    }

    private fun setBid() {

        /*val numberList = arrayListOf("1", "2", "3")
        modelBid = ModelBid(
            sharedPrefManager.getToken(),
            binding.spGameCtg.selectedItem.toString(),
            binding.spGameSubCtg.selectedItem.toString(),
            numberList,

            binding.etBidAmount.text.toString(),
            "Active",
            binding.etBidTransactionID.text.toString()
        )
        saveBid(modelBid)*/
    }


    private fun saveBid() {


        utils.startLoadingAnimation()
        db.collection(constants.ADMIN_COLLECTION).document("Dg33Yix08jocNtRCPF2D").get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val admin = it.result.toObject<Admin>()
                    if(admin?.bidStatus.equals("Active")) saveDataToFirestore(bids)
                    else {
                        utils.endLoadingAnimation()
                        Toast.makeText(mContext, "Bidding has been closed by admin ", Toast.LENGTH_SHORT).show()
                    }



                }
            }

    }





    fun saveDataToFirestore(bids: List<ModelBid>) {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)

        val completedCount = AtomicInteger(0)

        scope.launch {
            // Use async to concurrently save all bids
            val tasks = bids.map { bid ->
                async {
                    try {
                        db.collection(constants.BIDS_COLLECTION).add(bid).await()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle the case when data saving failed (optional)
                    }
                }
            }

            // Await all tasks to complete
            tasks.awaitAll()

            // All data is saved, continue with the next steps
            continueExecution()
        }

        // Cancel the coroutine job if needed (e.g., if the activity is destroyed)
        /*scope.invokeOnCompletion {
            if (it is CancellationException) {
                // Coroutine job was canceled
            }
        }*/
    }

    fun continueExecution() {
        // The loop has completed, continue with your next steps here

        // t1
        utils.endLoadingAnimation()
        runOnUiThread {
            Toast.makeText(this@ActivityNewBid, "Saved", Toast.LENGTH_SHORT).show()
        }
        startActivity(Intent(mContext, MainActivity::class.java))
        finish()
    }






}