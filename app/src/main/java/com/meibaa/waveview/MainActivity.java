package com.meibaa.waveview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.meibaa.waveview.view.WaveView;

public class MainActivity extends AppCompatActivity {

    private WaveView mWaveView;
    private TextView tvSize;
    private TextView tvCreateTime;
    private TextView tvKeepTime;
    private TextView tvMaxRate;
    public static final int TXT_SIZE = 1;
    public static final int CREATE_TIME = 2;
    public static final int KEEP_TIME = 3;
    public static final int MAX_RATE = 4;

    public static final int ADD_FLAG = 1;
    public static final int REDUCE_FLAG = 2;
    public static final int SIZE_D_VALUE = 1;
    public static final int TIME_D_VALUE = 100;
    public static final float RATE_D_VALUE = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaveView = (WaveView) findViewById(R.id.wave_view);
        initShowTxt();
    }

    private void initShowTxt() {
        tvSize = (TextView) findViewById(R.id.tv_show_txt_size);
        tvCreateTime = (TextView) findViewById(R.id.tv_show_create_time);
        tvKeepTime = (TextView) findViewById(R.id.tv_show_keep_time);
        tvMaxRate = (TextView) findViewById(R.id.tv_show_max_rate);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWaveView) {
            mWaveView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWaveView) {
            mWaveView.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWaveView) {
            mWaveView.stopImmediately();
        }
    }


    public void click(View v) {
        switch (v.getId()) {
            case R.id.tv_add_txt_size:
                modifyTxtSize(TXT_SIZE, tvSize, ADD_FLAG, SIZE_D_VALUE);
                break;
            case R.id.tv_reduce_txt_size:
                modifyTxtSize(TXT_SIZE, tvSize, REDUCE_FLAG, SIZE_D_VALUE);
                break;
            case R.id.tv_add_create_time:
                modifyTxtSize(CREATE_TIME, tvCreateTime, ADD_FLAG, TIME_D_VALUE);
                break;
            case R.id.tv_reduce_create_time:
                modifyTxtSize(CREATE_TIME, tvCreateTime, REDUCE_FLAG, TIME_D_VALUE);
                break;
            case R.id.tv_add_keep_time:
                modifyTxtSize(KEEP_TIME, tvKeepTime, ADD_FLAG, TIME_D_VALUE);
                break;
            case R.id.tv_reduce_keep_time:
                modifyTxtSize(KEEP_TIME, tvKeepTime, REDUCE_FLAG, TIME_D_VALUE);
                break;
            case R.id.tv_add_max_rate:
                modifyTxtSize(MAX_RATE, tvMaxRate, ADD_FLAG, RATE_D_VALUE);
                break;
            case R.id.tv_reduce_max_rate:
                modifyTxtSize(MAX_RATE, tvMaxRate, REDUCE_FLAG, RATE_D_VALUE);
                break;
        }

    }

    private void modifyTxtSize(int type, TextView tv, int flag, float value) {
        float textSize = Float.parseFloat(tv.getText().toString());
        if (ADD_FLAG == flag) {
            Log.i("----",textSize +"        +        "+ value);
            tv.setText(String.valueOf(textSize + value));
        } else {
            Log.i("----",textSize +"         -       "+ value);
            tv.setText(String.valueOf(textSize - value));
            switch (type) {
                case TXT_SIZE:
                    mWaveView.setTextSize((int) Float.parseFloat(tv.getText().toString()));
                    break;
                case CREATE_TIME:
                    mWaveView.setSpeed((int)Float.parseFloat(tv.getText().toString()));
                    break;
                case KEEP_TIME:
                    mWaveView.setDuration((int)Float.parseFloat(tv.getText().toString()));
                    break;
                case MAX_RATE:
                    mWaveView.setMaxRadiusRate(Float.parseFloat(tv.getText().toString()));
                    break;
            }
        }
        mWaveView.stopImmediately();
        mWaveView.start();
    }

}
