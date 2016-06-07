package com.pthzkj.fire_ee.fragment;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.SupportMapFragment;
import com.pthzkj.fire_ee.R;
import com.pthzkj.fire_ee.customView.CircleImageView;
import com.pthzkj.fire_ee.deploy.Common;
import com.pthzkj.fire_ee.util.NoDoubleClickUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Administrator on 2016/6/6.
 */
public class LeftMenuFragment extends Fragment {

    private ListView left_menu_list;

    private LinearLayout head_image_layout;

    private CircleImageView head_image;

    //用来判断点击事件
    private int[] everClick = {0, 0, 0, 0, 0, 0};

    private int width, height;

    private FrameLayout center_framelayout;

    private FrameLayout right_framelayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        center_framelayout = (FrameLayout) getActivity().findViewById(R.id.center_framelayout);
        right_framelayout = (FrameLayout) getActivity().findViewById(R.id.right_framelayout);
        left_menu_list = (ListView) getActivity().findViewById(R.id.left_menu_list);
        head_image_layout = (LinearLayout) getActivity().findViewById(R.id.head_image_layout);
        head_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(everClick[0] == 0){
                    initEverClick();
                    head_image_layout.setBackgroundColor(Color.parseColor("#11ADE8"));
                    Toast.makeText(getActivity(), "头像", Toast.LENGTH_SHORT).show();
                    everClick[0] = 1;
                }
            }
        });
        head_image = (CircleImageView) getActivity().findViewById(R.id.head_image);
