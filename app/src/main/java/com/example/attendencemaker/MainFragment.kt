package com.example.attendencemaker

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    var value = 0;
    var cal = Calendar.getInstance()
    var selectedDate: String? = null
    var listViewData : ListView? = null
    var adapter : ArrayAdapter<String>? = null
    var myArray: ArrayList<String> = arrayListOf()

    var startid = 0
    var endid = 0
    var prefix : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        value = bundle!!.getInt("id")

        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        var rs = db.rawQuery("SELECT * FROM SUBJECT WHERE ID=?", arrayOf(value.toString()))

        while(rs.moveToNext()) {
            prefix = rs.getString(2)
            startid = rs.getString(3).toInt()
            endid = rs.getString(4).toInt()
        }
        //Toast.makeText(context, value.toString()+" "+prefix+" "+startid+" "+endid, Toast.LENGTH_LONG).show()

        for(i in startid..endid){
            myArray.add(prefix+"-"+i)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_main, container, false)

        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        listViewData = v.findViewById(R.id.roll_calls)
        adapter = ArrayAdapter<String>(v.context, android.R.layout.simple_list_item_multiple_choice, myArray)
        listViewData!!.adapter = adapter

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(v)
            }
        }

        v.findViewById<Button>(R.id.saveBtn).setOnClickListener{
            var z = 0;
            for(i in 0..listViewData!!.count){
                if((listViewData!!.isItemChecked(i)) && (selectedDate != null)){
                    var x = (listViewData!!.getItemAtPosition(i).toString().split("-").toTypedArray())[1]
                    var cv = ContentValues()
                    cv.put("SUBJECT_ID", value)
                    cv.put("ROLLNO", x)
                    cv.put("DATE", selectedDate)
                    db.insert("ATTENDENCE", null, cv)
                    z+=1
                }
            }
            if(z == 0){
                Toast.makeText(v.context, "Select Date or atleast one Check Item", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(v.context, "Values inserted "+z.toString(), Toast.LENGTH_LONG).show()
            }
        }

        v.findViewById<Button>(R.id.datePicker)!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(v.context,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        return v
    }

    private fun updateDateInView(v: View) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        v.findViewById<Button>(R.id.datePicker).text = sdf.format(cal.getTime())
        selectedDate = sdf.format(cal.getTime())
    }


}