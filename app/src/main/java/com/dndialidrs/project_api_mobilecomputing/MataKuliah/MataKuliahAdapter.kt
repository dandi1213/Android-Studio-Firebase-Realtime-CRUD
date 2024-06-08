package com.dndialidrs.project_api_mobilecomputing.MataKuliah

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

class MataKuliahAdapter(private val context: Context, private var mataKuliahList: MutableList<MataKuliah?>) :
    RecyclerView.Adapter<MataKuliahAdapter.MataKuliahViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MataKuliahViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mata_kuliah_item, parent, false)
        return MataKuliahViewHolder(view)
    }

    override fun onBindViewHolder(holder: MataKuliahViewHolder, position: Int) {
        val mataKuliah = mataKuliahList[position]
        if (mataKuliah != null) {
            holder.mataKuliahTv.text = mataKuliah.mataKuliah

            holder.itemView.setOnClickListener {
                val options = arrayOf("Detail", "Edit", "Delete")
                AlertDialog.Builder(context)
                    .setTitle("Pilih Aksi")
                    .setItems(options) { dialog, which ->
                        when (which) {
                            0 -> startDetailActivity(mataKuliah)
                            1 -> startEditActivity(mataKuliah)
                            2 -> showDeleteConfirmationDialog(mataKuliah, position)
                        }
                    }
                    .show()
            }
        } else {
            Log.e("MataKuliahAdapter", "MataKuliah object is null at position $position")
        }
    }

    private fun startDetailActivity(mataKuliah: MataKuliah) {
        val detailIntent = Intent(context, DetailMataKuliahActivity::class.java).apply {
            putExtra("key", mataKuliah.key)
            putExtra("mataKuliah", mataKuliah.mataKuliah)
            putExtra("sks", mataKuliah.sks)
            putExtra("dosen", mataKuliah.dosen)
        }
        Log.d("MataKuliahAdapter", "Starting DetailMataKuliahActivity")
        if (context is Activity) {
            context.startActivity(detailIntent)
        } else {
            Toast.makeText(context, "Cannot start activity", Toast.LENGTH_SHORT).show()
            Log.e("MataKuliahAdapter", "Context is not an Activity")
        }
    }

    private fun startEditActivity(mataKuliah: MataKuliah) {
        val editIntent = Intent(context, EditMataKuliahActivity::class.java).apply {
            putExtra("key", mataKuliah.key)
            putExtra("mataKuliah", mataKuliah.mataKuliah)
            putExtra("sks", mataKuliah.sks)
            putExtra("dosen", mataKuliah.dosen)
        }
        Log.d("MataKuliahAdapter", "Starting EditMataKuliahActivity")
        if (context is Activity) {
            context.startActivity(editIntent)
        } else {
            Toast.makeText(context, "Cannot start activity", Toast.LENGTH_SHORT).show()
            Log.e("MataKuliahAdapter", "Context is not an Activity")
        }
    }

    private fun showDeleteConfirmationDialog(mataKuliah: MataKuliah, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Mata Kuliah")
            .setMessage("Apakah Anda yakin ingin menghapus mata kuliah ini?")
            .setPositiveButton("Ya") { dialog, which ->
                deleteMataKuliah(mataKuliah, position)
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteMataKuliah(mataKuliah: MataKuliah?, position: Int) {
        mataKuliah?.let {
            if (it.key != null) {
                // Hapus dari RecyclerView
                mataKuliahList.removeAt(position)
                notifyItemRemoved(position)

                // Hapus dari Firebase Database
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("mata_kuliah").child(it.key!!)
                reference.removeValue()
                    .addOnSuccessListener {
                        Log.d("MataKuliahAdapter", "Mata Kuliah deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("MataKuliahAdapter", "Error deleting Mata Kuliah from Firebase", e)
                        // Jika gagal menghapus dari Firebase, tambahkan kembali ke RecyclerView
                        mataKuliahList.add(position, mataKuliah)
                        notifyItemInserted(position)
                        Toast.makeText(context, "Failed to delete Mata Kuliah", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Log.e("MataKuliahAdapter", "Mata Kuliah key is null at position $position")
            }
        } ?: Log.e("MataKuliahAdapter", "Trying to delete null Mata Kuliah object at position $position")
    }

    override fun getItemCount(): Int {
        return mataKuliahList.size
    }

    fun updateMataKuliah(mataKuliahList: MutableList<MataKuliah?>) {
        this.mataKuliahList = mataKuliahList
        notifyDataSetChanged()
        Log.d("MataKuliahAdapter", "Mata Kuliah list updated")
    }

    class MataKuliahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mataKuliahTv: TextView = itemView.findViewById(R.id.nameTextView)
    }
}
