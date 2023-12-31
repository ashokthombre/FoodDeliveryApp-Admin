package com.example.adminfoodorderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderapp.adapter.MenuItemAdapter
import com.example.adminfoodorderapp.databinding.ActivityAllItemBinding
import com.example.adminfoodorderapp.model.AllMenu
import com.example.adminfoodorderapp.model.MenuItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private var menuItems:ArrayList<AllMenu> = ArrayList()

    private val binding:ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().reference
        database= FirebaseDatabase.getInstance()
        retriveMenuItem()

        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    private fun retriveMenuItem() {
       val foodRef:DatabaseReference=database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
            menuItems.clear()
                for (foodSnapshot in snapshot.children)
                {
                    val menuItem:Any?=foodSnapshot.getValue(AllMenu::class.java)
                   menuItem?.let {
                       menuItems.add(it as AllMenu)
                   }
                }
                     setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
               Log.d("onCanceled",error.toString())
            }
        })

    }

    private fun setAdapter() {

        val adapter=MenuItemAdapter(menuItems,this,databaseReference)
        binding.menuRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.menuRecyclerView.adapter=adapter
    }
}