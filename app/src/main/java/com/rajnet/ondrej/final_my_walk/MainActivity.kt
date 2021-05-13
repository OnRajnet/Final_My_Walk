package com.rajnet.ondrej.final_my_walk

import DatabaseHandler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHistory.setOnClickListener{
            val intent = Intent(this, History::class.java )
            this.startActivity(intent)
        }

        btnAdd.setOnClickListener { view ->
            addRecord()
        }


    }

    //Method for saving the employee records in database
    private fun addRecord() {

        val name = etName.text.toString()
        val date = etDateId.text.toString()
        val walk = 500
        walk.toLong()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !date.isEmpty()) {
            val status =
                databaseHandler.addWalk(HisModelClass(0, name, date, walk))
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
}