package com.example.administrator.counter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-09-02.
 */
public class MiniTabFragment extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ImageButton mButtonAction1;
    private ImageButton mButtonAction2;
    private ImageButton mButtonAction3;
    private ImageButton mButtonAction4;
    private ImageButton mButtonAction5;
    private ImageButton mButtonAction6;

    private String MAC_ADDR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        MAC_ADDR = getArguments().getString("mac_addr");
        return inflater.inflate(R.layout.fragment_mini_tabs, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MiniPagerAdapter());

        Log.d("MiniTabFragment", getArguments().getString("mac_addr"));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }


    class MiniPagerAdapter extends PagerAdapter {
        @Override
        public int getCount(){
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o){
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return "Item" + (position+1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            TextView title;
            switch (position) {
                case 0: // 첫번째 탭
                    view = getActivity().getLayoutInflater().inflate(
                            R.layout.paper_item, container, false);
                    // 새로 생성한 뷰 container(ViewPager)에 삽입
                    container.addView(view);

                    // 뷰 동작 설정
                    title = (TextView) view.findViewById(R.id.item_title);
                    title.setText(String.valueOf(position + 1));

                    mButtonAction1 = (ImageButton) view.findViewById(R.id.action1);
                    mButtonAction2 = (ImageButton) view.findViewById(R.id.action2);
                    mButtonAction3 = (ImageButton) view.findViewById(R.id.action3);
                    mButtonAction4 = (ImageButton) view.findViewById(R.id.action4);
                    mButtonAction5 = (ImageButton) view.findViewById(R.id.action5);
                    mButtonAction6 = (ImageButton) view.findViewById(R.id.action6);


                    // 첫번째 버튼
                    mButtonAction1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getActivity(), Count.class);
                            intent.putExtra("mac_addr", MAC_ADDR);
                            intent.putExtra("flag", "set");
                            startActivity(intent);
                        }
                    });

                    // 두번째 버튼
                    mButtonAction2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Stopwatch.class);
                            intent.putExtra("mac_addr", MAC_ADDR);
                            intent.putExtra("flag", "set");
                            startActivity(intent);
                        }
                    });

                    // 세번째 버튼
                    mButtonAction3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Alarm.class);
                            intent.putExtra("mac_addr", MAC_ADDR);
                            intent.putExtra("flag", "set");
                            startActivity(intent);
                        }
                    });

                    // 네번째 버튼
                    mButtonAction4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DownTimer.class);
                            intent.putExtra("mac_addr", MAC_ADDR);
                            intent.putExtra("flag", "set");
                            startActivity(intent);
                        }
                    });

                    // 다섯번째 버튼
                    mButtonAction5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Message.class);
                            intent.putExtra("mac_addr", MAC_ADDR);
                            intent.putExtra("flag", "set");
                            startActivity(intent);
                        }
                    });

                    // 여섯번째 버튼
                    mButtonAction6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getActivity(), Check.class);
                            intent.putExtra("mac_addr", MAC_ADDR);
                            intent.putExtra("flag", "set");
                            startActivity(intent);
                        }
                    });

                    break;

                case 1: // 두번째 탭
                    view = getActivity().getLayoutInflater().inflate(
                            R.layout.paper_item, container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    // Retrieve a TextView from the inflated View, and update it's text
                    title = (TextView) view.findViewById(R.id.item_title);
                    title.setText(String.valueOf(position + 1));

                    break;

                case 2: // 세번째 탭
                    view = getActivity().getLayoutInflater().inflate(
                            R.layout.paper_item, container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    // Retrieve a TextView from the inflated View, and update it's text
                    title = (TextView) view.findViewById(R.id.item_title);
                    title.setText(String.valueOf(position + 1));

                    break;

                default:
                    view = getActivity().getLayoutInflater().inflate(
                            R.layout.paper_item, container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    // Retrieve a TextView from the inflated View, and update it's text
                    title = (TextView) view.findViewById(R.id.item_title);
                    title.setText(String.valueOf(position + 1));

                    break;
            }

            // Return the View
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((View)object);
        }

    }

}
