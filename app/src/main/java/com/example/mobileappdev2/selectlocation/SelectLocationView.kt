package com.example.mobileappdev2.selectlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.mobileappdev2.R
import com.example.mobileappdev2.base.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_select_location.view.*

class SelectLocationView : BaseView(),GoogleMap.OnMarkerDragListener,GoogleMap.OnMarkerClickListener {

    lateinit var presenter: SelectLocationPresenter
    lateinit var locationView: View
    lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_select_location, container, false)
        locationView = view
        presenter = initPresenter(SelectLocationPresenter(this)) as SelectLocationPresenter
        locationView.mMapLocation.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                locationView.findNavController().navigateUp()
            }
        })


        view.mMapLocation.getMapAsync {
            val postModel = SelectLocationViewArgs.fromBundle(arguments!!).postModel
            val position = SelectLocationViewArgs.fromBundle(arguments!!).position
            map = it
            presenter.initMap(map,postModel,position)
            map.setOnMarkerClickListener(this)
            map.setOnMarkerDragListener(this)
        }

        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        locationView.mMapLocation.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        locationView.mMapLocation.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        locationView.mMapLocation.onPause()
    }

    override fun onResume() {
        super.onResume()
        locationView.mMapLocation.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        locationView.mMapLocation.onSaveInstanceState(outState)
    }

    override fun onMarkerDragEnd(marker: Marker) {
        val location = LatLng(marker.position.latitude,marker.position.longitude)
        presenter.doUpdateLocation(location)
    }

    override fun onMarkerDragStart(p0: Marker?) {}

    override fun onMarkerDrag(p0: Marker?) {}

    override fun onMarkerClick(p0: Marker?): Boolean {
            
        return false
    }
}
