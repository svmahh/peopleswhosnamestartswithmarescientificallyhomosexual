package com.example.firebaseobject

import android.database.DatabaseUtils
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.Reference

class MainActivity : AppCompatActivity() {

    // no tables or relationships in firebase

    private lateinit var rootNode: FirebaseDatabase

    private lateinit var userReference: DatabaseReference
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        listView = findViewById(R.id.lsOutput)
        // rootnode gets db instance
        // the rootnote IS kinda the db
        // flat json file and we structure it
        rootNode = FirebaseDatabase.getInstance()
        // look in rootnode for users
        // you can look for references in references ( so we can look for something in users if we made it
        userReference = rootNode.getReference("users")


        val dc = DataClass("b Tapes","Black Tape",85,800);

        // at user reference, create a child named key ( id of 85) and send data into it
        // easeier than maunually saying this is the name : , this is the cost etc.
        userReference.child(dc.id.toString()).setValue(dc)

        val list = ArrayList<String>()

        val adapter = ArrayAdapter<String>(this,R.layout.listitems,list)
        listView.adapter=adapter

        // value event listener, that's talking to firebase
        // basically this code goes oh you changed stuff for users? let me go though what you changed and add it
        userReference.addValueEventListener(object : ValueEventListener{
            // everytime db is refreshed its updated

            override fun onDataChange(snapshot: DataSnapshot)
            {
            list.clear()
                // pulls 1 item at a time and adds
                for(snapshot1 in snapshot.children)
                // this is how it updates db
                {
                    val dc2 = snapshot1.getValue(DataClass::class.java)
                    val txt = "Name is ${dc2?.name},Des: ${dc2?.description}"
                    txt?.let {list.add(it)}
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError){

            }
        })

    }

}