package com.dicoding.storyapp.view.maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map : GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getStoriesWithLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true

        setMapStyle()

        viewModel.stories.observe(this) { response ->
            response.story.forEach { story ->
                val latLng = LatLng(story.lat!!, story.lon!!)
                map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )
            }

            response.story.first().let {
                val firstLocation = LatLng(it.lat!!, it.lon!!)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 5f))
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}