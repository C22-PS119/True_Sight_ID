package com.truesightid.ui.language

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.truesightid.databinding.ActivityLanguageBinding
import com.truesightid.ui.main.MainActivity

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val languageManager = LanguageManager(this)

        binding.rlEnglish.setOnClickListener {
            languageManager.updateResource("en")
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("shouldProfile", true)
            startActivity(intent)
        }

        binding.rlIndonesia.setOnClickListener {
            languageManager.updateResource("id")
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("shouldProfile", true)
            startActivity(intent)
        }

        binding.ibBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("shouldProfile", true)
            startActivity(intent)
        }
    }
}