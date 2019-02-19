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
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.Endereco
import br.com.meupedidoapp.meupedidokt.utils.AndroidUtils
import br.com.meupedidoapp.meupedidokt.utils.PermissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_cad_endereco.*
import org.jetbrains.anko.*
import java.lang.ClassCastException
import java.util.ArrayList

class CadEnderecoActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private var mGoogleMap: GoogleMap? = null
    private val mapFragment: SupportMapFragment? by lazy {
        supportFragmentManager.findFragmentById(R.id.googleMapMyLocation) as SupportMapFragment
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
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 3000
        }
    }
    private val locationPermissao by lazy {
        PermissionUtils.validate(this, 0, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
    private val inflater by lazy { this.layoutInflater }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var localizacao: GeoPoint

    private val _requestGPS: Int = 2
    private val _requestErroPlayServices: Int = 3
    private var marker: Marker? = null

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

        val nomesLocais = ArrayList<String>()
        nomesLocais.addAll(resources.getStringArray(R.array.nomes_locais))

        ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesLocais).apply {
            this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            CadEnderecoActivity_spinnerNomeLocal.adapter = this
        }

        /*ArrayAdapter.createFromResource(this,
                R.array.nomes_locais, android.R.layout.simple_spinner_item).apply {
            this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            CadEnderecoActivity_spinnerNomeLocal.adapter = this
        }*/

        CadEnderecoActivity_btnCadastrar.setOnClickListener(this)

        CadEnderecoActivity_spinnerNomeLocal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == nomesLocais.size - 1) {
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this@CadEnderecoActivity)
                    val layout = LinearLayout(this@CadEnderecoActivity)
                    layout.layoutParams = (LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT))
                    layout.setPadding(30, 0, 30, 0)

                    val edittext = EditText(this@CadEnderecoActivity)
                    with(edittext) {
                        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        hint = "Nome do local"
                        textSize = 16f
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    }
                    layout.addView(edittext)

                    with(builder) {
                        setTitle("Digite o nome do local")
                        setMessage("Digite o nome do local de entrega do seu pedido.")
                        setView(layout)

                        setNegativeButton(android.R.string.cancel) { _, _ ->
                            CadEnderecoActivity_spinnerNomeLocal.setSelection(0)
                        }

                        setPositiveButton(android.R.string.ok) { _, _ ->
                            nomesLocais.add(0, edittext.text.toString().trim())
                            CadEnderecoActivity_spinnerNomeLocal.setSelection(0)
                        }
                    }
                    val dialog = builder.create()
                    dialog.setCancelable(false)
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    edittext.requestFocus()

                    edittext.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isEnabled = !edittext.text.toString().trim().isEmpty()
                            edittext.error = null
                        }
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })

                    edittext.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isEnabled)
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).performClick()
                            else
                                edittext.error = "Digite o nome do local"
                            true
                        }
                        false
                    }
                }
            }
        }

        CadEnderecoActitivy_checkboxPossuiNumero.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CadEnderecoActivity_txtInputLayoutEdtNumero.isEnabled = false
                CadEnderecoActivity_txtInputLayoutEdtComplemento.requestFocus()
                CadEnderecoActivity_txtInputLayoutNumero.error = null
            } else {
                CadEnderecoActivity_txtInputLayoutEdtNumero.isEnabled = true
                CadEnderecoActivity_txtInputLayoutEdtNumero.requestFocus()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.CadEnderecoActivity_btnCadastrar ->
                submitForm()
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

    private fun setMapLocation(location: Location?) {
        if (mGoogleMap != null && location != null) {
            LatLng(location.latitude, location.longitude).apply {
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(this))
                if (marker == null) {
                    marker = mGoogleMap?.addMarker(MarkerOptions().position(this).title("Local atual"))
                } else
                    marker?.position = this
            }
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

    //https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
    private fun verificarStatusGPS() {
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
                            toast("Localização indisponível")
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
                                    PermissionUtils.validate(this@CadEnderecoActivity, 0, android.Manifest.permission.ACCESS_FINE_LOCATION)
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
                                PermissionUtils.validate(this@CadEnderecoActivity, 0, android.Manifest.permission.ACCESS_FINE_LOCATION)
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

            val location = locationResult.lastLocation
            localizacao = GeoPoint(location.latitude, location.longitude)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    private fun submitForm() {
        if (!validarEndereco()) {
            return
        } else if (!validarBairro()) {
            return
        } else if (!validarNumero()) {
            return
        } else if (!validarReferencia()) {
            return
        } else {
            val nomeLocal = CadEnderecoActivity_spinnerNomeLocal.selectedItem.toString()
            val logradouro = CadEnderecoActivity_txtInputLayoutEdtLogradouro.text.toString().trim()
            val possuiNumero = !CadEnderecoActitivy_checkboxPossuiNumero.isChecked

            val numero = if (!CadEnderecoActitivy_checkboxPossuiNumero.isChecked)
                CadEnderecoActivity_txtInputLayoutEdtNumero.text.toString().trim()
            else
                ""

            val setor = CadEnderecoActivity_txtInputLayoutEdtBairro.text.toString().trim()
            val complemento =
                    if (CadEnderecoActivity_txtInputLayoutEdtComplemento.text.toString().trim().isEmpty()) {
                        ""
                    } else
                        CadEnderecoActivity_txtInputLayoutEdtComplemento.text.toString().trim()

            val referencia = CadEnderecoActivity_txtInputLayoutEdtReferencia.text.toString().trim()
            val endereco = Endereco(nomeLocal, logradouro, possuiNumero, numero, setor, complemento, localizacao, referencia)
            saveOnFirestore(endereco)
        }
    }

    private fun validarEndereco(): Boolean {
        if (CadEnderecoActivity_txtInputLayoutEdtLogradouro.text.toString().trim().isEmpty()) {
            CadEnderecoActivity_txtInputLayoutLogradouro.error = "Digite seu endereço"
            CadEnderecoActivity_txtInputLayoutEdtLogradouro.requestFocus()
            return false
        }
        return true
    }

    private fun validarBairro(): Boolean {
        if (CadEnderecoActivity_txtInputLayoutEdtBairro.text.toString().trim().isEmpty()) {
            CadEnderecoActivity_txtInputLayoutBairro.error = "Digite seu bairro/setor"
            CadEnderecoActivity_txtInputLayoutEdtBairro.requestFocus()
            return false
        }
        return true
    }

    private fun validarNumero(): Boolean {
        if (!CadEnderecoActitivy_checkboxPossuiNumero.isChecked) {
            if (CadEnderecoActivity_txtInputLayoutEdtNumero.text.toString().trim().isEmpty()) {
                CadEnderecoActivity_txtInputLayoutNumero.error = "Digite o número do local"
                CadEnderecoActivity_txtInputLayoutEdtNumero.requestFocus()
                return false
            }
            return true
        }
        return true
    }

    private fun validarReferencia(): Boolean {
        if (CadEnderecoActivity_txtInputLayoutEdtReferencia.text.toString().trim().isEmpty()) {
            CadEnderecoActivity_txtInputLayoutReferencia.error = "Digite uma referência do seu local"
            CadEnderecoActivity_txtInputLayoutEdtReferencia.requestFocus()
            return false
        }
        return true
    }

    private fun saveOnFirestore(endereco: Endereco) {
        if (AndroidUtils.isNetworkAvailable(this)) {
            val loadingDialogMP = LoadingDialogMP(this)
            loadingDialogMP.abrirDialog()
            db.collection("Cliente").document(mAuth.currentUser?.uid!!).collection("Endereco").document().set(endereco).addOnCompleteListener {
                if (it.isSuccessful) {
                    loadingDialogMP.fecharDialog()
                }
            }
        } else {
            toast("Sem conexão com a internet.")
        }
    }
}
