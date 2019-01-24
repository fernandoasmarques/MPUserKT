package br.com.meupedidoapp.meupedidokt.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.Endereco
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class EnderecoAdapter(options: FirestoreRecyclerOptions<Endereco>) : FirestoreRecyclerAdapter<Endereco, EnderecoAdapter.EnderecoHolder>(options) {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnderecoHolder {
        return EnderecoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_escolher_endereco, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EnderecoHolder, position: Int, model: Endereco) {
        holder.txtNomeLocal.text = model.nomeLocal.toString()
        holder.txtLogradouro.text = "${model.logradouro}${if (model.numero == "") ", S/Nº" else ", ${model.numero}"}${if (model.complemento == "") "" else ", ${model.complemento}"}, ${model.setor}"
        holder.txrReferencia.text = model.referencia.toString()

        holder.radioButtonEntregarAqui.tag = position
        holder.radioButtonEntregarAqui.isChecked = position == selectedPosition
        holder.radioButtonEntregarAqui.setOnClickListener{
            itemCheckChanged(it)
        }
    }

    private fun itemCheckChanged(v: View) {
        selectedPosition = v.tag as Int
        notifyDataSetChanged()
    }

    class EnderecoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNomeLocal = itemView.findViewById<TextView>(R.id.item_escolher_endereco_txtNomeLocal)!!
        val txtLogradouro = itemView.findViewById<TextView>(R.id.item_escolher_endereco_txtLogradouro)!!
        val txrReferencia = itemView.findViewById<TextView>(R.id.item_escolher_endereco_txtReferencia)!!
        val radioButtonEntregarAqui = itemView.findViewById<RadioButton>(R.id.item_escolher_endereco_radioButtonEntregarAqui)!!
    }
}