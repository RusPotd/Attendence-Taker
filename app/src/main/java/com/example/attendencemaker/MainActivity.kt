package com.example.attendencemaker

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var database:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        database = FirebaseFirestore.getInstance()

        database.collection("updates").get().addOnCompleteListener(object:
            OnCompleteListener<QuerySnapshot> {
            override fun onComplete(p0: Task<QuerySnapshot>) {
                if(p0.isSuccessful){
                    for(data in p0.result!!){
                        var avail = data.get("version")
                        try {
                            val pInfo: PackageInfo =
                                applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0)
                            val versionCode = pInfo.versionName
                            if(avail != versionCode){
                                if (!(this@MainActivity as Activity).isFinishing) {
                                    MaterialAlertDialogBuilder(this@MainActivity)
                                        .setTitle("Update Available !!")
                                        .setMessage("Compulsory update avaiable,  please update app to latest version to continue!!!")
                                        .setPositiveButton("update",
                                            DialogInterface.OnClickListener { p0, p1 ->
                                                val browserIntent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(data.get("path").toString())
                                                )
                                                startActivity(browserIntent)
                                                this@MainActivity.finish()
                                                exitProcess(0)
                                            })
                                        .setNegativeButton(
                                            "No",
                                            DialogInterface.OnClickListener { p0, p1 ->
                                                this@MainActivity.finish()
                                                exitProcess(0)
                                            })
                                        .setCancelable(false)
                                        .show()
                                }
                            }
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                        }

                    }
                }
            }

        })

    }
}