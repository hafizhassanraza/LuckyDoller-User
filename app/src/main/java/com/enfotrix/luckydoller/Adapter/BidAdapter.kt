package com.enfotrix.luckydoller.Adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.enfotrix.luckydoller.Constants
import com.enfotrix.luckydoller.Models.ModelBid
import com.enfotrix.luckydoller.databinding.ItemBidBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BidAdapter( val data: List<ModelBid>) : RecyclerView.Adapter<BidAdapter.ViewHolder>() {


    var constant= Constants()

    interface OnItemClickListener {
        fun onItemClick(ModelBid: ModelBid)
        fun onDeleteClick(ModelBid: ModelBid)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBidBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(data[position]) }
    override fun getItemCount(): Int { return data.size }
    inner class ViewHolder(val itemBinding: ItemBidBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(modelBid: ModelBid) {

            itemBinding.tvGameBidAmount.text=modelBid.amount
            itemBinding.tvGameBidNumber.text=modelBid.number
            itemBinding.tvGameCtg.text=modelBid.gameCtg
            itemBinding.tvGameDate.text= modelBid.createdAt
            itemBinding.tvGameResult.text=modelBid.result
            itemBinding.tvGameSubCtg.text=modelBid.gameSubCtg


            /*if(!activity.equals(constant.FROM_INVESTOR_ACCOUNTS)) itemBinding.imgDelete.setVisibility(View.GONE)
            itemBinding.tvBankName.text=ModelBid.bank_name
            itemBinding.tvAccountNumber.text=ModelBid.account_number
            itemBinding.tvAccountTittle.text=ModelBid.account_tittle
            itemBinding.layItem.setOnClickListener{ listener.onItemClick(ModelBid)}
            itemBinding.imgDelete.setOnClickListener{ listener.onDeleteClick(ModelBid)}*/

        }

    }



}