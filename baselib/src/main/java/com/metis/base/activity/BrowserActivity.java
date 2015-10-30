package com.metis.base.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.base.R;
import com.metis.base.utils.Log;
import com.metis.base.utils.PatternUtils;

import im.delight.android.webview.AdvancedWebView;

public class BrowserActivity extends TitleBarActivity {

    private static final String TAG = BrowserActivity.class.getSimpleName();

    private WebChromeClient mChromeClient = null;
    private AdvancedWebView mBrowserWv = null;

    private String mUrl = null;
    private String mTitle = null;
    private boolean canGoOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        mBrowserWv = (AdvancedWebView)findViewById(R.id.browser_web_view);
        /*mBrowserWv.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String s, Bitmap bitmap) {

            }

            @Override
            public void onPageFinished(String s) {

            }

            @Override
            public void onPageError(int i, String s, String s1) {

            }

            @Override
            public void onDownloadRequested(String s, String s1, String s2, String s3, long l) {

            }

            @Override
            public void onExternalPageRequest(String s) {

            }
        });*/
        mChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getTitleBar().setTitleCenter(title);
                mTitle = title;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.v(TAG, "chromeClient onJsAlert");
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Toast.makeText(BrowserActivity.this, "onShowFileChooser", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "chromeClient onShowFileChooser ");
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
                Log.v(TAG, "openFileChooser");
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
                Log.v(TAG, "openFileChooser");
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.v(TAG, "openFileChooser");
            }
        };
        mBrowserWv.setWebChromeClient(mChromeClient);
        mBrowserWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mUrl = url;
            }
        });
        WebSettings settings = mBrowserWv.getSettings();
        settings.setJavaScriptEnabled(true);

        canGoOut = getIntent().getBooleanExtra(ActivityDispatcher.KEY_CAN_GO_OUT, false);
        mUrl = getIntent().getStringExtra(ActivityDispatcher.KEY_URL);
        mBrowserWv.loadUrl(mUrl);

        getTitleBar().setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (canGoOut) {
            getTitleBar().setDrawableResourceRight(R.drawable.ic_dots_horizontal);
            getTitleBar().setTitleCenter(mUrl);

            getTitleBar().setOnRightBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                    //ActivityDispatcher.outBrowserActivity(BrowserActivity.this, mUrl);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mBrowserWv.canGoBack()) {
            mBrowserWv.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBrowserWv.destroy();
    }

    private void showDialog () {
        String[] titles = null;
        if (canGoOut) {
            titles = new String[] {getString(R.string.text_share)/*, getString(R.string.text_open_in_out_browser)*/};
        }/* else {
            titles = new String[] {getString(R.string.text_share), getString(R.string.text_open_in_out_browser)};
        }*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(mTitle)) {
                    ActivityDispatcher.shareActivity(BrowserActivity.this, mTitle, mTitle, "http://www.meishuquan.net/img/logo2.png", PatternUtils.deleteUidInfosInUrl(mUrl));
                }
            }
        });
        builder.create().show();
    }
}