//        head_image.setImageResource(R.drawable.cldwdh);
        initMenuList();

    }

    private void initMenuList(){
        Integer[] images = {R.drawable.i5, R.drawable.cldwdh, R.drawable.xfsxt, R.drawable.zddwya, R.drawable.zhlxcz, R.drawable.sybz,};
        String[] menus = {"首页", "定位导航", "消防栓", "单位预案", "灾害类型", "使用帮助"};
        ArrayList<HashMap<String, Object>> menuList = new ArrayList<>();
        SimpleAdapter simpleAdapter;
        for (int i = 0; i < menus.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemName", menus[i]);
            map.put("itemImage", images[i]);
            menuList.add(map);
        }
        simpleAdapter = new SimpleAdapter(getActivity(), menuList,
                R.layout.left_menu_list_item,
                new String[]{"itemImage", "itemName"},
                new int[]{R.id.left_menu_list_item_image_view, R.id.left_menu_list_item_text_view});
        left_menu_list.setAdapter(simpleAdapter);

        left_menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NoDoubleClickUtils.isDoubleClick()) {
//                    Intent intent = new Intent();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    switch (position) {
                        case 0://首页
//                            intent.setClass(DispatchEditActivity.this, FireControlActivity.class);
//                            startActivity(intent);
                            initEverClick();
                            Toast.makeText(getActivity(), "首页", Toast.LENGTH_SHORT).show();
                            break;
                        case 1://定位导航
//                            intent.setClass(DispatchEditActivity.this, DisasterActivity.class);
//                            startActivity(intent);
                            if(everClick[1] == 0){
                                initEverClick();
                                setSingleFramelayout();
                                Toast.makeText(getActivity(), "定位导航", Toast.LENGTH_SHORT).show();
                                everClick[1] = 1;

                                NavigationFragment navigationFragment = new NavigationFragment();
//
//                                MapStatus ms = new MapStatus.Builder().overlook(-20).zoom(15).build();
//                                BaiduMapOptions bo = new BaiduMapOptions().mapStatus(ms)
//                                        .compassEnabled(false).zoomControlsEnabled(false);
//                                SupportMapFragment map = SupportMapFragment.newInstance(bo);
//                                FragmentManager manager = getSupportFragmentManager();
//                                manager.beginTransaction().add(R.id.map, map, "map_fragment").commit();

                                Bundle args = new Bundle();
                                navigationFragment.setArguments(args);
                                transaction.replace(R.id.center_framelayout, navigationFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                            break;
                        case 2://消防栓
//                            intent.setClass(DispatchEditActivity.this, UnitPlanActivity.class);
//                            startActivity(intent);
                            if(everClick[2] == 0){
                                initEverClick();
                                setSingleFramelayout();
                                Toast.makeText(getActivity(), "消防栓", Toast.LENGTH_SHORT).show();
                                everClick[2] = 1;
                            }
                            break;
                        case 3://单位预案
//                            intent.setClass(DispatchEditActivity.this, UnitPhoneActivity.class);
//                            intent.putExtra("result", isUpdateContent("t_unit_phone_version", "t_unit_phone"));
//                            startActivity(intent);
                            if(everClick[3] == 0){
                                initEverClick();
                                setMultipleFramelayout();
                                Toast.makeText(getActivity(), "单位预案", Toast.LENGTH_SHORT).show();
                                everClick[3] = 1;
                            }
                            break;
                        case 4://灾害类型
                            if(everClick[4] == 0){
                                initEverClick();
                                setMultipleFramelayout();
                                Toast.makeText(getActivity(), "灾害类型", Toast.LENGTH_SHORT).show();
                                everClick[4] = 1;
                            }
                            break;
                        case 5://使用帮助
                            if(everClick[5] == 0){
                                initEverClick();
                                setMultipleFramelayout();
                                Toast.makeText(getActivity(), "使用帮助", Toast.LENGTH_SHORT).show();
                                everClick[5] = 1;

                                UnitPhoneFragment unitPhoneFragment = new UnitPhoneFragment();
                                Bundle args = new Bundle();
                                unitPhoneFragment.setArguments(args);
                                transaction.replace(R.id.center_framelayout, unitPhoneFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
        });
    }
    private void initEverClick(){
        for (int i = 0; i < everClick.length; i++) {
            if(everClick[0] == 1){
                head_image_layout.setBackgroundColor(Color.parseColor("#12B7F5"));
            }
            everClick[i] = 0;
        }
    }

    View.OnClickListener btnClicks  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            switch (v.getId()) {
//                case R.id.btn1:
//                    break;
//                case R.id.btn2:
//                    break;
//                case R.id.btn3:
//                    break;
//                case R.id.btn4:
//                    break;
//                case R.id.btn5:
//                    break;
//                case R.id.btn6:
//                    break;
//                case R.id.btn7:
////                    RightContentFragment firstFragment = new RightContentFragment();
////                    Bundle args1 = new Bundle();
////                    firstFragment.setArguments(args1);
////                    transaction.add(R.id.main_center_framelayout, firstFragment).commit();
//                    break;
//                case R.id.btn8:
//                    UnitPhoneFragment unitPhoneFragment = new UnitPhoneFragment();
//                    Bundle args = new Bundle();
//                    unitPhoneFragment.setArguments(args);
//                    transaction.replace(R.id.main_center_framelayout, unitPhoneFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    break;
//            }
        }
    };

    private void init(){
        WindowManager wm = getActivity().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        Log.d(Common.TAG, "width:" + width + "--- height:" + height);
    }

    /**
     * 设置右侧单个framelayout
     */
    private void setSingleFramelayout(){
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) center_framelayout.getLayoutParams();
        linearParams.width = width - 85;
        center_framelayout.setLayoutParams(linearParams);
        right_framelayout.setVisibility(View.GONE);
    }

    /**
     * 设置右侧两个framelayout
     */
    private void setMultipleFramelayout(){
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) center_framelayout.getLayoutParams();
        linearParams.width = 410;
        center_framelayout.setLayoutParams(linearParams);
        center_framelayout.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) right_framelayout.getLayoutParams();
        linearParams2.width = width - 85 - 410;
        right_framelayout.setLayoutParams(linearParams2);
        right_framelayout.setVisibility(View.VISIBLE);
    }

}
