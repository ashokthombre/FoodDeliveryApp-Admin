package com.example.adminfoodorderapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodorderapp.OrderDetailActivity
import com.example.adminfoodorderapp.R
import com.example.adminfoodorderapp.model.CartItems
import com.example.adminfoodorderapp.model.OrderDetails
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class PendingOrderAdapter(private var itemList:MutableList<CartItems>,private var mList:MutableList<OrderDetails>,private val requireContex:Context):RecyclerView.Adapter<PendingOrderAdapter.ViewHolder>() {
    private var items:MutableList<CartItems> = itemList as MutableList<CartItems>
     private val listData:MutableList<OrderDetails> = mList as MutableList<OrderDetails>
   private val database:FirebaseDatabase= FirebaseDatabase.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pending_order_list, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

                var order:OrderDetails=listData[position]
                holder.custmorname.text=order.userName
                holder.quantity.text="₹"+order.totalPrice
               if (order.orderAccepted)
               {
                   holder.accept.text="Dispatch"
               }
                else
               {
                   holder.accept.text="Accept"
               }

              for (item in order.itemList!!)
              {
                     items.add(item)

                     Glide.with(requireContex).load(item.foodImage.toString()).into(holder.foodImage)
                 }

             holder.itemView.setOnClickListener {

                 var intent=Intent(requireContex,OrderDetailActivity::class.java)
                 intent.putExtra("order",order as Serializable)
                 requireContex.startActivity(intent)
        }



        holder.accept.setOnClickListener {
             if (holder.accept.text.equals("Accept"))
             {

                 val data:HashMap<String,Boolean> =
                     hashMapOf("orderAccepted" to true)
                  val orderRef:DatabaseReference=database.reference.child("OrderDetails").child(
                      order.itemPushKey!!
                  )
                  orderRef.updateChildren(data as Map<String,Boolean>).addOnSuccessListener {
                      Toast.makeText(requireContex,"Order Accepted",Toast.LENGTH_SHORT).show()
                  }
                      .addOnFailureListener {
                          Toast.makeText(requireContex,"Something went wrong ",Toast.LENGTH_SHORT).show()
                          Log.d("Error",it.message.toString())
                      }
                 val buyHistoryRef:DatabaseReference=database.reference.child("user").child(order.userId!!).child("BuyHistory").child(
                     order.itemPushKey!!
                 )
                 buyHistoryRef.updateChildren(data as Map<String,Boolean>).addOnSuccessListener {

                 }
                   order.orderAccepted=true
                 database.reference.child("AcceptedOrder").child(order.itemPushKey!!).setValue(order)
                 holder.accept.text="Dispatch"

                 return@setOnClickListener
             }
            if (holder.accept.text.equals("Dispatch"))
            {
                holder.accept.text="Accept"
                order.orderAccepted=true
                database.reference.child("OrderDispatched").child(order.itemPushKey!!).setValue(order)
                removeFromOrder(position,order)
                return@setOnClickListener
            }
        }


    }

    private fun removeFromOrder(position: Int, order: OrderDetails) {

        database.reference.child("OrderDetails").child(order.itemPushKey!!).removeValue()
        listData.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(ItemView:View):RecyclerView.ViewHolder(ItemView) {

        val custmorname:TextView=itemView.findViewById(R.id.foodName)
        val quantity:TextView=itemView.findViewById(R.id.quantity_text)
        val accept:AppCompatButton=itemView.findViewById(R.id.acceptBtn)
        val foodImage:ImageView=itemView.findViewById(R.id.foodimage)


    }
}