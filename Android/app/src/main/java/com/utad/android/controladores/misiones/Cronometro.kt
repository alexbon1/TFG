package com.utad.android.controladores.misiones
import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.utad.android.R
import com.utad.android.databinding.ActivityCronometroBinding

class Cronometro : AppCompatActivity() {
    private lateinit var binding: ActivityCronometroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCronometroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countdownTimer: TextView = findViewById(R.id.countdown_timer)
        val customFont = Typeface.createFromAsset(assets, "fonts/newRocket.ttf")
        countdownTimer.typeface = customFont

        val oneHourInMillis: Long = 3600000 // 1 hour in milliseconds
        val countDownTimer = object : CountDownTimer(oneHourInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / 3600000
                val minutes = (millisUntilFinished % 3600000) / 60000
                val seconds = (millisUntilFinished % 60000) / 1000

                countdownTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                countdownTimer.text = "00:00:00"
            }
        }
        countDownTimer.start()

    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}
