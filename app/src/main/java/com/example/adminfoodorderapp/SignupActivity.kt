package com.example.adminfoodorderapp

import android.R
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.adminfoodorderapp.databinding.ActivitySignupBinding
import com.example.adminfoodorderapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


class SignupActivity : AppCompatActivity() {
    private  val binding:ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameofRestaurant:String
    private lateinit var database: DatabaseReference
    private lateinit var location:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= Firebase.auth
        database=Firebase.database.reference

        binding.alradyhaveaccount.setOnClickListener {

            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val locationList :Array<String> = arrayOf("Pune","Mumbai","Nagpur","Nashik","Kolhapur")

        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.simple_expandable_list_item_1, locationList)
        val autocomlpeteTextView: AutoCompleteTextView = binding.listoflocations
        autocomlpeteTextView.setAdapter(adapter)

        binding.createAccountBtn.setOnClickListener {
            email=binding.email.text.toString().trim()
            password=binding.password.text.toString().trim()
            userName=binding.userName.text.toString().trim()
            nameofRestaurant=binding.nameofRestaurant.text.toString().trim()
            location=binding.listoflocations.text.toString()

            Log.d("Location",location)
            if (email.isBlank() || password.isBlank() || userName.isBlank() || nameofRestaurant.isBlank())
            {
                Toast.makeText(this,"Fill All the fields",Toast.LENGTH_SHORT).show()
            }
            else
            {
                createUser(email,password,userName,nameofRestaurant)
            }


        }
    }

    private fun createUser(email: String, password: String,userName:String,nameOfRestaurent:String) {


        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                savedata(email,password,userName,nameOfRestaurent)
                var intent=Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                Log.d("Error","CreateAccount:Failure",task.exception)
            }

        }


        }

    private fun savedata(email: String, password: String, userName: String, nameOfRestaurent: String) {

        var user=UserModel(userName,nameOfRestaurent,email,password,"phone","address")
        val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }


}