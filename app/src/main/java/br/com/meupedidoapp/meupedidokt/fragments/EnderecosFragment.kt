package br.com.meupedidoapp.meupedidokt.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.activities.CadEnderecoActivity

class EnderecosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_enderecos, container, false)

        val button = rootView.findViewById<Button>(R.id.button2)

        button.setOnClickListener{
            startActivity(Intent(activity, CadEnderecoActivity::class.java))
        }
        return rootView
    }
}
