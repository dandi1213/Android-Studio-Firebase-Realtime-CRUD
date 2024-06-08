package com.dndialidrs.project_api_mobilecomputing.Mahasiswa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MahasiswaActivity : AppCompatActivity() {
    private lateinit var mahasiswaButton: FloatingActionButton

    private lateinit var mahasiswaRecyclerView: RecyclerView
    private lateinit var mahasiswaAdapter: MahasiswaAdapter
    private var mahasiswaList: MutableList<Mahasiswa?> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mahasiswa)

        mahasiswaButton = findViewById(R.id.mahasiswaButton)

        mahasiswaRecyclerView = findViewById(R.id.mahasiswa_rv)

        mahasiswaRecyclerView.layoutManager = LinearLayoutManager(this)
        mahasiswaAdapter = MahasiswaAdapter(this, mahasiswaList)
        mahasiswaRecyclerView.adapter = mahasiswaAdapter

        mahasiswaButton.setOnClickListener {
            val intent = Intent(this@MahasiswaActivity, AddMahasiswaActivity::class.java)
            startActivity(intent)
        }

        // Retrieve mahasiswa from Firebase
        val mahasiswaRef = FirebaseDatabase.getInstance().getReference("mahasiswa")

        mahasiswaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mahasiswaList.clear()
                for (snapshot in dataSnapshot.children) {
                    val mahasiswa = snapshot.getValue(Mahasiswa::class.java)
                    if (mahasiswa != null) {
                        mahasiswaList.add(mahasiswa)
                    }
                }
                Log.d("MahasiswaActivity", "Mahasiswa list size: ${mahasiswaList.size}")
                mahasiswaAdapter.updateMahasiswa(mahasiswaList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MahasiswaActivity", "Error retrieving data", databaseError.toException())
            }
        })
    }
}