package com.thegistapp.thegistapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.async.SelectChildTask;
import com.thegistapp.thegistapp.async.SelectionTask;
import com.thegistapp.thegistapp.async.UserSelectionTask;
import com.thegistapp.thegistapp.customlists.ChildCategoryAdapter;
import com.thegistapp.thegistapp.listener.SelectedChildListener;
import com.thegistapp.thegistapp.listener.SelectionsListener;
import com.thegistapp.thegistapp.listener.UserSelectionsListener;
import com.thegistapp.thegistapp.model.ChildCategoryData;
import com.thegistapp.thegistapp.model.IconCategoryData;
import com.thegistapp.thegistapp.model.MainCategoryData;
import com.thegistapp.thegistapp.model.SelectedChild;
import com.thegistapp.thegistapp.model.Selections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thegistapp.thegistapp.customlists.CustomItemDecorator;
import com.thegistapp.thegistapp.customlists.IconCatOffsetDecoration;
import com.thegistapp.thegistapp.customlists.IconCategoryAdapter;
import com.thegistapp.thegistapp.customlists.MainCategoryAdapter;
import com.thegistapp.thegistapp.model.UserClass;
import com.thegistapp.thegistapp.model.UserSelections;
import com.thegistapp.thegistapp.util.Util;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 */
public class MainCategoryActivity extends AppCompatActivity implements SelectionsListener, SelectedChildListener, UserSelectionsListener {
    private Context mContext;
    private String TAG  =   "MainCategoryActivityTAG";

    private RecyclerView rcvIcon, rcvMain;
    private TextView tvPageTitle;
    private ArrayList<IconCategoryData> iconCategoryLists  =   new ArrayList<IconCategoryData>();
    private ArrayList<MainCategoryData> mainCategoryLists  =   new ArrayList<MainCategoryData>();

    private IconCategoryAdapter iconCategoryAdapter;
    private MainCategoryAdapter mainCategoryAdapter;


    private PopupWindow popUpChildcat;
    private TextView tvSelectedCat;
    private RecyclerView rcvChildCategory;
    private ImageView ivEvacado, ivControl, ivSelection, ivClosePopup;
    private ArrayList<ChildCategoryData> childCategories  =   new ArrayList<ChildCategoryData>();

    private ChildCategoryAdapter childCategoryAdapter;

    private String mDeviceId="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mContext    =   MainCategoryActivity.this;
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initView() {

        ivEvacado   =   findViewById(R.id.iv_avocado);
        ivControl   =   findViewById(R.id.iv_control);

        rcvIcon =   findViewById(R.id.rcv_icon_category);
        rcvMain =   findViewById(R.id.rcv_main_category);
        tvPageTitle =   findViewById(R.id.tv_page_title);
        tvPageTitle.setText(tvPageTitle.getText().toString().toUpperCase());

        popUpChildcat = new PopupWindow(mContext);


        ivEvacado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNews = new Intent(mContext, StoryListActivity.class);
                startActivity(intentNews);
                finish();
            }
        });

        ivControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentControl = new Intent(mContext, ControlActivity.class);
                startActivity(intentControl);
                finish();
            }
        });




        getSelections();
    }

    private void getSelections(){
        mainCategoryLists.clear();
        iconCategoryLists.clear();
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("action", "get_selection");
            final String requestBody = jsonBody.toString();

            SelectionTask selectionTask = new SelectionTask(mContext, requestBody);
            selectionTask.mListener   =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectionsCallBack(ArrayList<Selections> selections) {
        if(selections.size() > 0){
            int errorCode   =   selections.get(0).getErrorCode();
            if(errorCode ==0){
                String iconCatData =   selections.get(0).getIconCategoryData();
                String mainCatData =   selections.get(0).getMainCategoryData();

                try {
                    JSONArray arrIconCat    =   new JSONArray(iconCatData);
                    if(arrIconCat.length() > 0){
                        for (int i=0; i < arrIconCat.length(); i++){
                            IconCategoryData iconCategory = new IconCategoryData();
                            if(arrIconCat.getJSONObject(i) != null){
                                JSONObject iconObject   =   arrIconCat.getJSONObject(i);
                                iconCategory.setId(iconObject.optInt("id"));
                                iconCategory.setCatName(iconObject.optString("category_name"));
                                iconCategory.setCatImage(iconObject.optString("category_image"));
                                iconCategoryLists.add(iconCategory);
                            }

                        }
                    }

                    JSONArray arrMainCat    =   new JSONArray(mainCatData);
                    if(arrMainCat.length() > 0){
                        for (int i=0; i < arrMainCat.length(); i++){
                            MainCategoryData mainCategory = new MainCategoryData();
                            if(arrMainCat.getJSONObject(i) != null){
                                JSONObject mainCatObject   =   arrMainCat.getJSONObject(i);
                                mainCategory.setId(mainCatObject.optInt("id"));
                                mainCategory.setCatName(mainCatObject.optString("category_name").toUpperCase());
                                mainCategory.setCatImage(mainCatObject.optString("category_image"));
                                mainCategoryLists.add(mainCategory);
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Collections.reverse(iconCategoryLists);
                iconCategoryAdapter =  new IconCategoryAdapter(mContext, iconCategoryLists);

                rcvIcon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                rcvIcon.setAdapter(iconCategoryAdapter);
                IconCatOffsetDecoration itemDecoration = new IconCatOffsetDecoration(mContext,R.dimen.icon_cateory_offset);
                rcvIcon.addItemDecoration(itemDecoration);

       /* rcvIcon.setAdapter(iconCategoryAdapter);
        rcvIcon.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false));
        IconCatOffsetDecoration itemDecoration = new IconCatOffsetDecoration(mContext,R.dimen.icon_cateory_offset);
        rcvIcon.addItemDecoration(itemDecoration);*/

        /*rcvIcon.addItemDecoration(new CustomItemDecorator(8));
        rcvIcon.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
*/

                mainCategoryAdapter =  new MainCategoryAdapter(mContext, mainCategoryLists);
                rcvMain.setAdapter(mainCategoryAdapter);
                rcvMain.addItemDecoration(new CustomItemDecorator(16));
                rcvMain.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

                iconCategoryAdapter.notifyDataSetChanged();
                mainCategoryAdapter.notifyDataSetChanged();
            }
            else{
                Util.showMessageWithOk(mContext, ""+getString(R.string.no_category_found));
                return;
            }

        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }







    }


    public void onMainCategoryClick(int categoryId, String categoryName) {
        Log.v(TAG, "category id: "+categoryId);

        if (popUpChildcat == null || !popUpChildcat.isShowing()) {

            getChildCategory(categoryId);

            LayoutInflater layoutInflater = (LayoutInflater)
                    mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popup_selection, null);
            popUpChildcat = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);


            ConstraintLayout layoutMain   =   (ConstraintLayout) findViewById(R.id.cl_content);

            tvSelectedCat   =   (TextView) popupView.findViewById(R.id.selected_category);
            rcvChildCategory   =   (RecyclerView) popupView.findViewById(R.id.rcv_child_category);
            ivSelection   =   (ImageView) popupView.findViewById(R.id.iv_selection);
            ivClosePopup   =   (ImageView) popupView.findViewById(R.id.iv_close_sub_cat);
            ivSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCategory();
                }
            });
            ivClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeChildPopup();
                }
            });

            tvSelectedCat.setText(""+categoryName);







