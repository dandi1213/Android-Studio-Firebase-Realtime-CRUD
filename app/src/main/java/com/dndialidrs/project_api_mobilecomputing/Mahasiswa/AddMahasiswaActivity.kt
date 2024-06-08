package com.dndialidrs.project_api_mobilecomputing.Mahasiswa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.firebase.database.FirebaseDatabase

class AddMahasiswaActivity : AppCompatActivity() {
    private var namaEditText: EditText? = null
    private var nimEditText: EditText? = null
    private var ttlEditText: EditText? = null
    private var addButton: Button? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mahasiswa)

        // Bind views
        namaEditText = findViewById<View>(R.id.editTextNama) as EditText
        nimEditText = findViewById<View>(R.id.editTextNim) as EditText
        ttlEditText = findViewById<View>(R.id.editTextTtl) as EditText
        addButton = findViewById<View>(R.id.addButton) as Button

        // Set up listener for the add button
        addButton!!.setOnClickListener(View.OnClickListener {
            // Get text from EditText fields
            val nama = namaEditText!!.text.toString()
            val nim = nimEditText!!.text.toString()
            val ttl = ttlEditText!!.text.toString()

            // Check if any field is empty
            if (nama.isEmpty() || nim.isEmpty() || ttl.isEmpty()) {
                Toast.makeText(this@AddMahasiswaActivity, "All fields are required", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            // Add data to the database
            addMahasiswaToDB(nama, nim, ttl)
        })
    }

    private fun addMahasiswaToDB(nama: String, nim: String, ttl: String) {
        // Create a HashMap to store Mahasiswa data
        val mahasiswaHashMap = HashMap<String, Any?>()
        mahasiswaHashMap["Nama"] = nama
        mahasiswaHashMap["Nim"] = nim
        mahasiswaHashMap["Ttl"] = ttl

        // Instantiate Firebase database reference
        val database = FirebaseDatabase.getInstance()
        val mahasiswaRef = database.getReference("mahasiswa")
        val key = mahasiswaRef.push().key
        mahasiswaHashMap["key"] = key
        mahasiswaRef.child(key!!).setValue(mahasiswaHashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Show toast message upon successful addition
                Toast.makeText(this@AddMahasiswaActivity, "Mahasiswa added", Toast.LENGTH_SHORT).show()

                // Clear EditText fields after adding data
                namaEditText!!.text.clear()
                nimEditText!!.text.clear()
                ttlEditText!!.text.clear()

                // Redirect to MahasiswaActivity
                val intent = Intent(this@AddMahasiswaActivity, MahasiswaActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close AddMahasiswaActivity
            } else {
                // Show toast message if addition failed
                Toast.makeText(this@AddMahasiswaActivity, "Failed to add", Toast.LENGTH_SHORT).show()
            }
        }
    }

}