package com.simpleweather.android.adapter;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.simpleweather.android.R;
import com.simpleweather.android.view.activity.SearchActivity;
import com.simpleweather.android.bean.CityBean;
import com.simpleweather.android.util.SpUtils;

import java.util.List;

public class SearchAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<CityBean> data;
    private SearchActivity activity;
    private String searchText;

    public SearchAdapter(SearchActivity activity, List<CityBean> data, String searchText) {
        this.activity = activity;
        this.data = data;
        this.searchText = searchText;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_searching,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder,
                                 @SuppressLint("RecyclerView") final int i) {
        MyViewHolder viewHolder = (MyViewHolder) myViewHolder;
        View itemView = viewHolder.itemView;
        String name = data.get(i).getCityName();
        int x = name.indexOf("-");
        String parentCity = name.substring(0, x);
        String location = name.substring(x + 1);

        String cityName = location + "，" + parentCity + "，" + data.get(i).getAdminArea() + "，"
                + data.get(i).getCnty();
        if (TextUtils.isEmpty(data.get(i).getAdminArea())) {
            cityName = location + "，" + parentCity + "，" + data.get(i).getCnty();
        }
        if (!TextUtils.isEmpty(cityName)) {
            viewHolder.srcHistoryCity.setText(cityName);
            if (cityName.contains(searchText)) {
                int index = cityName.indexOf(searchText);
                //创建一个 SpannableString对象
                SpannableString sp = new SpannableString(cityName);
                //设置高亮样式一
                sp.setSpan(new ForegroundColorSpan(activity.getResources()
                                .getColor(R.color.light_text_color)),
                        index, index + searchText.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.srcHistoryCity.setText(sp);
            }
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cid = data.get(i).getCityId();
                SpUtils.putString("cityId", cid);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView srcHistoryCity;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            srcHistoryCity = itemView.findViewById(R.id.scr_history_city);
        }
    }
}
