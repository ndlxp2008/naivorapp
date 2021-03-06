package com.naivor.app.presentation.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bugtags.library.Bugtags;
import com.naivor.app.AppApplication;
import com.naivor.app.AppPageManager;
import com.naivor.app.R;
import com.naivor.app.domain.presenter.BasePresenter;
import com.naivor.app.extras.utils.DpUtil;
import com.naivor.app.extras.utils.FontUtil;
import com.naivor.app.extras.utils.ToastUtil;
import com.naivor.app.presentation.di.component.ActivityComponent;
import com.naivor.app.presentation.di.component.ApplicationComponent;
import com.naivor.app.presentation.di.component.DaggerActivityComponent;
import com.naivor.app.presentation.di.module.ActivityModule;
import com.naivor.app.presentation.view.BaseUiView;
import com.naivor.app.presentation.widget.LoadingDialog;

import javax.inject.Inject;

/**
 * BaseActivity 是所有activity的基类，把一些公共的方法放到里面
 * <p>
 * Created by tianlai on 16-3-3.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseUiView {

    private ActivityComponent activityComponent;

    @Inject
    protected LayoutInflater inflater;

    @Inject
    protected Context context;

    @Inject
    protected AppPageManager appPageManager;

    protected Toolbar toolbar;

    protected LinearLayout rootView;

    protected BasePresenter presenter;

    protected LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用依赖注入
        initInjector();
        injectActivity(activityComponent);

        //设置根布局
        rootView = (LinearLayout) inflateView(R.layout.activity_base);
        setContentView(rootView);

        // 将当前activity加入ActivityManager中
        appPageManager.addActivity(this);

        // 设置导航栏
        toolbar = (Toolbar) find(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initToolbar(toolbar);

        //初始化加载数据对话框
        loadingDialog=new LoadingDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);

        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface loadingDialog) {
                presenter.cancleLoading();
            }
        });

        //初始化Presenter
        presenter = getPresenter();
        if (presenter == null) {
            throw new NullPointerException("presenter 不能为 Null");
        } else {
            presenter.oncreate(savedInstanceState, context);
        }


    }

    /**
     * 初始化Toolbar
     *
     * @param toolbar
     */
    protected void initToolbar(Toolbar toolbar) {

        toolbar.setNavigationIcon(R.mipmap.ic_toolbar_navigation);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 设置页面的标题
     *
     * @param title
     */
    protected void setPageTitle(String title) {
        setPageTitle(FontUtil.addColor("#F04877", title));
    }

    /**
     * @param title
     */
    protected void setPageTitle(CharSequence title) {
        toolbar.setTitle(title);
    }

    /**
     * @param resId
     */
    protected void setNavigationIcon(int resId) {
        toolbar.setNavigationIcon(resId);
    }

    /**
     * // 加载ContentView的内容
     *
     * @param view
     */
    protected void setContentViewToRoot(View view) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                .LayoutParams.MATCH_PARENT);
        rootView.addView(view, params);
    }

    protected void setContentViewToRoot(int layoutId) {
        setContentViewToRoot(inflateView(layoutId));
    }


    /**
     * 进行注入
     *
     * @param activityComponent
     */
    protected abstract void injectActivity(ActivityComponent activityComponent);

    /**
     * 获取 ActivityModule
     *
     * @return
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    /**
     * 获取注入Activity的Presenter对象
     *
     * @return
     */
    protected abstract BasePresenter getPresenter();


    /**
     * 隐藏Toolbar
     */
    protected void hideToolbar() {

        toolbar.setVisibility(View.GONE);
    }

    /**
     * 把布局变成View
     *
     * @param layoutId
     * @return
     */
    protected View inflateView(int layoutId) {
        return inflater.inflate(layoutId, null);
    }

    /**
     * 设置注入器
     */
    private void initInjector() {
        ApplicationComponent appComponent = getAppComponent();
        appComponent.inject(this);

        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build();
    }


    /**
     * 获取 ApplicationComponent
     *
     * @return
     */
    public ApplicationComponent getAppComponent() {
        return ((AppApplication) getApplication()).getAppComponent();
    }

    /**
     * activityComponent的getter方法
     *
     * @return
     */
    public ActivityComponent getActivityComponent() {

        return activityComponent;
    }

    /**
     * 显示页面加载状态
     */
    @Override
    public void showLoading() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 加载完成，隐藏加载状态
     */
    @Override
    public void loadingComplete() {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onResume(this);
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        presenter.onSave(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        appPageManager.removeActivity(this);

        presenter.onDestroy();

        presenter = null;

        ToastUtil.cancleToast();

    }

    /**
     * 生成Toolbar右边Action按钮
     *
     * @param name
     * @param viewId
     * @return
     */
    @SuppressLint("NewApi")
    public TextView addRightActionView(CharSequence name, int viewId) {
        TextView tv_right = new TextView(context);

        if (viewId == 0) {
            tv_right.setId(R.id.tv_right_action);
        } else {
            tv_right.setId(viewId);
        }

        tv_right.setTextColor(getResources().getColor(R.color.font_red));
        tv_right.setText(name);
        tv_right.setCompoundDrawablePadding(DpUtil.dp2px(context, 3));
        tv_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv_right.setGravity(Gravity.CENTER);

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = DpUtil.dp2px(context, 10);
        layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        tv_right.setLayoutParams(layoutParams);

        return tv_right;

    }

    /**
     * 生成Toolbar中心显示titile的View
     *
     * @return
     */
    @SuppressLint("NewApi")
    public TextView addCenterTitleView(String title, int viewId) {
        TextView tv_title = new TextView(context);

        if (viewId == 0) {
            tv_title.setId(R.id.tv_center_title);
        } else {
            tv_title.setId(viewId);
        }

        tv_title.setTextColor(getResources().getColor(R.color.font_red));
        tv_title.setCompoundDrawablePadding(DpUtil.dp2px(context, 3));
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_title.setGravity(Gravity.CENTER);

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        tv_title.setLayoutParams(layoutParams);

        if (title != null) {
            tv_title.setText(title);
        }

        return tv_title;
    }

    /**
     * 设置应用的字体不随系统设置的更改而更改
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return resources;
    }

    public AppPageManager getAppPageManager() {
        return appPageManager;
    }

    public void setAppPageManager(AppPageManager appPageManager) {
        this.appPageManager = appPageManager;
    }

    /**
     * 获取view
     *
     * @param viewId
     * @return
     */
    public View find(int viewId) {
        return findViewById(viewId);
    }

    /**
     * 获取view
     *
     * @param viewId
     * @return
     */
    public View find(View parent, int viewId) {
        return parent.findViewById(viewId);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideIME() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}