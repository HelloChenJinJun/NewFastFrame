package com.example.commonlibrary.baseadapter.viewholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.commonlibrary.R;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.utils.CommonLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      14:38
 * QQ:             1981367757
 */

public class BaseWrappedViewHolder extends RecyclerView.ViewHolder {
    private Set<Integer> mClickableItemIds;
    private Set<Integer> mNestIds;
    private Set<Integer> mLongClickableItemIds;
    private SparseArray<View> views;
    public View itemView;
    private BaseRecyclerAdapter adapter;

    public BaseWrappedViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        mClickableItemIds = new HashSet<>();
        mNestIds = new HashSet<>();
        mLongClickableItemIds = new HashSet<>();
        views = new SparseArray<>();
    }


    public Context getContext() {
        return itemView.getContext();
    }


    public Set<Integer> getClickableItemIds() {
        return mClickableItemIds;
    }

    public Set<Integer> getNestIds() {
        return mNestIds;
    }

    public Set<Integer> getLongClickableItemIds() {
        return mLongClickableItemIds;
    }

    public BaseWrappedViewHolder setVisible(int layoutId, boolean isVisible) {
        getView(layoutId).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        return this;
    }


    public BaseWrappedViewHolder setOnItemClickListener(final View.OnClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                if (adapter.getOnItemClickListener() != null) {
                    adapter.getOnItemClickListener().onItemClick(getAdapterPosition() - adapter.getItemUpCount(), v);
                }
            }
        });
        return this;
    }


    public BaseWrappedViewHolder setOnItemClickListener() {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getOnItemClickListener() != null) {
                    adapter.getOnItemClickListener().onItemClick(getAdapterPosition() - adapter.getItemUpCount(), v);
                }
            }
        });
        return this;
    }


    public BaseWrappedViewHolder setOnItemChildClickListener(int id) {
        if (getView(id) != null) {
            getView(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.getOnItemClickListener() != null) {
                        adapter.getOnItemClickListener().onItemChildClick(getAdapterPosition() - adapter.getItemUpCount(), v, v.getId());
                    }
                }
            });
        }
        return this;
    }


    public BaseWrappedViewHolder setOnItemChildClickListener(int id, final View.OnClickListener listener) {
        if (getView(id) != null) {
            getView(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(v);
                    }
                    if (adapter.getOnItemClickListener() != null) {
                        adapter.getOnItemClickListener().onItemChildClick(getAdapterPosition() - adapter.getItemUpCount(), v, v.getId());
                    }
                }
            });
        }
        return this;
    }


    public BaseWrappedViewHolder setOnItemLongClickListener() {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (adapter.getOnItemClickListener() != null) {
                    return adapter.getOnItemClickListener().onItemLongClick(getAdapterPosition() - adapter.getItemUpCount(), v);
                }
                return false;
            }
        });
        return this;
    }


    public BaseWrappedViewHolder setOnItemChildLongClickListener(int id) {
        if (getView(id) != null) {
            getView(id).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (adapter.getOnItemClickListener() != null) {
                        return adapter.getOnItemClickListener().onItemChildLongClick(getAdapterPosition() - adapter.getItemUpCount(), v, v.getId());
                    }
                    return false;
                }
            });
        }
        return this;
    }


    /**
     * 直接给view主题设置事件监听
     *
     * @param id
     * @param onClickListener
     * @return
     */
    public BaseWrappedViewHolder setOnClickListener(int id, View.OnClickListener onClickListener) {
        getView(id).setOnClickListener(onClickListener);
        return this;
    }

    public View getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            if (view == null) {
                if (itemView.getId() == id) {
                    views.put(id, itemView);
                }
            } else {
                views.put(id, view);
            }
        }
        return view;
    }

    public BaseWrappedViewHolder setText(int id, CharSequence content) {
        ((TextView) getView(id)).setText(content);
        return this;
    }

    public BaseWrappedViewHolder setTextColor(int id, int color) {
        ((TextView) getView(id)).setTextColor(color);
        return this;
    }

    public BaseWrappedViewHolder setTextColor(int id, ColorStateList colorStateList) {
        ((TextView) getView(id)).setTextColor(colorStateList);
        return this;
    }


    public BaseWrappedViewHolder setImageUrl(int id, String url) {
        setImageUrl(id, url, 0, 0);
        return this;
    }

    private Set<Integer> set = new HashSet<>();

    private String url;


    public BaseWrappedViewHolder setImageUrl(int id, String url, int errorId, int placeHolderId) {
        View view=getView(id);
        if (view instanceof ImageView){
            view.setTag(R.id.image_url_tag,url);
            Glide.with(itemView.getContext()).load(url).placeholder(placeHolderId == 0 ? R.drawable.custom_drawable_place_holder : placeHolderId)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    String tag= (String)view.getTag(R.id.image_url_tag);
                    if (tag!=null&&tag.equals(url)) {
                        ((ImageView)view).setImageDrawable(resource);
                    }
                }


                @Override
                public void onLoadStarted(Drawable placeholder) {
                    ((ImageView) view).setImageDrawable(placeholder);
                }
            });
        }
        return this;
    }


    public BaseWrappedViewHolder setImageUrl(int id, File file) {
        if (file.exists()) {
            Glide.with(itemView.getContext()).load(file).into((ImageView) getView(id));
        }
        return this;
    }


    public BaseWrappedViewHolder setImageUri(int id, Uri uri) {
        if (uri != null) {
            Glide.with(itemView.getContext()).load(uri).into((ImageView) getView(id));
        }
        return this;
    }

    public BaseWrappedViewHolder setImageResource(int id, int resId) {
        if (getView(id) instanceof ImageView) {
            ((ImageView) getView(id)).setImageResource(resId);
        }
        return this;
    }

    public BaseWrappedViewHolder setImageBg(final int id, String url) {
        if (getView(id) instanceof ImageView) {
            Glide.with(itemView.getContext()).load(url).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    CommonLogger.e("设置背景");
                    getView(id).setBackground(resource);
                }
            });
        }
        return this;
    }

    public BaseWrappedViewHolder bindAdapter(BaseRecyclerAdapter adapter) {
        this.adapter = adapter;
        return this;
    }


    public BaseRecyclerAdapter getAdapter() {
        return adapter;
    }

    public BaseWrappedViewHolder setImageDrawable(int id, Drawable drawable) {
        View view = getView(id);
        if (view != null && view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        }
        return this;
    }

    public void clear() {
//        for (Integer id:
//                set) {
//            Glide.clear(getView(id));
//        }
    }
}
