package edu.ischool.lton2.awty

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var receiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText = findViewById<EditText>(R.id.inputText)
        val button = findViewById<Button>(R.id.btnStart)
        button.text = R.string.start.toString()
        inputText.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                inputText.removeTextChangedListener(this)
                if (validate(inputText.toString())) {
                    button.text = R.string.stop.toString()
                } else {

                }
                inputText.addTextChangedListener(this)
            }

        })

    }

    // Check whether the phone number entered is valid
    fun validate(input: String): Boolean {
        return false
    }

    fun handleClick() {
        val activityThis = this
        if (receiver == null) {
            receiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Toast.makeText(activityThis, "Hello", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val filter = IntentFilter()
        registerReceiver(receiver, filter)
    }
}