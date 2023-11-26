package com.example.adminfoodorderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderapp.adapter.DeliveryAdapter
import com.example.adminfoodorderapp.databinding.ActivityOutForDeliveryBinding
import com.example.adminfoodorderapp.model.DeliveryModel
import com.example.adminfoodorderapp.model.OrderDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding:ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var  database:FirebaseDatabase
    private lateinit var reference:DatabaseReference
    private lateinit var auth:FirebaseAuth
    private var orderDetailList:ArrayList<OrderDetails> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
         auth=Firebase.auth
        database= FirebaseDatabase.getInstance()
        reference=database.reference.child("OrderDispatched")
        retriveData(this)
//
//
//
//       val data=ArrayList<DeliveryModel>()
//       data.add(DeliveryModel("Ashok Thombre","received","received"))
//       data.add(DeliveryModel("Amol Bundhe","received","not received"))
//       data.add(DeliveryModel("Sunil Pawar","not received","not received"))
//       data.add(DeliveryModel("Vishal Gaikwad","not received","received"))
//       data.add(DeliveryModel("Akash Mhoprekar","received","received"))
        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    private fun retriveData(contex: OutForDeliveryActivity) {
        reference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children)
                {
                    val order:OrderDetails= dataSnapshot.getValue(OrderDetails::class.java)!!
                    orderDetailList.add(order)

                }
                var adapter=DeliveryAdapter(orderDetailList,contex,database)
                binding.deliveryRecyclerView.layoutManager=LinearLayoutManager(contex)
                binding.deliveryRecyclerView.adapter=adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OutForDeliveryActivity,"Something went wrong..",Toast.LENGTH_SHORT).show()
                Log.d("Error",error.message)
            }

        })
    }


}