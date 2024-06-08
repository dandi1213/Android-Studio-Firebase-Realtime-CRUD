package com.dndialidrs.project_api_mobilecomputing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dndialidrs.project_api_mobilecomputing.Mahasiswa.MahasiswaActivity
import com.dndialidrs.project_api_mobilecomputing.MataKuliah.MataKuliahActivity
import com.dndialidrs.project_api_mobilecomputing.Nilai.NilaiActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mahasiswaButton: Button
    private lateinit var mataKuliahButton: Button
    private lateinit var nilaiButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mahasiswaButton = findViewById(R.id.mahasiswaButton)
        mataKuliahButton = findViewById(R.id.mataKuliahButton)
        nilaiButton = findViewById(R.id.nilaiButton)

        mahasiswaButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MahasiswaActivity::class.java)
            startActivity(intent)
        }

        mataKuliahButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MataKuliahActivity::class.java)
            startActivity(intent)
        }

        nilaiButton.setOnClickListener {
            val intent = Intent(this@MainActivity, NilaiActivity::class.java)
            startActivity(intent)
        }
    }
}
