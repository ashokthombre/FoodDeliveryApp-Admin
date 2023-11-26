package com.example.adminfoodorderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.adminfoodorderapp.databinding.ActivityMainBinding
import com.example.adminfoodorderapp.model.OrderDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database:FirebaseDatabase
    private var pendingOredrList:ArrayList<OrderDetails> = ArrayList()
    private var completedOredrList:ArrayList<OrderDetails> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeData()
    }

    private fun initializeData() {
        auth=Firebase.auth
        database= FirebaseDatabase.getInstance()

        setPendingOrderCount()
        setCompletedOrderCount()




        binding.additemimage.setOnClickListener {
            var intent=Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allitem.setOnClickListener {
            var intent=Intent(this,AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.orderDispatch.setOnClickListener {
            var intent=Intent(this,OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            var intent=Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOredr.setOnClickListener {
            var intent=Intent(this,PendingOrdersActivity::class.java)
            startActivity(intent)
        }
        binding.createNewUser.setOnClickListener {
            var intent=Intent(this,CrateUserActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun setCompletedOrderCount() {
        val completedOrderRef:DatabaseReference=database.reference.child("OrderDispatched")
        completedOrderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var count:Int=0
                var sum:Double=0.0
                for (dataSnapshot in snapshot.children)
                {
                    var order:OrderDetails= dataSnapshot.getValue(OrderDetails::class.java)!!
                    sum += order.totalPrice!!.toDouble()
                    completedOredrList.add(order)
                    count++
                }
                binding.completedOrderCount.text=count.toString()
                binding.totalEarning.text="₹ "+sum.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setPendingOrderCount() {
        val pendingOrderRef:DatabaseReference=database.reference.child("OrderDetails")
        pendingOrderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var count:Int=0
                for (dataSnapshot in snapshot.children)
                {
                    var order:OrderDetails= snapshot.getValue(OrderDetails::class.java)!!
                    pendingOredrList.add(order)
                    count++
                }
                binding.pendingOrderCount.text=count.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onResume() {
        super.onResume()
        initializeData()
    }
}