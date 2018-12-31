package br.com.meupedidoapp.meupedidokt.adapters

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.activities.LojistaActivity
import br.com.meupedidoapp.meupedidokt.model.Lojista
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class LojistaAdapter(options: FirestoreRecyclerOptions<Lojista>) : FirestoreRecyclerAdapter<Lojista, LojistaAdapter.LojistaHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LojistaHolder {
        return LojistaHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lojista, parent, false))
    }

    override fun onBindViewHolder(holder: LojistaHolder, position: Int, model: Lojista) {
        holder.tituloLojista.text = model.nomeFantasia
        holder.subTitulo.text = model.categoriaPrimaria

        Glide.with(holder.imagemPerfil.context)
                .load(model.imagemPerfil)
                .into(holder.imagemPerfil)

        Glide.with(holder.marketingBanner.context)
                .load(model.marketingBanner)
                .into(holder.marketingBanner)

        if(model.isEstaAberto){
            holder.txtEstaAberto.text = "Aberto"
            holder.txtEstaAberto.background.setColorFilter(Color
                    .parseColor("#008000"), PorterDuff.Mode.SRC_IN)
        }else{
            holder.txtEstaAberto.text = "Fechado"
            holder.txtEstaAberto.background.setColorFilter(Color.RED,
                    PorterDuff.Mode.SRC_IN)
        }

        val cores = intArrayOf(android.R.color.transparent, Color.parseColor(model.tema.corPrincipal))
        val gd = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, cores)

        holder.lojistaGradient.background = gd
        holder.backgroundImgPerfilLojista.setBackgroundColor(Color.parseColor(model.tema.corPrincipal))
        //holder.cardLojista.strokeColor = Color.parseColor(model.tema.corPrincipal)

        if(!model.qtdeAvaliacao.equals(0) && !model.avaliacao.equals(0)){
            var valueRating = model.avaliacao / model.qtdeAvaliacao
            holder.txtAvaliacao.text = valueRating.toString()
        }else{
            holder.txtAvaliacao.text = "Não Avaliado"
        }

        holder.btnCardapio.setOnClickListener {
            val intent = Intent(it.context, LojistaActivity::class.java)
            Bundle().apply {
                this.putParcelable("tema", model.tema)
                this.putString("nomeFantasia", model.nomeFantasia)
                this.putString("uidLojista", model.uid)
                intent.putExtras(this)
            }
            val opts = ActivityOptionsCompat.makeCustomAnimation(it.context, R.anim.slide_in_left, R.anim.slide_out_left)
            ActivityCompat.startActivity(it.context, intent, opts.toBundle())
        }
    }

    class LojistaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconStar = itemView.findViewById<ImageView>(R.id.imgStar)!!
        val tituloLojista = itemView.findViewById<TextView>(R.id.ItemLojista_tituloLojista)!!
        val subTitulo = itemView.findViewById<TextView>(R.id.ItemLojista_subTitulo)!!
        val marketingBanner = itemView.findViewById<ImageView>(R.id.ItemLojista_marketingBanner)!!
        val imagemPerfil = itemView.findViewById<ImageView>(R.id.ItemLojista_imagemPerfil)!!
        val txtAvaliacao = itemView.findViewById<TextView>(R.id.ItemLojista_txtAvaliacao)!!
        val btnCardapio = itemView.findViewById<Button>(R.id.ItemLojista_btnCardapio)!!
        val lojistaGradient = itemView.findViewById<View>(R.id.ItemLojista_lojistaGradient)!!
        val backgroundImgPerfilLojista = itemView.findViewById<View>(R.id.ItemLojista_backgroundImgLojista)!!
        val txtEstaAberto = itemView.findViewById<TextView>(R.id.ItemLojista_txtEstaAberto)!!
        //val cardLojista = itemView.findViewById<MaterialCardView>(R.id.ItemLojista_cardLojista)!!
    }
}