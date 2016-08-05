package io.githut.kevinsu917.demoproject.customview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.githut.kevinsu917.demoproject.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Creator: KevinSu kevinsu917@126.com
 * Date 2016-08-05-09:10
 * Description:带Indicator的无限循环广告轮播页
 */
public class CyclePager extends FrameLayout {

    private final int MAX_COUNT = Integer.MAX_VALUE;
    private Context mContext;
    private ViewPager viewpager;
    private AdPagerAdapter mAdapter;
    private RecyclerView recyclerView;
    private IndicatorAdapter indicatorAdapter;
    private ArrayList<String> pics = new ArrayList<>();
    private int currentPos = 0;//当前选中的位置
    private long lastPagerSelectedTime;//上一次页面滑动的时间

    public CyclePager(Context context) {
        this(context, null);
    }

    public CyclePager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CyclePager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    /**
     * 启动轮播
     * @param data
     */
    public void start(ArrayList<String> data){
        pics.clear();
        if(data != null){
            pics.addAll(data);
        }
        initViews();
        initTimer();
    }

    private void initViews() {
        viewpager = new ViewPager(mContext);
        mAdapter = new AdPagerAdapter();
        viewpager.setAdapter(mAdapter);
        if (!pics.isEmpty()) {
            viewpager.setCurrentItem(MAX_COUNT / 2 - (MAX_COUNT / 2) % pics.size());
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                lastPagerSelectedTime = new Date().getTime();
                currentPos = position % pics.size();
                indicatorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FrameLayout.LayoutParams match2Params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(viewpager, match2Params);

        recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        indicatorAdapter = new IndicatorAdapter();
        recyclerView.setAdapter(indicatorAdapter);
        FrameLayout.LayoutParams wrap2Params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wrap2Params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        wrap2Params.setMargins(0,0,0, 15);
        addView(recyclerView, wrap2Params);
    }

    class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.IndicatorViewHolder> {


        @Override
        public IndicatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IndicatorViewHolder(LayoutInflater.from(mContext).inflate(R.layout.indicator_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(IndicatorViewHolder holder, int position) {
            holder.setItemSelected(position == currentPos);
        }

        @Override
        public int getItemCount() {
            return pics.size();
        }

        class IndicatorViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            public IndicatorViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.ivIndicator);
            }

            public void setItemSelected(boolean selected) {
                imageView.setSelected(selected);
            }
        }

    }

    class AdPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return MAX_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = (position % pics.size());
            View view = LayoutInflater.from(mContext).inflate(R.layout.ad_item_view, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(mContext).load(pics.get(position)).into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object imageView) {
            container.removeView((View) imageView);
        }
    }

    /**
     * 初始化间隔发射器
     */
    private void initTimer() {
        lastPagerSelectedTime = new Date().getTime();
        Observable.interval(1, TimeUnit.SECONDS)
                .filter(aLong -> (new Date().getTime() - lastPagerSelectedTime) >= 5000)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            //如果下一页已经超出最大值,那么就从头开始滑动,同时不要平滑
            if(viewpager != null){
                int nextPos = viewpager.getCurrentItem() + 1;
                viewpager.setCurrentItem(nextPos == MAX_COUNT ? 0 : nextPos % MAX_COUNT, nextPos != MAX_COUNT);
            }
        });



    }

    /**
     *  刷新数据
     * @param data
     */
    public void refreshData(ArrayList<String> data){
        pics.clear();
        if(data != null){
            pics.addAll(data);
        }
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        if(indicatorAdapter != null){
            indicatorAdapter.notifyDataSetChanged();
        }
    }
}
