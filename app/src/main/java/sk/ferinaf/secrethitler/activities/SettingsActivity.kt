package sk.ferinaf.secrethitler.activities

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.SettingsCategory
import sk.ferinaf.secrethitler.common.getSettingsFor
import java.net.HttpURLConnection
import java.net.URL

class SettingsActivity : BaseActivity() {
    override var fullScreen = false

    override var statusBarColor: Int? = R.color.secretGray
    override var navigationColor: Int? = R.color.secretGray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sp = getSharedPreferences("Settings", 0)

        val vibrations = getSettingsFor(SettingsCategory.VIBRATIONS)
        settings_vibration_settingsItem?.switch?.isChecked = vibrations
        settings_vibration_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("vibrations", isChecked)
            editor.apply()
        }

        val notHitler = getSettingsFor(SettingsCategory.NOT_HITLER)
        settings_notHitler_settingsItem?.switch?.isChecked = notHitler
        settings_notHitler_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("not_hitler", isChecked)
            editor.apply()
        }

        val fastVote = getSettingsFor(SettingsCategory.FAST_VOTING)
        settings_fastVote_settingsItem?.switch?.isChecked = fastVote
        settings_fastVote_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("fast_vote", isChecked)
            editor.apply()
        }

        val pileSize = getSettingsFor(SettingsCategory.PILE_SIZE)
        settings_pileSize_settingsItem?.switch?.isChecked = pileSize
        settings_pileSize_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("pile_size", isChecked)
            editor.apply()
        }

        checkUpdates_button?.setOnClickListener {
            Toast.makeText(this, "CHECKING UPDATES", Toast.LENGTH_SHORT).show()
            checkUpdates()
            checkUpdates_button?.isEnabled = false
        }
    }

    private fun checkUpdates() {

        val offlineVersion = resources.openRawResource(R.raw.version).bufferedReader().use { it.readLine() }.toFloatOrNull() ?: 0f

        val requestUrl = URL("https://raw.githubusercontent.com/michalgeci/SecretHitler/master/app/src/main/res/raw/version.txt")
        val connection = requestUrl.openConnection() as HttpURLConnection
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val onlineVersion = connection.inputStream.bufferedReader().use { it.readText() }.toFloatOrNull() ?: 0f

                if (onlineVersion > offlineVersion ) {
                    // needs update
                    runOnUiThread {
                        showNeedsUpdateDialog(onlineVersion.toString())
                        checkUpdates_button?.isEnabled = true
                    }
                } else {
                    // is current
                    runOnUiThread {
                        showIsCurrentDialog()
                        checkUpdates_button?.isEnabled = true
                    }
                }
            } finally {
                connection.disconnect()
                runOnUiThread {
                    checkUpdates_button?.isEnabled = true
                }
            }
        }
    }

    private fun showIsCurrentDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("VERSION STATUS").setMessage("Your version is current.")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val dialog = builder.create()
        dialog.show()
    }

    private fun showNeedsUpdateDialog(version: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("VERSION STATUS").setMessage("New version is available.")
        builder.setPositiveButton("DOWNLOAD") { dialog, _ ->
            startDownloading(version)
            dialog.dismiss()
        }

        builder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun startDownloading(version: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        val uri = Uri.parse("https://github.com/michalgeci/SecretHitler/releases/download/v$version/SetcretHitler$version.apk")
        val request = DownloadManager.Request(uri)
        request.setTitle("Secret Hitler")
        request.setDescription("Downloading new version")
        request.setMimeType("application/vnd.android.package-archive")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/SetcretHitler$version.apk")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager?.enqueue(request)
    }
}