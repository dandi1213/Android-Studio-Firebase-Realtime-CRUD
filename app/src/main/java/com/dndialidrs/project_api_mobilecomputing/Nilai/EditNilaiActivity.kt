package com.dndialidrs.project_api_mobilecomputing.Nilai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R
import com.dndialidrs.project_api_mobilecomputing.Mahasiswa.Mahasiswa
import com.dndialidrs.project_api_mobilecomputing.MataKuliah.MataKuliah
import com.google.firebase.database.*

class EditNilaiActivity : AppCompatActivity() {

    private lateinit var nilaiEditText: EditText
    private lateinit var peringkatEditText: EditText
    private lateinit var tombolupdate: Button
    private lateinit var spinnerNamaMahasiswa: Spinner
    private lateinit var spinnerNamaMatkul: Spinner
    private var nilaiKey: String? = null
    private lateinit var mahasiswaList: MutableList<Mahasiswa>
    private lateinit var mataKuliahList: MutableList<MataKuliah>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nilai)

        // Bind views
        nilaiEditText = findViewById(R.id.editTextNilai)
        peringkatEditText = findViewById(R.id.editTextPeringkat)
        tombolupdate = findViewById(R.id.tombolupdate)
        spinnerNamaMahasiswa = findViewById(R.id.spinnerNamaMahasiswa)
        spinnerNamaMatkul = findViewById(R.id.spinnerNamaMatkul)

        // Initialize lists
        mahasiswaList = mutableListOf()
        mataKuliahList = mutableListOf()

        // Get data from intent
        nilaiKey = intent.getStringExtra("key")
        val nilaiText = intent.getStringExtra("nilai")
        val peringkatText = intent.getStringExtra("peringkat")
        val mahasiswaText = intent.getStringExtra("mahasiswa")
        val mataKuliahText = intent.getStringExtra("mataKuliah")

        // Set data to EditText
        nilaiEditText.setText(nilaiText)
        peringkatEditText.setText(peringkatText)

        // Fetch data from Firebase and populate spinners
        fetchMahasiswaData(mahasiswaText)
        fetchMataKuliahData(mataKuliahText)

        // Set up update button listener
        tombolupdate.setOnClickListener {
            val updatedNilai = nilaiEditText.text.toString()
            val updatedPeringkat = peringkatEditText.text.toString()
            val selectedMahasiswa = spinnerNamaMahasiswa.selectedItem as String
            val selectedMataKuliah = spinnerNamaMatkul.selectedItem as String

            if (updatedNilai.isEmpty()) {
                nilaiEditText.error = "Tidak boleh kosong"
                return@setOnClickListener
            }

            if (updatedPeringkat.isEmpty()) {
                peringkatEditText.error = "Tidak boleh kosong"
                return@setOnClickListener
            }

            // Update the nilai in the database
            updateNilaiInDB(nilaiKey, updatedNilai, updatedPeringkat, selectedMahasiswa, selectedMataKuliah)
        }
    }

    private fun fetchMahasiswaData(selectedMahasiswa: String?) {
        val database = FirebaseDatabase.getInstance()
        val mahasiswaRef = database.getReference("mahasiswa")
        mahasiswaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mahasiswaList.clear()
                for (mahasiswaSnapshot in snapshot.children) {
                    val mahasiswa = mahasiswaSnapshot.getValue(Mahasiswa::class.java)
                    mahasiswa?.let { mahasiswaList.add(it) }
                }
                val adapter = ArrayAdapter(
                    this@EditNilaiActivity,
                    android.R.layout.simple_spinner_item,
                    mahasiswaList.map { it.Nama!! }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNamaMahasiswa.adapter = adapter
                spinnerNamaMahasiswa.setSelection(mahasiswaList.map { it.Nama }.indexOf(selectedMahasiswa))
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun fetchMataKuliahData(selectedMataKuliah: String?) {
        val database = FirebaseDatabase.getInstance()
        val mataKuliahRef = database.getReference("mata_kuliah")
        mataKuliahRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mataKuliahList.clear()
                for (mataKuliahSnapshot in snapshot.children) {
                    val mataKuliah = mataKuliahSnapshot.getValue(MataKuliah::class.java)
                    mataKuliah?.let { mataKuliahList.add(it) }
                }
                val adapter = ArrayAdapter(
                    this@EditNilaiActivity,
                    android.R.layout.simple_spinner_item,
                    mataKuliahList.map { it.mataKuliah!! }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNamaMatkul.adapter = adapter
                spinnerNamaMatkul.setSelection(mataKuliahList.map { it.mataKuliah }.indexOf(selectedMataKuliah))
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun updateNilaiInDB(key: String?, nilai: String, peringkat: String, mahasiswa: String, mataKuliah: String) {
        val nilaiRef = FirebaseDatabase.getInstance().getReference("nilai")
        val updateMap = HashMap<String, Any>().apply {
            put("nilai", nilai)
            put("peringkat", peringkat)
            put("mahasiswa", mahasiswa)
            put("mataKuliah", mataKuliah)
        }

        nilaiRef.child(key!!).updateChildren(updateMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Nilai berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal mengupdate Nilai", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
