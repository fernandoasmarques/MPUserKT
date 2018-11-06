package br.com.meupedidoapp.meupedidokt.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.utils.PermissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import java.lang.ClassCastException

class CadEnderecoActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private var mGoogleMap: GoogleMap? = null

    private val mapFragment: SupportMapFragment? by lazy {
        supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }

    private val mGoogleApiClient: GoogleApiClient? by lazy {
        GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private val locRequest by lazy {
        // Lecheta Kotlin pág 464
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
    }

    private val inflater by lazy { this.layoutInflater }

    private val REQUEST_CHECAR_GPS: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_endereco)
        mapFragment?.getMapAsync(this)
    }

    override fun onConnected(p0: Bundle?) {
        PermissionUtils.validate(this, 0, android.Manifest.permission.ACCESS_FINE_LOCATION)
        verificarStatusGPS()
    }

    override fun onConnectionSuspended(p0: Int) {
        toast("Conexão interrompida")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        toast("Erro ao conectar: $connectionResult")
    }

    override fun onMapReady(googlemap: GoogleMap?) {
        mGoogleMap = googlemap
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        LocationServices.getFusedLocationProviderClient(this).apply {
            this.lastLocation.addOnSuccessListener {
                // permissão aqui
                setMapLocation(it)
            }.addOnFailureListener {
                toast("Não foi possível buscar a localização do GPS")
            }
        }
    }

    private fun setMapLocation(l: Location) {
        if (mGoogleMap != null) {
            val latLng = LatLng(l.latitude, l.longitude)
            val update = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            mGoogleMap?.animateCamera(update)

            val circle = CircleOptions().center(latLng)
            circle.fillColor(Color.RED)
            circle.radius(25.0)
            mGoogleMap?.clear()
            mGoogleMap?.addCircle(circle)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(this).apply {
            requestLocationUpdates(locRequest, locationCallback, Looper.myLooper()) // permissão aqui
        }
    }

    private fun stopLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(this).apply {
            removeLocationUpdates(locationCallback)
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()

    }

    override fun onStop() {
        stopLocationUpdates()
        if (mGoogleApiClient?.isConnected!!)
            mGoogleApiClient?.disconnect()
        super.onStop()
    }

    override fun onPause() {
        stopLocationUpdates()
        if (mGoogleApiClient?.isConnected!!)
            mGoogleApiClient?.disconnect()
        super.onPause()
    }

    private fun verificarStatusGPS() { //https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val builder = LocationSettingsRequest.Builder()
                .setAlwaysShow(true)
                .addLocationRequest(locRequest)
        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)!!
                startLocationUpdates()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvable: ResolvableApiException = exception as ResolvableApiException
                            resolvable.startResolutionForResult(this, REQUEST_CHECAR_GPS)
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        } catch (e: ClassCastException) {
                            e.printStackTrace()
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        toast("Algo errado aconteceu. Contate a equipe Meu Pedido")
                }
            }
        }
    }

    private fun toast(s: String) {
        Toast.makeText(baseContext, s, Toast.LENGTH_LONG).show()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val location = locationResult?.lastLocation
            // Salvar essa localizaçao no Firebase
            println("Latitude: ${location?.latitude}")
            println("Longitude: ${location?.longitude}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //val states: LocationSettingsStates? = LocationSettingsStates.fromIntent(intent)
        when (requestCode) {
            REQUEST_CHECAR_GPS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startLocationUpdates()
                        getLastLocation()
                    }
                    Activity.RESULT_CANCELED ->
                        toast("Não é possível capturar a sua localização sem GPS")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog.Builder(this).apply {
                            val view = inflater.inflate(R.layout.alert_dialog_permissao, null)
                            view.findViewById<TextView>(R.id.alertdialog_textoMensagem).text = resources.getString(R.string.lorem)
                            this.setView(view)
                            setPositiveButton("Concordo") { _, _ ->
                                PermissionUtils.validate(this@CadEnderecoActivity, 0,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                            setNegativeButton("Discordo") { _, _ ->
                                finish()
                            }
                            setCancelable(false)
                            create()
                            show()
                        }
                    } else {
                        AlertDialog.Builder(this).apply {
                            val view = inflater.inflate(R.layout.alert_dialog_permissao, null)
                            view.findViewById<TextView>(R.id.alertdialog_textoTitulo).text = "Ative a localização!"
                            view.findViewById<TextView>(R.id.alertdialog_textoMensagem).text = resources.getString(R.string.alertdialog_permissaoLocalizacaoConfig)
                            this.setView(view)
                            setPositiveButton("Configurações") { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            setNegativeButton("Cancelar"){_,_-> finish()}
                            setCancelable(false)
                            create()
                            show()
                        }
                    }
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            }
        }
    }
}
