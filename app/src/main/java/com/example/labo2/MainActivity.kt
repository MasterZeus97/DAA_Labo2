package com.example.labo2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.Group
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private lateinit var birthdayDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_labo2)

        val name =findViewById<EditText>(R.id.edit_name)
        val surname =findViewById<EditText>(R.id.edit_surname)
        val nationality = findViewById<Spinner>(R.id.spinner_nation)
        val student = findViewById<RadioButton>(R.id.rb_student)
        val teacher = findViewById<RadioButton>(R.id.rb_teacher)
        val grpStudent = findViewById<Group>(R.id.grp_student)
        val grpWorker = findViewById<Group>(R.id.grp_worker)
        val btnBirthday = findViewById<ImageButton>(R.id.btn_birthday)
        birthdayDate = findViewById<EditText>(R.id.edit_birthday)

        ArrayAdapter.createFromResource(this, R.array.nationalities, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            nationality.adapter = adapter
        }

        // nationality.onItemSelectedListener =

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }

        btnBirthday.setOnClickListener {
            DatePickerDialog(
                this@MainActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDate() {
        val myFormat = "dd MMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        birthdayDate.text = sdf.format(cal.getTime())
    }

}