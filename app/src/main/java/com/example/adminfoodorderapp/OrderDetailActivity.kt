package com.example.adminfoodorderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderapp.adapter.OrderAdapter
import com.example.adminfoodorderapp.databinding.ActivityOrderDetailBinding
import com.example.adminfoodorderapp.model.CartItems
import com.example.adminfoodorderapp.model.OrderDetails

class OrderDetailActivity : AppCompatActivity() {
    private  val binding:ActivityOrderDetailBinding by lazy {
        ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val order: OrderDetails = intent.extras!!.get("order") as OrderDetails
       binding.phoneNumber.setText(order.phoneNumber)
        binding.userName.setText(order.userName)
        binding.userAddress.setText(order.address)
        binding.totalPrice.setText(order.totalPrice)
        binding.back.setOnClickListener {
            finish()
        }

      var cartList:MutableList<CartItems> = mutableListOf()

        for (list in order.itemList!!)
        {
            cartList.add(list)
        }



        val adapter=OrderAdapter(cartList,this)
        binding.itemRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.itemRecyclerView.adapter=adapter


    }
}