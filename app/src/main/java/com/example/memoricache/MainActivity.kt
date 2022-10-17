package com.example.memoricache

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var spnKecamatan: Spinner
    private lateinit var spnKelurahan: Spinner
    private lateinit var tvId: TextView
    private lateinit var tvKecamatan: TextView
    private lateinit var tvKelurahan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spnKecamatan = findViewById(R.id.spnKecamatan)
        spnKelurahan = findViewById(R.id.spnKelurahan)
        val btnCheckIn = findViewById<Button>(R.id.btnCheckIn)
        tvId = findViewById(R.id.tvId)
        tvKecamatan = findViewById(R.id.tvKecamatan)
        tvKelurahan = findViewById(R.id.tvKelurahan)

        val nmKecamatan = arrayOf(
            "Bukit Intan", "Gabek", "Gerunggang", "Girimaya",
            "Pangkalbalam", "Rangkui", "Taman Sari"
        )
        spnKecamatan.adapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            nmKecamatan
        )
        spnKecamatan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val nmKelurahan = when(position) {
                    0 -> arrayOf(
                        "Air Itam", "Air Mawar", "Bacang", "Pasir Putih",
                        "Semabung Lama", "Sinar Bulan", "Temberan"
                    )
                    1 -> arrayOf(
                        "Air Salemba", "Gabek Dua", "Gabek Satu",
                        "Jerambah Gantung", "Selindung", "Selindung baru"
                    )
                    2 -> arrayOf(
                        "Air Kepala Tujuh", "Bukit Merapin", "Bukit Sari",
                        "Kacang Pedang", "Taman Bunga", "Tuatunu"
                    )
                    3 -> arrayOf(
                        "Batu Intan", "Bukit Beasar", "Pasar Padi",
                        "Semabung Baru", "Sriwijaya"
                    )
                    4 -> arrayOf(
                        "Ampui", "Ketapang", "Lontong Pancur",
                        "Pasir Garam", "Rejosari"
                    )
                    5 -> arrayOf(
                        "Asam", "Bintang", "Gajah Mada", "Keramat",
                        "Masjid Jamik", "Melintang", "Parit Lalang", "Pintu Air"
                    )
                    else -> arrayOf(
                        "Batin Tikal", "Gedung Nasional", "Kejaksaan",
                        "Opas Indah", "Rawa Bangun"
                    )
                }
                spnKelurahan.adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    nmKelurahan
                )
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        loadCache()

        btnCheckIn.setOnClickListener {
            try {
                class Lokasi(waktu: Long, kecamatan: String, kelurahan: String)

                val file = File(cacheDir, "data_cache")
                val fos = FileOutputStream(file)
                val waktu = System.currentTimeMillis()
                val kec = "${spnKecamatan.selectedItem}"
                val kel = "${spnKelurahan.selectedItem}"
                val lok = "${Lokasi(waktu, kec, kel)}"
                val id = lok.substring(lok.indexOf('@') + 1 until lok.length)
                fos.write("$id,$kec,$kel".toByteArray())
                fos.close()
                loadCache()
            } catch(e: IOException) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadCache() {
        try {
            val file = File(cacheDir, "data_cache")
            val fis = FileInputStream(file)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            var line: String?
            while (run { line = br.readLine(); line } != null) {
                val data = TextUtils.split(line, ";")
                tvId.text = data[0]
                tvKecamatan.text = data[1]
                tvKelurahan.text = data[2]
            }
            fis.close()
        } catch (_: IOException) {}
    }
}