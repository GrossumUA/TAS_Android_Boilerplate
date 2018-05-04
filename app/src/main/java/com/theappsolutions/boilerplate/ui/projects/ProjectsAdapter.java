package com.theappsolutions.boilerplate.ui.projects;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.data.model.realm.CachedProject;
import com.theappsolutions.boilerplate.util.storage.FileSystemHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class ProjectsAdapter extends RealmRecyclerViewAdapter<CachedProject, ProjectsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_logo)
        ImageView ivLogo;

        @BindView(R.id.tv_type)
        TextView tvType;

        private Context context;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            context = v.getContext();
        }

        public Context getContext() {
            return context;
        }
    }

    public ProjectsAdapter(OrderedRealmCollection<CachedProject> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @Override
    public ProjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CachedProject dataItem = getItem(position);

        holder.tvName.setText(dataItem.getName());
        holder.tvType.setText(dataItem.getType());

        Glide.with(holder.getContext()).load(FileSystemHelper.getAssetsFilePath(dataItem.getLogoSrc()))
                .into(holder.ivLogo);
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

}