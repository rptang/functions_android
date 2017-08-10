package com.project.rptang.android.data_binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Android
 * com.project.rptang.android.data_binding
 * MyAdapter
 * <p>
 * Created by Stiven on 2017/8/10.
 * Copyright © 2017 ZYZS-TECH. All rights reserved.
 */

public class MyAdapter<T> extends BaseAdapter{

    private List<T> data;
    private int itemLayoutId;
    private int variableId;

    public MyAdapter(List<T> data, int itemLayoutId, int variableId) {
        this.data = data;
        this.itemLayoutId = itemLayoutId;
        this.variableId = variableId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(
                    parent.getContext()),itemLayoutId,parent,false);
        }else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(variableId,data.get(position));
        return binding.getRoot();
    }
}
