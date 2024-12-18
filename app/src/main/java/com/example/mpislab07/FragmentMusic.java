package com.example.mpislab07;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentMusic extends Fragment {

    private TextView currentAudio;
    private Button playBtn, stopBtn, nextBtn, prevBtn;

    private RecyclerView audioRecyclerView;

    private MediaPlayer mediaPlayer;
    private int currentTrackIndex = 0;
//    private final int[] tracks = {R.raw.diss, R.raw.future_club, R.raw.soviet_connection};

    private final List<Audio> audios = new ArrayList<>();

    private Boolean isAudioPlay = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (audios.isEmpty()) {
            audios.add(new Audio("Lie Lie Lie ", "Serj Tankian", R.raw.lie_lie_lie));
            audios.add(new Audio("Future Club", "Pertubator", R.raw.future_club));
            audios.add(new Audio("Breathe", "The Prodigy", R.raw.breathe));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        currentAudio = view.findViewById(R.id.currentAudio);

        playBtn = view.findViewById(R.id.playBtn);
        // stopBtn = view.findViewById(R.id.stopBtn);
        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn = view.findViewById(R.id.prevBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickPlayBtn();
            }
        });

//        stopBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pauseAudio();
//            }
//        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTrack();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevTrack();
            }
        });

        audioRecyclerView = view.findViewById(R.id.audioRecyclerView);

        AudioAdapter audioAdapter = new AudioAdapter(audios, this::playSelectedAudio);
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        audioRecyclerView.setAdapter(audioAdapter);

        return view;
    }

    private void handleClickPlayBtn() {
        if (isAudioPlay) {
            pauseAudio();
        } else {
            playAudio();
        }
    }

    private void playSelectedAudio(int position) {
        currentTrackIndex = position;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        playAudio();
    }

    private void playAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireContext(), audios.get(currentTrackIndex).getFile());
            mediaPlayer.setOnCompletionListener(mp -> nextTrack());
        }
        playBtn.setText(R.string.audio_pause_btn);
        isAudioPlay = true;

        String songName = audios.get(currentTrackIndex).getName();
        String songAuthor = audios.get(currentTrackIndex).getAuthor();

        currentAudio.setText(songName + " - " + songAuthor);
        mediaPlayer.start();
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            isAudioPlay = false;
            playBtn.setText(R.string.audio_play_btn);

            mediaPlayer.pause();
        }
    }

    private void nextTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        currentTrackIndex = (currentTrackIndex + 1) % audios.size();
        playAudio();
    }

    private void prevTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        currentTrackIndex = (currentTrackIndex - 1 + audios.size()) % audios.size();
        playAudio();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onPause() {
        super.onPause();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            isAudioPlay = false;
            playBtn.setText(R.string.audio_play_btn);

            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}