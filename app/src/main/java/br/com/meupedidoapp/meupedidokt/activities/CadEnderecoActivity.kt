package br.com.meupedidoapp.meupedidokt.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.utils.PermissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.toast
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
        LocationRequest.create().apply {
            // Em 30 segundos ele fará 5 tentativas
            // Lecheta Kotlin pág 464
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 6000
            fastestInterval = 3000
            //numUpdates = 5
        }
    }
    private val locationPermissao by lazy {
        PermissionUtils.validate(this, 0, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
    private val inflater by lazy { this.layoutInflater }

    private val _requestGPS: Int = 2
    private val _requestErroPlayServices: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_endereco)
        mapFragment?.getMapAsync(this)

        with(supportActionBar) {
            title = "Novo endereço"
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.background_xml_actionbar, null))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googlemap: GoogleMap?) {
        mGoogleMap = googlemap
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-16.440798, -51.118119), 14.5f))
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        mGoogleMap?.uiSettings?.setAllGesturesEnabled(false)
    }

    override fun onConnected(p0: Bundle?) {
        if (locationPermissao) {
            verificarStatusGPS()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        toast("Conexão interrompida")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, _requestErroPlayServices)
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }
        }
    }

    private fun setMapLocation(l: Location?) {
        if (mGoogleMap != null && l != null) {
            mGoogleMap?.clear()
            val latLng = LatLng(l.latitude, l.longitude)
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            mGoogleMap?.addMarker(MarkerOptions().position(latLng).title("Local atual"))
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(this).apply {
            requestLocationUpdates(locRequest, locationCallback, null)
        }
    }

    private fun stopLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(this).apply {
            removeLocationUpdates(locationCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient?.connect()
    }

    override fun onPause() {
        stopLocationUpdates()
        super.onPause()
    }

    override fun onStop() {
        stopLocationUpdates()
        if (mGoogleApiClient?.isConnected!!)
            mGoogleApiClient?.disconnect()
        super.onStop()
    }

    //https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
    private fun verificarStatusGPS(){
        LocationServices.getSettingsClient(this).checkLocationSettings(LocationSettingsRequest.Builder()
                .setAlwaysShow(true)
                .addLocationRequest(locRequest).build()).apply {
            this.addOnCompleteListener {
                try {
                    val response: LocationSettingsResponse = it.getResult(ApiException::class.java)!!
                    startLocationUpdates()
                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                val resolvable: ResolvableApiException = exception as ResolvableApiException
                                resolvable.startResolutionForResult(this@CadEnderecoActivity, _requestGPS)
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
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val states: LocationSettingsStates? = LocationSettingsStates.fromIntent(intent)
        when (requestCode) {
            _requestGPS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startLocationUpdates()
                    }

                    Activity.RESULT_CANCELED -> // quando o usuário clica em "não, obrigado"
                        verificarStatusGPS()
                }
            }

            _requestErroPlayServices -> {
                if (resultCode == Activity.RESULT_OK) {
                    mGoogleApiClient?.connect()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            when (result) {
                PackageManager.PERMISSION_GRANTED -> {
                    verificarStatusGPS()
                }

                PackageManager.PERMISSION_DENIED -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            AlertDialog.Builder(this).apply {
                                val view = inflater.inflate(R.layout.alert_dialog_permissao, null)
                                view.findViewById<TextView>(R.id.alertdialog_textoMensagem).text = resources.getString(R.string.lorem)
                                this.setView(view)
                                setPositiveButton("Concordo") { _, _ ->
                                    locationPermissao
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
                                setNegativeButton("Cancelar") { _, _ -> finish() }
                                setCancelable(false)
                                create()
                                show()
                            }
                        }
                    } else {
                        AlertDialog.Builder(this).apply {
                            val view = inflater.inflate(R.layout.alert_dialog_permissao, null)
                            view.findViewById<TextView>(R.id.alertdialog_textoMensagem).text = resources.getString(R.string.lorem)
                            this.setView(view)
                            setPositiveButton("Concordo") { _, _ ->
                                locationPermissao
                            }
                            setNegativeButton("Discordo") { _, _ ->
                                finish()
                            }
                            setCancelable(false)
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setMapLocation(location)
            }

            val location = locationResult?.lastLocation
            println("Latitude: ${location?.latitude}")
            println("Longitude: ${location?.longitude}")
        }
    }


}
