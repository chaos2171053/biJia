package bijia.com.bijia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import bijia.com.bijia.activity.LoginActivity;

/**
 * Created by user on 2015/11/6.
 */
public class GuideActivity extends Activity implements View.OnClickListener
{

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();
    // TAB

    private LinearLayout mTabWeixin;
    private LinearLayout mTabFrd;
    private LinearLayout mTabAddress;
    private LinearLayout mTabSetting;

    private Button mEnterButton;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_guide);

        initView();

        initEvents();

    }

    private void initEvents()
    {


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
//                arg0 :当前页面，及你点击滑动的页面
//
//                arg1:当前页面偏移的百分比
//
//                arg2:当前页面偏移的像素位置
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
//                onPageScrollStateChanged(int arg0)   ，此方法是在状态改变的时候调用，其中arg0这个参数
//                有三种状态（0，1，2）。arg0 ==1的时正在滑动，arg0==2的时示滑动完毕了，arg0==0的时什么都没做。
            }
        });
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);


        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab01 = mInflater.inflate(R.layout.tab01, null);
        View tab02 = mInflater.inflate(R.layout.tab02, null);
        View tab03 = mInflater.inflate(R.layout.tab03, null);
        View tab04 = mInflater.inflate(R.layout.tab04, null);
        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);
        mViews.add(tab04);
        mEnterButton=(Button)tab04.findViewById(R.id.imgbtn_enter);
        mEnterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        mAdapter = new PagerAdapter()
        {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {
                container.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }

            @Override
            public int getCount()
            {
                return mViews.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {

    }


}
