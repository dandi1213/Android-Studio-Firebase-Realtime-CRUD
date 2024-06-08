package com.dndialidrs.project_api_mobilecomputing.Mahasiswa

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

class MahasiswaAdapter(private val context: Context, private var mahasiswaList: MutableList<Mahasiswa?>) :
    RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mahasiswa_item, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        val mahasiswa = mahasiswaList[position]
        if (mahasiswa != null) {
            holder.namaTv.text = mahasiswa.Nama

            holder.itemView.setOnClickListener {
                val options = arrayOf("Detail", "Edit", "Delete")
                AlertDialog.Builder(context)
                    .setTitle("Pilih Aksi")
                    .setItems(options) { dialog, which ->
                        when (which) {
                            0 -> startDetailActivity(mahasiswa)
                            1 -> startEditActivity(mahasiswa)
                            2 -> showDeleteConfirmationDialog(mahasiswa, position)
                        }
                    }
                    .show()
            }
        } else {
            Log.e("MahasiswaAdapter", "Mahasiswa object is null at position $position")
        }
    }

    private fun startDetailActivity(mahasiswa: Mahasiswa) {
        val detailIntent = Intent(context, DetailMahasiswaActivity::class.java).apply {
            putExtra("key", mahasiswa.key)
            putExtra("Nama", mahasiswa.Nama)
            putExtra("Nim", mahasiswa.Nim)
            putExtra("Ttl", mahasiswa.Ttl)
        }
        Log.d("MahasiswaAdapter", "Starting DetailMahasiswaActivity")
        if (context is Activity) {
            context.startActivity(detailIntent)
        } else {
            Toast.makeText(context, "Cannot start activity", Toast.LENGTH_SHORT).show()
            Log.e("MahasiswaAdapter", "Context is not an Activity")
        }
    }

    private fun startEditActivity(mahasiswa: Mahasiswa) {
        val editIntent = Intent(context, EditMahasiswaActivity::class.java).apply {
            putExtra("key", mahasiswa.key)
            putExtra("Nama", mahasiswa.Nama)
            putExtra("Nim", mahasiswa.Nim)
            putExtra("Ttl", mahasiswa.Ttl)
        }
        Log.d("MahasiswaAdapter", "Starting EditMahasiswaActivity")
        if (context is Activity) {
            context.startActivity(editIntent)
        } else {
            Toast.makeText(context, "Cannot start activity", Toast.LENGTH_SHORT).show()
            Log.e("MahasiswaAdapter", "Context is not an Activity")
        }
    }

    private fun showDeleteConfirmationDialog(mahasiswa: Mahasiswa, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Mahasiswa")
            .setMessage("Apakah Anda yakin ingin menghapus mahasiswa ini?")
            .setPositiveButton("Ya") { dialog, which ->
                deleteMahasiswa(mahasiswa, position)
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteMahasiswa(mahasiswa: Mahasiswa?, position: Int) {
        mahasiswa?.let {
            if (it.key != null) {
                // Hapus dari RecyclerView
                mahasiswaList.removeAt(position)
                notifyItemRemoved(position)

                // Hapus dari Firebase Database
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("mahasiswa").child(it.key!!)
                reference.removeValue()
                    .addOnSuccessListener {
                        Log.d("MahasiswaAdapter", "Mahasiswa deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("MahasiswaAdapter", "Error deleting Mahasiswa from Firebase", e)
                        // Jika gagal menghapus dari Firebase, tambahkan kembali ke RecyclerView
                        mahasiswaList.add(position, mahasiswa)
                        notifyItemInserted(position)
                        Toast.makeText(context, "Failed to delete Mahasiswa", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Log.e("MahasiswaAdapter", "Mahasiswa key is null at position $position")
            }
        } ?: Log.e("MahasiswaAdapter", "Trying to delete null Mahasiswa object at position $position")
    }


    override fun getItemCount(): Int {
        return mahasiswaList.size
    }

    fun updateMahasiswa(mahasiswaList: MutableList<Mahasiswa?>) {
        this.mahasiswaList = mahasiswaList
        notifyDataSetChanged()
        Log.d("MahasiswaAdapter", "Mahasiswa list updated")
    }

    class MahasiswaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTv: TextView = itemView.findViewById(R.id.nama_tv)
    }
}
