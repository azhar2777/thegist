/*
 *
 */

package com.thegistapp.thegistapp.customlists;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.activity.MainCategoryActivity;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.model.MainCategoryData;

import java.util.List;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>{

    private String TAG = "CategoryAdapter";
    private List<MainCategoryData> mainCategoryData;
    private Context mContext;

    public MainCategoryAdapter(Context context, List<MainCategoryData> mainCategoryData){
        this.mContext = context;
        this.mainCategoryData = mainCategoryData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(mContext).inflate(R.layout.items_main_catgory, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder called");
        final MainCategoryData mainCategory = this.mainCategoryData.get(position);
        final String imageUrl	=	mainCategory.getCatImage();




        final ViewHolder finalHolder = viewHolder;
        Glide.with(mContext.getApplicationContext())
                .load(""+ Consts.MAIN_CATEGORY+imageUrl)
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
                .into(viewHolder.ivmainCategory);

        viewHolder.ivmainCategory.setClipToOutline(true);

        viewHolder.tvCategoryName.setText(""+mainCategory.getCatName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int parentCategory  =   mainCategory.getId();

                String parentCategoryName = mainCategory.getCatName();

                /*Intent intentCategory = new Intent(mContext	,
                        CategorySelectionActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("parent_category_id", parentCategory);
                bundle.putString("parent_category_name", mainCategory.getCatName());
                intentCategory.putExtras(bundle);
                mContext.startActivity(intentCategory);*/
                ((MainCategoryActivity) mContext).onMainCategoryClick(parentCategory, parentCategoryName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainCategoryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivmainCategory;
        TextView tvCategoryName;
        ConstraintLayout clMainCategory;

        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clMainCategory =    itemView.findViewById(R.id.cl_main_category);
            ivmainCategory =   itemView.findViewById(R.id.iv_main_category);
            tvCategoryName  =   itemView.findViewById(R.id.tv_main_cat_name);


        }
    }


}
