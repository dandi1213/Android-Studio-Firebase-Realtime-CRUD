package com.dndialidrs.project_api_mobilecomputing.Mahasiswa

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.R

class DetailMahasiswaActivity : AppCompatActivity() {

    private lateinit var namaTextView: TextView
    private lateinit var nimTextView: TextView
    private lateinit var ttlTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mahasiswa)

        // Bind views
        namaTextView = findViewById(R.id.textViewNama)
        nimTextView = findViewById(R.id.textViewNim)
        ttlTextView = findViewById(R.id.textViewTtl)

        // Get data from intent
        val nama = intent.getStringExtra("Nama")
        val nim = intent.getStringExtra("Nim")
        val ttl = intent.getStringExtra("Ttl")

        // Set data to TextViews
        namaTextView.text = nama
        nimTextView.text = nim
        ttlTextView.text = ttl
    }
}
