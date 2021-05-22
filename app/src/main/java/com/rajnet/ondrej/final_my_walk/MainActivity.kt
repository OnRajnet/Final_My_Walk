package com.rajnet.ondrej.final_my_walk

import DatabaseHandler
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var ps = "900"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_totalMax.setText(ps)

        btnHistory.setOnClickListener{
            val intent = Intent(this, History::class.java )
            this.startActivity(intent)
        }

        btnAdd.setOnClickListener { view ->
            addRecord()
        }

        resetStep()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


    }

    private fun addRecord() {

        val name = etName.text.toString()
        val date = etDateId.text.toString()
        val walk = totalSteps
        walk.toLong()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !date.isEmpty()) {
            val status =
                databaseHandler.addWalk(HisModelClass(0, name, date, totalSteps.toInt()))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etDateId.text.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this, "Zařízení nemá pohybový senzor", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this, stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            tv_stepsTaken.text = ("$currentSteps")
            progress_circular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    private fun resetStep(){
        tv_stepsTaken.setOnClickListener{
            Toast.makeText(this, "Dlouhé podržení pro reset kroků", Toast.LENGTH_SHORT).show()
        }
        tv_stepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            true
        }
    }
}