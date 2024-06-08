package com.dndialidrs.project_api_mobilecomputing.Nilai

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dndialidrs.project_api_mobilecomputing.R
import com.google.firebase.database.FirebaseDatabase

class NilaiAdapter(private val context: Context, private var nilaiList: MutableList<Nilai?>) :
    RecyclerView.Adapter<NilaiAdapter.NilaiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NilaiViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.nilai_item, parent, false)
        return NilaiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NilaiViewHolder, position: Int) {
        val nilai = nilaiList[position]
        if (nilai != null) {
            holder.nilaiTextView.text = nilai.nilai
            holder.peringkatTextView.text = nilai.peringkat
            holder.mahasiswaTextView.text = nilai.mahasiswa
            holder.mataKuliahTextView.text = nilai.mataKuliah

            holder.itemView.setOnClickListener {
                val options = arrayOf("Edit", "Delete")
                AlertDialog.Builder(context)
                    .setTitle("Pilih Aksi")
                    .setItems(options) { dialog, which ->
                        when (which) {
                            0 -> startEditActivity(nilai)
                            1 -> showDeleteConfirmationDialog(nilai, position)
                        }
                    }
                    .show()
            }
        } else {
            Log.e("NilaiAdapter", "Nilai object is null at position $position")
        }
    }

    private fun startEditActivity(nilai: Nilai) {
        val editIntent = Intent(context, EditNilaiActivity::class.java).apply {
            putExtra("key", nilai.key)
            putExtra("nilai", nilai.nilai)
            putExtra("peringkat", nilai.peringkat)
            putExtra("mahasiswa", nilai.mahasiswa)
            putExtra("mataKuliah", nilai.mataKuliah)
        }
        Log.d("NilaiAdapter", "Starting EditNilaiActivity")
        if (context is Activity) {
            context.startActivity(editIntent)
        } else {
            Toast.makeText(context, "Cannot start activity", Toast.LENGTH_SHORT).show()
            Log.e("NilaiAdapter", "Context is not an Activity")
        }
    }


    private fun showDeleteConfirmationDialog(nilai: Nilai, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Nilai")
            .setMessage("Apakah Anda yakin ingin menghapus nilai ini?")
            .setPositiveButton("Ya") { dialog, which ->
                deleteNilai(nilai, position)
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteNilai(nilai: Nilai?, position: Int) {
        nilai?.let {
            if (it.key != null) {
                // Hapus dari RecyclerView
                nilaiList.removeAt(position)
                notifyItemRemoved(position)

                // Hapus dari Firebase Database
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("nilai").child(it.key!!)
                reference.removeValue()
                    .addOnSuccessListener {
                        Log.d("NilaiAdapter", "Nilai deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("NilaiAdapter", "Error deleting Nilai from Firebase", e)
                        // Jika gagal menghapus dari Firebase, tambahkan kembali ke RecyclerView
                        nilaiList.add(position, nilai)
                        notifyItemInserted(position)
                        Toast.makeText(context, "Failed to delete Nilai", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Log.e("NilaiAdapter", "Nilai key is null at position $position")
            }
        } ?: Log.e("NilaiAdapter", "Trying to delete null Nilai object at position $position")
    }

    override fun getItemCount(): Int {
        return nilaiList.size
    }

    fun updateNilai(nilaiList: MutableList<Nilai?>) {
        this.nilaiList = nilaiList
        notifyDataSetChanged()
        Log.d("NilaiAdapter", "Nilai list updated")
    }

    class NilaiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nilaiTextView: TextView = itemView.findViewById(R.id.nilaiTextView)
        val peringkatTextView: TextView = itemView.findViewById(R.id.peringkatTextView)
        val mahasiswaTextView: TextView = itemView.findViewById(R.id.mahasiswaTextView)
        val mataKuliahTextView: TextView = itemView.findViewById(R.id.mataKuliahTextView)
    }
}
