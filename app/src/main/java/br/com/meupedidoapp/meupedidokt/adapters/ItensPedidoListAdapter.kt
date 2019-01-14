package br.com.meupedidoapp.meupedidokt.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.util.ArrayList
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.model.ItensPedido
import br.com.meupedidoapp.meupedidokt.model.Tema
import java.math.BigDecimal

class ItensPedidoListAdapter(private val context: Context, private val itensPedidos: ArrayList<ItensPedido>,
                             private val tema: Tema) : BaseAdapter() {

    override fun getCount(): Int {
        return itensPedidos.size
    }

    override fun getItem(position: Int): Any {
        return itensPedidos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val itensPedido = itensPedidos[position]
        val holder: ViewHolder

        val cores = intArrayOf(android.R.color.transparent, Color.parseColor(tema.corPrincipal))
        val gd = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, cores)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_itens_selecionados, LinearLayout(context))
            holder = ViewHolder()
            holder.txtItemProdutoQtde = convertView.findViewById<View>(R.id.txtItemProdutoQtde) as TextView
            holder.txtItemProdutoNome = convertView.findViewById<View>(R.id.txtItemProdutoNome) as TextView
            holder.txtItemProdutoPreco = convertView.findViewById<View>(R.id.txtItemProdutoPreco) as TextView
            holder.txtItemProdutoSubPreco = convertView.findViewById<View>(R.id.txtItemProdutoSubPreco) as TextView
            holder.backgroundTxtItemProdutoQtde = convertView.findViewById<View>(R.id.backgroundTxtItemProdutoQtde) as FrameLayout
            holder.backgroundTxtItemProdutoQtde.background = gd
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.txtItemProdutoNome.text = itensPedido.produto.nome
        holder.txtItemProdutoQtde.text = itensPedido.quantidade.toString()
        holder.txtItemProdutoPreco.text = itensPedido.precoTotal.toInt().toString()

        if (itensPedido.precoTotal.toDouble() - itensPedido.precoTotal.toInt() != 0.0)
            holder.txtItemProdutoSubPreco.text = "." + itensPedido.precoTotal.remainder(BigDecimal.ONE).movePointRight(2).abs().toInt().toString()
        else
            holder.txtItemProdutoSubPreco.text = ".00"

        return convertView
    }

    class ViewHolder {
        internal lateinit var txtItemProdutoNome: TextView
        internal lateinit var txtItemProdutoQtde: TextView
        internal lateinit var txtItemProdutoPreco: TextView
        internal lateinit var txtItemProdutoSubPreco: TextView
        internal lateinit var backgroundTxtItemProdutoQtde: FrameLayout
    }
}
