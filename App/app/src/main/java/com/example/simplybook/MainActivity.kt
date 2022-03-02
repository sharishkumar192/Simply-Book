package com.example.simplybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    lateinit var imgbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       imgbutton = findViewById(R.id.imgbutton)

        imgbutton.setOnClickListener {
            val intent = Intent(this, Image::class.java)
            startActivity(intent)

        }



        }
    }
