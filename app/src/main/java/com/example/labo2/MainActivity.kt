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
        val teacher = findViewById<RadioButton>(R.id.rb_worker)
        val grpStudent = findViewById<Group>(R.id.grp_student)
        val grpWorker = findViewById<Group>(R.id.grp_worker)
        val btnBirthday = findViewById<ImageButton>(R.id.btn_birthday)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val radGrp = findViewById<RadioGroup>(R.id.radio_grp)
        birthdayDate = findViewById<EditText>(R.id.edit_birthday)
        val email = findViewById<EditText>(R.id.edit_email)
        val remark = findViewById<EditText>(R.id.edit_remarks)

        // Supp Student
        val ecole_name = findViewById<EditText>(R.id.edit_ecole_name)
        val year_degree = findViewById<EditText>(R.id.edit_year_degree)

        // Supp worker
        val worker_name = findViewById<EditText>(R.id.edit_worker_name)
        val sector = findViewById<Spinner>(R.id.spinner_sector)
        val experience = findViewById<EditText>(R.id.edit_experience)

        // Cancel on click
        btnCancel.setOnClickListener {
            name.setText("")
            surname.setText("")
            birthdayDate.text = ""
            nationality.setSelection(0)
            radGrp.clearCheck()
            ecole_name.setText("")
            year_degree.setText("")
            worker_name.setText("")
            sector.setSelection(0)
            experience.setText("")
            email.setText("")
            remark.setText("")
        }

        // Radio callback for supplementary data
        radGrp.setOnCheckedChangeListener{_, choice ->
            grpStudent.visibility = if (choice == R.id.rb_student) View.VISIBLE else View.GONE
            grpWorker.visibility = if (choice == R.id.rb_worker) View.VISIBLE else View.GONE
        }

        // Date management
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
        birthdayDate.text = sdf.format(cal.time)
    }

}