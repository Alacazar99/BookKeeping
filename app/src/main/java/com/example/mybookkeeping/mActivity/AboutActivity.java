package com.example.mybookkeeping.mActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mybookkeeping.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        back = findViewById(R.id.About_back);
        back.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.About_back:
                finish();
                break;
        }
    }
}
