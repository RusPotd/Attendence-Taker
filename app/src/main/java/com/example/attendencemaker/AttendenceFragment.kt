package com.example.attendencemaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class AttendenceFragment : Fragment() {

    var subjectId : String = ""
    var myDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = this.arguments
        myDate = bundle!!.getString("date")!!
        subjectId = bundle!!.getString("subId")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v: View = inflater.inflate(R.layout.fragment_attendence, container, false)


        var helper = MyDBHelper(v.context)
        var db = helper.readableDatabase

        var vx = db.rawQuery("SELECT NAME FROM SUBJECT WHERE ID=?", arrayOf(subjectId))
        var temp = "";
        while(vx.moveToNext()) {
            temp = " "+vx.getString(0).toString()
        }

        var zzz = myDate+" "+temp
        v.findViewById<TextView>(R.id.head_info).text = zzz


        var rs = db.rawQuery("SELECT ROLLNO FROM ATTENDENCE WHERE DATE=? AND SUBJECT_ID=?", arrayOf(myDate, subjectId))

        var allRollNos = ""
        while(rs.moveToNext()) {
            allRollNos += rs.getString(0)+"\n"
        }

        v.findViewById<TextView>(R.id.attendence_text).text = allRollNos

        return v
    }

}