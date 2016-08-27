package com.wangmaodou.heartview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    HeartView mHeartView;
    TextView mTextView;

    int count=99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHeartView=(HeartView)findViewById(R.id.mian_cardview_heartview);
        mTextView=(TextView)findViewById(R.id.main_cardview_textview);

        mTextView.setText(count+"");
        mHeartView.setStateChangeListener(new HeartView.StatusChangeListener(){

            @Override
            public void onLike() {
                count++;
                mTextView.setText(count+"");
            }

            @Override
            public void onUnlike() {
                count--;
                mTextView.setText(count+"");
            }

        });
    }
}
