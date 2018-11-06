package br.com.meupedidoapp.meupedidokt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.adapters.LojistaAdapter
import br.com.meupedidoapp.meupedidokt.model.Lojista
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedFragment : Fragment() {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var lojistaAdapter: LojistaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_feed, container, false)
        val recyclerview = rootView.findViewById<RecyclerView>(R.id.FragmentFeed_recyclerView)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity?.baseContext)

        val lojistaRef: CollectionReference by lazy { db.collection("Lojista") }
        val lojaAberta: Query by lazy { lojistaRef.orderBy("estaAberto", Query.Direction.DESCENDING).whereEqualTo("usuarioAtivo", true) }

        val options by lazy {
            FirestoreRecyclerOptions.Builder<Lojista>()
                    .setQuery(lojaAberta, Lojista::class.java)
                    .build()
        }

        LojistaAdapter(options).apply {
            this.startListening()
            recyclerview.adapter = this
            lojistaAdapter = this
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        lojistaAdapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        lojistaAdapter.stopListening()
    }
}
