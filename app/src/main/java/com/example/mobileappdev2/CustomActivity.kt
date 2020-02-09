package com.example.mobileappdev2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_custom.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class CustomActivity(
    var activity: Activity,
    internal var adapter: RecyclerView.Adapter<*>,
    private var landmarks: MemoryStoreRoom,
    private val listener: CountryListener
) : Dialog(activity),
    View.OnClickListener {

    var dialog: Dialog? = null
    lateinit var customDialog: CustomActivity

    internal var recyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null



    override fun onClick(v: View) {
            when (v.id) {
                R.id.yes -> {
                }
                R.id.no -> dismiss()
                else -> {
                }
            }//Do Something
            dismiss()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)


        recyclerView = recycler_view
        mLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter

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

                val searchedLandmarks = landmarks.searchCountries(characterSearch.toString().toUpperCase())
                recycler_view.adapter = DataAdapter(searchedLandmarks, listener)
                recycler_view.adapter?.notifyDataSetChanged()
            }

        })

        yes.setOnClickListener(this)
        no.setOnClickListener(this)
    }

}
