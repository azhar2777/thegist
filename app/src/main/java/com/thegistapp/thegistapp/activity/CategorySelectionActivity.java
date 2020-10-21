package com.thegistapp.thegistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.async.SelectChildTask;
import com.thegistapp.thegistapp.customlists.ChildCategoryAdapter;
import com.thegistapp.thegistapp.customlists.CustomItemDecorator;
import com.thegistapp.thegistapp.listener.SelectedChildListener;
import com.thegistapp.thegistapp.model.ChildCategoryData;
import com.thegistapp.thegistapp.model.SelectedChild;
import com.thegistapp.thegistapp.util.KpiInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CategorySelectionActivity extends AppCompatActivity implements SelectedChildListener {
    private Context mContext;
    private TextView tvSelectedCat;
    private RecyclerView rcvChildCategory;
    private ArrayList<ChildCategoryData> childCategories  =   new ArrayList<ChildCategoryData>();

    private ChildCategoryAdapter childCategoryAdapter;
    private int parentId;
    private String catName="";
    SharedPreferences settings;
    String CONTROL_PREFS = "";

    KpiInfo kpiInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        mContext    =   CategorySelectionActivity.this;

        kpiInfo =   new KpiInfo(mContext);

        tvSelectedCat   =   findViewById(R.id.selected_category);
        rcvChildCategory   =   findViewById(R.id.rcv_child_category);

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("parent_category_id"))
            parentId  =   bundle.getInt("parent_category_id");
        if(getIntent().hasExtra("parent_category_name")) {
            catName = bundle.getString("parent_category_name");
            if(!catName.equals("")){
                tvSelectedCat.setText("#"+catName.toUpperCase());
            }
        }
        getChildCategory(parentId);
    }

    private void getChildCategory(int parentId) {
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("action", "get_child");
            jsonBody.put("parent_cat", parentId);
            final String requestBody = jsonBody.toString();

            SelectChildTask selectChildTask = new SelectChildTask(mContext, requestBody);
            selectChildTask.mListener   =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectedChildCallBack(ArrayList<SelectedChild> selectChild) {
        if(selectChild.size() > 0){
            int errorCode   =   selectChild.get(0).getErrorCode();
            if(errorCode ==0){
                String categoryData =   selectChild.get(0).getChildCategoryData();

                try {
                    JSONArray arrCategories    =   new JSONArray(categoryData);
                    if(arrCategories.length() > 0){
                        for (int i=0; i < arrCategories.length(); i++){
                            ChildCategoryData childCategoryData = new ChildCategoryData();
                            if(arrCategories.getJSONObject(i) != null){
                                JSONObject iconObject   =   arrCategories.getJSONObject(i);
                                childCategoryData.setId(iconObject.optInt("id"));
                                childCategoryData.setCatName(iconObject.optString("category_name"));
                                childCategoryData.setCatImage(iconObject.optString("category_image"));
                                childCategories.add(childCategoryData);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        childCategoryAdapter =  new ChildCategoryAdapter(mContext, childCategories, 0);
        rcvChildCategory.setAdapter(childCategoryAdapter);
        rcvChildCategory.addItemDecoration(new CustomItemDecorator(16));
        rcvChildCategory.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        CONTROL_PREFS = ""+getResources().getString(R.string.gist_control_prefs);
        settings = getSharedPreferences(CONTROL_PREFS, MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int currentSessonId    = settings.getInt("current_sesson_id", 0);
        if(currentSessonId > 0){
            kpiInfo.endSesson(currentSessonId, 0);
        }
    }
}
