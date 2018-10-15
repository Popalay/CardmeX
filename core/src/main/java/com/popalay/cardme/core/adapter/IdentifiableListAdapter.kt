package com.popalay.cardme.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.popalay.cardme.core.extensions.vibrate
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class IdentifiableListAdapter<I : Identifiable>(
    @LayoutRes val layoutRes: Int,
    diffCallback: DiffUtil.ItemCallback<I> = IdentifiableDiffCallback(),
    val createViewHolder: (view: View) -> BindableViewHolder<I>
) : ListAdapter<I, BindableViewHolder<I>>(diffCallback) {

    private val itemClickSubject = PublishSubject.create<I>()
    private val itemLongClickSubject = PublishSubject.create<I>()

    val itemClickObservable: Observable<I> get() = itemClickSubject
    val itemLongClickObservable: Observable<I> get() = itemLongClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<I> {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return createViewHolder(view)
    }

    override fun onBindViewHolder(holder: BindableViewHolder<I>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: BindableViewHolder<I>, position: Int, payloads: MutableList<Any>) {
        holder.bindClick(itemClickSubject, getItem(position))
        holder.bindLongClick(itemLongClickSubject, getItem(position))
        if (payloads.isEmpty()) onBindViewHolder(holder, position) else holder.bindPayloads(getItem(position), payloads)
    }
}

interface Identifiable {
    val id: Long
}

open class IdentifiableDiffCallback<I : Identifiable> : DiffUtil.ItemCallback<I>() {

    override fun areItemsTheSame(oldItem: I, newItem: I): Boolean =
        oldItem.javaClass == newItem.javaClass && (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: I, newItem: I): Boolean = oldItem == newItem
}

interface Bindable<I> {

    fun bind(item: I)

    fun bindPayloads(item: I, payloads: List<Any>) {}

    fun bindClick(subject: PublishSubject<I>, item: I)

    fun bindLongClick(subject: PublishSubject<I>, item: I)
}

abstract class BindableViewHolder<I : Any>(itemView: View) : RecyclerView.ViewHolder(itemView), Bindable<I> {

    override fun bindClick(subject: PublishSubject<I>, item: I) {
        itemView.setOnClickListener { subject.onNext(item) }
    }

    override fun bindLongClick(subject: PublishSubject<I>, item: I) {
        itemView.setOnLongClickListener {
            subject.onNext(item)
            itemView.context.vibrate(longArrayOf(100, 100, 100))
            true
        }
    }
}