import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.rajnet.ondrej.final_my_walk.HisModelClass

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 3
        private val DATABASE_NAME = "WalkDatabase"

        private val TABLE_CONTACTS = "WalkTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_DATE = "date"
        private val KEY_STEPS = "steps"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_STEPS + " INTEGER" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    /**
     * Function to insert data
     */
    fun addWalk(his: HisModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, his.name) // EmpModelClass Name
        contentValues.put(KEY_DATE, his.date) // EmpModelClass Email
        contentValues.put(KEY_STEPS, his.steps) // EmpModelClass Steps

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    //Method to read the records from database in form of ArrayList
    fun viewWalk(): ArrayList<HisModelClass> {

        val walkList: ArrayList<HisModelClass> = ArrayList<HisModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var date: String
        var steps: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                steps = cursor.getInt(cursor.getColumnIndex(KEY_STEPS))

                val his = HisModelClass(id = id, name = name, date = date, steps = steps)
                walkList.add(his)

            } while (cursor.moveToNext())
        }
        return walkList
    }

    fun updateWalk(his: HisModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, his.name) // EmpModelClass Name
        contentValues.put(KEY_DATE, his.date) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + his.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    /**
     * Function to delete record
     */
    fun deleteWalk(his: HisModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, his.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + his.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
}