/*
 *
 */

package com.thegistapp.thegistapp.customlists;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.activity.StoryListActivity;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.model.IconCategoryData;

import java.util.List;


public class IconCategoryAdapter extends RecyclerView.Adapter<IconCategoryAdapter.ViewHolder>{

    private String TAG = "CategoryAdapter";

    private static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";

    private List<IconCategoryData> iconCategoryData;
    private Context mContext;

    public IconCategoryAdapter(Context context, List<IconCategoryData> iconCategoryData){
        this.mContext = context;
        this.iconCategoryData = iconCategoryData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(mContext).inflate(R.layout.items_icon_catgory, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder called");
        final IconCategoryData iconCategory = iconCategoryData.get(position);
        final String imageUrl	=	iconCategory.getCatImage();

        final int iconCategoryid    =   iconCategory.getId();




        final ViewHolder finalHolder = viewHolder;
        Glide.with(mContext.getApplicationContext())
                .load(""+ Consts.ICON_CATEGORY+imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        //finalHolder.llProduct.setVisibility(View.VISIBLE);
                        //finalHolder.llProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(viewHolder.ivIconCategory);

        viewHolder.tvCategoryName.setText(""+iconCategory.getCatName());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intentNav = new Intent(mContext	,
                        NavActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("icon_cat", iconCategoryid);
                intentNav.putExtras(bundle);
                mContext.startActivity(intentNav);*/


                /*StoryFragment detailFragment = StoryFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_fragment_container, detailFragment, "TAG_DETAIL_FRAGMENT")
                        .commit();*/



                /*AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new StoryFragment();
                myFragment.setArguments(bundle);

                FragmentManager fragmentManager =   activity.get();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, myFragment, "TAG_DETAIL_FRAGMENT").addToBackStack(null).commit();

                fragmentManager.beginTransaction()
                        .add(viewHolder.R.id.detail_fragment_container, myFragment, TAG_DETAIL_FRAGMENT)
                        .commit();*/

                Log.v("StoryFragmentTAG", "icon_catgeory_id"+iconCategoryid);



                /*Fragment myFragment = StoryFragment.newInstance(iconCategoryid);
                Bundle bundle = new Bundle();
                bundle.putInt("icon_cat", iconCategoryid);
                myFragment.setArguments(bundle);
                myFragment.getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container,myFragment, TAG_DETAIL_FRAGMENT)
                        .addToBackStack(null).commit();*/




               /* AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new StoryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("icon_cat", iconCategoryid);
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container, myFragment, TAG_DETAIL_FRAGMENT).addToBackStack(null).commit();

//                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.detail_fragment_container, myFragment, TAG_DETAIL_FRAGMENT).addToBackStack(null).commit();
//
*/

               Intent intentNews = new Intent(mContext, StoryListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("icon_cat", iconCategoryid);
                bundle.putString("icon_cat_name", iconCategory.getCatName());
                intentNews.putExtras(bundle);
                mContext.startActivity(intentNews);


            }
        });
    }

    @Override
    public int getItemCount() {
        return iconCategoryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivIconCategory;
        TextView tvCategoryName;
        LinearLayout llProductWrapper, llProgress;

        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIconCategory  =   itemView.findViewById(R.id.iv_icon_category);
            tvCategoryName  =   itemView.findViewById(R.id.ic_cat_name);


        }
    }
}
