package com.dndialidrs.project_api_mobilecomputing.Nilai

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R

class DetailNilaiActivity : AppCompatActivity() {

    private lateinit var nilaiTextView: TextView
    private lateinit var peringkatTextView: TextView
    private lateinit var mahasiswaTextView: TextView
    private lateinit var mataKuliahTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_nilai)

        // Bind views
        nilaiTextView = findViewById(R.id.textViewNilai)
        peringkatTextView = findViewById(R.id.textViewPeringkat)
        mahasiswaTextView = findViewById(R.id.textViewMahasiswa)
        mataKuliahTextView = findViewById(R.id.textViewMataKuliah)

        // Get data from intent
        val nilai = intent.getStringExtra("nilai")
        val peringkat = intent.getStringExtra("peringkat")
        val mahasiswa = intent.getStringExtra("mahasiswa")
        val mataKuliah = intent.getStringExtra("mataKuliah")

        // Set data to TextViews
        nilaiTextView.text = nilai
        peringkatTextView.text = peringkat
        mahasiswaTextView.text = mahasiswa
        mataKuliahTextView.text = mataKuliah
    }
}

