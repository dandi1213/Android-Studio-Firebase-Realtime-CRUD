package com.dndialidrs.project_api_mobilecomputing.MataKuliah

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.firebase.database.FirebaseDatabase

class AddMataKuliahActivity : AppCompatActivity() {
    private var mataKuliahEditText: EditText? = null
    private var sksEditText: EditText? = null
    private var dosenEditText: EditText? = null
    private var addButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mata_kuliah)

        // Bind views
        mataKuliahEditText = findViewById<View>(R.id.editTextMataKuliah) as EditText
        sksEditText = findViewById<View>(R.id.editTextSks) as EditText
        dosenEditText = findViewById<View>(R.id.editTextDosen) as EditText
        addButton = findViewById<View>(R.id.addButton) as Button

        // Set up click listener for add button
        addButton!!.setOnClickListener(View.OnClickListener {
            // Get text from EditText fields
            val mataKuliah = mataKuliahEditText!!.text.toString()
            val sks = sksEditText!!.text.toString()
            val dosen = dosenEditText!!.text.toString()

            // Check if any field is empty
            if (mataKuliah.isEmpty()) {
                mataKuliahEditText!!.error = "Cannot be empty"
                return@OnClickListener
            }
            if (sks.isEmpty()) {
                sksEditText!!.error = "Cannot be empty"
                return@OnClickListener
            }
            if (dosen.isEmpty()) {
                dosenEditText!!.error = "Cannot be empty"
                return@OnClickListener
            }

            // Add the mata kuliah to the database
            addMataKuliahToDB(mataKuliah, sks, dosen)
        })
    }

    private fun addMataKuliahToDB(mataKuliah: String, sks: String, dosen: String) {
        // Create a hashmap to store mata kuliah data
        val mataKuliahHashMap = HashMap<String, Any?>()
        mataKuliahHashMap["mataKuliah"] = mataKuliah
        mataKuliahHashMap["sks"] = sks
        mataKuliahHashMap["dosen"] = dosen

        // Get database reference
        val database = FirebaseDatabase.getInstance()
        val mataKuliahRef = database.getReference("mata_kuliah")

        // Push data to Firebase
        val key = mataKuliahRef.push().key
        mataKuliahHashMap["key"] = key
        mataKuliahRef.child(key!!).setValue(mataKuliahHashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Show toast message upon successful addition
                Toast.makeText(this@AddMataKuliahActivity, "Added", Toast.LENGTH_SHORT).show()

                // Clear EditText fields after addition
                mataKuliahEditText!!.text.clear()
                sksEditText!!.text.clear()
                dosenEditText!!.text.clear()

                // Redirect to MataKuliahActivity
                val intent = Intent(this@AddMataKuliahActivity, MataKuliahActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close AddMataKuliahActivity
            } else {
                // Show toast message if addition failed
                Toast.makeText(this@AddMataKuliahActivity, "Failed to add", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
