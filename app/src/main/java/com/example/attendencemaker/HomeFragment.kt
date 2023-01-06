package com.example.attendencemaker

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(), TestAdapter.OnItemClickListener,
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
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)

        names = ArrayList()

        val showAll : Button = v.findViewById<Button>(R.id.showAll)
        showAll.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, ShowFragment::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("show")
                .commit()
        }

        val addNewBtn : Button = v.findViewById<Button>(R.id.addNew)
        addNewBtn.setOnClickListener {

            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, AddClassFragment::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("add")
                .commit()
        }

        var helper = MyDBHelper(v.context)
        var db = helper.readableDatabase

        var rs = db.rawQuery("SELECT * FROM SUBJECT", null)

        while(rs.moveToNext()) {
            val item = TestExample(rs.getString(1), rs.getString(0))
            names += item
        }


        var recyclerview = v.findViewById(R.id.recyclerview) as RecyclerView
        recyclerview.adapter = TestAdapter(names, this, this)
        recyclerview.layoutManager = LinearLayoutManager(this.context)
        recyclerview.setHasFixedSize(true)

        //Toast.makeText(context, names.toString(), Toast.LENGTH_LONG).show()

        return v
    }


    override fun onItemClick(position: Int) {
        val clickedItem = names[position].id

        val arguments = Bundle()
        arguments.putInt("id", clickedItem.toInt())

        val mainFragment = MainFragment()
        mainFragment.arguments = arguments

        //Toast.makeText(context, arguments.getInt("id").toString(), Toast.LENGTH_LONG).show()

        val fragmentManager: FragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, mainFragment::class.java, arguments)
            .setReorderingAllowed(true)
            .addToBackStack("main")
            .commit()
    }

    override fun onItemLongClick(position: Int) {
        var helper = MyDBHelper(requireContext())
        var db = helper.readableDatabase

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to delete Subject and associated Records of: "+names[position].text)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            db.execSQL("DELETE FROM ATTENDENCE WHERE SUBJECT_ID=?", arrayOf(names[position].id))
            db.execSQL("DELETE FROM SUBJECT WHERE ID=?", arrayOf(names[position].id))

            Toast.makeText(requireContext(),"Deleted", Toast.LENGTH_SHORT).show()

            val fragment = HomeFragment()
            var transa = requireFragmentManager().beginTransaction()
            transa.replace(R.id.fragmentContainerView2, fragment).commit()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            //Toast.makeText(requireContext(),"Item Not Deleted", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }
}