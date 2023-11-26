package com.example.adminfoodorderapp

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfoodorderapp.databinding.ActivityAddItemBinding
import com.example.adminfoodorderapp.model.AllMenu
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddItemActivity : AppCompatActivity() {
    private lateinit var foodName:String
    private lateinit var foodPrice:String
    private lateinit var foodDescription:String
    private lateinit var foodIngredient:String
    private  var foodImageUri:Uri?=null

    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase

    private lateinit var progressDialog:ProgressDialog

    private val binding:ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=Firebase.auth
        database= FirebaseDatabase.getInstance()

         progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Data Uploading..")
        progressDialog.setMessage("Please wait..")


        binding.backBtn.setOnClickListener {
            finish()
        }

       binding.selectiamge.setOnClickListener {

          pickImage.launch("image/*")
       }


    }
   private val pickImage= registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if (uri!= null)
        {
            binding.selectedImage.setImageURI(uri)
            foodImageUri=uri
        }
       binding.additemBtn.setOnClickListener {

        foodName=binding.enterfoodname.text.toString().trim()
        foodPrice=binding.enterfoodprice.text.toString().trim()
        foodDescription=binding.description.text.toString().trim()
        foodIngredient=binding.ingradient.text.toString().trim()


         if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank()))
         {

             uploadData()
             Toast.makeText(this,"Item Added Successfully !",Toast.LENGTH_SHORT).show()

             finish()

         }
        else
         {
             Toast.makeText(this,"Fill All Fields",Toast.LENGTH_SHORT).show()
         }
    }

    }

    private fun uploadData() {

        val menuRef:DatabaseReference= database.getReference("menu")
        val newItemKey:String?=menuRef.push().key

        if (foodImageUri !=null)
        {

            val storageRef:StorageReference= FirebaseStorage.getInstance().reference

               val imageRef:StorageReference=storageRef.child("menu_items/${newItemKey}.jpg")
               val uploadTask: UploadTask =imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->

                    val newItem=AllMenu(
                        newItemKey,
                        foodName=foodName,
                        foodPrice=foodPrice,
                        foodDescription=foodDescription,
                        foodIngrediant=foodIngredient,
                        foodImage=downloadUrl.toString()

                    )
                    newItemKey?.let {
                        key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this,"Data uploaded Successfully !",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this,"Data uploaded failed.",Toast.LENGTH_SHORT).show()
                            }
                    }


                }
                    .addOnFailureListener {
                        Toast.makeText(this,"image uploaded failed.",Toast.LENGTH_SHORT).show()
                    }
            }
        }
        else
        {
            Toast.makeText(this,"Please select an image ",Toast.LENGTH_SHORT).show()
        }


    }
}