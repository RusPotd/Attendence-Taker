package com.example.attendencemaker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "STUDENTSDB", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE SUBJECT(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(50), PREFIX VARCHAR(15), STARTID INT, ENDID INT)")
        db?.execSQL("CREATE TABLE ATTENDENCE(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, SUBJECT_ID INT, ROLLNO INT, DATE DATE, FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT(ID))")

        //db?.execSQL("INSERT INTO SUBJECT VALUES(1, 'TEST', 'SCT', 64, 104)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}