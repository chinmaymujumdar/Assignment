package com.example.carwaleassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.carwaleassignment.viewmodel.CasesViewModel

abstract class DataBindingAdapter<T>(difCallback: DiffUtil.ItemCallback<T>,val viewModel: CasesViewModel?): ListAdapter<T, DataBindingViewHolder<T>>(difCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater=LayoutInflater.from(parent.context)
        val dataBinding=DataBindingUtil.inflate<ViewDataBinding>(layoutInflater,viewType,parent,false)
        return DataBindingViewHolder(dataBinding,viewModel)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }
}