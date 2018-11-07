package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Recipe.Recipe;
import com.example.android.bakingapp.Recipe.Step.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepFragment extends Fragment {

    @BindView(R.id.step_desc)
    TextView stepDescriptionTextView;
    private SimpleExoPlayer player;
    @BindView(R.id.step_video)
    PlayerView playerView;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.step_btn_layout)
    RelativeLayout layoutButtons;
    @BindView(R.id.step_image)
    ImageView stepImageImageView;
    private boolean playWhenReady = true;
    private long playBackPosition = -1;
    private int currentWindow;
    Context mContext;

    Step currentStep;
    Recipe currentRecipe;

    private static final String APP_NAME = "Baking App";
    private static final String STEP_KEY = "step";
    private static final String RECIPE_KEY = "recipe";
    private static final String CURRENT_WINDOW = "CurrentWindow";
    private static final String PLAYER_POSITION = "PlayerPosition";
    private static final String PLAY_WHEN_READY = "PlayWhenReady";

    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentStep = getArguments().getParcelable(STEP_KEY);
            currentRecipe = getArguments().getParcelable(RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        mContext = container.getContext();

        //setContentView(R.layout.activity_step);
        if (getResources().getBoolean(R.bool.isLandscape) && !getResources().getBoolean(R.bool.isTablet)) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        }
        ButterKnife.bind(this,view);

        if (getResources().getBoolean(R.bool.isLandscape)) {
            stepDescriptionTextView.setVisibility(View.GONE);
            layoutButtons.setVisibility(View.GONE);
            btnNextStep.setVisibility(View.GONE);
            btnPreviousStep.setVisibility(View.GONE);
        } else if (!getResources().getBoolean(R.bool.isLandscape) || getResources().getBoolean(R.bool.isTablet)) {
            stepDescriptionTextView.setVisibility(View.VISIBLE);
            layoutButtons.setVisibility(View.VISIBLE);

        }

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getParcelable(STEP_KEY);
            currentRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            playBackPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
        }
        showStep(currentStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null)
                    releasePlayer();
                List<Step> stepList = currentRecipe.getSteps();
                for (int i = 0; i < stepList.size(); i++)
                    if (intVal(stepList.get(i).getId()) == intVal(currentStep.getId())) {
                        if (i + 1 < stepList.size()) {
                            currentStep = stepList.get(i + 1);
                            showStep(currentStep);
                        }
                        break;
                    }
            }
        });
        btnPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null)
                    releasePlayer();
                List<Step> stepList = currentRecipe.getSteps();
                for (int i = 0; i < stepList.size(); i++)
                    if (intVal(stepList.get(i).getId()) == intVal(currentStep.getId())) {
                        if (i != 0) {
                            currentStep = stepList.get(i - 1);
                            showStep(currentStep);
                        }
                        break;
                    }
            }
        });

        return view;
    }

    private int intVal(String val) {
        return Integer.valueOf(val);
    }

    private void showStep(Step step) {
        String videoUrl = step.getVideoURL();
        String thumbnailUrl = step.getThumbnailURL();
        String description = step.getDescription();

        //Set previous and next buttons visibility
        List<Step> stepList = currentRecipe.getSteps();
        if (step.getId().equals(stepList.get(0).getId()))
            btnPreviousStep.setVisibility(View.GONE);
        else
            btnPreviousStep.setVisibility(View.VISIBLE);
        if (step.getId().equals(stepList.get(stepList.size() - 1).getId()))
            btnNextStep.setVisibility(View.GONE);
        else
            btnNextStep.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(videoUrl)) {
            playerView.setVisibility(View.VISIBLE);
            stepImageImageView.setVisibility(View.GONE);
            initializePlayer(Uri.parse(videoUrl));
        } else {
            playerView.setVisibility(View.GONE);
            stepImageImageView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                Glide.with(this).load(thumbnailUrl).into(stepImageImageView);
            } else
                stepImageImageView.setImageResource(R.drawable.baker);
        }

        stepDescriptionTextView.setText(description);
    }

    /**
     * Method that will initialize player for the video if the API has information for the video
     *
     * @param mediaUri the Uri received from the API
     */
    public void initializePlayer(Uri mediaUri) {
        if (player == null) {
            //Create and instance for ExoPLayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            playerView.setPlayer(player);
            //prepare the MediaSource
            String userAgent = Util.getUserAgent(mContext, APP_NAME);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new
                    DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
        if (playBackPosition != -1) {
            player.seekTo(currentWindow, playBackPosition);
            player.setPlayWhenReady(playWhenReady);
            playBackPosition = -1;
        }
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            playBackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_KEY, currentStep);
        outState.putParcelable(RECIPE_KEY, currentRecipe);
        if (player != null) {
            outState.putLong(PLAYER_POSITION, player.getCurrentPosition());
            outState.putInt(CURRENT_WINDOW, player.getCurrentWindowIndex());
            outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        }
    }


}
