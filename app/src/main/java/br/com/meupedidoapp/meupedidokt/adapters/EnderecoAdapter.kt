package br.com.meupedidoapp.meupedidokt.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.activities.FinalizarPedidoActivity
import br.com.meupedidoapp.meupedidokt.model.Endereco
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class EnderecoAdapter(options: FirestoreRecyclerOptions<Endereco>) : FirestoreRecyclerAdapter<Endereco, EnderecoAdapter.EnderecoHolder>(options) {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnderecoHolder {
        return EnderecoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_escolher_endereco, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EnderecoHolder, position: Int, model: Endereco) {
        holder.txtNomeLocal.text = model.nomeLocal.toString()
        holder.txtLogradouro.text = "${model.logradouro}${if (model.numero == "") ", S/Nº" else ", ${model.numero}"}${if (model.complemento == "") "" else ", ${model.complemento}"}, ${model.setor}"
        holder.txrReferencia.text = model.referencia.toString()
        holder.radioButtonEntregarAqui.isChecked = position == selectedPosition

        if (holder.radioButtonEntregarAqui.isChecked) {
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(android.R.color.transparent, Color.parseColor("#FFC107"))).apply {
                holder.backgroundGradient.background = this
            }
            holder.txtNomeLocal.textColor = Color.parseColor("#FFC107")

            FinalizarPedidoActivity.pedidoRealizado.enderecoEntrega = model
            println("O logradouro é: ${FinalizarPedidoActivity.pedidoRealizado.enderecoEntrega?.logradouro}")
        } else {
            holder.backgroundGradient.backgroundColor = android.R.color.transparent
            holder.txtNomeLocal.textColor = Color.parseColor("#99000000")
        }
    }

    inner class EnderecoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNomeLocal = itemView.findViewById<TextView>(R.id.item_escolher_endereco_txtNomeLocal)!!
        val txtLogradouro = itemView.findViewById<TextView>(R.id.item_escolher_endereco_txtLogradouro)!!
        val txrReferencia = itemView.findViewById<TextView>(R.id.item_escolher_endereco_txtReferencia)!!
        val radioButtonEntregarAqui = itemView.findViewById<RadioButton>(R.id.item_escolher_endereco_radioButtonEntregarAqui)!!
        val backgroundRadioButton = itemView.findViewById<RelativeLayout>(R.id.item_escolher_endereco_backgroundRadioButton)!!
        val backgroundGradient = itemView.findViewById<View>(R.id.item_escolher_endereco_gradient)!!

        init {
            radioButtonEntregarAqui.setOnClickListener {
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            }

            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}