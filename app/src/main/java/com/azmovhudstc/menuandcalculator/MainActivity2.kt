package com.azmovhudstc.menuandcalculator

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity2: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var linerLayout=LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        )
        linerLayout.setBackgroundColor(Color.MAGENTA)
        linerLayout.layoutParams=layoutParams
        setContentView(linerLayout,layoutParams)
    }
}