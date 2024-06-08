package com.dndialidrs.project_api_mobilecomputing.Nilai

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.firebase.database.*
import com.dndialidrs.project_api_mobilecomputing.Mahasiswa.Mahasiswa
import com.dndialidrs.project_api_mobilecomputing.MataKuliah.MataKuliah

class AddNilaiActivity : AppCompatActivity() {
    private lateinit var nilaiEditText: EditText
    private lateinit var peringkatEditText: EditText
    private lateinit var addButton: Button
    private lateinit var spinnerNamaMahasiswa: Spinner
    private lateinit var spinnerNamaMatkul: Spinner
    private lateinit var mahasiswaList: MutableList<Mahasiswa>
    private lateinit var mataKuliahList: MutableList<MataKuliah>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nilai)

        // Bind views
        nilaiEditText = findViewById(R.id.editTextNilai)
        peringkatEditText = findViewById(R.id.editTextPeringkat)
        addButton = findViewById(R.id.addButton)
        spinnerNamaMahasiswa = findViewById(R.id.spinnerNamaMahasiswa)
        spinnerNamaMatkul = findViewById(R.id.spinnerNamaMatkul)

        // Initialize lists
        mahasiswaList = mutableListOf()
        mataKuliahList = mutableListOf()

        // Fetch data from Firebase and populate spinners
        fetchMahasiswaData()
        fetchMataKuliahData()

        // Set click listener for the add button
        addButton.setOnClickListener {
            val nilai = nilaiEditText.text.toString()
            val peringkat = peringkatEditText.text.toString()
            val selectedMahasiswa = spinnerNamaMahasiswa.selectedItem as String
            val selectedMataKuliah = spinnerNamaMatkul.selectedItem as String

            // Check if "Pilih Mahasiswa" or "Pilih Mata Kuliah" is selected
            if (selectedMahasiswa == "Pilih Mahasiswa") {
                Toast.makeText(this@AddNilaiActivity, "Please select a Mahasiswa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedMataKuliah == "Pilih Mata Kuliah") {
                Toast.makeText(this@AddNilaiActivity, "Please select a Mata Kuliah", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nilai.isEmpty()) {
                nilaiEditText.error = "Cannot be empty"
                return@setOnClickListener
            }
            if (peringkat.isEmpty()) {
                peringkatEditText.error = "Cannot be empty"
                return@setOnClickListener
            }

            // Add data to the database
            addNilaiToDB(nilai, peringkat, selectedMahasiswa, selectedMataKuliah)
        }
    }

    private fun fetchMahasiswaData() {
        val database = FirebaseDatabase.getInstance()
        val mahasiswaRef = database.getReference("mahasiswa")
        mahasiswaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mahasiswaList.clear()
                mahasiswaList.add(Mahasiswa("Pilih Mahasiswa", "")) // Add default option
                for (mahasiswaSnapshot in snapshot.children) {
                    val mahasiswa = mahasiswaSnapshot.getValue(Mahasiswa::class.java)
                    mahasiswa?.let { mahasiswaList.add(it) }
                }
                val adapter = ArrayAdapter(
                    this@AddNilaiActivity,
                    android.R.layout.simple_spinner_item,
                    mahasiswaList.map { it.Nama!! }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNamaMahasiswa.adapter = adapter
                spinnerNamaMahasiswa.setSelection(0) // Set default selection to "Pilih Mahasiswa"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun fetchMataKuliahData() {
        val database = FirebaseDatabase.getInstance()
        val mataKuliahRef = database.getReference("mata_kuliah")
        mataKuliahRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mataKuliahList.clear()
                mataKuliahList.add(MataKuliah("Pilih Mata Kuliah")) // Add default option
                for (mataKuliahSnapshot in snapshot.children) {
                    val mataKuliah = mataKuliahSnapshot.getValue(MataKuliah::class.java)
                    mataKuliah?.let { mataKuliahList.add(it) }
                }
                val adapter = ArrayAdapter(
                    this@AddNilaiActivity,
                    android.R.layout.simple_spinner_item,
                    mataKuliahList.map { it.mataKuliah!! }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNamaMatkul.adapter = adapter
                spinnerNamaMatkul.setSelection(0) // Set default selection to "Pilih Mata Kuliah"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }


    private fun addNilaiToDB(nilai: String, peringkat: String, mahasiswa: String, mataKuliah: String) {
        val nilaiHashMap = HashMap<String, Any?>()
        nilaiHashMap["nilai"] = nilai
        nilaiHashMap["peringkat"] = peringkat
        nilaiHashMap["mahasiswa"] = mahasiswa
        nilaiHashMap["mataKuliah"] = mataKuliah

        val database = FirebaseDatabase.getInstance()
        val nilaiRef = database.getReference("nilai")
        val key = nilaiRef.push().key
        nilaiHashMap["key"] = key
        nilaiRef.child(key!!).setValue(nilaiHashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@AddNilaiActivity, "Added", Toast.LENGTH_SHORT).show()
                // Clear input fields
                nilaiEditText.text.clear()
                peringkatEditText.text.clear()

                // Redirect to NilaiActivity
                val intent = Intent(this@AddNilaiActivity, NilaiActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close AddNilaiActivity
            } else {
                Toast.makeText(this@AddNilaiActivity, "Failed to add", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
