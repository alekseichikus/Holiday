package ru.createtogether.feature_photo.adapter

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.feature_photo.PhotoSmallView
import ru.createtogether.feature_photo.helpers.PhotoDiffUtilCallback
import ru.createtogether.feature_photo_utils.PhotoModel

class PhotoAdapter(
    private var photos: MutableList<PhotoModel>,
    private val onPhotoClick: (PhotoModel) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoSmallViewHolder>(), AdapterActions {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSmallViewHolder{
        val photoSmallView = PhotoSmallView(parent.context, onPhotoClick = onPhotoClick)
        return PhotoSmallViewHolder(photoSmallView)
    }

    override fun onBindViewHolder(holder: PhotoSmallViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size
    override fun getData() = photos

    override fun setData(list: List<Any>) {
        with(PhotoDiffUtilCallback(getData(), (list as List<PhotoModel>))) {
            photos = list.toMutableList()
            DiffUtil.calculateDiff(this).dispatchUpdatesTo(this@PhotoAdapter)
        }
    }

    inner class PhotoSmallViewHolder(
        private val photoSmallView: PhotoSmallView
    ) : RecyclerView.ViewHolder(photoSmallView) {
        fun bind(photo: PhotoModel) {
            photoSmallView.setPhoto(photo = photo)
        }
    }
}