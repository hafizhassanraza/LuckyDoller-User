package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enfotrix.luckydoller.Adapter.BidAdapter
import com.enfotrix.luckydoller.Adapter.BidViewPagerAdapter
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityBidBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ActivityBid : AppCompatActivity() , BidAdapter.OnItemClickListener{


    var gameCTG = ArrayList<String>()
    var gameFirstSubCTG = ArrayList<String>()
    var gameSecondSubCTG = ArrayList<String>()


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityBidBinding
    private lateinit var modelUser: ModelUser
    private lateinit var modelBid: ModelBid
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager


    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidBinding.inflate(layoutInflater)
        setContentView(binding.root)              //initialization of the bojects of the other classes
        mContext=this@ActivityBid
        supportActionBar?.hide()
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)


        setupViewPager()
        setupTabLayout()

        gameCTG.add("فرسٹ")
        gameCTG.add("سیکنڈ")

        gameFirstSubCTG.add("حرف")
        gameFirstSubCTG.add("آکرہ")
        gameFirstSubCTG.add("ٹنڈولا")
        gameFirstSubCTG.add("پنگہورا")
        gameSecondSubCTG.add("آکرہ")
        gameSecondSubCTG.add("ٹنڈولا")
        gameSecondSubCTG.add("پنگہورا")
    }






    private fun setupTabLayout() {
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab,
            position ->
            if(position==0) tab.text ="Pending"
            else if(position==1) tab.text="Active"
            else if(position==2) tab.text="Close"
        }.attach()
    }

    private fun setupViewPager() {
        val adapter = BidViewPagerAdapter(this, 3)
        binding.viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        val viewPager = binding.viewPager
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 2
        }
    }





    private fun showBidDialog() {



        var dialog = Dialog (mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_bid)

        val spGameCtg = dialog.findViewById<Spinner>(R.id.spGameCtg)
        val spGameSubCtg = dialog.findViewById<Spinner>(R.id.spGameSubCtg)
        val etBidNumber = dialog.findViewById<EditText>(R.id.etBidNumber)
        val etBidAmount = dialog.findViewById<EditText>(R.id.etBidAmount)
        val btnBid = dialog.findViewById<Button>(R.id.btnBid)



        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameCTG)
        spGameCtg.adapter= adapterGameCTG


        spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position==0) {
                    etBidNumber.setText("")
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameFirstSubCTG)
                    spGameSubCtg.adapter= adapterGameSubCTG

                    //////////////////////// VALIDATION CODE FOR BID NUMBER ////////////////////////////

                    spGameSubCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                            if(position==0) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(1))
                            }

                            else if(position==1) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(2))
                            }
                            else if(position==2) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(3))
                            }
                            else if(position==3) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(4))
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    })

                }
                else if(position==1) {
                    etBidNumber.setText("")
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg, gameSecondSubCTG)
                    spGameSubCtg.adapter= adapterGameSubCTG

                    //////////////////////// VALIDATION CODE FOR BID NUMBER ////////////////////////////

                    spGameSubCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            if(position==0) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(2))
                            }
                            else if(position==1) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(3))
                            }
                            else if(position==2) {
                                etBidNumber.setText("")
                                etBidNumber.filters = arrayOf(InputFilter.LengthFilter(4))
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

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        btnBid.setOnClickListener {
            dialog.dismiss()


            /*modelBid=ModelBid(
                sharedPrefManager.getToken(),
                spGameCtg.selectedItem.toString(),
                spGameSubCtg.selectedItem.toString(),
                etBidNumber.text.toString(),
                etBidAmount.text.toString(),
                "Active")
                saveBid(modelBid)*/

        }

        dialog.show()




    }

    private fun saveBid(modelBid: ModelBid) {

        utils.startLoadingAnimation()

        db.collection(constants.ADMIN_COLLECTION).get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    var bidStatus:String=""
                    for(admin in it.result){

                        bidStatus= admin.getString("bidStatus").toString()

                    }

                    if(bidStatus=="Active"){

                        db.collection(constants.BIDS_COLLECTION).add(modelBid)
                            .addOnCompleteListener{
                                utils.endLoadingAnimation()
                                if(it.isSuccessful){
                                    Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show()
                                    ////////////////// Green Colour Validation Code For Status //////////////////////
//                                    val tvGameStatus = findViewById<TextView>(R.id.tvGameStatus)
//                                    tvGameStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green))
                                }
                            }
                    }
                    else{
                        Toast.makeText(mContext, "Bidding has been closed by admin ", Toast.LENGTH_SHORT).show()

                        ////////////////// Red Colour Validation Code For Status //////////////////////
//                        val tvGameStatus = findViewById<TextView>(R.id.tvGameStatus)
//                        tvGameStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
                    }


                }
            }

    }

    override fun onItemClick(ModelBid: ModelBid) {

    }

    override fun onDeleteClick(ModelBid: ModelBid) {
    }
}