//            popUpChildcat.setFocusable(true);
//
//
//            popUpChildcat.setTouchable(true);
//            popUpChildcat.setOutsideTouchable(true);
            //popupSearch.setOutsideTouchable(false);
            popUpChildcat.showAtLocation(layoutMain, Gravity.CENTER_HORIZONTAL, 50, 100);
            popUpChildcat.update();


//            ivClosePopup.setVisibility(View.GONE);
            /*ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.v(TAG, "Close popup");
//                    if (popUpChildcat != null) {
//                        popUpChildcat.dismiss();
//                    }

                    closeChildPopup();
                }
            });*/

        }
        else {
            popUpChildcat.dismiss();
        }
    }

    private void getChildCategory(int parentId) {
        childCategories.clear();
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

    public void onChildCategoryClick(int categoryId) {
        childCategories.clear();
        Log.v(TAG,"id "+categoryId);
    }

    private void selectedCategory(){
        List<Integer> selectedCategories = new ArrayList<Integer>();
        String text = "";
        int []arrCategories = new int[50];
        int mCounter    =   0;
        for (ChildCategoryData model : childCategories) {
            if (model.isSelected()) {
                text += model.getId();
                selectedCategories.add(model.getId());
                //arrCategories[mCounter] =   model.getId();
            }
        }
        if(selectedCategories.size() > 2){
            Util.showMessageWithOk(mContext, ""+getString(R.string.can_not_select_more_than_two_cat));
            return;
        }
        else if(selectedCategories.size()<= 0){
            Util.showMessageWithOk(mContext, ""+getString(R.string.select_any_two_cat));
            return;
        }


        JSONObject objSelection =   new JSONObject();
        JSONArray arrCategoriesSelected = new JSONArray(selectedCategories);
        mDeviceId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            objSelection.put("categories", arrCategoriesSelected);
            objSelection.put("action", "set_user_selection");
            objSelection.put("device_id", ""+mDeviceId);
            String userId   =   Util.fetchUserClass(mContext).getUserId();
                objSelection.put("user_id", ""+userId);


            String requestSelection  =  objSelection.toString();

            Log.v(TAG, requestSelection);

            UserSelectionTask userSelectionTask = new UserSelectionTask(mContext, requestSelection);
            userSelectionTask.mListener = this;

        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* Intent intent = new Intent(mContext, NavActivity.class);
        intent.putIntegerArrayListExtra("categories_selected", (ArrayList<Integer>)selectedCategories);
        startActivity(intent);*/

    }

    @Override
    public void userSelectionsCallBack(ArrayList<UserSelections> userSelections) {
        if(userSelections.size() > 0){
            int errorCode = userSelections.get(0).getErrorCode();
            if(errorCode ==0){

                String userId  =   Util.fetchUserClass(mContext).getUserId();
                String name     =   Util.fetchUserClass(mContext).getUserFullName();

                UserClass userClass   =   new UserClass();
                userClass.setUserId(""+userId);
                userClass.setUserFullName(""+name);
                userClass.setLoggedIn(true);
                userClass.setHasSelection(true);
                Util.saveUserClass(mContext, userClass);

                Intent intent = new Intent(mContext, StoryListActivity.class);
                startActivity(intent);
//                int userId = userSelections.get(0).getUserId();
//                if(userId > 0){
//                    UserClass userClass   =   new UserClass();
//                    userClass.setUserId(""+userId);
//                    userClass.setUserFullName("");
//                    userClass.setLoggedIn(true);
//                    Util.saveUserClass(mContext, userClass);
//
//                    Intent intent = new Intent(mContext, NavActivity.class);
//                    startActivity(intent);
//
//                }
            }

        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }

    public void closeChildPopup(){
        Log.v(TAG,"closeChildPopup ");
//        if (popUpChildcat == null || !popUpChildcat.isShowing()) {
        popUpChildcat.dismiss();
    }
}
