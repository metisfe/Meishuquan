package com.metis.base.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.base.R;

public class VipActivity extends TitleBarActivity {

    private RecyclerView mVipRv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        mVipRv = (RecyclerView)findViewById(R.id.vip_recycler_view);
        mVipRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean showAsUpEnable() {
        return true;
    }

    @Override
    public CharSequence getTitleCenter() {
        return getString(R.string.text_my_vip);
    }

    private class VipPayHolder extends RecyclerView.ViewHolder {

        public TextView durationTv, priceTv;

        public VipPayHolder(View itemView) {
            super(itemView);
            durationTv = (TextView)itemView.findViewById(R.id.vip_pay_item_duration);
            priceTv = (TextView)itemView.findViewById(R.id.vip_pay_item_price);
        }
    }

    private class VipPayAdapter extends RecyclerView.Adapter<VipPayHolder> {

        @Override
        public VipPayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VipPayHolder(LayoutInflater.from(VipActivity.this).inflate(R.layout.layout_vip_pay_item, null));
        }

        @Override
        public void onBindViewHolder(VipPayHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
