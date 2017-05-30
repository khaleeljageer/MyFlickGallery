package com.jskhaleel.myflickgallery.activities.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jskhaleel.myflickgallery.R;
import com.jskhaleel.myflickgallery.utils.AnimationFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlickrListAdapter extends RecyclerView.Adapter<FlickrListAdapter.GridViewHolder> {
    private Context context;
    private ArrayList<FlickrGalleryActivity.GalleryBean> galleryBeenList;

    public FlickrListAdapter(Context context, ArrayList<FlickrGalleryActivity.GalleryBean> galleryBeenList) {
        this.context = context;
        this.galleryBeenList = galleryBeenList;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.grid_photo_item, null);
        return new GridViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        FlickrGalleryActivity.GalleryBean item = galleryBeenList.get(position);
        holder.bindViews(context, item);
    }

    @Override
    public int getItemCount() {
        return galleryBeenList.size();
    }

    public void updateList(ArrayList<FlickrGalleryActivity.GalleryBean> flickParser) {
        this.galleryBeenList = flickParser;
        notifyDataSetChanged();
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private ViewFlipper rootView;
        private TextView txtTitle;
        private View itemView;

        public GridViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            rootView = (ViewFlipper) itemView.findViewById(R.id.vf_root_view);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            imgPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
        }

        public void bindViews(Context context, final FlickrGalleryActivity.GalleryBean item) {
            Picasso.with(context).load(item.getUrl()).into(imgPhoto);
            txtTitle.setText(item.getTitle());
            rootView.setDisplayedChild(item.getVisibleView());

            itemView.setOnClickListener(new View.OnClickListener() {
                boolean hasTransientState = true;

                @Override
                public void onClick(View view) {
                    AnimationFactory.flipTransition(rootView, AnimationFactory.FlipDirection.RIGHT_LEFT);
                    itemView.setHasTransientState(hasTransientState);
                    item.setVisibleView(rootView.getDisplayedChild());
                    hasTransientState = !hasTransientState;
                }
            });
        }
    }
}

