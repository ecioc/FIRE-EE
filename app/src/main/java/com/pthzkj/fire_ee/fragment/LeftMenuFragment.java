package com.pthzkj.fire_ee.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pthzkj.fire_ee.R;


/**
 * Created by Administrator on 2016/6/6.
 */
public class LeftMenuFragment extends Fragment {

    private int[] btn_id = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8};
    private Button[] btn = new Button[btn_id.length];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < btn.length; i++) {
            btn[i] = (Button) getActivity().findViewById(btn_id[i]);
            btn[i].setOnClickListener(btnClicks);
        }
    }

    View.OnClickListener btnClicks  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            switch (v.getId()) {
                case R.id.btn1:
                    break;
                case R.id.btn2:
                    break;
                case R.id.btn3:
                    break;
                case R.id.btn4:
                    break;
                case R.id.btn5:
                    break;
                case R.id.btn6:
                    break;
                case R.id.btn7:
//                    RightContentFragment firstFragment = new RightContentFragment();
//                    Bundle args1 = new Bundle();
//                    firstFragment.setArguments(args1);
//                    transaction.add(R.id.main_center_framelayout, firstFragment).commit();
                    break;
                case R.id.btn8:
                    UnitPhoneFragment unitPhoneFragment = new UnitPhoneFragment();
                    Bundle args = new Bundle();
                    unitPhoneFragment.setArguments(args);
                    transaction.replace(R.id.main_center_framelayout, unitPhoneFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
            }
        }
    };

}
