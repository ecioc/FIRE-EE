package com.pthzkj.fire_ee.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;


/****
 * map 操作类
 * 
 * @author 庄志潮
 * 
 */
public class MapUtil {

	/****
	 * 
	 * @param o
	 *            javaBean
	 * @param map
	 * @return
	 */
//	public static Object getMapToObject(Object o, Map<String, Object> map) {
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			// System.out.println("Key = " + entry.getKey() + ", Value = "+
//			// entry.getValue());
//			Field[] f = o.getClass().getDeclaredFields();
//			for (int i = 0; i < f.length; i++) {
//				DAORunner.invokeSet(o, entry.getKey(), entry.getValue());
//			}
//		}
//		return o;
//	}

	/**
	 * Json 转成 Map<>
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> getMapForJson(String jsonStr) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonStr);

			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (Exception e) {
			e.printStackTrace();
			// Log.e(HttpClientUtils.TAG, e.toString());
		}
		return null;
	}

	/**
	 * Json 转成 List<Map<>>
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static List<Map<String, Object>> getlistForJson(String jsonStr) {
		List<Map<String, Object>> list = null;
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			JSONObject jsonObj;
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = (JSONObject) jsonArray.get(i);
				list.add(getMapForJson(jsonObj.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取登录者信息
	 * 
	 * @return
	 */
	public static Map<String, String> getPreferences(Context context) {
		SharedPreferences pre = context.getSharedPreferences("account",
				Context.MODE_PRIVATE);
		// 如果得到的name没有值则设置为空 pre.getString("name", "");
		Map<String, String> params = new HashMap<String, String>();
		params.put("opId", pre.getString("op_id", ""));
		params.put("opName", pre.getString("op_name", ""));
		params.put("opRole", pre.getString("op_role", ""));
		return params;
	}
   /**
   图文并茂
   */
	 public static Bitmap getBitmapFromView(View view) {
		  view.destroyDrawingCache();
		  view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
		    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		  view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		  view.setDrawingCacheEnabled(true);
		  Bitmap bitmap = view.getDrawingCache(true);
		  return bitmap;
	}
}
