package com.riswan.pockemonandroid

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        checkPermission()
        loadPokemon()
    }

    var accessLocation = 124

    fun checkPermission () {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), accessLocation)
                return
            }
        }
        GetUserLocation()

    }

    fun GetUserLocation () {
        Toast.makeText(this, "Pengguna mengakses di ", Toast.LENGTH_LONG).show()

        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3,3f,myLocation)
        var myThread = MyThread()
        myThread.start()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            accessLocation-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, "Kami tidak mengakses lokasi ", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

    var location:Location?=null

    inner class MyLocationListener:LocationListener {


        constructor() {
            location = Location("Start")
            location!!.latitude = 0.0
            location!!.longitude = 0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location = p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    var oldLocation:Location?=null
    inner class MyThread: Thread {
        constructor(): super() {
           oldLocation = Location("Start")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        override fun run () {
            while (true) {
                try {

                    if (oldLocation!!.distanceTo(location)== 0f) {
                        continue
                    }

                    oldLocation = location
                    runOnUiThread {

                        // show user

                        mMap!!.clear()
                    val depok = LatLng(location!!.latitude, location!!.longitude)
                        println(location!!.latitude)
                    mMap.addMarker(MarkerOptions().position(depok).title("Nopal")
                        .snippet("Disini aku berada eheee")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.a)))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(depok, 8f))

                        // show pokemons

                        for (i in 0..listPockemons.size - 1) {
                            var newPockemon = listPockemons[i]
                            if (newPockemon.isCatch === false) {
                                val pokemonLoc = LatLng(newPockemon.lat!!, newPockemon.log!!)
                                println(location!!.latitude)
                                mMap.addMarker(MarkerOptions().position(pokemonLoc).title(newPockemon.name)
                                    .snippet(newPockemon.des)
                                    .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))

                            }
                        }
                    }
                    Thread.sleep(1000)
                } catch (ex: Exception) {}
            }
        }
    }

    var listPockemons = ArrayList<Pokemon>()
    fun loadPokemon () {
        listPockemons.add(Pokemon("gulbasur","gulbasur dari jakarta", R.drawable.b,55.0, -6.439305, 106.904217))
        listPockemons.add(Pokemon("pikacu","gulbasur dari bogor", R.drawable.c,56.0, -6.439305, 107.804228))
        listPockemons.add(Pokemon("dragon","gulbasur dari tanggerang", R.drawable.d,85.0, -6.439305, 108.804239))
    }
}
