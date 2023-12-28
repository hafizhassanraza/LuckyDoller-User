package com.enfotrix.luckydoller.UI

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.Models.ModelResult
import com.enfotrix.luckydoller.Models.ModelUser
import com.enfotrix.luckydoller.R
import com.enfotrix.luckydoller.SharedPrefManager
import com.enfotrix.luckydoller.Utils
import com.enfotrix.luckydoller.databinding.ActivityResultBinding
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ActivityResult : AppCompatActivity() {


    var gameCTG = ArrayList<String>()
    var gameFirstSubCTG = ArrayList<String>()
    var gameSecondSubCTG = ArrayList<String>()



    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityResultBinding
    private lateinit var modelUser: ModelUser
    private lateinit var modelResult: ModelResult
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager

    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityResult
        supportActionBar?.hide()
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)

        setData()

        binding.btnGetResult.setOnClickListener { getResult() }

    }

    private fun getResult() {

        //var data=binding.spGameSubCtg.selectedItem.toString()

        val day: Int = binding.dpResult.getDayOfMonth()
        val month: Int = binding.dpResult.getMonth() + 1
        val year: Int = binding.dpResult.getYear()
        val date = String.format("%02d/%02d/%04d", day, month, year)
        val formattedDate = getFormattedDate(date) // Jul 21, 2023




        showResults(date)





        //Toast.makeText(mContext, formattedDate, Toast.LENGTH_SHORT).show()

    }

    fun getFormattedDate(date: String): String {
        // Assuming date has the format "dd/MM/yyyy"
        val dateFormatInput = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateObj = dateFormatInput.parse(date)

        val dateFormatOutput = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
        return dateFormatOutput.format(dateObj)
    }


    private fun setData() {

        gameCTG.add("فرسٹ")
        gameCTG.add("سیکنڈ")

        gameFirstSubCTG.add("حرف")
        gameFirstSubCTG.add("آکرہ")
        gameFirstSubCTG.add("ٹنڈولا")
        gameFirstSubCTG.add("پنگہورا")

        gameSecondSubCTG.add("آکرہ")
        gameSecondSubCTG.add("ٹنڈولا")
        gameSecondSubCTG.add("پنگہورا")

        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.item_spinner_gamectg,gameCTG)
        binding.spGameCtg.adapter= adapterGameCTG

        binding.spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position == 0) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(
                        applicationContext,
                        R.layout.item_spinner_gamectg,
                        gameFirstSubCTG
                    )
                    binding.spGameSubCtg.adapter = adapterGameSubCTG
                } else if (position == 1) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(
                        applicationContext,
                        R.layout.item_spinner_gamectg,
                        gameSecondSubCTG
                    )
                    binding.spGameSubCtg.adapter = adapterGameSubCTG
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }


        })
    }

    fun convertTimestampToDate(timestamp: Long): String {
        // Create a Date object from the timestamp
        val date = Date(timestamp)

        // Format the date as "dd/MM/yy"
        val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return sdf.format(date)
    }

    private fun showSelectionDialog() {


        var dialog = Dialog (mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_result_selection)

        val spGameCtg = dialog.findViewById<Spinner>(R.id.spGameCtg)
        val spGameSubCtg = dialog.findViewById<Spinner>(R.id.spGameSubCtg)
        val btnResults = dialog.findViewById<Button>(R.id.btnResults)
        val cvDate = dialog.findViewById<CalendarView>(R.id.cvDate)

        val adapterGameCTG: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item,gameCTG)
        spGameCtg.adapter= adapterGameCTG

        /*spGameCtg.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position == 0) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(
                        applicationContext,
                        android.R.layout.simple_spinner_item,
                        gameFirstSubCTG
                    )
                    spGameSubCtg.adapter = adapterGameSubCTG
                } else if (position == 1) {
                    val adapterGameSubCTG: ArrayAdapter<String> = ArrayAdapter<String>(
                        applicationContext,
                        android.R.layout.simple_spinner_item,
                        gameSecondSubCTG
                    )
                    spGameSubCtg.adapter = adapterGameSubCTG
                }

            }



        })*/


        btnResults.setOnClickListener {
            dialog.dismiss()

            /////////////////////////// To Set Text In Text View////////////////////////////////

            /*binding.tvGameCtg.setText(spGameCtg.selectedItem.toString())
            binding.tvGameSubCtg.setText("("+spGameSubCtg.selectedItem.toString()+")")
            binding.tvDate.setText(convertTimestampToDate(cvDate.date))


            showResults(
                spGameCtg.selectedItem.toString(),
                spGameSubCtg.selectedItem.toString(),
                convertTimestampToDate(cvDate.date)
            )*/
        }

        dialog.show()


    }

    fun showResults( date: String) {


        binding.tvdateTimeResult.text="No results"
        binding.tvFirstNumber.text=""
        binding.tvSecondNumber.text=""
        binding.tvThirdNumber.text=""
        binding.tvFourthNumber.text=""

        binding.tvdateTimeResult2.text="No results"
        binding.tvEFirstNumber.text=""
        binding.tvESecondNumber.text=""
        binding.tvEThirdNumber.text=""
        binding.tvEFourthNumber.text=""

        utils.startLoadingAnimation()
        db.collection(constants.RESULT_COLLECTION)
            //.whereEqualTo(constants.BIDS_GAMECTG,gameCtg)
            .get()
            .addOnCompleteListener{
                utils.endLoadingAnimation()
                if(it.isSuccessful){
                    //Toast.makeText(mContext, it.result.size().toString(), Toast.LENGTH_SHORT).show()
                    val list = ArrayList<ModelResult>()
                    for(document in it.result){

                        var result= document.toObject(ModelResult::class.java)
                        var date_=SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(result.createdAt!!.toDate()).toString()

                        if(date_.equals(date)){

                            if(result.result.equals("1")){
                                binding.tvdateTimeResult.text="("+SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault()).format(result.createdAt!!.toDate()).toString()+")"

                                binding.tvFirstNumber.text=result.numberFirst
                                binding.tvSecondNumber.text=result.numberSecond
                                binding.tvThirdNumber.text=result.numberThird
                                binding.tvFourthNumber.text=result.numberFourth
                            }
                            else if(result.result.equals("2")){
                                binding.tvdateTimeResult2.text="("+SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault()).format(result.createdAt!!.toDate()).toString()+")"

                                binding.tvEFirstNumber.text=result.numberFirst
                                binding.tvESecondNumber.text=result.numberSecond
                                binding.tvEThirdNumber.text=result.numberThird
                                binding.tvEFourthNumber.text=result.numberFourth
                            }
                        }

                        /*if(document.toObject(ModelBid::class.java).equals("Morning")){
                        }
                        //list.add( document.toObject(ModelBid::class.java))
                        else if(document.toObject(ModelBid::class.java).status.equals("Evening")){

                        }*/
                    }


                }
            }
    }


}

