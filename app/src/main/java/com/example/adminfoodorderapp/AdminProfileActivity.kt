package com.example.adminfoodorderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminfoodorderapp.databinding.ActivityAdminProfileBinding
import com.example.adminfoodorderapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding:ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var  auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=Firebase.auth
        database= FirebaseDatabase.getInstance()

        binding.name.isEnabled=false
        binding.email.isEnabled=false
        binding.address.isEnabled=false
        binding.phone.isEnabled=false
        binding.password.isEnabled=false
        binding.saveinformation.isEnabled=false
        setData()
        var isEnabled=false

        binding.editprofile.setOnClickListener {

         isEnabled= !isEnabled

            binding.name.isEnabled=true
            binding.email.isEnabled=true
            binding.address.isEnabled=true
            binding.phone.isEnabled=true
            binding.password.isEnabled=true
            binding.saveinformation.isEnabled=true

            if (isEnabled)
            {
                binding.name.requestFocus()
            }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.saveinformation.setOnClickListener {

            var name:String=binding.name.text.toString().trim()
            var address:String=binding.address.text.toString().trim()
            var email:String=binding.email.text.toString().trim()
            var phone:String=binding.phone.text.toString().trim()
            var password:String=binding.password.text.toString().trim()

            updateUserData(name,address,email,phone,password)
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    private fun updateUserData(name: String, address: String, email: String, phone: String, password: String) {

        if (name.isBlank() && address.isBlank() && email.isBlank() && phone.isBlank() && password.isBlank())
        {
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show()
            return
        }
        else
        {
            var userId:String= auth.currentUser!!.uid
            var userRef:DatabaseReference=database.reference.child("user").child(userId)
             var data:HashMap<String,String> = hashMapOf(
                 "name" to name,
                 "email" to email,
                 "password" to password,
                 "phone" to phone,
                 "address" to address
             )
            userRef.updateChildren(data  as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(this,"User updated successfully..",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                Toast.makeText(this,"Something Went Wrong..",Toast.LENGTH_SHORT).show()
            }


        }
    }


    private fun setData() {
        if (auth.currentUser!=null)
        {
            var userId:String= auth.currentUser!!.uid

            var userRef:DatabaseReference=database.reference.child("user").child(userId)
            userRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        var user:UserModel= snapshot.getValue(UserModel::class.java)!!
                        binding.name.setText(user.name)
                        binding.password.setText(user.password)
                        binding.email.setText(user.email)
                        binding.address.setText(user.address)
                        binding.phone.setText(user.phone)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminProfileActivity,"Something went wrong..",Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}