package br.com.meupedidoapp.meupedidokt.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.meupedidoapp.meupedidokt.fragments.ProdutoFragment
import br.com.meupedidoapp.meupedidokt.model.Tema

class ProdutoPagerAdapter(fm: FragmentManager?, private val qtdeCategorias: Int, private val uidLojista: String,
                          private val idCategorias: ArrayList<String>, private val nomeCategorias: ArrayList<String>,
                          private val tema: Tema) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return ProdutoFragment.newInstance(uidLojista, idCategorias[position], tema)
    }

    override fun getCount(): Int {
        return qtdeCategorias
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return nomeCategorias[position]
    }
}