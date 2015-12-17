package com.cui.animas.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cui.animas.BaseActivity;
import com.cui.animas.R;
import com.cui.animas.fragment.CircularReveal_fragment;
import com.cui.animas.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cuiyang on 15/12/16.
 */
public class CircularReveal_Activity extends BaseActivity {

    @InjectView(R.id.account_img_usertitle)
    CircleImageView accountImgUsertitle;
    @InjectView(R.id.trans_layout)
    LinearLayout transLayout;
    @InjectView(R.id.alpha_layout)
    LinearLayout alphaLayout;
    @InjectView(R.id.mTable_layout)
    TabLayout mTableLayout;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.content_layout)
    FrameLayout contentLayout;
    @InjectView(R.id.animation_layout)
    View animationLayout;
    @InjectView(R.id.root_layout)
    CoordinatorLayout root_layout;

    private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(2f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_act);
        ButterKnife.inject(this);
        initToolbar();
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(android.R.color.transparent);

        root_layout.setVisibility(View.INVISIBLE);

        initViewPagerAndTabs();
        initContentView();
    }

    private void initViewPagerAndTabs() {
        //等待绘图完成再进行动画这样才能取到控件的宽高 oncreate中只是绘制
        root_layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                root_layout.getViewTreeObserver().removeOnPreDrawListener(this);
                startCircularReveal();
                return true;
            }
        });
    }

    /**
     * 模仿createCircularReveal方法实现的效果.
     */
    //  ViewAnimationUtils.createCircularReveal(contentLayout, 0, 0, 0, (float) Math.hypot(contentLayout.getWidth(), contentLayout.getHeight())).setDuration(1500).start();不能在主线程直接.目前试验只能在点击事件里才行.
    private void startCircularReveal() {
        ViewPropertyAnimator animator = animationLayout.animate();
        animator.scaleY(contentLayout.getHeight() * 2).scaleX(contentLayout.getWidth() * 2).setDuration(800).setStartDelay(300).setInterpolator(new AccelerateInterpolator()).start();
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startTitleViewAimation();
            }
        });
    }

    private void startTitleViewAimation() {
        root_layout.setVisibility(View.VISIBLE);

        accountImgUsertitle.setTranslationY(-accountImgUsertitle.getHeight());
        accountImgUsertitle.animate().translationY(0f).setDuration(800).setInterpolator(mDecelerateInterpolator);

        transLayout.setTranslationY(-transLayout.getHeight());
        transLayout.animate().translationY(0f).setDuration(1300).setInterpolator(mDecelerateInterpolator);

        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(alphaLayout, "alpha", 0, 1f);
        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(mTableLayout, "alpha", 0, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2);
        set.setDuration(1000);
        set.setInterpolator(mDecelerateInterpolator);
        set.start();
    }

    private void initContentView() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(CircularReveal_fragment.createInstance(50), null);
        pagerAdapter.addFragment(CircularReveal_fragment.createInstance(20), null);
        pagerAdapter.addFragment(CircularReveal_fragment.createInstance(20), null);
        pagerAdapter.addFragment(CircularReveal_fragment.createInstance(20), null);
        viewPager.setAdapter(pagerAdapter);
        mTableLayout.setupWithViewPager(viewPager);
        mTableLayout.getTabAt(0).setIcon(R.mipmap.ic_grid_on_white);
        mTableLayout.getTabAt(1).setIcon(R.mipmap.ic_list_white);
        mTableLayout.getTabAt(2).setIcon(R.mipmap.ic_place_white);
        mTableLayout.getTabAt(3).setIcon(R.mipmap.ic_label_white);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            if (title != null)
                fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return fragmentTitleList.size() > 0 ? fragmentTitleList.get(position) : "";
//            / Generate title based on item position
        // return tabTitles[position];
//        }

    }
}
