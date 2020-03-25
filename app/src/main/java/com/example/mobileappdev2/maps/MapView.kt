package com.example.mobileappdev2.maps

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.R
import com.example.mobileappdev2.adapter.ImageAdapter
import com.example.mobileappdev2.adapter.ImageAdapterMap
import com.example.mobileappdev2.base.BaseView
import com.example.mobileappdev2.models.Location
import com.example.mobileappdev2.models.PostModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_map.view.*
import java.lang.Exception

class MapView : BaseView(),GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var mapView: View
    lateinit var presenter: MapsPresenter
    var postModel = PostModel()




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
            map.setOnMarkerClickListener(this)
        }
        mapView.mMapToolbar.title = getString(R.string.locations_on_maps)

        (activity as AppCompatActivity?)!!.setSupportActionBar(mapView.mMapToolbar)
        setHasOptionsMenu(true)

        mapView.mMapViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                try {
                    mapView.mMapName.text = postModel.locations[position].title
                    val loc = LatLng(postModel.locations[position].latitude, postModel.locations[position].longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLng(loc))
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

        })

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

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerClick(marker.title)
        return false
    }

    override fun markerLocations(
        location: Location,
        post: PostModel,
        index: Int
    ) {
        mapView.mMapName.text = location.title
        mapView.mMapDescription.text = post.description
        postModel = post
        val viewPager = mapView.findViewById<ViewPager>(R.id.mMapViewPager)
        val adapter = ImageAdapterMap(mapView.context, post.images[index])
        viewPager.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                mapView.findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.return_menu,menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

}
