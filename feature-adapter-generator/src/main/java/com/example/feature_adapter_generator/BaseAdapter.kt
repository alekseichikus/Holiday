package com.example.feature_adapter_generator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

internal class BaseAdapter<T, V, BA : BaseAction<T>>(
    private val viewClass: Class<V>,
    private val diffUtilTheSameCallback: DiffUtilTheSameCallback<T>
) :
    RecyclerView.Adapter<BaseViewHolder<T, V>>(),
    AdapterAction<T, BA> where V : ViewGroup, V : ViewAction<T> {
    override var action: BA? = null
    override var items: Collection<T> by Delegates.observable(emptyList()) { _, old, new ->
        BaseDiffUtilCallback(
            oldList = old,
            newList = new,
            diffUtilTheSameCallback = diffUtilTheSameCallback
        ).calculateDiff().dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T, V>, position: Int) {
        holder.bind(items.elementAt(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, V> =
        BaseViewHolder(
            view = viewClass.getConstructor(*getParametersType())
                .newInstance(*getArgs(parent.context))
        )

    private fun getParametersType(): Array<Class<*>> {
        var parametersType = arrayOf<Class<*>>(Context::class.java)
        action?.let {
            parametersType += it.javaClass.interfaces.first()
        }
        return parametersType
    }

    private fun getArgs(context: Context): Array<Any> {
        var parametersType = arrayOf<Any>(context)
        action?.let {
            parametersType += it
        }
        return parametersType
    }
}