package com.thegistapp.thegistapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.async.AddToBookmarkTask;
import com.thegistapp.thegistapp.async.AnswerPollTask;
import com.thegistapp.thegistapp.async.GetNewsTask;
import com.thegistapp.thegistapp.async.LovedStoryTask;
import com.thegistapp.thegistapp.async.ReactStoryTask;
import com.thegistapp.thegistapp.consts.Consts;
import com.thegistapp.thegistapp.custom.CustomAsynkLoader;
import com.thegistapp.thegistapp.customlists.StoryListAdapter;
import com.thegistapp.thegistapp.helper.RecyclerItemTouchHelper;
import com.thegistapp.thegistapp.listener.AddToBookmarkListener;
import com.thegistapp.thegistapp.listener.AnswerPollListener;
import com.thegistapp.thegistapp.listener.GetStoriesListener;
import com.thegistapp.thegistapp.listener.LovedStoryListener;
import com.thegistapp.thegistapp.listener.ReactStoryListener;
import com.thegistapp.thegistapp.model.AdItems;
import com.thegistapp.thegistapp.model.AddToBookmark;
import com.thegistapp.thegistapp.model.AnswerPoll;
import com.thegistapp.thegistapp.model.GetStory;
import com.thegistapp.thegistapp.model.LovedStory;
import com.thegistapp.thegistapp.model.ReactToStory;
import com.thegistapp.thegistapp.model.StoryListData;
import com.thegistapp.thegistapp.util.KpiInfo;
import com.thegistapp.thegistapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StoryListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener, GetStoriesListener, AnswerPollListener, AddToBookmarkListener, LovedStoryListener, ReactStoryListener {
    RecyclerView recyclerView;
    List<StoryListData> listStories;
    List<AdItems> listAds;
    List<Integer> arrStoryIds;
    StoryListAdapter adapter;
    private Context mContext;

    private CustomAsynkLoader mDialog;

    private String TAG = "StoryListActivityTAG";
    private ConstraintLayout clParent;
    private TextView tvNextStoryTitle, tvPageTitle, tvPageTitleBelow, tvEmoji;
    //poll
    private ConstraintLayout clNextNews, clPollquestion, clFooter, clHeader;
    int questionId =0;
    private TextView tvPollQuestion;
    private ImageView ivControl, ivAvocado;
    private RadioGroup rdgPollAnswer;
    private RadioButton rdbAnser1, rdbAnser2;
    String pollQuestion = "", pollOption1="", pollOption2="";

    private String userId;
    private int iconCategoryId;
    String dailyQuote="", userSelections="", iconCategoryName="";

    LinearLayoutManager mLayoutManager;
    private int visibleItemCount, totalItemCount, firstVisibleItemPosition, lastVisibleItem;

    boolean isLoading = false;

    private int storyLimit=0;
    boolean isFirstCalled =true;
    ProgressBar pbLoadMore;
    private boolean isScrolled = false;

    private TextToSpeech textToSpeech;

    private int isLoved = 0;


    private int showAdAfter=0;
    private PopupWindow popupStoryReactions;
    private View popupView;
    private ImageView ivCloseAd, ivAds;
    private VideoView videoViewAd;
    private ProgressBar progressBarAd;
    private TextView tvAdTimer;
    ProgressBar progressBarVideo;
    private int adCounter =0;
    private boolean adPopupshown = false;
    private int showAdCounter = 0;


    private String adURL="";
    private ConstraintLayout clStoryParent, clStoryContainer, clAdImage;
    private int itemScrolled;

    private int reactStoryId=0;
    ImageView ivReactLove, ivReactSurprised, ivReactSad, ivReactAngry;

    Map<Integer, Integer> arrStoryReaction = new HashMap<Integer, Integer>();
    Map<Integer, Integer> arrStoryBookmaked = new HashMap<Integer, Integer>();
    Map<Integer, Integer> arrStoryLoved = new HashMap<Integer, Integer>();

    int bookmarkViewPosition = 0, speakStoryViewPosion=0, heartStoryViewPosition=0;


    private boolean isVolume = false;

    int deviceHeight, deviceWidth;

    SharedPreferences settings;
    String CONTROL_PREFS = "";
    String userFontSize = "";

    KpiInfo kpiInfo;
    private String mDeviceToken = "";

    private int pushNotificationId=0;


    private int lastStoryId= 0;
    private int postViewd= 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        mContext = StoryListActivity.this;

        arrStoryIds =   new ArrayList<Integer>();

        kpiInfo =   new KpiInfo(mContext);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        popupStoryReactions = new PopupWindow(mContext);

        clStoryContainer = findViewById(R.id.cl_story_container);
        clParent = findViewById(R.id.cl_parent);

        clParent.setVisibility(View.GONE);

        clHeader =findViewById(R.id.cl_header);
        clFooter =findViewById(R.id.ll_footer);

        tvPageTitle = findViewById(R.id.tv_page_title);
        tvPageTitleBelow = findViewById(R.id.tv_page_title2);
        tvEmoji = findViewById(R.id.tv_emoji);

        ivControl   =   findViewById(R.id.iv_control);
        ivControl.setOnClickListener(this);
        ivAvocado   =   findViewById(R.id.iv_nav_avacodo);
        ivAvocado.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_story);
        listStories = new ArrayList<StoryListData>();
        listAds     =   new ArrayList<AdItems>();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        pbLoadMore  =   findViewById(R.id.pb_image);





        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, recyclerView);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);




        tvNextStoryTitle = findViewById(R.id.tv_next_title);
        clPollquestion        =   findViewById(R.id.cl_pollquestion);
        clNextNews        =   findViewById(R.id.cl_next_slide);
        clStoryParent        =   findViewById(R.id.cl_parent);


        clNextNews.setVisibility(View.GONE);


        // ADS ==
        clAdImage = findViewById(R.id.cl_ad_image);
        progressBarAd = (ProgressBar)findViewById(R.id.ad_progress);
        ivAds = (ImageView) findViewById(R.id.iv_ad);
        tvAdTimer = (TextView) findViewById(R.id.tv_ad_timer);
        progressBarVideo = findViewById(R.id.ad_progress_video);

        ivCloseAd =   (ImageView) findViewById(R.id.iv_ad_close);
        videoViewAd =   (VideoView) findViewById(R.id.video_view_ad);
        ivCloseAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clStoryContainer.setVisibility(View.VISIBLE);
               clAdImage.setVisibility(View.GONE);
               adPopupshown = false;
               if(videoViewAd.isPlaying()) {
                   videoViewAd.stopPlayback();
               }

            }
        });

        clAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(URLUtil.isValidUrl(adURL)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adURL));
                    startActivity(browserIntent);
                }
                else{
                    Toast.makeText(mContext,"Not a valid Url "+adURL, Toast.LENGTH_SHORT).show();
                }
            }
        });





