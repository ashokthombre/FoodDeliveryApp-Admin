package com.example.adminfoodorderapp.adapter

import android.content.Context
import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodorderapp.R

import com.example.adminfoodorderapp.model.OrderDetails
import com.google.firebase.database.FirebaseDatabase


class DeliveryAdapter(private val mList:List<OrderDetails>,private val requireContex:Context,private val databse:FirebaseDatabase):RecyclerView.Adapter<DeliveryAdapter.ViewHolder>() {

    private val listData:MutableList<OrderDetails> = mList as MutableList<OrderDetails>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.delivery_item, parent, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val delivery:OrderDetails=listData[position]

        holder.customerName.text=delivery.userName

        if (delivery.paymentReceived)
        {
            holder.acceptPayment.visibility=View.INVISIBLE
            holder.moneyStatus.text="Received"
           holder.moneyStatus.setTextColor(Color.parseColor("#1fc57A"))
        }
        else
        {
            holder.acceptPayment.visibility=View.VISIBLE
            holder.moneyStatus.text="Not Received"
           holder.moneyStatus.setTextColor(Color.parseColor("#FFF80202"))
        }
        if (delivery.orderAccepted)
        {
            holder.deliveryText.text="Received"
          holder.deliveryText.setTextColor(Color.parseColor("#1fc57A"))
          holder.status.setBackgroundColor(Color.parseColor("#1fc57A"))
        }
        else
        {
            holder.deliveryText.text="Not Received"
            holder.deliveryText.setTextColor(Color.parseColor("#FFF80202"))
            holder.status.setBackgroundColor(Color.parseColor("#FFF80202"))
        }

        holder.acceptPayment.setOnClickListener {

            val data:HashMap<String,Boolean> = hashMapOf("paymentReceived" to true)
            val dispatchRef=databse.reference.child("OrderDispatched").child(delivery.itemPushKey!!)
            delivery.paymentReceived=true
            dispatchRef.updateChildren(data as Map<String,Boolean>).addOnFailureListener {

            }
            val acceptdRef =databse.reference.child("AcceptedOrder").child(delivery.itemPushKey!!)
            acceptdRef.updateChildren(data as Map<String,Boolean>).addOnSuccessListener {

            }
            val buyHistoryRef=databse.reference.child("user").child(delivery.userId!!).child("BuyHistory").child(
                delivery.itemPushKey!!
            )
            buyHistoryRef.updateChildren(data as Map<String,Boolean>).addOnSuccessListener {

            }
            holder.moneyStatus.text="Received"
            holder.moneyStatus.setTextColor(Color.parseColor("#1fc57A"))
            holder.acceptPayment.visibility=View.INVISIBLE
            Toast.makeText(requireContex,"Payment Received",Toast.LENGTH_SHORT).show()
        }




    }

    override fun getItemCount(): Int {
        return listData.size
    }


    class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView) {

        val customerName:TextView=itemView.findViewById(R.id.customerName)
        val deliveryStatus:CardView=itemView.findViewById(R.id.deliveryStatus)
        val moneyStatus:TextView=itemView.findViewById(R.id.moneyStatus)
        val deliveryText:TextView=itemView.findViewById(R.id.deliveryText)
        val status:View=itemView.findViewById(R.id.status)
        val acceptPayment:TextView=itemView.findViewById(R.id.acceptPayment)


    }
}