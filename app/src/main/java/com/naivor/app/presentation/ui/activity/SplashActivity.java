package com.naivor.app.presentation.ui.activity;


import android.content.Intent;
import android.os.Bundle;

import com.naivor.app.R;
import com.naivor.app.presentation.di.component.ActivityComponent;
import com.naivor.app.presentation.presenter.BasePresenter;
import com.naivor.app.presentation.presenter.SplashPresenter;
import com.naivor.app.presentation.view.SplashView;
import com.naivor.requestdialog.LoadingDialog;

import javax.inject.Inject;

/**
 * SplashActivity app的欢迎页面
 * <p>
 * Created by tianlai on 16-3-3.
 */
public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    SplashPresenter splashPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToolbar();

    }

    @Override
    protected LoadingDialog initLoadingDialog() {
        return null;
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return splashPresenter;
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void LoadingComplete() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onResume(this);
    }

    @Override
    public void toMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void toLoginPage() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}