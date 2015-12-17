package com.cui.animas.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.cui.animas.utils.Screen_Utils;
import com.squareup.picasso.Callback;
import com.cui.animas.R;
import com.squareup.picasso.Picasso;

/**
 * Created by cuiyang on 15/12/2.
 * demo中其实可以不用picasso.只是为了方便他人参考时加载网络图同时模仿网络加载
 */
public class Simple_IMG_Adapter extends RecyclerView.Adapter<Simple_IMG_Adapter.ViewHolder> {

    private DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(2f);
    private static final int PHOTO_ANIMATION_DELAY = 700;

    int size = 0;
    private Context context;
    private boolean lockedAnimations = false;
    private int lastAnimatedItem = -1;

    public Simple_IMG_Adapter(int size, Context context) {
        this.size = size;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindPhoto(holder, position);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    private void bindPhoto(final ViewHolder holder, int position) {
        Picasso.with(context)
                .load(R.mipmap.accout_img)
                .resize(200, 200)
                .config(Bitmap.Config.RGB_565)
                .centerCrop()
                .into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {
                        animatePhoto(holder);
                    }

                    @Override
                    public void onError() {

                    }
                });
        if (lastAnimatedItem < position) lastAnimatedItem = position;
    }

    private void animatePhoto(ViewHolder holder) {
        if (!lockedAnimations) {
            if (lastAnimatedItem == holder.getPosition()) {
                setLockedAnimations(true);
            }
            long animationDelay = PHOTO_ANIMATION_DELAY + holder.getPosition() * 30;

            holder.img.setScaleY(0);
            holder.img.setScaleX(0);
            holder.img.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(800)
                    .setInterpolator(mDecelerateInterpolator)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_item_single);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }
}
