package com.project.rptang.android.quick_index_bar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.project.rptang.android.R;

public class QuickIndexBarActivity extends Activity {

    private static final String TAG = "QuickIndexBarActivity";
    private QuickIndexBar mQuickIndexBar;
    private ListView mListView;
    private static final int DEFAULT_DELAY = 1200;

    private TextView mLabel;    //在屏幕中间显示的字母放大版标签

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_index_bar);
        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quickbar);
        mListView = (ListView) findViewById(R.id.listview);
        mLabel = (TextView) findViewById(R.id.showLabel);

        final ArrayList<Friend> friends = FriendsManager.getFriends();
        Collections.sort(friends);
        mAdapter = new MyAdapter(this, R.layout.list_item, friends);
        mListView.setAdapter(mAdapter);

        mQuickIndexBar.setIndexChangedListener(new QuickIndexBar.IndexChangedListener() {
            @Override
            public void indexChanged(String word) {
                Log.i(TAG, word);
                showIndexLabel(word);
                for (int i=0;i<friends.size();i++) {
                    String indexWord=friends.get(i).getName().charAt(0)+"";
                    if (indexWord.equals(word)) {
                        mListView.setSelection(i);
                        break;
                    }
                }


            }
        });


    }


    private Handler mHandler = new Handler();

    private void showIndexLabel(String word) {
        mLabel.setVisibility(View.VISIBLE);
        mLabel.setText(word);

        mHandler.removeCallbacksAndMessages(null); //移除所有消息队列
        mHandler.postDelayed(new Runnable() {      //发送延时消息
            @Override
            public void run() {
                mLabel.setVisibility(View.GONE);
            }
        }, DEFAULT_DELAY);
    }


    private class MyAdapter extends ArrayAdapter<Friend> {
        private Context mContext;
        private int mResource;

        public MyAdapter(Context context, int resource, List<Friend> objects) {
            super(context, resource, objects);
            mContext = context;
            mResource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, mResource, null);
            }
            holder = getHolder(convertView);
            Friend item = getItem(position);
            holder.nameTextView.setText(item.getName());

            String currIndexWord = item.getName().charAt(0) + "";

            if (position > 0) {
                String lastIndexWord = getItem(position - 1).getName().charAt(0) + "";
                if (lastIndexWord.equals(currIndexWord)) {
                    holder.indexTextView.setVisibility(View.GONE);
                } else {
                    holder.indexTextView.setVisibility(View.VISIBLE);
                    holder.indexTextView.setText(currIndexWord);
                }
            } else {
                holder.indexTextView.setVisibility(View.VISIBLE);
                holder.indexTextView.setText(currIndexWord);
            }


            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.index);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(viewHolder);
            }


            return viewHolder;
        }

        private ViewHolder holder;

        class ViewHolder {
            TextView indexTextView;
            TextView nameTextView;
        }

    }
}
