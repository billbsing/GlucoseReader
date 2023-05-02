package com.connectedlife.glucosereader

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcV
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.connectedlife.glucosereader.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Start scanning", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                val tagList: Array<String> = tag.getTechList()
                val searchedTech = NfcV::class.java.name
                Log.d(TAG, "Enter NdefReaderTask: $tag");

                Log.d(TAG, "Tag ID: $tag.getId()");

                val nfcvTag = NfcV.get(tag)
                nfcvTag.connect()
                nfcvTag.close()
            }

            /*
            val data: Data.Builder = Data.Builder()
            val parcel: Parcel = Parcel.obtain()
            tag.writeToParcel(parcel)
            data.putString("TAG", parcel.toString())
            data.putStringArray("NFC_LIST", tagList)
            val glucoseReaderWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<GlucoseReaderWorker>()
                    .setInputData(data.build())
                    .build()
            WorkManager.getInstance(this).enqueueUniqueWork(glucoseReaderWorkRequest)
            */
        }
    }
}