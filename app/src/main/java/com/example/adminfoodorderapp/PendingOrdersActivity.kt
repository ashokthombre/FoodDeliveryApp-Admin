package com.example.adminfoodorderapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderapp.adapter.PendingOrderAdapter
import com.example.adminfoodorderapp.databinding.ActivityPendingOrdersBinding
import com.example.adminfoodorderapp.model.CartItems
import com.example.adminfoodorderapp.model.OrderDetails
import com.example.adminfoodorderapp.model.OrderDetailsExtra
import com.example.adminfoodorderapp.model.PendinfOrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrdersActivity : AppCompatActivity() {
    private val binding:ActivityPendingOrdersBinding by lazy {
        ActivityPendingOrdersBinding.inflate(layoutInflater)
    }
    private var listOfName:MutableList<String> = mutableListOf()
    private var listOfTotalPrice:MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItem:MutableList<OrderDetails> = mutableListOf()
    private var listOfOrderItemExtra:MutableList<OrderDetailsExtra> = mutableListOf()
    private lateinit var database:FirebaseDatabase
    private lateinit var databaseOredrDetails:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        database= FirebaseDatabase.getInstance()
        databaseOredrDetails=database.reference.child("OrderDetails")

        getOrderDetails(this)

//        val data=ArrayList<PendinfOrderModel>()
//        data.add(PendinfOrderModel("Ashok Thombre","2",R.drawable.menu1))
//        data.add(PendinfOrderModel("Amol Bundhe","3",R.drawable.menu2))
//        data.add(PendinfOrderModel("Vishal Gaikwad","4",R.drawable.menu3))
//        data.add(PendinfOrderModel("Sunil Pawar","1",R.drawable.menu4))
//        data.add(PendinfOrderModel("Akash Mhoprekar","5",R.drawable.menu5))
//        data.add(PendinfOrderModel("Sandip Mhoprekar","6",R.drawable.menu1))


        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    private fun getOrderDetails(requireContwxt:Context) {
        databaseOredrDetails.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderDetailsSnapshot in snapshot.children)
                {
                    val order:OrderDetails= orderDetailsSnapshot.getValue(OrderDetails::class.java)!!
                    listOfOrderItem.add(order)

                    for (items in order.itemList!!)
                    {
                        val extra=OrderDetailsExtra(order.userId,order.userName,order.address,order.totalPrice,order.phoneNumber,order.orderAccepted,
                            order.paymentReceived,order.itemPushKey,order.currentTime,items.foodDescription,items.foodImage,items.foodName,items.foodPrice,items.foodQuantity.toString())
                        listOfOrderItemExtra.add(extra)
                    }

                }
                Log.d("Extra",listOfOrderItemExtra.size.toString())


                var itemLlist:MutableList<CartItems> = mutableListOf()

                for (items in listOfOrderItem)
                {
                    for (i in items.itemList!!)
                    {
                        itemLlist.add(i)

                    }

                }
                Log.d("orderItems",listOfOrderItem.size.toString())
                Log.d("Size",itemLlist.size.toString())
                val adapter=PendingOrderAdapter(itemLlist,listOfOrderItem,requireContwxt)
                binding.pendingOrderRecyclerView.layoutManager=LinearLayoutManager(requireContwxt)
                binding.pendingOrderRecyclerView.adapter=adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDatatoRecyclerView() {

       var itemLlist:MutableList<CartItems> = mutableListOf()

        for (items in listOfOrderItem)
        {
            for (i in items.itemList!!)
            {
                itemLlist.add(i)

            }
        }
//        val adapter=PendingOrderAdapter(listOfOrderItem,this)
//        binding.pendingOrderRecyclerView.layoutManager=LinearLayoutManager(this)
//        binding.pendingOrderRecyclerView.adapter=adapter
    }
}