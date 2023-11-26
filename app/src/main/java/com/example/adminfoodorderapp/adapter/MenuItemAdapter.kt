package com.example.adminfoodorderapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodorderapp.R
import com.example.adminfoodorderapp.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(private var mList: List<AllMenu>,private val contex:Context,private var databaseReference: DatabaseReference) :
    RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {
    val itemQuantities = IntArray(mList.size) { 1 }
    private var listData: MutableList<AllMenu> = mList as MutableList<AllMenu>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem: AllMenu = listData[position]
        val uriString:String?=menuItem.foodImage
        val uri:Uri= Uri.parse(uriString)

        holder.foodname.text=menuItem.foodName
        holder.foodprice.text=menuItem.foodPrice
        Glide.with(contex).load(uri).into(holder.foodimage)

        holder.delete.setOnClickListener {
            databaseReference.child("menu").child(menuItem.key!!).removeValue().addOnSuccessListener {
                listData.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listData.size)
                notifyDataSetChanged()
            }

        }
        var c: Int
        holder.plus.setOnClickListener {
            var count = holder.quantity.text.toString()
            c = count.toInt()
            c++
            holder.quantity.text = c.toString()
            holder.minus.isClickable = true

        }
        holder.minus.setOnClickListener {
            var count = holder.quantity.text.toString()
            c = count.toInt()
            if (c == 0) {
                holder.minus.isClickable = false
            } else {
                c--
                holder.quantity.text = c.toString()

            }


        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val foodname: TextView = itemView.findViewById(R.id.foodName)
        val foodimage: ImageView = itemView.findViewById(R.id.foodimage)
        val foodprice: TextView = itemView.findViewById(R.id.quan)
        val minus: ImageButton = itemView.findViewById(R.id.minus)
        val plus: ImageButton = itemView.findViewById(R.id.plus)
        val delete: ImageButton = itemView.findViewById(R.id.delete)
        val quantity: TextView = itemView.findViewById(R.id.quantity)

    }
}