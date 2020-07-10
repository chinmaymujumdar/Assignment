package com.example.carwaleassignment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.carwaleassignment.R
import com.example.carwaleassignment.model.Country
import com.example.carwaleassignment.viewmodel.CasesViewModel

class CountryAdapter(viewModel: CasesViewModel?):DataBindingAdapter<Country>(DiffCallBack(),viewModel) {

    class DiffCallBack: DiffUtil.ItemCallback<Country>(){
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item
    }
}