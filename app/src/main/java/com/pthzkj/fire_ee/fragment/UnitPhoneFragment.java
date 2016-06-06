package com.pthzkj.fire_ee.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pthzkj.fire_ee.R;
import com.pthzkj.fire_ee.deploy.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/6/6.
 */
public class UnitPhoneFragment extends Fragment {

    private ListView listView;

    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

    private ArrayList<HashMap<String, Object>> fixList = new ArrayList<>();

    private SimpleAdapter simpleAdapter;

    private SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unit_phone, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUrl();
        init();
        searchUnitPhoneKey();
    }

    private void init(){
        listView = (ListView) getActivity().findViewById(R.id.unit_phone_list);
        searchView = (SearchView) getActivity().findViewById(R.id.searchView);
    }

    /**
     * get unit_phone data
     */
    private void getUrl(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.URL + Common.FIRST_INTERFACE + "getUnitPhone", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    JSONArray jsonArray = response.getJSONArray("list");
                    Log.d(Common.TAG, "onSuccess: " + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("unitPhoneName", jsonArray.getJSONObject(i).getString("opName"));
                        map.put("unitPhoneNumber", jsonArray.getJSONObject(i).getString("opPhone"));
                        map.put("unitPhoneId", jsonArray.getJSONObject(i).getString("opId"));
                        arrayList.add(map);
                        fixList.add(map);
                    }
                    simpleAdapter = new SimpleAdapter(getActivity(), arrayList,
                            R.layout.unit_phone_list_item,
                            new String[]{"unitPhoneName", "unitPhoneNumber", "unitPhoneId"},
                            new int[]{R.id.recyclerview_center_fragment_list_item_title, R.id.recyclerview_center_fragment_list_item_phone, R.id.recyclerview_center_fragment_list_item_id});
                    listView.setAdapter(simpleAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView temp = (TextView) view.findViewById(R.id.recyclerview_center_fragment_list_item_phone);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            UnitPhoneContentFragment unitPhoneContentFragment = new UnitPhoneContentFragment();
                            Bundle args = new Bundle();
                            args.putString("unitPhoneNumber", temp.getText().toString());
                            unitPhoneContentFragment.setArguments(args);
                            transaction.replace(R.id.main_right_framelayout, unitPhoneContentFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     *  search unit_phone key
     */
    private void searchUnitPhoneKey(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchItem(newText);
                return false;
            }
        });
    }

    public void searchItem(String name) {
        for (int i = 0; i < fixList.size(); i++) {
            int index = fixList.get(i).get("unitPhoneName").toString().indexOf(name);
            Log.d(Common.TAG, "index:" + index);
            // 存在匹配的数据
            if (index != -1) {
                arrayList.clear();
                arrayList.add(fixList.get(i));
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }
}
