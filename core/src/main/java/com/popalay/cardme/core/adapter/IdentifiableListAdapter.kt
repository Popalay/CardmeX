package com.popalay.cardme.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class IdentifiableListAdapter<I : Identifiable>(
    @LayoutRes val layoutRes: Int,
    val createViewHolder: (view: View) -> BindableViewHolder<I>
) : ListAdapter<I, BindableViewHolder<I>>(IdentifiableDiffCallback<I>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<I> {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view)
    }

    override fun onBindViewHolder(holder: BindableViewHolder<I>, position: Int) {
        holder.bind(getItem(position))
    }
}

interface Identifiable {
    val id: Long
}

class IdentifiableDiffCallback<I : Identifiable> : DiffUtil.ItemCallback<I>() {

    override fun areItemsTheSame(oldItem: I, newItem: I): Boolean =
        oldItem.javaClass == newItem.javaClass && (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: I, newItem: I): Boolean = oldItem == newItem
}

interface Bindable<I> {

    fun bind(item: I)
}

abstract class BindableViewHolder<I : Any>(itemView: View) : RecyclerView.ViewHolder(itemView), Bindable<I>