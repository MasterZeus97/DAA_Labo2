package com.example.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_labo2)

        val name =findViewById<EditText>(R.id.edit_name)
        val surname =findViewById<EditText>(R.id.edit_surname)
        val nationality = findViewById<Spinner>(R.id.spinner_nation)
        val student = findViewById<RadioButton>(R.id.rb_student)
        val teacher = findViewById<RadioButton>(R.id.rb_teacher)

    }

}