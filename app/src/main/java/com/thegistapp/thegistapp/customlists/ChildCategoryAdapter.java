/*
 *
 */

package com.thegistapp.thegistapp.customlists;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.model.ChildCategoryData;

import java.util.List;


public class ChildCategoryAdapter extends RecyclerView.Adapter<ChildCategoryAdapter.ViewHolder>{

    private String TAG = "ChildCategoryAdapter";
    private List<ChildCategoryData> childCategoryData;
    private Context mContext;
    private boolean isSelected = false;
    private int lastSelectedPosition = -1;
    private int itemsSelected = 0;

    public ChildCategoryAdapter(Context context, List<ChildCategoryData> childCategoryData, int selectedCount){
        this.mContext = context;
        this.childCategoryData = childCategoryData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(mContext).inflate(R.layout.items_category_selection, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {


        Log.d(TAG, "onBindViewHolder called");
        final ChildCategoryData childCategory = childCategoryData.get(position);

        viewHolder.tvCategoryName.setText(""+childCategory.getCatName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int categoryId  =   0;
                childCategory.setSelected(!childCategory.isSelected());
                if(childCategory.isSelected()){
                    /*itemsSelected += 1;
                    if(itemsSelected <= 2) {
                        viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.category_selected_drawable));
                        viewHolder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                        categoryId = childCategory.getId();
                    }
                    else {
                        itemsSelected -= 1;
                        return;
                    }*/
                    viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.category_selected_drawable));
                    viewHolder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                else{
                    /*itemsSelected -= 1;
                    viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.category_selection_rounded_border));
                    viewHolder.tvCategoryName.setTextColor(mContext.getResources().getColorStateList(R.color.category_selection_text_color));
                    if(itemsSelected < 0){
                        itemsSelected = 0;
                    }*/
                    viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.category_selection_rounded_border));
                    viewHolder.tvCategoryName.setTextColor(mContext.getResources().getColorStateList(R.color.category_selection_text_color));



                }
                //viewHolder.itemView.setBackgroundColor(childCategory.isSelected() ? Color.CYAN : Color.WHITE);

                //((MainCategoryActivity) mContext).onChildCategoryClick(categoryId);
                lastSelectedPosition = viewHolder.getAdapterPosition();

            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });



    }

    @Override
    public int getItemCount() {

        return childCategoryData == null ? 0 : childCategoryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategoryName;
        ConstraintLayout clMainCategory;

        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName  =   itemView.findViewById(R.id.tv_child_category);


        }
    }
}
