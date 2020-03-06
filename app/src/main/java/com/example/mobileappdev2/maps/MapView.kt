package com.example.mobileappdev2.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileappdev2.R
import com.example.mobileappdev2.base.BaseView
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapView : BaseView() {

    lateinit var map: GoogleMap
    lateinit var mapView: View

    lateinit var presenter: MapsPresenter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_map, container, false)

        presenter = initPresenter(MapsPresenter(this)) as MapsPresenter

        view.mapView.onCreate(savedInstanceState)
        mapView = view
        //      Sets up the presenter
//      Sets up map
        view.mapView.getMapAsync {
            map = it
            presenter.initMap(map)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.mapView.onSaveInstanceState(outState)
    }

}
