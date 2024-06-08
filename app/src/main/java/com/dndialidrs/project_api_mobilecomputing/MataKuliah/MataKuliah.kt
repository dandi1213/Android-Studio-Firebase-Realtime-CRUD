package com.dndialidrs.project_api_mobilecomputing.MataKuliah

data class MataKuliah(
    val key: String? = "",
    val mataKuliah: String? = "", // Mengubah 'nama' menjadi 'mataKuliah'
    val sks: String? = "", // Menambahkan atribut 'sks'
    val dosen: String? = ""
)
