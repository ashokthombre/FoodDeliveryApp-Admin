package com.example.adminfoodorderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfoodorderapp.databinding.ActivityCrateUserBinding

class CrateUserActivity : AppCompatActivity() {
    private val binding:ActivityCrateUserBinding by lazy {
        ActivityCrateUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}