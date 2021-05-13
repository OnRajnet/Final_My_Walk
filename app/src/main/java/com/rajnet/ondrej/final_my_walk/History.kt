package com.rajnet.ondrej.final_my_walk

import DatabaseHandler
import ItemAdapter
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.dialog_update.*

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupListofDataIntoRecyclerView()
    }

    private fun getItemsList(): ArrayList<HisModelClass> {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val hisList: ArrayList<HisModelClass> = databaseHandler.viewWalk()

        return hisList
    }

    private fun setupListofDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }
    /**
     * Method is used to show the custom update dialog.
     */
    fun updateRecordDialog(hisModelClass: HisModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(hisModelClass.name)
        updateDialog.etUpdateDateId.setText(hisModelClass.date)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener {

            val name = updateDialog.etUpdateName.text.toString()
            val date = updateDialog.etUpdateDateId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !date.isEmpty()) {
                val status =
                    databaseHandler.updateWalk(HisModelClass(hisModelClass.id, name, date, hisModelClass.steps))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    /**
     * Method is used to show the delete alert dialog.
     */
    fun deleteRecordAlertDialog(hisModelClass: HisModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${hisModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteWalk(HisModelClass(hisModelClass.id, "", "", hisModelClass.steps))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

}