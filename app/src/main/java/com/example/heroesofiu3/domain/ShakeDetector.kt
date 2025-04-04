package com.example.heroesofiu3.domain

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val context: Context,
    private val onShake: () -> Unit
) : SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var isListening = false

    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val shakeThreshold = 12.0f // Порог для определения тряски

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun startListening() {
        if (!isListening && accelerometer != null) {
            sensorManager?.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            isListening = true
        }
    }

    fun stopListening() {
        if (isListening) {
            sensorManager?.unregisterListener(this)
            isListening = false
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // Получаем данные с акселерометра
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration
            
            // Вычисляем текущее ускорение
            currentAcceleration = sqrt(x * x + y * y + z * z)
            
            // Рассчитываем дельту
            val delta = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Если превышен порог тряски, вызываем колбэк
            if (acceleration > shakeThreshold) {
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не требуется обработка
    }
} 