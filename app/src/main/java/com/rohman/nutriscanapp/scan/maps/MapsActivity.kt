//package com.rohman.nutriscanapp.scan.maps
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
//import com.google.android.libraries.places.api.net.PlacesClient
//import com.rohman.nutriscanapp.R
//import com.rohman.nutriscanapp.databinding.ActivityMapsBinding
//import java.util.*
//
//class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
//
//    private lateinit var mMap: GoogleMap
//    private lateinit var binding: ActivityMapsBinding
//    private lateinit var placesClient: PlacesClient
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private var lastKnownLocation: Location? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMapsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Initialize Places API
//        Places.initialize(applicationContext, getString(R.string.google_maps_key))
//        placesClient = Places.createClient(this)
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        // Initialize FusedLocationProviderClient
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Get current location
//        getLocation()
//    }
//
//    private fun getLocation() {
//        // Check location permission
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Request location permission if not granted
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//            return
//        }
//
//        // Get last known location
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            lastKnownLocation = location
//            lastKnownLocation?.let {
//                Log.d("MapsActivity", "Current Location: $it")
//                updateMap(it)
//            }
//        }
//    }
//
//    private fun updateMap(location: Location) {
//        // Move camera to current location
//        val currentLatLng = LatLng(location.latitude, location.longitude)
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM.toFloat()))
//
//        Log.d("MapsActivity", "Current LatLng: $currentLatLng")
//
//        // Perform nearby search for Nasi Goreng
//        performNearbySearch(currentLatLng)
//    }
//
//    private fun performNearbySearch(location: LatLng) {
//        Log.d("MapsActivity", "Performing Nearby Search...")
//
//        // Define the fields to specify which types of place data to return.
//        val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
//
//        // Check location permission
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Request location permission if not granted
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//            return
//        }
//
//        // Create a FindCurrentPlaceRequest.
//        val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//        // Get the likely places - that is, the businesses and other points of interest near
//        // the device's current location.
//        val placeResponse = placesClient.findCurrentPlace(request)
//
//        placeResponse.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val likelyPlaces = task.result
//                Log.d("MapsActivity", "Number of Nearby Places Found: ${likelyPlaces?.placeLikelihoods?.size}")
//                for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
//                    val place = placeLikelihood.place
//                    // Filter hanya untuk tempat-tempat yang mengandung "Nasi Goreng"
//                    if (place.name?.contains("Nasi Goreng", ignoreCase = true) == true) {
//                        mMap.addMarker(
//                            MarkerOptions()
//                                .position(place.latLng!!)
//                                .title(place.name)
//                        )
//                        Log.d("MapsActivity", "Nearby Nasi Goreng Place: ${place.name} (${place.latLng})")
//                    }
//                }
//            } else {
//                Log.e("MapsActivity", "Nearby Places search failed: ${task.exception}")
//            }
//        }
//    }
//
//
//
//    companion object {
//        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//        private const val DEFAULT_ZOOM = 15
//    }
//}