//        addItemsToList();

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }


        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("icon_cat")) {
            iconCategoryId = bundle.getInt("icon_cat");

            Log.v(TAG, "iconCategoryId: "+iconCategoryId);
        }
        if(getIntent().hasExtra("icon_cat_name")) {
            iconCategoryName = bundle.getString("icon_cat_name");

            Log.v(TAG, "iconCategoryName: "+iconCategoryName);
        }
        if(getIntent().hasExtra("device_token")){
            mDeviceToken = bundle.getString("device_token");

            Log.v(TAG, "deviceTOKEn: "+mDeviceToken);
        }
        if(getIntent().hasExtra("story_id")){
            pushNotificationId = bundle.getInt("story_id");

            Log.v(TAG, "pushNotificationId: "+pushNotificationId);
        }

        if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
            userId  =   Util.fetchUserClass(mContext).getUserId();
            tvPageTitle.setText("Hey "+Util.fetchUserClass(mContext).getUserFullName());
            if (Util.fetchUserClass(mContext).getUserEmoji() != null && !Util.fetchUserClass(mContext).getUserEmoji().equals("")) {
                tvEmoji.setText(Html.fromHtml(""+Util.fetchUserClass(mContext).getUserEmoji()));
                tvEmoji.setVisibility(View.VISIBLE);
            }
            else{
                tvEmoji.setVisibility(View.GONE);
            }
            if(mDeviceToken.equals("") || mDeviceToken == null) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        mDeviceToken = instanceIdResult.getToken();

                        Log.v(TAG, "device_token " + mDeviceToken);
                    }
                });
            }

        }




        //== POLL  ==

        tvPollQuestion = findViewById(R.id.tv_polquestion);
        rdgPollAnswer= findViewById(R.id.rdg_poll_options);
        rdbAnser1 = findViewById(R.id.rdoption1);
        rdbAnser2 = findViewById(R.id.rdoption2);

        rdgPollAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.v(TAG, "checked id "+i);

                int id=radioGroup.getCheckedRadioButtonId();


                RadioButton rdChecked =findViewById(id);
                if(rdChecked != null && rdChecked.isChecked()) {
                    String userAnswer = rdChecked.getText().toString();
                    Log.v(TAG, "userAnswer "+userAnswer);
                    answerPoll(userAnswer);
                }



            }
        });



        CONTROL_PREFS = ""+getResources().getString(R.string.gist_control_prefs);
        settings = getSharedPreferences(CONTROL_PREFS, MODE_PRIVATE);
        userFontSize    = settings.getString("font_size", ""+getResources().getString(R.string.ctrl_scr_txt_size_medium));
        clHeader.setVisibility(View.GONE);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
            userId = Util.fetchUserClass(mContext).getUserId();
            updateLastLogin(userId);
        }

        settings = getSharedPreferences(CONTROL_PREFS, MODE_PRIVATE);
        userFontSize    = settings.getString("font_size", ""+getResources().getString(R.string.ctrl_scr_txt_size_medium));

        Log.v("CONTROL_PREFS", ""+CONTROL_PREFS+", "+userFontSize);

        int pageTitleDefaultFontSize = (int) (getResources().getDimension(R.dimen.story_page_title_size) / getResources().getDisplayMetrics().scaledDensity);


        if(userFontSize.equals(""+getResources().getString(R.string.ctrl_scr_txt_size_medium))){
            tvPageTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, pageTitleDefaultFontSize);
        }
        else if(userFontSize.equals(""+getResources().getString(R.string.ctrl_scr_txt_size_large))){
            tvPageTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, pageTitleDefaultFontSize+2);
        }
        else if(userFontSize.equals(""+getResources().getString(R.string.ctrl_scr_txt_size_small))){
            tvPageTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, pageTitleDefaultFontSize-2);
        }
        else{
            tvPageTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.story_page_title_size));
        }
        Log.v("CONTROL_PREFS", ""+pageTitleDefaultFontSize);

        int currentSessonId    = settings.getInt("current_sesson_id", 0);

        Log.v(TAG, "Current sesson "+currentSessonId);

        if(adapter !=null) {
            listStories.clear();
            adapter.notifyDataSetChanged();
        }
        storyLimit  =   0;


        getNews(userId);




    }

    private void getNews(String userId) {

//        LoaderPopup.showLoaderPopup(mContext, clParent);


//        pushNotificationId = 1323;
//        pushNotificationId = 1130;
        JSONObject objGetNews =   new JSONObject();
        try {
            objGetNews.put("action", "get_news");
            objGetNews.put("user_id", userId);
            objGetNews.put("device_token", mDeviceToken);
            objGetNews.put("icon_category", iconCategoryId);
            objGetNews.put("story_limit", storyLimit);
            objGetNews.put("news_id", pushNotificationId);
            if(pushNotificationId > 0) {
                objGetNews.put("is_pushed", 1);
            }
            else{
                objGetNews.put("is_pushed", 0);
            }
            String requestNews = objGetNews.toString();
            Log.v(TAG, "requestNews"+requestNews);


//            GetStoryTask getStoryTask = new GetStoryTask(mContext, requestNews);
//            getStoryTask.mListener  =   this;

            boolean showLoader = true;
            if(storyLimit > 0){
                showLoader = false;
            }
            else{
                recyclerView.smoothScrollToPosition(0);

            }

            GetNewsTask getNewsTask = new GetNewsTask(mContext, requestNews, showLoader);
            getNewsTask.mListener = this;
            getNewsTask.execute("");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_control :
                //Log.v(TAG, "Open Control screen");
                Intent intentControl    =   new Intent(mContext, ControlActivity.class);
                startActivity(intentControl);
                break;

            case R.id.iv_nav_avacodo:
                //Log.v(TAG, "Open Control screen");
                Intent intentSelection    =   new Intent(mContext, MainCategoryActivity.class);
                startActivity(intentSelection);
                break;



            default:
                break;
        }
    }

    @Override
    public void getStoriesCallBack(ArrayList<GetStory> getStories) {
        pbLoadMore.setVisibility(View.GONE);

//        LoaderPopup.hideLoaderPopup();
        isLoading = false;
        if(getStories.size() > 0) {

            clHeader.setVisibility(View.VISIBLE);


            int errorCode = getStories.get(0).getErrorCode();
            if(showAdAfter ==0){
                showAdAfter = getStories.get(0).getShowAdAfter();
            }
            dailyQuote = getStories.get(0).getDailyQuote();
            userSelections = getStories.get(0).getUserSelections();
            if(dailyQuote !="" && !isScrolled){
                tvPageTitleBelow.setText(""+dailyQuote);
            }
            if(iconCategoryId > 0){
                iconCategoryName = getStories.get(0).getIconCategory();
            }


            if (errorCode == 0) {


                //adProgressBar.setVisibility(View.VISIBLE);

                String storyData = getStories.get(0).getStories();
                String addData = getStories.get(0).getAdData();
                //Log.v(TAG, addData);



                try {
                    JSONArray arrStories = new JSONArray(storyData);
                    JSONArray arrAds = new JSONArray(addData);
                    Log.v(TAG, "Stories Length "+arrStories.length());

                    if(arrAds.length()> 0){
                        for (int j=0; j<arrAds.length(); j++){
                            AdItems adItem = new AdItems();
                            JSONObject adObj   =   arrAds.getJSONObject(j);
                            adItem.setId(adObj.optInt("id"));
                            adItem.setImage(adObj.optString("image"));
                            adItem.setUrl(adObj.optString("url"));
                            if(adObj.optInt("is_video")==1){
                                adItem.setIsVideo(1);
                                adItem.setVideoURL(adObj.optString("video_url"));
                            }
                            else{
                                adItem.setIsVideo(0);
                                adItem.setVideoURL("");
                            }


                            listAds.add(adItem);
                        }
                    }

                    Log.v(TAG, ""+arrStories.length());
                    if(arrStories.length() > 0){
                        storyLimit += arrStories.length();
                        for (int i = 0; i< arrStories.length(); i++){




                            StoryListData storyListData = new StoryListData();
                            JSONObject storyObj   =   arrStories.getJSONObject(i);
                            storyListData.setStoryId(storyObj.optInt("story_id"));



                            arrStoryIds.add(storyObj.optInt("story_id"));

                            storyListData.setStoryShareUrl(storyObj.optString("story_share_url"));

                            int storyReaction = storyObj.optInt("reaction_type");
                            storyListData.setStoryReactionType(storyReaction);

                            arrStoryReaction.put(storyObj.optInt("story_id"), storyReaction);

                            int storyBookmarked = storyObj.optInt("bookmarked");
                            storyListData.setStoryBookmarked(storyBookmarked);

                            int storyHearted = storyObj.optInt("loved_story");

                            storyListData.setStoryHearted(storyHearted);

                            int storyLovedCounter = storyObj.optInt("story_loved_counter");

                            storyListData.setStoryLovedCounter(storyLovedCounter);



                            //storyListData.setStoryTitle((storyObj.optInt("story_id"))+" - "+storyObj.optString("story_title"));
                            storyListData.setStoryTitle(""+storyObj.optString("story_title"));
                            storyListData.setStoryContent(""+storyObj.optString("story_content"));
                            storyListData.setStoryContent(""+storyObj.optString("story_content"));
                            int isVideo = storyObj.optInt("is_video");
                            if(isVideo ==1){
                                storyListData.setIsVideo(1);
                                storyListData.setVideoLink(""+storyObj.optString("video_link"));
                                storyListData.setStoryImage(""+storyObj.optString(""));
                            }
                            else{
                                storyListData.setIsVideo(0);
                                storyListData.setVideoLink(""+storyObj.optString(""));
                                storyListData.setStoryImage(""+storyObj.optString("img_video"));
                            }

                            storyListData.setContentProviderLogo1(""+storyObj.optString("provider_logo1"));
                            storyListData.setContentProviderLogo2(""+storyObj.optString("provider_logo2"));
                            storyListData.setContentProviderLogo3(""+storyObj.optString("provider_logo3"));

                            storyListData.setContentProviderLink1(""+storyObj.optString("content_provider_link1"));
                            storyListData.setContentProviderLink2(""+storyObj.optString("content_provider_link2"));
                            storyListData.setContentProviderLink3(""+storyObj.optString("content_provider_link3"));

                            storyListData.setPollQuestionId(storyObj.optInt("ques_id"));
                            storyListData.setPollQuestion(storyObj.optString("poll_question"));
                            storyListData.setPollOption1(storyObj.optString("poll_option1"));
                            storyListData.setPollOption2(storyObj.optString("poll_option2"));

                            int isPhotographer = storyObj.optInt("is_photographer");
                            if (isPhotographer == 1) {
                                storyListData.setPhotographer(true);
                                storyListData.setPhographerName(""+storyObj.optString("photographer_name"));

                            } else {
                                storyListData.setPhotographer(false);
                                storyListData.setPhographerName("");
                            }
                            storyListData.setWriterName(""+storyObj.optString("writer"));
                            storyListData.setEditorName(""+storyObj.optString("editor"));

                            int isAd = storyObj.optInt("show_ad");
//                            if(isAd ==1){
//                                storyListData.setShowAd(true);
//                            }
//                            else{
//                                storyListData.setShowAd(false);
//                            }

                            showAdCounter++;
                            if(showAdCounter%showAdAfter ==0){
                                storyListData.setShowAd(true);
                            }
                            else{
                                storyListData.setShowAd(false);
                            }

                            listStories.add(storyListData);


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else{
                clPollquestion.setVisibility(View.GONE);
                Util.showMessageWithOk(mContext, ""+getString(R.string.no_story_found));
            }
        }
        else{
            clFooter.setVisibility(View.GONE);
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }

        if(isFirstCalled) {
            //clFooter.setVisibility(View.VISIBLE);
            initScrollListener();
            if (listStories.size() > 0) {

                if (listStories.size() == 1) {
                    clNextNews.setVisibility(View.GONE);
                }
                else{
                    clNextNews.setVisibility(View.VISIBLE);
                    tvNextStoryTitle.setText("" + listStories.get(1).getStoryTitle());
                }
                questionId = listStories.get(0).getPollQuestionId();
                if (questionId > 0) {
                    pollQuestion = listStories.get(0).getPollQuestion();
                    pollOption1 = listStories.get(0).getPollOption1();
                    pollOption2 = listStories.get(0).getPollOption2();

                    tvPollQuestion.setText(pollQuestion);
                    rdbAnser1.setText(pollOption1);
                    rdbAnser2.setText(pollOption2);
                    clPollquestion.setVisibility(View.VISIBLE);
                } else {
                    clPollquestion.setVisibility(View.GONE);
                }

            }
            if (clParent.getVisibility() == View.GONE) {
                clParent.setVisibility(View.VISIBLE);

            }
            adapter = new StoryListAdapter(mContext, listStories, userFontSize);

            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            isFirstCalled = false;
        }
        else{
            //adapter.notifyDataSetChanged();
            initScrollListener();



        }

        if(pushNotificationId > 0) {
            int storyPosition = getCategoryPos(pushNotificationId, arrStoryIds);

            //Toast.makeText(mContext, "Position "+storyPosition, Toast.LENGTH_SHORT).show();
        }


    }

    private int getCategoryPos(int storyId, List<Integer> arrIds) {
        return arrIds.indexOf(storyId);
    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                boolean hasStarted = newState == RecyclerView.SCROLL_STATE_DRAGGING;
//                boolean hasEnded = newState == RecyclerView.SCROLL_STATE_IDLE;

                //stopSpeak(speakStoryViewPosion);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

                    int lastItemScrolled = mLayoutManager.findLastVisibleItemPosition();



                    if(lastVisibleItemPosition > 0 && !isScrolled){
                        if(iconCategoryId > 0){
                            tvPageTitleBelow.setText("" + iconCategoryName);
                        }
                        else {
                            tvPageTitleBelow.setText("" + userSelections);
                        }
                        isScrolled = true;
                    }
                    if(lastVisibleItemPosition >= 0) {
                        if (lastVisibleItemPosition < listStories.size() - 1) {
                            tvNextStoryTitle.setText("" + listStories.get(lastVisibleItemPosition + 1).getStoryTitle());

                            stopSpeak(speakStoryViewPosion);



                        }

                        if(lastStoryId != listStories.get(lastVisibleItemPosition).getStoryId()){
                            lastStoryId = listStories.get(lastVisibleItemPosition).getStoryId();

                            postViewd++;

                            Log.v(TAG, "lastStoryId "+postViewd);
                        }

                        questionId = listStories.get(lastVisibleItemPosition).getPollQuestionId();


                        if (questionId > 0) {
                            pollQuestion = listStories.get(lastVisibleItemPosition).getPollQuestion();
                            pollOption1 = listStories.get(lastVisibleItemPosition).getPollOption1();
                            pollOption2 = listStories.get(lastVisibleItemPosition).getPollOption2();

                            tvPollQuestion.setText(pollQuestion);
                            rdbAnser1.setText(pollOption1);
                            rdbAnser2.setText(pollOption2);
                            clPollquestion.setVisibility(View.VISIBLE);
                        } else {
                            clPollquestion.setVisibility(View.GONE);
                        }
                    }

                }


               if (newState != RecyclerView.SCROLL_STATE_IDLE) {


                    //LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                   int lastItemScrolled = mLayoutManager.findLastVisibleItemPosition();

                   if(lastItemScrolled !=itemScrolled && (listStories.get(lastItemScrolled).isShowAd() && !adPopupshown)){
                       showAdPopup();
                       Log.v(TAG+"1", ""+lastItemScrolled);
                       adPopupshown = true;
                       itemScrolled = lastItemScrolled;
                   }



                    /* if(getPosition > 0 && !isScrolled){
                        if(iconCategoryId > 0){
                            tvPageTitleBelow.setText("" + iconCategoryName);
                        }
                        else {
                            tvPageTitleBelow.setText("" + userSelections);
                        }
                        isScrolled = true;
                    }
                    if(getPosition >= 0) {
                        if (getPosition < listStories.size() - 1) {
                            tvNextStoryTitle.setText("" + listStories.get(getPosition + 1).getStoryTitle());


                        }

                        questionId = listStories.get(getPosition).getPollQuestionId();


                        if (questionId > 0) {
                            pollQuestion = listStories.get(getPosition).getPollQuestion();
                            pollOption1 = listStories.get(getPosition).getPollOption1();
                            pollOption2 = listStories.get(getPosition).getPollOption2();

                            tvPollQuestion.setText(pollQuestion);
                            rdbAnser1.setText(pollOption1);
                            rdbAnser2.setText(pollOption2);
                            clPollquestion.setVisibility(View.VISIBLE);
                        } else {
                            clPollquestion.setVisibility(View.GONE);
                        }
                    }*/
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Log.v("getPosition", "getPosition ONSCROLLED"+numberOfScroll++);

                //LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int getPosition = mLayoutManager.findLastVisibleItemPosition();


                if(mLayoutManager.findLastCompletelyVisibleItemPosition() == listStories.size() -1){
                    //bottom of list!
                    if(!isLoading) {
                        pbLoadMore.setVisibility(View.VISIBLE);
                        getNews(userId);
                        isLoading = true;
                        //clFooter.setVisibility(View.GONE);
                    }
                }

            }
        });


    }




    private void answerPoll(String pollAnswer){
        Log.v(TAG, "answer is "+pollAnswer+" question id is"+questionId);

        JSONObject objPollAnswer =   new JSONObject();
        try {
            objPollAnswer.put("action", "answer_to_poll");
            objPollAnswer.put("user_id", userId);
            objPollAnswer.put("question_id", questionId);
            objPollAnswer.put("user_answer", pollAnswer);

            String requestAnswer = objPollAnswer.toString();
            Log.v(TAG, "requestAnswer: "+requestAnswer);

            AnswerPollTask answerPollTask = new AnswerPollTask(mContext, requestAnswer);
            answerPollTask.mListener    =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void answerPollCallBack(ArrayList<AnswerPoll> answerPolls) {
        if(answerPolls.size() > 0){
            int errorCode = answerPolls.get(0).getErrorCode();
            if(errorCode == 0){
                //Util.showMessageWithOk(mContext, ""+getString(R.string.poll_answered_successfully));
                return;
            }
            else{
                Util.showMessageWithOk(mContext, ""+getString(R.string.poll_answered_failed));
                return;
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }

    public void lovedStory(int currentStoryId, int viewPosition, int isLoved){
        heartStoryViewPosition = viewPosition;
        //Toast.makeText(mContext, "Loved story", Toast.LENGTH_SHORT).show();
        userId  =   Util.fetchUserClass(mContext).getUserId();

        JSONObject objLoved =   new JSONObject();
        try {
            objLoved.put("action", "loved_story");
            objLoved.put("user_id", userId);
            objLoved.put("story_id", currentStoryId);
            if(isLoved ==1){
                objLoved.put("remove_Loved", isLoved);
            }

            String requestLoved = objLoved.toString();
            Log.v(TAG, "requestLoved"+requestLoved);

            LovedStoryTask lovedStoryTask = new LovedStoryTask(mContext, requestLoved);
            lovedStoryTask.mListener = this;



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void lovedStoryCallBack(ArrayList<LovedStory> lovedStories) {
        if(lovedStories.size() > 0){
            int errorCode = lovedStories.get(0).getErrorCode();
            if(errorCode == 0){



                int isLoved = lovedStories.get(0).getIsLoved();
                StoryListAdapter.ViewHolder holder = ((StoryListAdapter)recyclerView.getAdapter()).getViewByPosition(heartStoryViewPosition);
                View view = holder.itemView;
                ImageView ivHeart = view.findViewById(R.id.iv_like);
                TextView tvLovedCounter = view.findViewById(R.id.tv_love_counter);

                if(isLoved ==1){
                    Log.v(TAG, "Story Loved");
                    ivHeart.setImageDrawable(getDrawable(R.drawable.ic_story_love_active));

                    listStories.get(heartStoryViewPosition).setStoryHearted(1);

                    int lovedCounter = listStories.get(heartStoryViewPosition).getStoryLovedCounter() +1;


                    Log.v(TAG, "lovedCounter: "+lovedCounter );

                    listStories.get(heartStoryViewPosition).setStoryLovedCounter(lovedCounter);

                    tvLovedCounter.setText(""+lovedCounter);



                    adapter.notifyDataSetChanged();
                }
                else{
                    listStories.get(heartStoryViewPosition).setStoryHearted(0);


                    int lovedCounter = listStories.get(heartStoryViewPosition).getStoryLovedCounter();
                    if(lovedCounter > 0){
                        lovedCounter = lovedCounter - 1;
                    }
                    else{
                        lovedCounter = 0;
                    }
                    Log.v(TAG, "lovedCounter: "+lovedCounter );
                    listStories.get(heartStoryViewPosition).setStoryLovedCounter(lovedCounter);

                    tvLovedCounter.setText(""+lovedCounter);

                    adapter.notifyDataSetChanged();
                    Log.v(TAG, "Story Loved removed");
                    ivHeart.setImageDrawable(getDrawable(R.drawable.ic_story_love));

                }
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }


    public void bookMarkStory(int currentStoryId, int itemPosition, int isBookmarked){
        userId  =   Util.fetchUserClass(mContext).getUserId();
        bookmarkViewPosition = itemPosition;
        JSONObject objBookmark =   new JSONObject();
        try {
            objBookmark.put("action", "bookmark_story");
            objBookmark.put("user_id", userId);
            objBookmark.put("story_id", currentStoryId);
            objBookmark.put("remove_bookmark", isBookmarked);
            String requestBookmark = objBookmark.toString();
            Log.v(TAG, "requestBookmark"+requestBookmark);

            AddToBookmarkTask addToBookmarkTask = new AddToBookmarkTask(mContext, requestBookmark);
            addToBookmarkTask.mListener    =   this;





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToBookmarkCallBack(ArrayList<AddToBookmark> addToBookmarks) {
        if(addToBookmarks.size() > 0) {
            int errorCode = addToBookmarks.get(0).getErrorCode();
            if (errorCode == 0) {

                StoryListAdapter.ViewHolder holder = ((StoryListAdapter)recyclerView.getAdapter()).getViewByPosition(bookmarkViewPosition);
                View view = holder.itemView;
                ImageView ivBookmark = view.findViewById(R.id.iv_bookmark);



                int removeBookmark = addToBookmarks.get(0).getBookmarkedRemoved();
                if(removeBookmark ==1) {
                    ivBookmark.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
                    listStories.get(bookmarkViewPosition).setStoryBookmarked(0);
                    adapter.notifyDataSetChanged();
                }
                else{
                    ivBookmark.setImageDrawable(getDrawable(R.drawable.ic_bookmarked_active));
                    listStories.get(bookmarkViewPosition).setStoryBookmarked(1);
                    adapter.notifyDataSetChanged();
                }

//                ImageView imgView = findViewBytag(bookmarkViewId);
//                imgView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmarked_active));




                //Util.showMessageWithOk(mContext, getResources().getString(R.string.story_bookmarked));
            }
            else{
                Util.showMessageWithOk(mContext, getResources().getString(R.string.story_not_bookmarked));
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }





    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof StoryListAdapter.ViewHolder) {


            StoryListAdapter.ViewHolder mHolder = (StoryListAdapter.ViewHolder)viewHolder;
            View itemView = mHolder.itemView;
            View viewBackground = mHolder.viewBackground;
            ImageView addView = mHolder.ivReactLove;
            ImageView delView = mHolder.ivReactAngry;


            mHolder.viewBackground.getTag();

            Log.v(TAG, "Direction "+direction+", "+mHolder.viewBackground.getTag());

            View column_var = itemView.findViewWithTag(mHolder.viewBackground.getTag());

            View c = recyclerView.getChildAt(0);
            int scrolly = -c.getTop() + mLayoutManager.findFirstVisibleItemPosition()* c.getHeight();

            Log.v("ScrollPosition", "scroll position "+scrolly);


            showReactPopup(column_var, position, mHolder.viewBackground.getTag().toString(), mHolder);


        }
    }



    @Override
    public void onClearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, boolean showPopup, float dX, float swipedDX) {
        if (!showPopup){
            popupStoryReactions.dismiss();
        }
    }

    public void reactStory(int storyId, String reactionType, int reaction){
        //Toast.makeText(mContext, storyId+", Reaction "+reactionType, Toast.LENGTH_SHORT).show();
        userId  =   Util.fetchUserClass(mContext).getUserId();

        JSONObject objReactOnNews =   new JSONObject();
        try {
            objReactOnNews.put("action", "react_to_story");
            objReactOnNews.put("user_id", userId);
            objReactOnNews.put("news_id", storyId);
            objReactOnNews.put("reaction", reactionType);
            objReactOnNews.put("reaction_type", reaction);

            String requestReactNews = objReactOnNews.toString();
            Log.v(TAG, "requestReactNews"+requestReactNews);

            ReactStoryTask reactStoryTask = new ReactStoryTask(mContext, requestReactNews);
            reactStoryTask.mListener    =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public static void watchYoutubeVideo(Context context, String id){
        Log.v("StoryListActivityTAG", "Youtube id "+id);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
        context.startActivity(intent);
    }

    public static boolean isVisible(final View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);
        final Rect screen = new Rect(0, 0,  Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
        return actualPosition.intersect(screen);
    }

    private void showReactPopup(View viewBackground, int position, String tag, StoryListAdapter.ViewHolder viewHoder){

        reactStoryId = listStories.get(position).getStoryId();
        //int reactionType = listStories.get(position).getStoryReactionType();
        int reactionType = arrStoryReaction.get(reactStoryId);

        Log.v("ReactionType", ""+reactionType);




        View itemView = viewHoder.itemView;

        View viewBackgroundBottom = itemView.findViewById(position+1000);
        int[] location = new int[2];
        viewBackgroundBottom.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        Log.v("ScrollPosition", deviceHeight+", scroll position "+y);


        int diffrence = deviceHeight - y;
        Log.v("ScrollPosition2", "diffrence  "+diffrence);


        if(pushNotificationId > 0){

        }
        else {
            if (isVisible(viewBackgroundBottom) && diffrence > 100) {
                Log.v(TAG + "1", "Visible");

            } else {
                Log.v(TAG + "1", "Invisible");
                return;
            }
        }



        Log.v(TAG, "showReactPopup reactionType "+tag);
        if (popupStoryReactions == null || !popupStoryReactions.isShowing()) {



            LayoutInflater layoutInflater = (LayoutInflater)
                    mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater.inflate(R.layout.popup_react, null);
            popupStoryReactions = new PopupWindow(popupView, viewBackground.getWidth(),
                    250);




            ConstraintLayout layoutMain   =   (ConstraintLayout) findViewById(R.id.view_background);

            ivReactLove = popupView.findViewById(R.id.iv_react_love);
            ivReactSurprised = popupView.findViewById(R.id.iv_react_surprised);
            ivReactSad = popupView.findViewById(R.id.iv_react_sad);
            ivReactAngry = popupView.findViewById(R.id.iv_react_angry);


//            ivReactLove.setOnClickListener(this);
//            ivReactSurpried.setOnClickListener(this);
//            ivReactSad.setOnClickListener(this);
//            ivReactAngry.setOnClickListener(this);

            ivReactLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reactStory(reactStoryId,"Love", 1);
                }
            });
            ivReactSurprised.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reactStory(reactStoryId,"Surprised", 2);
                }
            });
            ivReactSad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reactStory(reactStoryId,"Sad", 3);
                }
            });
            ivReactAngry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reactStory(reactStoryId,"Angry", 4);
                }
            });


            ivReactLove.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
            ivReactSurprised.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
            ivReactSad.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
            ivReactAngry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));



            if(reactionType ==1){
                ivReactLove.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));
            }
            else if(reactionType ==2){
                ivReactSurprised.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));
            }
            else if(reactionType ==3){
                ivReactSad.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));
            }
            else if(reactionType ==4){
                ivReactAngry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));
            }

            popupStoryReactions.setFocusable(false);


            popupStoryReactions.setTouchable(true);
            popupStoryReactions.setOutsideTouchable(false);
            //popupSearch.setOutsideTouchable(false);
            //popupStoryReactions.showAtLocation(viewBackground, Gravity.BOTTOM, 0, -80);
            popupStoryReactions.showAsDropDown(viewBackground,0, -200, Gravity.BOTTOM);


            popupStoryReactions.update();
        }
        else {

            popupStoryReactions.dismiss();
        }
    }

    private void showAdPopup() {
        Log.v(TAG, "showAdPopup"+adCounter);
        if(!adPopupshown) {
            if(adCounter >= listAds.size()){
                adCounter =0;
            }
            clAdImage.setVisibility(View.VISIBLE);
            clStoryContainer.setVisibility(View.GONE);
            ivCloseAd.setVisibility(View.GONE);
            progressBarVideo.setVisibility(View.GONE);

            String imageName = listAds.get(adCounter).getImage();
            adURL = listAds.get(adCounter).getUrl();

            int isVideo = listAds.get(adCounter).getIsVideo();
            String videoURL = listAds.get(adCounter).getVideoURL();


            if(isVideo == 1 && URLUtil.isValidUrl(videoURL)){
                Log.v("VideoURL", videoURL+", " + videoURL);




                ivAds.setVisibility(View.GONE);
                videoViewAd.setVisibility(View.VISIBLE);
                progressBarAd.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(mContext);
                mediaController.setAnchorView(videoViewAd);

                //specify the location of media file
                Uri uri = Uri.parse(videoURL);

                //Setting MediaController and URI, then starting the videoView
                videoViewAd.setMediaController(mediaController);
                videoViewAd.setVideoURI(uri);
                videoViewAd.start();
                videoViewAd.requestFocus();
                videoViewAd.setMediaController(null);
                videoViewAd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        videoViewAd.setVisibility(View.VISIBLE);
                        progressBarAd.setVisibility(View.GONE);
                        progressBarVideo.setVisibility(View.GONE);
                        ivCloseAd.setVisibility(View.VISIBLE);
//                        new CountDownTimer(30000, 1000) {
//
//                            public void onTick(long millisUntilFinished) {
//                                tvAdTimer.setText("Skip ad in : " + millisUntilFinished / 1000);
//                                ivCloseAd.setVisibility(View.GONE);
//                                progressBarAd.setProgress(1);
//                                //here you can have your logic to set text to edittext
//                            }
//
//                            public void onFinish() {
//                                tvAdTimer.setText("");
//                                ivCloseAd.setVisibility(View.VISIBLE);
//                            }
//
//                        }.start();
                    }
                });
                videoViewAd.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.d("video", "setOnErrorListener ");
                        return true;
                    }
                });


                videoViewAd.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });

            }
            else {
                ivAds.setVisibility(View.VISIBLE);
                videoViewAd.setVisibility(View.GONE);
                ivCloseAd.setVisibility(View.VISIBLE);
                progressBarAd.setVisibility(View.GONE);

                Glide.with(mContext)
                        .load("" + Consts.STORY_AD_IMAGES + imageName)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                progressBarAd.setVisibility(View.GONE);
                                ivAds.setVisibility(View.VISIBLE);
                                Log.v(TAG, "Image Falied");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBarAd.setVisibility(View.GONE);
                                ivAds.setVisibility(View.VISIBLE);
                                Log.v(TAG, "Image Ready");

                                return false;
                            }

                        })
                        .into(ivAds);
            }
            if (adCounter <= listAds.size() - 1) {
                adCounter++;
            } else {
                adCounter = 0;
            }
        }
    }

    @Override
    public void hidePopup(float dx) {
        if(dx == 0){
            Log.v(TAG, "dimensionX"+dx);
        }
    }

    @Override
    public void reactStoryCallBack(ArrayList<ReactToStory> reactToStories) {
        if(reactToStories.size() > 0){
            int errorCode   =   reactToStories.get(0).getErrorCode();
            if(errorCode ==0){
                int reactionType = reactToStories.get(0).getReactionType();
                int storyId = reactToStories.get(0).getStoryId();
                int padding = (int) getResources().getDimension(R.dimen.ic_react_image_padding);

                arrStoryReaction.put(storyId, reactionType);

                if(reactionType ==1){
                    ivReactLove.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));



//                    ivReactLove.setPadding(padding,padding,padding,padding);
//
                    ivReactSurprised.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactSad.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactAngry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
//
//
//                    ivReactSurprised.setPadding(0,0,0,0);
//                    ivReactSad.setPadding(0,0,0,0);
//                    ivReactAngry.setPadding(0,0,0,0);

                }
                else if(reactionType ==2){
                    ivReactSurprised.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));

