package br.com.meupedidoapp.meupedidokt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.meupedidoapp.meupedidokt.R
import com.google.firebase.firestore.FirebaseFirestore

class FinalizarPedidoActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalizar_pedido)

        val args = intent.extras

    }
}
