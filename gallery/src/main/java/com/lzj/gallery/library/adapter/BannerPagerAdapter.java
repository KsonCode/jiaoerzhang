package com.lzj.gallery.library.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzj.gallery.library.R;
import com.lzj.gallery.library.transformer.CornerTransform;

import java.util.List;


public class BannerPagerAdapter extends PagerAdapter {
    private List<String> mList;
    private Context mContext;
    private int defaultImg = R.mipmap.ic_banner_error;//默认图片
    private int mRoundCorners = -1;
    private boolean isTitle;

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public void setmRoundCorners(int mRoundCorners) {
        this.mRoundCorners = mRoundCorners;
    }

    public BannerPagerAdapter(List<String> list, Context context, boolean isTitle) {
        this.mList = list;
        this.mContext = context;
        this.isTitle = isTitle;
    }

    @Override
    public int getCount() {
        return 500000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_img_layout, container, false);
        ImageView imageView = view.findViewById(R.id.img);



        int index = position % mList.size();
        LoadImage(mList.get(index), imageView);

        container.addView(view);
        return view;
    }

    /**
     * 加载图片
     */
    public void LoadImage(String url, ImageView imageview) {
        if (mRoundCorners == -1) {

            RequestOptions options = new RequestOptions();
            options.placeholder(defaultImg);
            options.centerCrop();
            options.dontAnimate();
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(mContext)
                    .load(url)
                    .apply(options)
                    .into(imageview);
        } else {
            RequestOptions options = new RequestOptions();
            options.placeholder(defaultImg);
            options.centerCrop();
            options.dontAnimate();
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            options.transform(new CornerTransform(mContext, mRoundCorners));
            Glide.with(mContext)
                    .load(url)
                    .apply(options).into(imageview);
        }
    }


}