//                    ivReactSurprised.setPadding(padding,padding,padding,padding);
//
//
                    ivReactLove.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactSad.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactAngry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
//
//                    ivReactLove.setPadding(0,0,0,0);
//                    ivReactSad.setPadding(0,0,0,0);
//                    ivReactAngry.setPadding(0,0,0,0);

                }
                else if(reactionType ==3){
                    ivReactSad.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));
//                    ivReactSad.setPadding(padding,padding,padding,padding);
//
                    ivReactLove.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactSurprised.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactAngry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
//
//                    ivReactLove.setPadding(0,0,0,0);
//                    ivReactSurprised.setPadding(0,0,0,0);
//                    ivReactAngry.setPadding(0,0,0,0);
                }
                else if(reactionType ==4){
                    ivReactAngry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_selected_drawable));
//                    ivReactAngry.setPadding(padding,padding,padding,padding);
//
                    ivReactLove.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactSurprised.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
                    ivReactSad.setBackground(mContext.getResources().getDrawable(R.drawable.btn_reaction_background_drawable));
//
//
//                    ivReactLove.setPadding(0,0,0,0);
//                    ivReactSurprised.setPadding(0,0,0,0);
//                    ivReactSad.setPadding(0,0,0,0);

                }
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }

    public void speakText(final String text, int viewPosition){
        if(speakStoryViewPosion !=0 && viewPosition != speakStoryViewPosion) {
            stopSpeak(speakStoryViewPosion);
        }
        speakStoryViewPosion = viewPosition;

        if(!isVolume) {
                for (int i = 0; i < listStories.size(); i++) {
                    StoryListAdapter.ViewHolder holder = ((StoryListAdapter) recyclerView.getAdapter()).getViewByPosition(i);
                    if(holder != null) {
                        View view = holder.itemView;
                        ImageView ivStoryVolume = view.findViewById(R.id.st_volume);
                        if (i == viewPosition) {
                            ivStoryVolume.setImageResource(R.drawable.ic_volume_on);
                        } else {
                            ivStoryVolume.setImageResource(R.drawable.ic_volume_off);
                        }
                    }

                }

                textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {

                        if (status == TextToSpeech.SUCCESS) {

                            int result = textToSpeech.setLanguage(Locale.US);

                            if (result == TextToSpeech.LANG_MISSING_DATA
                                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "This Language is not supported");
                            } else {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                } else {
                                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                }
                                Log.e(TAG, "speak :- "+text);
                                isVolume = true;
                            }

                        } else {
                            Log.e("TTS", "`Initilization Failed!");
                        }

                    }
                });

        }
        else{
            stopSpeak(speakStoryViewPosion);
        }

    }

    public void stopSpeak(int viewPosition){
        isVolume    =   false;
        if (textToSpeech != null) {
            textToSpeech.stop();
            StoryListAdapter.ViewHolder holder = ((StoryListAdapter) recyclerView.getAdapter()).getViewByPosition(viewPosition);
            View view = holder.itemView;
            ImageView ivStoryVolume = view.findViewById(R.id.st_volume);
            ivStoryVolume.setImageResource(R.drawable.ic_volume_off);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


       //Toast.makeText(mContext, "Close", Toast.LENGTH_SHORT).show();
        if(videoViewAd.isPlaying()){
            videoViewAd.stopPlayback();
        }
        int currentSessonId    = settings.getInt("current_sesson_id", 0);
        if(currentSessonId > 0){
            kpiInfo.endSesson(currentSessonId, postViewd);
        }

    }


    public void updateLastLogin(String userId){
        JSONObject objUpdateLogin =   new JSONObject();
        try {
            objUpdateLogin.put("action", "update_login");
            objUpdateLogin.put("user_id", userId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }





}

