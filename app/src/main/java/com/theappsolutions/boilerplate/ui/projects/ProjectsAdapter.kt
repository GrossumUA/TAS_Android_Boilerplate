package com.theappsolutions.boilerplate.ui.projects

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.data.model.realm.CachedProject
import com.theappsolutions.boilerplate.util.storage.FileSystemHelper

import butterknife.BindView
import butterknife.ButterKnife
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class ProjectsAdapter(data: OrderedRealmCollection<CachedProject>) :
        RealmRecyclerViewAdapter<CachedProject, ProjectsAdapter.ViewHolder>(data, true) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ProjectsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.get(position)?.let {
            with(it) {
                holder.apply {
                    tvName.text = name
                    tvType.text = type
                    Glide.with(context).load(FileSystemHelper.getAssetsFilePath(logoSrc))
                            .into(ivLogo)
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val ivLogo: ImageView = itemView.findViewById(R.id.iv_logo)
        val tvType: TextView = itemView.findViewById(R.id.tv_type)
        val context: Context = itemView.context
    }
}