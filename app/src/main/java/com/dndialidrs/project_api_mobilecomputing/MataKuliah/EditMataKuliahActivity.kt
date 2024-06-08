package com.dndialidrs.project_api_mobilecomputing.MataKuliah

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.firebase.database.FirebaseDatabase

class EditMataKuliahActivity : AppCompatActivity() {

    private lateinit var mataKuliahEditText: EditText
    private lateinit var sksEditText: EditText
    private lateinit var dosenEditText: EditText
    private lateinit var updateButton: Button
    private var mataKuliahKey: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_mata_kuliah)

        // Bind views
        mataKuliahEditText = findViewById(R.id.editTextMataKuliah)
        sksEditText = findViewById(R.id.editTextSks) // Menambahkan EditText untuk SKS
        dosenEditText = findViewById(R.id.editTextDosen)
        updateButton = findViewById(R.id.tombolupdate)

        // Get data from intent
        mataKuliahKey = intent.getStringExtra("key")
        val mataKuliahText = intent.getStringExtra("mataKuliah")
        val sksText = intent.getStringExtra("sks")
        val dosenText = intent.getStringExtra("dosen")

        // Set data to EditText
        mataKuliahEditText.setText(mataKuliahText)
        sksEditText.setText(sksText)
        dosenEditText.setText(dosenText)

        // Set up update button listener
        updateButton.setOnClickListener {
            val updatedMataKuliah = mataKuliahEditText.text.toString()
            val updatedSks = sksEditText.text.toString()
            val updatedDosen = dosenEditText.text.toString()

            if (updatedMataKuliah.isEmpty()) {
                mataKuliahEditText.error = "Tidak boleh kosong"
                return@setOnClickListener
            }
            if (updatedSks.isEmpty()) {
                sksEditText.error = "Tidak boleh kosong"
                return@setOnClickListener
            }
            if (updatedDosen.isEmpty()) {
                dosenEditText.error = "Tidak boleh kosong"
                return@setOnClickListener
            }

            // Update the mata kuliah in the database
            updateMataKuliahInDB(mataKuliahKey, updatedMataKuliah, updatedSks, updatedDosen)
        }
    }

    private fun updateMataKuliahInDB(key: String?, mataKuliah: String, sks: String, dosen: String) {
        val mataKuliahRef = FirebaseDatabase.getInstance().getReference("mata_kuliah")
        val updateMap = HashMap<String, Any>().apply {
            put("mataKuliah", mataKuliah)
            put("sks", sks)
            put("dosen", dosen)
        }

        mataKuliahRef.child(key!!).updateChildren(updateMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Mata Kuliah berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal mengupdate Mata Kuliah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
