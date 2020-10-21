/*
 *
 */

package com.thegistapp.thegistapp.customlists;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.activity.StoryListActivity;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.custom.BlurBuilder;
import com.thegistapp.thegistapp.model.StoryListData;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.ViewHolder>{

    private String TAG = "StoryListAdapter";
    private List<StoryListData> storyLists;
    private Context mContext;

   private String userFontSize;
    int storyTitleDefaultFontSize, storyContentDefaultFontSize;


    HashMap<Integer,ViewHolder> holderlist;

    public StoryListAdapter(Context context, List<StoryListData> storyLists, String userFontSize){
        this.mContext = context;
        this.storyLists = storyLists;
        this.userFontSize = userFontSize;
        holderlist = new HashMap<>();



        storyTitleDefaultFontSize = (int) (mContext.getResources().getDimension(R.dimen.story_title_fsize) / mContext.getResources().getDisplayMetrics().scaledDensity);
        storyContentDefaultFontSize = (int) (mContext.getResources().getDimension(R.dimen.story_content_fsize) / mContext.getResources().getDisplayMetrics().scaledDensity);
        Log.v(TAG, "storyTitleDefaultFontSize "+storyTitleDefaultFontSize);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_story_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        Log.d(TAG, "onBindViewHolder called");

        if(!holderlist.containsKey(position)){
            holderlist.put(position,viewHolder);
        }


        viewHolder.viewBackground.setTag(position);
        viewHolder.viewBackgroundBottom.setTag(position+1000);
        viewHolder.viewBackgroundBottom.setId(position+1000);

        viewHolder.ivBookmark.setTag(position);



        final StoryListData currentStoryData = storyLists.get(position);
        Log.v("StoryAdapter", currentStoryData.getStoryTitle());

        viewHolder.tvStoryTitle.setText(""+currentStoryData.getStoryTitle());
        viewHolder.tvContent.setText(""+currentStoryData.getStoryContent());
        String storyImageURL = "";
        final String videoURL = currentStoryData.getVideoLink();

        int isVideo = currentStoryData.getIsVideo();

        if( isVideo ==1 ){
            //Log.v(TAG, ""+currentStoryData.getIsVideo());


            if( URLUtil.isValidUrl(videoURL)){


                viewHolder.ivStory.setVisibility(View.GONE);
                viewHolder.clVideoYoutube.setVisibility(View.GONE);
                viewHolder.vwStory.setVisibility(View.VISIBLE);


                //String videoURL = "https://imasdk.googleapis.com/js/core/bridge3.391.0_en.html#goog_1686398574";
                Log.v("VideoURL", currentStoryData.getStoryId()+", " + videoURL);


                MediaController mediaController = new MediaController(mContext);
                mediaController.setAnchorView(viewHolder.vwStory);

                //specify the location of media file
                Uri uri = Uri.parse(videoURL);

                //Setting MediaController and URI, then starting the videoView
                viewHolder.vwStory.setMediaController(mediaController);
                viewHolder.vwStory.setVideoURI(uri);
                viewHolder.vwStory.start();
                viewHolder.vwStory.requestFocus();
                viewHolder.vwStory.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.d("video", "setOnErrorListener ");
                        return true;
                    }
                });

            }
            else {
                viewHolder.ivStory.setVisibility(View.GONE);
                viewHolder.vwStory.setVisibility(View.GONE);
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.clVideoYoutube.setVisibility(View.VISIBLE);

                viewHolder.clVideoYoutube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("Youtube Videos", "Youtube id "+videoURL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videoURL));
                        mContext.startActivity(intent);
                    }
                });




            }

        }
        else {
            //viewHolder.ivStory.setVisibility(View.VISIBLE);
            viewHolder.vwStory.setVisibility(View.GONE);
            viewHolder.clVideoYoutube.setVisibility(View.GONE);
             storyImageURL = currentStoryData.getStoryImage();



            String imageUrl = "" + storyImageURL;
            if(!storyImageURL.equals("")){
                storyImageURL = Consts.STORY_IMAGES + imageUrl;
            }
            /*Drawable imgDrawable = StoryListAdapter.LoadImageFromWebOperations(Consts.STORY_IMAGES + imageUrl);
            viewHolder.imageBG = drawableToBitmap(imgDrawable);
            //viewHolder.ivStory.setBackground(imgDrawable);
            Bitmap blurredBitmap = BlurBuilder.blur(mContext, viewHolder.imageBG);

            viewHolder.ivStory.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));*/


            Glide.with(mContext)
                    .asBitmap()
                    .load(storyImageURL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap blurredBitmap = BlurBuilder.blur(mContext, resource);

                            viewHolder.ivStory.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


             /*try {
               URL url = new URL(Consts.STORY_IMAGES + imageUrl);


                viewHolder.imageBG = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //BitmapDrawable ob = new BitmapDrawable(getResources(), imageBG);

                //clStoryImage.setBackground(ob);

                //ivStory.setBackground(ob);


                if(viewHolder.imageBG != null) {
                    Bitmap blurredBitmap = BlurBuilder.blur(mContext, viewHolder.imageBG);

                    viewHolder.ivStory.setBackground(new BitmapDrawable(mContext.getResources(), blurredBitmap));
                }



            } catch(IOException e) {
                System.out.println(e);
            }*/
            String storyimage = "" + currentStoryData.getStoryImage();
            Glide.with(mContext)
                    .load("" + Consts.STORY_IMAGES + storyimage)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            viewHolder.progressBar.setVisibility(View.GONE);
                            viewHolder.ivStory.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            viewHolder.progressBar.setVisibility(View.GONE);
                            viewHolder.ivStory.setVisibility(View.VISIBLE);
                            Log.v(TAG, "Image Ready");
                            return false;
                        }
                    })
                    .into(viewHolder.ivStory);
        }


        //============== Content Provider ==============
        String cpLogo1 = "" + currentStoryData.getContentProviderLogo1();
        String cpLogo2 = "" + currentStoryData.getContentProviderLogo2();
        String cpLogo3 = "" + currentStoryData.getContentProviderLogo3();

        if (cpLogo1.equals("")) {
            viewHolder.ivCP1.setVisibility(View.GONE);
            viewHolder.cvCp1.setVisibility(View.GONE);
        } else {

            viewHolder.ivCP1.setVisibility(View.VISIBLE);
            viewHolder.cvCp1.setVisibility(View.VISIBLE);
            //Log.v(TAG, "CP1= " + Consts.STORY_CONTENT_POROVIDERS + cpLogo1);
            Glide.with(mContext.getApplicationContext())
                    .load("" + Consts.STORY_CONTENT_POROVIDERS + cpLogo1)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(viewHolder.ivCP1);

            final String cp1URL = "" + currentStoryData.getContentProviderLink1();
            viewHolder.ivCP1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cp1URL));
                    mContext.startActivity(browserIntent);
                }
            });
        }


        if (cpLogo2.equals("")) {
            viewHolder.ivCP2.setVisibility(View.GONE);
            viewHolder.cvCp2.setVisibility(View.GONE);
        } else {

            viewHolder.ivCP2.setVisibility(View.VISIBLE);
            viewHolder.cvCp2.setVisibility(View.VISIBLE);

            //Log.v(TAG, "CP1= " + Consts.STORY_CONTENT_POROVIDERS + cpLogo1);
            Glide.with(mContext.getApplicationContext())
                    .load("" + Consts.STORY_CONTENT_POROVIDERS + cpLogo2)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(viewHolder.ivCP2);

            final String cp2URL = "" + currentStoryData.getContentProviderLink2();
            viewHolder.ivCP2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cp2URL));
                    mContext.startActivity(browserIntent);
                }
            });
        }


        if (cpLogo3.equals("")) {
            viewHolder.ivCP3.setVisibility(View.GONE);
            viewHolder.cvCp3.setVisibility(View.GONE);
        } else {

            viewHolder.ivCP3.setVisibility(View.VISIBLE);
            viewHolder.cvCp3.setVisibility(View.VISIBLE);
            //Log.v(TAG, "CP1= " + Consts.STORY_CONTENT_POROVIDERS + cpLogo1);
            Glide.with(mContext.getApplicationContext())
                    .load("" + Consts.STORY_CONTENT_POROVIDERS + cpLogo3)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(viewHolder.ivCP3);

            final String cp3URL = "" + currentStoryData.getContentProviderLink2();
            viewHolder.ivCP3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cp3URL));
                    mContext.startActivity(browserIntent);
                }
            });
        }

        viewHolder.tvStoryLovedCounter.setText(""+currentStoryData.getStoryLovedCounter());


        //=============== Stry Icon On Click ===
        viewHolder.ivStoryVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StoryListActivity)mContext).speakText(""+currentStoryData.getStoryContent(), position);
