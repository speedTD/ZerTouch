package com.assistive.assistivetouch.datasave

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.assistive.assistivetouch.until.contrast
import com.assistive.assistivetouch.model.Appmodel
import com.assistive.assistivetouch.model.Eventmodel

class Database(context: Context?) :
    SQLiteOpenHelper(context, contrast.DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
           db?.execSQL(contrast.DROPTABLE_2)
           db?.execSQL(contrast.DROPTABLE_FAKE)
           db?.execSQL(contrast.DROPTABLE_1)
           db?.execSQL(contrast.CREATE_TABLE_FAKE)
           db?.execSQL(contrast.CREATE_TABLE)
           db?.execSQL(contrast.CREATE_TABLE2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    @Throws(SQLiteConstraintException::class)
    fun insertEvent(model: Eventmodel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        val values = ContentValues()
        values.put(contrast.COLUMNS_1, model.idevent)
        values.put(contrast.COLUMNS_2, model.iconevent)
        values.put(contrast.COLUMNS_3, model.textevent)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(contrast.TABLE_NAME, null, values)
        return true
    }
    @Throws(SQLiteConstraintException::class)
    fun getbyID(id:Int): Eventmodel? {
        // Gets the data repository in write mode
        val db = writableDatabase
        val selectALLQuery = "SELECT * FROM "+ contrast.TABLE_NAME+" WHERE id ='"+id+"'"
        val cursor = db.rawQuery(selectALLQuery, null)
        var eventmodel:Eventmodel?=null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var idevent = cursor.getInt(cursor.getColumnIndex(contrast.COLUMNS_1))
                    var icon = cursor.getInt(cursor.getColumnIndex(contrast.COLUMNS_2))
                    var textevent = cursor.getString(cursor.getColumnIndex(contrast.COLUMNS_3))
                    eventmodel= Eventmodel(idevent,icon,textevent)
                    db.close()
                    Log.d(contrast.TAG,"Đã get idevent của button")
                } while (cursor.moveToNext())
            }
        }
        return eventmodel
    }
    @Throws(SQLiteConstraintException::class)
    fun updateEvent(model: Eventmodel,idbutton:Int): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        val values = ContentValues()
        values.put(contrast.COLUMNS_1, model.idevent)
        values.put(contrast.COLUMNS_2, model.iconevent)
        values.put(contrast.COLUMNS_3, model.textevent)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.update(contrast.TABLE_NAME, values, contrast.ID +" = "+idbutton,null)
        return true
    }
    // bên dưới sử dụng để lưu trữ package app tương tứng với 8 nut trên control

    fun insertApp(model: Appmodel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        val values = ContentValues()
        values.put(contrast.COLUMNS_32, model.packagename)
        values.put(contrast.COLUMNS_33, model.packagename)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(contrast.TABLE_NAME2, null, values)
        return true
    }

    fun getbyIDApp(id:Int): Appmodel? {
        // Gets the data repository in write mode
        val db = writableDatabase
        val selectALLQuery = "SELECT * FROM "+ contrast.TABLE_NAME2+" WHERE id2 ='"+id+"'"
        val cursor = db.rawQuery(selectALLQuery, null)
        var model:Appmodel?=null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var packagename = cursor.getString(cursor.getColumnIndex(contrast.COLUMNS_32))
                    var appname = cursor.getString(cursor.getColumnIndex(contrast.COLUMNS_33))
                    model= Appmodel(packagename,appname)
                    db.close()
                } while (cursor.moveToNext())
            }
        }
        return model
    }

    fun updateApp(model: Appmodel,idbutton:Int): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        val values = ContentValues()
        values.put(contrast.COLUMNS_32, model.packagename)
        values.put(contrast.COLUMNS_33, model.nameapp)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.update(contrast.TABLE_NAME2, values, contrast.ID2 +" = "+idbutton,null)
        return true
    }
    /*
    SAVE FAKE DATA CHANGE EVENT
     */
    //fake data
    @Throws(SQLiteConstraintException::class)
    fun insertEventFake(model: Eventmodel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        val values = ContentValues()
        values.put(contrast.COLUMNS_1FAKE, model.idevent)
        values.put(contrast.COLUMNS_2FAKE, model.iconevent)
        values.put(contrast.COLUMNS_3FAKE, model.textevent)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(contrast.TABLE_NAMEFAKE, null, values)
        return true
    }
    @Throws(SQLiteConstraintException::class)
    fun getbyIDFake(id:Int): Eventmodel? {
        // Gets the data repository in write mode
        val db = writableDatabase
        val selectALLQuery = "SELECT * FROM "+ contrast.TABLE_NAMEFAKE+" WHERE idfake ='"+id+"'"
        val cursor = db.rawQuery(selectALLQuery, null)
        var eventmodel:Eventmodel?=null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var idevent = cursor.getInt(cursor.getColumnIndex(contrast.COLUMNS_1FAKE))
                    var icon = cursor.getInt(cursor.getColumnIndex(contrast.COLUMNS_2FAKE))
                    var textevent = cursor.getString(cursor.getColumnIndex(contrast.COLUMNS_3FAKE))
                    eventmodel= Eventmodel(idevent,icon,textevent)
                    db.close()
                    Log.d(contrast.TAG,"Đã get idevent của button")
                } while (cursor.moveToNext())
            }
        }
        return eventmodel
    }
    @Throws(SQLiteConstraintException::class)
    fun updateEventFake(model: Eventmodel,idbutton:Int): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        val values = ContentValues()
        values.put(contrast.COLUMNS_1FAKE, model.idevent)
        values.put(contrast.COLUMNS_2FAKE, model.iconevent)
        values.put(contrast.COLUMNS_3FAKE, model.textevent)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.update(contrast.TABLE_NAMEFAKE, values, contrast.IDFAKE +" = "+idbutton,null)
        return true
    }

}