package com.example.flowerboard.activity

import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.flowerboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding:ActivityMainBinding

    //Cancel signal
    private var cancel: CancellationSignal? = null

    //Authentication
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
    get() =
        @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                popup("Authentication Failed: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                popup("Authentication success")
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.finger.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Title of prompt")
                .setSubtitle("Authentication is required")
                .setDescription("This app uses fingerprint protection to keep your data secure")
                .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                    //popup("Authentication cancelled")
                    Toast.makeText(this,"Authentication cancelled", Toast.LENGTH_SHORT).show()
                }).build()
            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
        }

        binding.mapButton.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }
    }

    //Cancel
    private fun getCancellationSignal(): CancellationSignal{
        cancel = CancellationSignal()
        cancel?.setOnCancelListener {
            Toast.makeText(this,"Authentication cancelled", Toast.LENGTH_SHORT).show()
        }
        return cancel as CancellationSignal
    }

    //Popup message
    private fun popup(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}