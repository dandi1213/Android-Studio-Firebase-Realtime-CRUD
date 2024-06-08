package com.dndialidrs.project_api_mobilecomputing.Nilai

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

class NilaiActivity : AppCompatActivity() {

    private lateinit var nilaiButton: FloatingActionButton

    private lateinit var nilaiRecyclerView: RecyclerView
    private lateinit var nilaiAdapter: NilaiAdapter
    private var nilaiList: MutableList<Nilai?> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nilai)

        nilaiButton = findViewById(R.id.nilaiButton)

        nilaiRecyclerView = findViewById(R.id.nilai_rv)

        nilaiRecyclerView.layoutManager = LinearLayoutManager(this)
        nilaiAdapter = NilaiAdapter(this, nilaiList)
        nilaiRecyclerView.adapter = nilaiAdapter

        nilaiButton.setOnClickListener {
            val intent = Intent(this@NilaiActivity, AddNilaiActivity::class.java)
            startActivity(intent)
        }

        // Retrieve nilai from Firebase
        val nilaiRef = FirebaseDatabase.getInstance().getReference("nilai")

        nilaiRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nilaiList.clear()
                for (snapshot in dataSnapshot.children) {
                    val nilai = snapshot.getValue(Nilai::class.java)
                    if (nilai != null) {
                        nilaiList.add(nilai)
                    }
                }
                Log.d("NilaiActivity", "Nilai list size: ${nilaiList.size}")
                nilaiAdapter.updateNilai(nilaiList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("NilaiActivity", "Error retrieving data", databaseError.toException())
            }
        })
    }
}