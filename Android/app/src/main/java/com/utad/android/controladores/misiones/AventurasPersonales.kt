package com.utad.android.controladores.misiones

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.android.R

class AventurasPersonales : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aventuras_personales)
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}