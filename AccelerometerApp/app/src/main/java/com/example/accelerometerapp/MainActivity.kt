    package com.example.accelerometerapp

    import android.hardware.Sensor
    import android.hardware.SensorEvent
    import android.hardware.SensorEventListener
    import android.hardware.SensorManager
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.ImageView
    import android.widget.TextView

    class MainActivity : AppCompatActivity(), SensorEventListener {

        private lateinit var sensorManager: SensorManager
        private var accelerometer: Sensor? = null
        private lateinit var tvAccelerometer: TextView
        private lateinit var ball: ImageView

        private var xPos = 0f
        private var yPos = 0f

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Ánh xạ view
            tvAccelerometer = findViewById(R.id.tv_accelerometer)
            ball = findViewById(R.id.ball)

            // Lấy SensorManager và cảm biến gia tốc
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        override fun onResume() {
            super.onResume()
            accelerometer?.also { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
            }
        }

        override fun onPause() {
            super.onPause()
            sensorManager.unregisterListener(this)
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                // Cập nhật TextView
                tvAccelerometer.text = "Gia tốc: x=$x, y=$y, z=$z"

                // Cập nhật vị trí ImageView
                xPos -= x * 5
                yPos += y * 5

                ball.translationX = xPos
                ball.translationY = yPos
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }
