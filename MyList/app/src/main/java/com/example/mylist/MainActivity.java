package com.example.mylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.mylist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //animated background
        AnimationDrawable animationDrawable = (AnimationDrawable) binding.rlBackground.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //recycler view linking with task_layout.xml
        binding.rvTasks.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //Floating button to add tasks
        binding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);

            }
        });



    }
}