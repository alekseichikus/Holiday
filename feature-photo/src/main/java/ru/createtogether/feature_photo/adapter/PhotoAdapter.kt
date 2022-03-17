package ru.createtogether.feature_photo.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.common.helpers.baseFragment.base.adapter.BaseAdapter
import ru.createtogether.feature_photo.PhotoSmallView
import ru.createtogether.feature_photo.helpers.PhotoAdapterListener
import ru.createtogether.feature_photo_utils.PhotoModel

class PhotoAdapter :
    BaseAdapter<PhotoModel, PhotoAdapter.PhotoSmallViewHolder>() {

    inner class PhotoSmallViewHolder(
        private val photoSmallView: PhotoSmallView
    ) : RecyclerView.ViewHolder(photoSmallView) {
        fun bind(photo: PhotoModel) {
            photoSmallView.setPhoto(photo = photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSmallViewHolder {
        val photoSmallView =
            PhotoSmallView(context = parent.context, adapterListener = getActions())
        return PhotoSmallViewHolder(photoSmallView)
    }

    override fun onBindViewHolder(holder: PhotoSmallViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override var items: Array<PhotoModel> = emptyArray()
}