package com.example.commonlibrary.cusotomview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.commonlibrary.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/10/26     13:19
 */
public class BannerViewPager extends WrappedViewPager {


    private AutoPlayTask autoPlayTask;
    private long delayTime = 3000;

    private List<Object> dataList;
    private List<View> imageList;
    private BannerViewPagerAdapter mBannerViewPagerAdapter;


    private List<ViewPager.OnPageChangeListener> mOnPageChangeListeners;
    private int itemElevation;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }


    @Override
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (!mOnPageChangeListeners.contains(listener)) {
            mOnPageChangeListeners.add(listener);
        }
    }


    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = (position - 1) % (dataList.size() - 2);
        if (realPosition < 0)
            realPosition += (dataList.size() - 2);
        return realPosition;
    }


    private void initData() {
        mOnPageChangeListeners = new ArrayList<>();
        dataList = new ArrayList<>();
        imageList = new ArrayList<>();
        autoPlayTask = new AutoPlayTask(this);
        setPageChangeDuration(800);
        super.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (OnPageChangeListener listener :
                        mOnPageChangeListeners) {
                    listener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {

                for (OnPageChangeListener listener :
                        mOnPageChangeListeners) {
                    listener.onPageSelected(toRealPosition(position));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                for (OnPageChangeListener listener :
                        mOnPageChangeListeners) {
                    listener.onPageScrollStateChanged(state);
                }
                switch (state) {
                    case 0:
                        if (getCurrentItem() == 0) {
                            setCurrentItem(imageList.size() - 2, false);
                        } else if (getCurrentItem() == imageList.size() - 1) {
                            setCurrentItem(1, false);
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
    }


    public void refreshData(List<Object> data) {
        dataList.clear();
        imageList.clear();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size() + 2; i++) {
                CardView cardView = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.view_banner_item, this, false);
                //                cardView.setMaxCardElevation(itemElevation);
                imageList.add(cardView);
                if (i == 0) {
                    dataList.add(data.get(data.size() - 1));
                } else if (i == data.size() + 1) {
                    dataList.add(data.get(0));
                } else {
                    dataList.add(data.get(i - 1));
                }
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.custom_drawable_place_holder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA);
                Glide.with(getContext()).load(dataList.get(i)).apply(options).into((ImageView) imageList.get(i).findViewById(R.id.iv_view_banner_item_image));
            }
            if (data.size() > 1) {
                setNeedScroll(true);
            } else {
                setNeedScroll(false);
            }
        }
    }

    public void start() {
        if (mBannerViewPagerAdapter == null) {
            mBannerViewPagerAdapter = new BannerViewPagerAdapter();
        }
        setAdapter(mBannerViewPagerAdapter);
        setCurrentItem(1);
        startAutoPlay();
    }

    public void setItemElevation(int itemElevation) {
        this.itemElevation = itemElevation;
    }


    private static class AutoPlayTask implements Runnable {
        private final WeakReference<BannerViewPager> mBanner;

        private AutoPlayTask(BannerViewPager banner) {
            mBanner = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            BannerViewPager banner = mBanner.get();
            if (banner != null) {
                int currentPosition = banner.getCurrentItem();
                currentPosition = (currentPosition % (banner.getAdapter().getCount() - 2)) + 1;
                if (currentPosition == 1) {
                    banner.setCurrentItem(currentPosition, false);
                    banner.postDelayed(this, banner.getDelayTime());
                } else {
                    banner.setCurrentItem(currentPosition);
                    banner.postDelayed(this, banner.getDelayTime());
                }
            }
        }
    }

    private long getDelayTime() {
        return delayTime;
    }

    private void startAutoPlay() {
        if (autoPlayTask != null) {
            removeCallbacks(autoPlayTask);
        }
        postDelayed(autoPlayTask, delayTime);
    }

    /**
     * 设置调用setCurrentItem(int item, boolean smoothScroll)方法时，page切换的时间长度
     *
     * @param duration page切换的时间长度
     */
    public void setPageChangeDuration(int duration) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new CustomScroller(getContext(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            startAutoPlay();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stopAutoPlay();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void stopAutoPlay() {
        removeCallbacks(autoPlayTask);
    }

    private static class CustomScroller extends Scroller {

        private int mDuration;

        public CustomScroller(Context context, int duration) {
            super(context);
            this.mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }


    private class BannerViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageList.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageList.get(position));
            if (mOnItemClickListener != null) {
                imageList.get(position).setOnClickListener(v -> mOnItemClickListener.onItemClick(v, toRealPosition(position)));
            }
            return imageList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }


}
