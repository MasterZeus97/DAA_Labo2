package com.example.labo2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.Group
import android.widget.AdapterView.OnItemSelectedListener
import ch.heigvd.iict.and.labo2.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Perrenoud Pascal
 * @author Seem Thibault
 * @description Layout pour le labo2 de DAA, formulaire d'inscription/identification
 */

class MainActivityLabo2 : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private lateinit var birthdate: EditText
    private var nationalityValue: String? = null
    private var sectorValue: String? = null

    // Common inputs
    private lateinit var name: EditText
    private lateinit var surname: EditText
    private lateinit var nationality: Spinner
    private lateinit var email: EditText
    private lateinit var remark: EditText

    // Supp Student
    private lateinit var schoolName: EditText
    private lateinit var yearDegree: EditText

    // Supp Worker
    private lateinit var companyName: EditText
    private lateinit var sector: Spinner
    private lateinit var experience: EditText

    // Actions
    private lateinit var radGrp: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_labo2)

        // Group view
        val grpStudent = findViewById<Group>(R.id.grp_student)
        val grpWorker = findViewById<Group>(R.id.grp_worker)

        // Buttons and actions
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val btnOK = findViewById<Button>(R.id.btn_ok)
        radGrp = findViewById(R.id.radio_grp)
        val btnBirthday = findViewById<ImageButton>(R.id.btn_birthday)

        // Common inputs
        name = findViewById(R.id.edit_name)
        surname = findViewById(R.id.edit_surname)
        nationality = findViewById(R.id.spinner_nation)
        email = findViewById(R.id.edit_email)
        remark = findViewById(R.id.edit_remarks)
        birthdate = findViewById(R.id.edit_birthday)

        // Supp Student
        schoolName = findViewById(R.id.edit_school_name)
        yearDegree = findViewById(R.id.edit_year_degree)

        // Supp worker
        companyName = findViewById(R.id.edit_company_name)
        sector = findViewById(R.id.spinner_sector)
        experience = findViewById(R.id.edit_experience)

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
            birthdate.setText("")
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

        email.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    btnOK.performClick()
                    true
                }
                else -> false
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
                pickDate(year, monthOfYear, dayOfMonth)
            }

        btnBirthday.setOnClickListener {
            DatePickerDialog(
                this@MainActivityLabo2,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set calendar to current time
        updateDate()

        // Example loads
        load(Person.exampleStudent)
        // load(Person.exampleWorker)
    }

    private fun spinnerSelect(s: Spinner, arrayId: Int, v: String) {
        val arr = resources.getStringArray(arrayId)
        for (i in arr.indices) {
            if (arr[i].toString() == v) {
                s.setSelection(i)
                return
            }
        }
    }

    private fun load(p: Person) {
        name.setText(p.name)
        surname.setText(p.firstName)
        spinnerSelect(nationality, R.array.nationalities, p.nationality)
        email.setText(p.email)
        remark.setText(p.remark)
        pickDate(p.birthDay.get(Calendar.YEAR), p.birthDay.get(Calendar.MONTH), p.birthDay.get(Calendar.DAY_OF_MONTH))
    }

    public fun load(s: Student) {
        load(s as Person)
        radGrp.check(R.id.rb_student)
        schoolName.setText(s.university)
        yearDegree.setText(s.graduationYear.toString())
    }

    public fun load(w: Worker) {
        load(w as Person)
        radGrp.check(R.id.rb_worker)
        companyName.setText(w.company)
        spinnerSelect(sector, R.array.sectors, w.sector)
        experience.setText(w.experienceYear.toString())
    }

    private fun pickDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDate()
    }

    private fun updateDate() {
        val myFormat = "dd MMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        birthdate.setText(sdf.format(cal.time))
    }

}