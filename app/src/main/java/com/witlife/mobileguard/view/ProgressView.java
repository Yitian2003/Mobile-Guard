package com.witlife.mobileguard.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.witlife.mobileguard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruce on 6/11/2017.
 */

public class ProgressView extends LinearLayout {
    @BindView(R.id.tv_internal)
    TextView tvInternal;
    @BindView(R.id.pb_internal)
    ProgressBar pbInternal;
    @BindView(R.id.tv_in_left)
    TextView tvInLeft;
    @BindView(R.id.tv_in_right)
    TextView tvInRight;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_progress, this);

        ButterKnife.bind(view);
    }

    public void setTitle(String title){
        tvInternal.setText(title);
    }

    public void setLeftText(String text){
        tvInLeft.setText(text);
    }

    public void setRightText(String text){
        tvInRight.setText(text);
    }

    public void setProgress(int progress){
        pbInternal.setProgress(progress);
    }
}
