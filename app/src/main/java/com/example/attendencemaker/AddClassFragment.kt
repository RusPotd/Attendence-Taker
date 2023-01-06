package com.example.attendencemaker

import android.app.ActionBar
import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddClassFragment : Fragment() {

    var subName : String = ""
    var prefix : String = ""
    var startId : String = ""
    var endId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View =  inflater.inflate(R.layout.fragment_add_class, container, false)

        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        val addBtn : Button = v.findViewById(R.id.createSubButton)
        addBtn.setOnClickListener {
            subName = v.findViewById<EditText>(R.id.inputSubName).text.toString()
            prefix = v.findViewById<EditText>(R.id.inputPrefix).text.toString()
            startId = v.findViewById<EditText>(R.id.inputStartIndex).text.toString()
            endId = v.findViewById<EditText>(R.id.inputEndIndex).text.toString()

            var cv = ContentValues()
            cv.put("NAME", subName)
            cv.put("PREFIX", prefix)
            cv.put("STARTID", startId)
            cv.put("ENDID", endId)
            db.insert("SUBJECT", null, cv)
            Toast.makeText(activity, "Insert Success", Toast.LENGTH_SHORT).show()
        }

        return v
    }

}