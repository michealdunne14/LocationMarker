package com.example.mobileappdev2.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.CountryListener
import com.example.mobileappdev2.adapter.DataAdapter
import com.example.mobileappdev2.room.MemoryStoreRoom
import kotlinx.android.synthetic.main.activity_custom.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CustomDialog(
    var activity: Activity,
    internal var adapter: RecyclerView.Adapter<*>,
    private var landmarks: MemoryStoreRoom,
    private val listener: CountryListener
) : Dialog(activity),AnkoLogger{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)
        info { "Custom Dialog has started" }

//      Lists all countries in a recycler view
        val mLayoutManager = LinearLayoutManager(activity)
        mDialogRecyclerView.layoutManager = mLayoutManager
        mDialogRecyclerView.adapter = adapter

        mDialogSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("DefaultLocale")
            override fun onTextChanged(
                characterSearch: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                info { "Text Changed to $characterSearch"}
                val searchedLandmarks = landmarks.searchCountries(characterSearch.toString().toUpperCase())
                mDialogRecyclerView.adapter =
                    DataAdapter(
                        searchedLandmarks,
                        listener
                    )
                mDialogRecyclerView.adapter?.notifyDataSetChanged()
            }

        })
    }

}
