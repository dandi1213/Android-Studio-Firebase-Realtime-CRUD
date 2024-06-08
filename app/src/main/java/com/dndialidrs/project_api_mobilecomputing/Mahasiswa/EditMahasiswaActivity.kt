package com.dndialidrs.project_api_mobilecomputing.Mahasiswa

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.firebase.database.FirebaseDatabase

class EditMahasiswaActivity : AppCompatActivity() {

    private lateinit var namaEditText: EditText
    private lateinit var nimEditText: EditText
    private lateinit var ttlEditText: EditText
    private lateinit var updateButton: Button
    private var mahasiswaKey: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_mahasiswa)

        // Bind views
        namaEditText = findViewById(R.id.editTextNama)
        nimEditText = findViewById(R.id.editTextNim)
        ttlEditText = findViewById(R.id.editTextTtl)
        updateButton = findViewById(R.id.updateButton)

        // Get data from intent
        mahasiswaKey = intent.getStringExtra("key")
        val namaText = intent.getStringExtra("Nama")
        val nimText = intent.getStringExtra("Nim")
        val ttlText = intent.getStringExtra("Ttl")

        // Set data to EditText fields
        namaEditText.setText(namaText)
        nimEditText.setText(nimText)
        ttlEditText.setText(ttlText)

        // Set up listener for the update button
        updateButton.setOnClickListener {
            val updatedNama = namaEditText.text.toString()
            val updatedNim = nimEditText.text.toString()
            val updatedTtl = ttlEditText.text.toString()

            // Check if any field is empty
            if (updatedNama.isEmpty() || updatedNim.isEmpty() || updatedTtl.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the mahasiswa in the database
            updateMahasiswaInDB(mahasiswaKey, updatedNama, updatedNim, updatedTtl)
        }
    }

    private fun updateMahasiswaInDB(key: String?, nama: String, nim: String, ttl: String) {
        val mahasiswaRef = FirebaseDatabase.getInstance().getReference("mahasiswa")
        val updateMap = hashMapOf<String, Any>(
            "Nama" to nama,
            "Nim" to nim,
            "Ttl" to ttl
        )

        mahasiswaRef.child(key!!).updateChildren(updateMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Mahasiswa updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update mahasiswa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
