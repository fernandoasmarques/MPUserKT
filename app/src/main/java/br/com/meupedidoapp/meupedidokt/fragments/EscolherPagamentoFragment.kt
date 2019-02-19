package br.com.meupedidoapp.meupedidokt.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.meupedidoapp.meupedidokt.R
import br.com.meupedidoapp.meupedidokt.activities.LojistaActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_escolher_pagamento.*
import kotlinx.android.synthetic.main.item_pagamento_dinheiro.*

class EscolherPagamentoFragment : Fragment() {

    companion object {
        lateinit var viewSwitcherPagamento: ViewAnimator
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val size = Point()
        activity!!.windowManager.defaultDisplay.apply {
            this.getSize(size)
        }

        val rootView = inflater.inflate(R.layout.fragment_escolher_pagamento, container, false)
        viewSwitcherPagamento = rootView.findViewById<ViewAnimator>(R.id.EscolherPagamentoFragment_viewAnimatorPagamento)

        //val itemCartaoDinheiro = activity?.layoutInflater?.inflate(R.layout.item_cartao_dinheiro, ConstraintLayout(activity?.baseContext)) // index 0

        val itemCartaoDinheiro = inflater.inflate(R.layout.item_cartao_dinheiro, ConstraintLayout(activity?.baseContext)) // index 0
        val itemPagamentoDinheiro = inflater.inflate(R.layout.item_pagamento_dinheiro, RelativeLayout(activity?.baseContext)) // index 1
        val itemPagamentoCartao = inflater.inflate(R.layout.item_pagamento_cartao, RelativeLayout(activity?.baseContext)) // index 2

        with(viewSwitcherPagamento) {
            addView(itemCartaoDinheiro, 0)
            addView(itemPagamentoDinheiro, 1)
            addView(itemPagamentoCartao, 2)

            getChildAt(0).findViewById<MaterialCardView>(R.id.item_cartao_dinheiro_cardDinheiro).setOnClickListener {
                viewSwitcherPagamento.displayedChild = 1
                ObjectAnimator.ofFloat(EscolherPagamentoFragment_txtPagamento, "translationX", size.x.toFloat() / 3).apply {
                    this.start()
                }
                viewPagamentoDinheiro(this)
            }

            getChildAt(0).findViewById<MaterialCardView>(R.id.item_cartao_dinheiro_cardCartao).setOnClickListener {
                viewSwitcherPagamento.displayedChild = 2
                ObjectAnimator.ofFloat(EscolherPagamentoFragment_txtPagamento, "translationX", -size.x.toFloat() / 3).apply {
                    this.start()
                }
                viewPagamentoCartao(this)
            }
        }
        return rootView
    }

    private fun viewPagamentoDinheiro(v: ViewAnimator) {
        val edtTroco = v.getChildAt(1).findViewById<TextInputEditText>(R.id.item_pagamento_dinheiro_txtInputLayoutEdtTroco)
        val checkBoxTroco = v.getChildAt(1).findViewById<CheckBox>(R.id.item_pagamento_dinheiro_checkboxTroco)

        val text = LojistaActivity.somarPrecosItensSelecionados().setScale(2).toString()
        SpannableStringBuilder(text).apply {
            val cor = ForegroundColorSpan(Color.parseColor("#99000000"))
            this.setSpan(cor, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.append("R$")
            v.getChildAt(1).findViewById<TextView>(R.id.item_pagamento_dinheiro_txtValor).text = this
        }

        v.getChildAt(1).findViewById<Button>(R.id.item_pagamento_dinheiro_botaoVoltar).setOnClickListener {
            v.displayedChild = 0
            ObjectAnimator.ofFloat(EscolherPagamentoFragment_txtPagamento, "translationX", 0f).apply {
                this.start()
            }
        }

        checkBoxTroco.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edtTroco.isEnabled = false
                edtTroco.setText("")
                //edtTroco.hint = "Sem necessidade de troco."
                item_pagamento_dinheiro_txtInputLayoutTroco.hint = "Sem necessidade de troco."
            } else {
                edtTroco.isEnabled = true
                item_pagamento_dinheiro_txtInputLayoutTroco.hint = "Troco para"
            }
        }
    }

    private fun viewPagamentoCartao(v: ViewAnimator) {
        val text = LojistaActivity.somarPrecosItensSelecionados().setScale(2).toString()
        val pagamentoRadioGroup = v.getChildAt(2).findViewById<RadioGroup>(R.id.item_pagamento_cartao_pagamentoRadioGroup)
        SpannableStringBuilder(text).apply {
            val cor = ForegroundColorSpan(Color.parseColor("#99000000"))
            this.setSpan(cor, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.append("R$")
            v.getChildAt(2).findViewById<TextView>(R.id.item_pagamento_cartao_txtValor).text = this
        }

        v.getChildAt(2).findViewById<Button>(R.id.item_pagamento_cartao_botaoVoltar).setOnClickListener {
            v.displayedChild = 0
            ObjectAnimator.ofFloat(EscolherPagamentoFragment_txtPagamento, "translationX", 0f).apply {
                this.start()
            }
        }
    }
}
