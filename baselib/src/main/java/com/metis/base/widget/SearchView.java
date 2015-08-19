package com.metis.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.metis.base.R;

/**
 * Created by Beak on 2015/8/19.
 */
public class SearchView extends RelativeLayout {

    private EditText mSearchInput = null;
    private ImageView mSearchCloseBtn = null;

    private OnSearchListener mSearchListener = null;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis(context);
    }

    private void initThis (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_search_view, this);
        mSearchInput = (EditText)findViewById(R.id.search_input);
        mSearchCloseBtn = (ImageView)findViewById(R.id.search_close_btn);

        mSearchCloseBtn.setOnClickListener(mClickListener);
        mSearchInput.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (mSearchListener != null) {
                            mSearchListener.onSearchHappened(mSearchInput.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    if (mSearchListener != null) {
                        mSearchListener.onSearchClear();
                    }
                }
            }
        });
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final int id = v.getId();
            if (id == mSearchCloseBtn.getId()) {
                mSearchInput.setText("");
                mSearchInput.requestFocus();
                if (mSearchListener != null) {
                    mSearchListener.onSearchClear();
                }
            }
        }
    };

    public void setOnSearchListener (OnSearchListener listener) {
        mSearchListener = listener;
    }

    public interface OnSearchListener {
        public void onSearchHappened (String content);
        public void onSearchClear ();
    }
}
