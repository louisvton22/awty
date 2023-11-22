package edu.ischool.lton2.awty

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


const val ALARM_ACTIOM = "edu.ischool.lton2.ALARM"
class MainActivity : AppCompatActivity() {
    var receiver: BroadcastReceiver? = null
    private val MY_PERMISSIONS_REQUEST_SEND_SMS: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS), MY_PERMISSIONS_REQUEST_SEND_SMS)
        }

        val activity = this
        val inputPhone = findViewById<EditText>(R.id.inputPhone)
        val inputDuration = findViewById<EditText>(R.id.inputDuration)
        val button = findViewById<Button>(R.id.btnStart)


        button.text = this.getString(R.string.start)
        button.isEnabled = true
        button.setOnClickListener {

            if (validate(inputPhone.text.toString(), inputDuration.text.toString())) {
                Log.i("MainActivity", "Setting start to stop")
                handleClick(inputPhone.text.toString(), inputDuration.text.toString(), button, activity)
                if (button.text == "Start") {
                    button.text = this.getString(R.string.stop)
                } else {
                    button.text = this.getString(R.string.start)
                }

            } else {
                Toast.makeText(activity, "Invalid parameters", Toast.LENGTH_SHORT).show()
                inputPhone.setText("")
                inputDuration.setText("")
                return@setOnClickListener
            }

        }
    }

    // Check whether the phone number entered is valid
    fun validate(input: String, duration:String): Boolean {
        val phonePattern = "^\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}\$"
        Log.i("MainActivity", " ${Regex(phonePattern).matches(input)}  $input" )
        Log.i("MainActivity", " ${Regex("[1-9]+[0-9]*").matches(duration)} $duration" )
        return Regex(phonePattern).matches(input) && Regex("[1-9]+[0-9]*").matches(duration)
    }


    fun handleClick(phone: String, duration: String, button: Button, activityThis: Context) {
        val duration = duration.toInt()
        var smsManager: SmsManager?
        if (button.text == "Start") {
            if (receiver == null) {
//                val sentPI = PendingIntent.getBroadcast(activityThis,100, Intent("SMS_SENT"),
//                    PendingIntent.FLAG_IMMUTABLE)
//                val deliveredPI = PendingIntent.getBroadcast(activityThis,200, Intent("SMS_DELIVERED"),
//                    PendingIntent.FLAG_IMMUTABLE)
                receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {

                        if (Build.VERSION.SDK_INT > 31) {
                            smsManager = activityThis.getSystemService(SmsManager::class.java)
                        } else {
                            smsManager = SmsManager.getDefault()
                        }
                        Log.i("MainActivity", "${smsManager == null} $phone" )
//                        smsManager?.sendTextMessage("$phone", null, "Are we there yet?", sentPI, deliveredPI)
                        smsManager?.sendTextMessage("+1$phone", null, "Are we there yet?", null, null)
                        Toast.makeText(
                            activityThis,
                            "Message Sent!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.i("MainActivity", "success toast sent")
                    }

                }
                val filter = IntentFilter(ALARM_ACTIOM)
                registerReceiver(receiver, filter)

            }
            // make pending intent
            val intent = Intent(ALARM_ACTIOM)
            val pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            Log.i("MainActivity", "$duration")
            val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + duration * 60000L,
                duration * 60000L,
                pendingIntent
            )
        } else {
            Log.i("MainActivity", "Removing receiver")
            receiver?.let {
                unregisterReceiver(it)
                receiver = null
            }
        }
    }


}
fun formatPhone(phone: String): String {
    return "(${phone.slice(0..2)}) ${phone.slice(3..5)}-${phone.slice(6..9)}"
}