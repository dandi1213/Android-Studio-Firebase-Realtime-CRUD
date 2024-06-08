package com.dndialidrs.project_api_mobilecomputing.MataKuliah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dndialidrs.project_api_mobilecomputing.Nilai.Nilai
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MataKuliahActivity : AppCompatActivity() {

    private lateinit var mataKuliahButton: FloatingActionButton


    private lateinit var mataKuliahRecyclerView: RecyclerView
    private lateinit var mataKuliahAdapter: MataKuliahAdapter
    private var mataKuliahList: MutableList<MataKuliah?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mata_kuliah)

        mataKuliahButton = findViewById(R.id.mataKuliahButton)

        mataKuliahRecyclerView = findViewById(R.id.mataKuliah_rv)

        mataKuliahRecyclerView.layoutManager = LinearLayoutManager(this)
        mataKuliahAdapter = MataKuliahAdapter(this, mataKuliahList)
        mataKuliahRecyclerView.adapter = mataKuliahAdapter

        mataKuliahButton.setOnClickListener {
            val intent = Intent(this@MataKuliahActivity, AddMataKuliahActivity::class.java)
            startActivity(intent)
        }

        // Retrieve mata kuliah from Firebase
        val mataKuliahRef = FirebaseDatabase.getInstance().getReference("mata_kuliah")

        mataKuliahRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mataKuliahList.clear()
                for (snapshot in dataSnapshot.children) {
                    val mataKuliah = snapshot.getValue(MataKuliah::class.java)
                    if (mataKuliah != null) {
                        mataKuliahList.add(mataKuliah)
                    }
                }
                Log.d("MataKuliahActivity", "Mata Kuliah list size: ${mataKuliahList.size}")
                mataKuliahAdapter.updateMataKuliah(mataKuliahList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MataKuliahActivity", "Error retrieving data", databaseError.toException())
            }
        })
    }
}