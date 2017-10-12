package com.example.chat.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.bean.ImageItem;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/13      22:45
 * QQ:             1981367757
 */

public class HappyContentDisplayActivity extends SlideBaseActivity {
        private TextView content;
        private ImageView display;
        private String textContent;
        private String url;


        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.happy_content_layout;
        }

        @Override
        public void initView() {
                textContent=getIntent().getStringExtra("content");
                url=getIntent().getStringExtra("url");
                content = (TextView) findViewById(R.id.tv_happy_content_text_display);
                if (url != null) {
                        display = (ImageView) findViewById(R.id.iv_happy_content_image_display);
                        display.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        List<ImageItem> list=new ArrayList<>();
                                        ImageItem imageItem =new ImageItem();
                                        imageItem.setPath(url);
                                        list.add(imageItem);
                                        BasePreViewActivity.startBasePreview(HappyContentDisplayActivity.this,list,0);
                                }
                });
        }
        }


        @Override
        public void initData() {
                if (textContent != null) {
                        content.setText(textContent);
                }
                if (url != null) {
                        Glide.with(this).load(url)
                                .into(display);
                }
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
                toolBarOption.setTitle("内容");
                setToolBar(toolBarOption);
        }

        @Override
        public void updateData(Object o) {

        }
}
