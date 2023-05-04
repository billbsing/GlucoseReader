package com.connectedlife.glucosereader

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
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
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent

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
            Log.d(TAG, "start scanning")
            Snackbar.make(view, "Start scanning", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()
        }
        if (!this::nfcAdapter.isInitialized) {
            loadNfcAdapter()
        }

    }

    override fun onPause() {
        super.onPause()
        if (this::nfcAdapter.isInitialized) {
            nfcAdapter.disableForegroundDispatch(this)
        }
    }

    public override fun onResume() {
        super.onResume()

        if (!this::nfcAdapter.isInitialized) {
            loadNfcAdapter()
        }
        if (this::nfcAdapter.isInitialized) {
            val techList = arrayOf(
                arrayOf(
                    Ndef::class.java.name
                ), arrayOf(NdefFormatable::class.java.name)
            )
            // techList =  arrayOf(arrayOf<String>())
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, arrayOf<IntentFilter>(), techList )
        }
    }


    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        //do something with tagFromIntent
        Log.d(TAG, "NFC Intent")
        if (tag != null) {
            val tagList: Array<String> = tag.getTechList()
            val searchedTech = NfcV::class.java.name
            Log.d(TAG, "Enter NdefReaderTask: $tag");

            Log.d(TAG, "Tag ID: $tag.getId()");

            val glucoseReader: GlucoseReader = GlucoseReader(tag)
            glucoseReader.readIdentity()
            glucoseReader.readData()
            val glucoseDecoder: GlucoseDecoder = GlucoseDecoder(glucoseReader.data)
            glucoseDecoder.sensorData.log()
            glucoseDecoder.load()

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

    /*
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d(TAG, "NFC Intent")
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
     */

    private fun loadNfcAdapter() {
        nfcAdapter = (this.getSystemService(NFC_SERVICE) as NfcManager).defaultAdapter

        if (nfcAdapter.isEnabled) {
            Log.d(TAG, "nfc is enabled")
        }
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_MUTABLE)

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                addDataType("*/*")    /* Handles all MIME based dispatches.
                                                You should specify only the ones that you need. */
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
        }

        // intentFiltersArray = arrayOf(ndef)

    }
}