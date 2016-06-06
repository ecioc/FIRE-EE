package com.pthzkj.fire_ee.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pthzkj.fire_ee.R;
import com.pthzkj.fire_ee.deploy.Common;

/**
 * Created by Ecioc on 2016/6/6.
 */
public class UnitPhoneContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_unit_phone, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String unitPhoneNumber = getArguments().getString("unitPhoneNumber");
        TextView textView = (TextView) getActivity().findViewById(R.id.content_unit_phone_view);
        textView.setText(unitPhoneNumber);
    }
}
