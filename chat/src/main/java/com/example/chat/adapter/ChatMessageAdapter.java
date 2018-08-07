package com.example.chat.adapter;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.chat.R;
import com.example.chat.adapter.holder.chat.BaseChatHolder;
import com.example.chat.adapter.holder.chat.ReceiveImageHolder;
import com.example.chat.adapter.holder.chat.ReceiveLocationHolder;
import com.example.chat.adapter.holder.chat.ReceiveTextHolder;
import com.example.chat.adapter.holder.chat.ReceiveVideoHolder;
import com.example.chat.adapter.holder.chat.ReceiveVoiceHolder;
import com.example.chat.adapter.holder.chat.SendImageHolder;
import com.example.chat.adapter.holder.chat.SendLocationHolder;
import com.example.chat.adapter.holder.chat.SendTextHolder;
import com.example.chat.adapter.holder.chat.SendVideoHolder;
import com.example.chat.adapter.holder.chat.SendVoiceHolder;
import com.example.chat.bean.BaseMessage;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      17:44
 * QQ:             1981367757
 */

public class ChatMessageAdapter extends BaseMultipleRecyclerAdapter<BaseMessage, BaseChatHolder> {
        public static final int TYPE_SEND_TEXT = 0;
        public static final int TYPE_RECEIVER_TEXT = 1;
        public static final int TYPE_SEND_IMAGE = 2;
        public static final int TYPE_RECEIVER_IMAGE = 3;
        public static final int TYPE_SEND_VOICE = 4;
        public static final int TYPE_RECEIVER_VOICE = 5;
        public static final int TYPE_SEND_LOCATION = 6;
        public static final int TYPE_RECEIVER_LOCATION = 7;
        public static final int TYPE_SEND_VIDEO=8;
        public static final int TYPE_RECEIVER_VIDEO=9;


        //        5秒没回应就显示时间
        private static final long TIME_INTERVAL = 5 * 60 * 1000;









        @Override
        protected SparseIntArray getLayoutIdMap() {
                SparseIntArray sparseArray = new SparseIntArray();
                sparseArray.put(TYPE_SEND_TEXT, R.layout.item_activity_chat_send);
                sparseArray.put(TYPE_SEND_IMAGE, R.layout.item_activity_chat_send);
                sparseArray.put(TYPE_SEND_VOICE, R.layout.item_activity_chat_send);
                sparseArray.put(TYPE_SEND_LOCATION, R.layout.item_activity_chat_send);
                sparseArray.put(TYPE_SEND_VIDEO,R.layout.item_activity_chat_send);
                sparseArray.put(TYPE_RECEIVER_TEXT, R.layout.item_activity_chat_receive);
                sparseArray.put(TYPE_RECEIVER_IMAGE, R.layout.item_activity_chat_receive);
                sparseArray.put(TYPE_RECEIVER_LOCATION, R.layout.item_activity_chat_receive);
                sparseArray.put(TYPE_RECEIVER_VOICE, R.layout.item_activity_chat_receive);
                sparseArray.put(TYPE_RECEIVER_VIDEO,R.layout.item_activity_chat_receive);
                return sparseArray;
        }


        @Override
        public BaseChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                if (viewType == TYPE_SEND_TEXT) {
                        return new SendTextHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                } else if (viewType == TYPE_SEND_IMAGE) {
                        return new SendImageHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                } else if (viewType == TYPE_SEND_LOCATION) {
                        return new SendLocationHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));

                } else if (viewType == TYPE_SEND_VOICE) {
                        return new SendVoiceHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                }else if (viewType==TYPE_SEND_VIDEO){
                        return new SendVideoHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                }else if (viewType == TYPE_RECEIVER_TEXT) {
                        return new ReceiveTextHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));

                } else if (viewType == TYPE_RECEIVER_IMAGE) {
                        return new ReceiveImageHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));

                } else if (viewType == TYPE_RECEIVER_LOCATION) {
                        return new ReceiveLocationHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                } else if (viewType == TYPE_RECEIVER_VOICE) {
                        return new ReceiveVoiceHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                }else if (viewType==TYPE_RECEIVER_VIDEO){
                        return new ReceiveVideoHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
                }
                return new SendTextHolder(layoutInflater.inflate(getLayoutIds().get(viewType), parent, false));
        }


        @Override
        protected void convert(BaseChatHolder holder, BaseMessage data) {
                holder.bindAdapter(this);
                holder.initCommonData(data,shouldShowTime(holder.getAdapterPosition()));
        }

        private boolean shouldShowTime(int position) {
                return position == 0 || data.get(position).getCreateTime() - data.get(position - 1).getCreateTime() > TIME_INTERVAL;
        }






}
