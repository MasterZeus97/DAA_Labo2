package com.example.labo2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.Group
import android.widget.AdapterView.OnItemSelectedListener;
import ch.heigvd.iict.and.labo2.Student
import ch.heigvd.iict.and.labo2.Worker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private lateinit var birthdate: TextView
    private var nationalityValue: String? = null
    private var sectorValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_labo2)

        // Group view
        val grpStudent = findViewById<Group>(R.id.grp_student)
        val grpWorker = findViewById<Group>(R.id.grp_worker)

        // Buttons and actions
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val btnOK = findViewById<Button>(R.id.btn_ok)
        val radGrp = findViewById<RadioGroup>(R.id.radio_grp)
        val btnBirthday = findViewById<ImageButton>(R.id.btn_birthday)

        // Common inputs
        val name = findViewById<EditText>(R.id.edit_name)
        val surname = findViewById<EditText>(R.id.edit_surname)
        val nationality = findViewById<Spinner>(R.id.spinner_nation)
        val email = findViewById<EditText>(R.id.edit_email)
        val remark = findViewById<EditText>(R.id.edit_remarks)
        birthdate = findViewById<EditText>(R.id.edit_birthday)

        // Supp Student
        val schoolName = findViewById<EditText>(R.id.edit_school_name)
        val yearDegree = findViewById<EditText>(R.id.edit_year_degree)

        // Supp worker
        val companyName = findViewById<EditText>(R.id.edit_company_name)
        val sector = findViewById<Spinner>(R.id.spinner_sector)
        val experience = findViewById<EditText>(R.id.edit_experience)

        // Nationality selection
        nationality.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                nationalityValue = if (nationality.selectedItemPosition == 0) {
                    null
                } else {
                    nationality.selectedItem.toString()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                nationalityValue = null
            }
        }

        // Sector selection
        sector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sectorValue = if (sector.selectedItemPosition == 0) {
                    null
                } else {
                    sector.selectedItem.toString()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                sectorValue = null
            }
        }

        // Cancel on click
        btnCancel.setOnClickListener {
            name.setText("")
            surname.setText("")
            birthdate.text = ""
            nationality.setSelection(0)
            radGrp.clearCheck()
            schoolName.setText("")
            yearDegree.setText("")
            companyName.setText("")
            sector.setSelection(0)
            experience.setText("")
            email.setText("")
            remark.setText("")
        }

        // Button OK : Print values in log
        btnOK.setOnClickListener {
            if (null == nationalityValue) {
                Log.e("Validation", "No nationality selected")
                return@setOnClickListener
            }
            when (radGrp.checkedRadioButtonId) {
                R.id.rb_student -> {
                    val graduationYear: Int
                    try {
                        graduationYear = yearDegree.text.toString().toInt()
                    } catch (_: Exception) {
                        Log.e("Validation", "No value given for graduation year")
                        return@setOnClickListener
                    }

                    println(Student(
                        name.text.toString(),
                        surname.text.toString(),
                        cal,
                        nationalityValue!!,
                        schoolName.text.toString(),
                        graduationYear,
                        email.text.toString(),
                        remark.text.toString()
                    ).toString())
                }
                R.id.rb_worker -> {
                    if (null == sectorValue) {
                        Log.e("Validation", "No selection for work sector")
                        return@setOnClickListener
                    }
                    val exp: Int
                    try {
                        exp = experience.text.toString().toInt()
                    } catch (_: Exception) {
                        Log.e("Validation", "No value for experience")
                        return@setOnClickListener
                    }

                    println(Worker(
                        name.text.toString(),
                        surname.text.toString(),
                        cal,
                        nationalityValue!!,
                        companyName.text.toString(),
                        sectorValue!!,
                        exp,
                        email.text.toString(),
                        remark.text.toString()
                    ).toString())
                }
                else -> {
                    Log.e("Validation", "No specification selected")
                }
            }
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

        // Set calendar to current time
        updateDate()
    }

    private fun updateDate() {
        val myFormat = "dd MMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        birthdate.text = sdf.format(cal.time)
    }

}