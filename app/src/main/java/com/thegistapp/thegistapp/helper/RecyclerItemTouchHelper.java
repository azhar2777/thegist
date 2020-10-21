package com.thegistapp.thegistapp.helper;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.thegistapp.thegistapp.customlists.StoryListAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
    private String TAG = "ItemSwipeHelper";
    private RecyclerView recyclerView;
    public boolean showPopup = false;
    private float mDX = 0;
    private float swipeDX=0;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, RecyclerView recyclerView) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((StoryListAdapter.ViewHolder) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((StoryListAdapter.ViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);




    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((StoryListAdapter.ViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);

        Log.v(TAG, "clearView Called"+foregroundView.getVisibility());
//        if(foregroundView.getVisibility()== View.VISIBLE){
//            listener.onClearView(recyclerView, viewHolder);
//        }


    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            final int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((StoryListAdapter.ViewHolder) viewHolder).viewForeground;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX/2, dY,
                actionState, isCurrentlyActive);

        Log.v(TAG, "onChildDraw1 Called"+dX);
        mDX = dX;
        swipeDX = mDX/2;
        if(dX==0){
            showPopup = false;
            listener.onClearView(recyclerView, viewHolder, showPopup, mDX, swipeDX);
        }
        else{
            showPopup = true;
            listener.onClearView(recyclerView, viewHolder, showPopup, mDX, swipeDX);
        }



        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive){
            Log.v(TAG+"1", "SWIPED "+isCurrentlyActive);

        }
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        Log.v(TAG, "onSwiped called");
        if(mDX ==0){
            listener.onClearView(recyclerView, viewHolder, showPopup, mDX, swipeDX);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
        void onClearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, boolean showPopup, float dX, float swipeDX);
        void hidePopup(float dx);


    }


}