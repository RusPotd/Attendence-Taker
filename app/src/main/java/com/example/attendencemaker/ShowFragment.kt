package com.example.attendencemaker

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ShowFragment : Fragment(), TestAdapter.OnItemClickListener,
    TestAdapter.OnItemLongClickListener {

    private var names = ArrayList<TestExample>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_show, container, false)

        names = ArrayList()

        var helper = MyDBHelper(v.context)
        var db = helper.readableDatabase

        var rs = db.rawQuery("SELECT SUBJECT_ID, DATE FROM ATTENDENCE GROUP BY SUBJECT_ID, DATE", null)

        while(rs.moveToNext()) {
            var vx = db.rawQuery("SELECT NAME FROM SUBJECT WHERE ID=?", arrayOf(rs.getString(0)))
            var temp = "";
            while(vx.moveToNext()) {
                temp = " "+vx.getString(0).toString()
            }
            val item = TestExample(rs.getString(1).toString()+temp, rs.getString(0))
            names += item
        }


        var recyclerview = v.findViewById(R.id.recyclerview_all) as RecyclerView
        recyclerview.adapter = TestAdapter(names, this, this)
        recyclerview.layoutManager = LinearLayoutManager(this.context)
        recyclerview.setHasFixedSize(true)

        return v
    }

    override fun onItemClick(position: Int) {
        val item = names[position].text
        //Toast.makeText(context, item.toString(), Toast.LENGTH_LONG).show()
        var x = (item.split(" ").toTypedArray())

        val arguments = Bundle()
        arguments.putString("date", x[0])
        arguments.putString("subId", names[position].id)

        val attendenceFragment = AttendenceFragment()
        attendenceFragment.arguments = arguments

        val fragmentManager: FragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, attendenceFragment::class.java, arguments)
            .setReorderingAllowed(true)
            .addToBackStack("attendence")
            .commit()

    }

    override fun onItemLongClick(position: Int) {
        val item = names[position].text
        var x = (item.split(" ").toTypedArray())

        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to delete Attendence of record: "+names[position].text)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            db.execSQL("DELETE FROM ATTENDENCE WHERE SUBJECT_ID=? AND DATE=?", arrayOf(names[position].id, x[0]))

            Toast.makeText(requireContext(),"Deleted", Toast.LENGTH_SHORT).show()

            val fragment = ShowFragment()
            var transa = requireFragmentManager().beginTransaction()
            transa.replace(R.id.fragmentContainerView2, fragment).commit()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            //Toast.makeText(requireContext(),"Item Not Deleted", Toast.LENGTH_LONG).show()
        }
        builder.show()

    }

}