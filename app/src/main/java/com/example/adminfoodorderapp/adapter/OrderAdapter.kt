package com.example.adminfoodorderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodorderapp.R
import com.example.adminfoodorderapp.model.CartItems

class OrderAdapter(private val mList:MutableList<CartItems>,private val requireContex:Context): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(requireContex).inflate(R.layout.item_sample,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var item:CartItems=mList[position]
        holder.foodName.text=item.foodName
        holder.quantity.text=item.foodQuantity.toString()
        holder.price.text=item.foodPrice
        Glide.with(requireContex).load(item.foodImage.toString()).into(holder.foodImage)

    }
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView) {

        val foodImage:ImageView=itemView.findViewById(R.id.foodimage)
        val foodName:TextView=itemView.findViewById(R.id.foodName)
        val price:TextView=itemView.findViewById(R.id.totalPrice)
        val quantity:TextView=itemView.findViewById(R.id.quan)

    }


}