//                if(!isVolume) {
//                    viewHolder.ivStoryVolume.setImageResource(R.drawable.ic_volume_up);
//                    speakText(""+currentStoryData.getStoryContent());
//                    isVolume    =   true;
//                }
//                else{
//                    viewHolder.ivStoryVolume.setImageResource(R.drawable.ic_volume_off);
//                    stopSpeak();
//                }
            }
        });

        //=============== Bookmark Icon On Click ===
        final int isBookmarked = currentStoryData.getStoryBookmarked();
        if(isBookmarked==1){
            viewHolder.ivBookmark.setImageDrawable(mContext.getDrawable(R.drawable.ic_bookmarked_active));
        }
        else{
            viewHolder.ivBookmark.setImageDrawable(mContext.getDrawable(R.drawable.ic_bookmark));
        }
        viewHolder.ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StoryListActivity)mContext).bookMarkStory(currentStoryData.getStoryId(), position, isBookmarked);
            }
        });

        //=============== Loved Story Icon =========

        final int storyHearted = currentStoryData.getStoryHearted();
        if(storyHearted==1){
            viewHolder.ivLovedStory.setImageDrawable(mContext.getDrawable(R.drawable.ic_story_love_active));
        }
        else{
            viewHolder.ivLovedStory.setImageDrawable(mContext.getDrawable(R.drawable.ic_story_love));
        }

        viewHolder.ivLovedStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StoryListActivity)mContext).lovedStory(currentStoryData.getStoryId(), position, storyHearted);
            }
        });

        //=============== Stry Icon On Click ===
        viewHolder.ivShareStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storyTitle = ""+currentStoryData.getStoryTitle();
                String storyUrl = "http://www.thegistapp.com/";
                String shareUrl =   ""+currentStoryData.getStoryShareUrl();
                //((StoryListActivity)mContext).shareStory(storyTitle, storyUrl);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, shareUrl);

                mContext.startActivity(Intent.createChooser(share, ""+storyTitle));
            }
        });


        // ============ Backgroundd

        viewHolder.tvGistBy.setText(currentStoryData.getWriterName());

        if(currentStoryData.isPhotographer()){
            viewHolder.tvPictureBy.setText(currentStoryData.getPhographerName());
            viewHolder.tvPicturebyLabel.setVisibility(View.VISIBLE);
            viewHolder.tvPictureBy.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.tvPicturebyLabel.setVisibility(View.GONE);
            viewHolder.tvPictureBy.setVisibility(View.GONE);
        }










    }

    @Override
    public int getItemCount() {

        return storyLists == null ? 0 : storyLists.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    public void showMenu(int position) {
        for(int i=0; i<storyLists.size(); i++){
            storyLists.get(i).setShowMenu(false);
        }
        storyLists.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuShown() {
        for(int i=0; i<storyLists.size(); i++){
            if(storyLists.get(i).isShowMenu()){
                return true;
            }
        }
        return false;
    }

    public void closeMenu() {
        for(int i=0; i<storyLists.size(); i++){
            storyLists.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvStoryTitle, tvContent;
        ConstraintLayout clMainCategory;
        public ConstraintLayout viewBackground, viewForeground, clVideoYoutube, clImageContainer;
        public View viewBackgroundBottom;
        ImageView ivStory;
        VideoView vwStory;

        ProgressBar progressBar;
        Bitmap imageBG;

        ImageView ivStoryVolume, ivAvocado, ivControl, ivBookmark, ivLovedStory;
        ImageView ivCP1, ivCP2, ivCP3, ivShareStory;

        CardView cvCp1, cvCp2, cvCp3;

//        ConstraintLayout cvCp1;

        // backgroundView;

        private TextView tvGistBy, tvPictureBy, tvPicturebyLabel, tvStoryLovedCounter;
        public ImageView ivReactLove, ivReactSurprised, ivReactSad, ivReactAngry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            clVideoYoutube = itemView.findViewById(R.id.cl_youtubevideo);
            tvStoryTitle =   itemView.findViewById(R.id.tv_story_title);
            tvContent =   itemView.findViewById(R.id.tv_story_content);
            ivStory = itemView.findViewById(R.id.iv_story);
            vwStory = itemView.findViewById(R.id.vw_story);

            progressBar =   itemView.findViewById(R.id.pb_image);

            ivStoryVolume = itemView.findViewById(R.id.st_volume);
            ivBookmark  =   itemView.findViewById(R.id.iv_bookmark);
            ivLovedStory = itemView.findViewById(R.id.iv_like);

            tvStoryLovedCounter =   itemView.findViewById(R.id.tv_love_counter);


            //===== Content provoder and share==
            ivCP1   =   itemView.findViewById(R.id.iv_cp1);
            ivCP2   =   itemView.findViewById(R.id.iv_cp2);
            ivCP3   =   itemView.findViewById(R.id.iv_cp3);

            cvCp1   =   itemView.findViewById(R.id.cv_cp1);
            cvCp2   =   itemView.findViewById(R.id.cv_cp2);
            cvCp3   =   itemView.findViewById(R.id.cv_cp3);

            ivShareStory    =   itemView.findViewById(R.id.iv_share_story);


            //====== Background view =======
            tvGistBy = itemView.findViewById(R.id.tv_gist_by);
            tvPicturebyLabel = itemView.findViewById(R.id.tv_picture_by_label);
            tvPictureBy = itemView.findViewById(R.id.tv_picture_by);

            ivReactLove =   itemView.findViewById(R.id.iv_react_love);
            ivReactSurprised =   itemView.findViewById(R.id.iv_react_surprised);
            ivReactSad =   itemView.findViewById(R.id.iv_react_sad);
            ivReactAngry =   itemView.findViewById(R.id.iv_react_angry);


            viewBackgroundBottom = itemView.findViewById(R.id.view_background_bottom);

            Log.v("CONTROL_PREFS_size", "test"+userFontSize);

//            settings = mContext.getSharedPreferences(CONTROL_PREFS, MODE_PRIVATE);
//            userFontSize    = settings.getString("font_size", ""+mContext.getResources().getString(R.string.ctrl_scr_txt_size_medium));
//
            Log.v("CONTROL_PREFS", ""+userFontSize);
//
//
            if(userFontSize.equals(""+mContext.getResources().getString(R.string.ctrl_scr_txt_size_medium))){
                tvStoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyTitleDefaultFontSize);
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyContentDefaultFontSize);
            }
            else if(userFontSize.equals(""+mContext.getResources().getString(R.string.ctrl_scr_txt_size_large))){
                tvStoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyTitleDefaultFontSize+2);
                Log.v(TAG, "storyTitleDefaultFontSize"+tvStoryTitle.getTextSize());
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyContentDefaultFontSize+2);

            }
            else if(userFontSize.equals(""+mContext.getResources().getString(R.string.ctrl_scr_txt_size_small))){
                tvStoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyTitleDefaultFontSize-2);
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyContentDefaultFontSize-2);

            }
            else{
                tvStoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyTitleDefaultFontSize);
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, storyContentDefaultFontSize);

            }
            Log.v("CONTROL_PREFS", ""+tvStoryTitle.getTextSize());


        }


    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    public void  showBackgroundView(int position){
        String title = storyLists.get(position).getStoryTitle();
        Log.v("showBackgroundView", ""+title);

    }

    public void reactionOnStory2(View view){
        Toast.makeText(mContext, "Love", Toast.LENGTH_SHORT).show();
        Log.v(TAG, "reactionOnStory");
    }


    public StoryListAdapter.ViewHolder getViewByPosition(int position) { return holderlist.get(position); }



}
