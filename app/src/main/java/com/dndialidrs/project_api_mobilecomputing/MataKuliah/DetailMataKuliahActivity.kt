package com.dndialidrs.project_api_mobilecomputing.MataKuliah

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R

class DetailMataKuliahActivity : AppCompatActivity() {

    private lateinit var mataKuliahTextView: TextView
    private lateinit var sksTextView: TextView
    private lateinit var dosenTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mata_kuliah)

        // Bind views
        mataKuliahTextView = findViewById(R.id.textViewMataKuliah)
        sksTextView = findViewById(R.id.textViewSks)
        dosenTextView = findViewById(R.id.textViewDosen)

        // Get data from intent
        val mataKuliah = intent.getStringExtra("mataKuliah")
        val sks = intent.getStringExtra("sks")
        val dosen = intent.getStringExtra("dosen")

        // Set data to TextViews
        mataKuliahTextView.text = mataKuliah
        sksTextView.text = sks
        dosenTextView.text = dosen
    }
}
