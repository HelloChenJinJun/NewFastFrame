package com.example.chat.mvp.bottomFolder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat.R;
import com.example.chat.adapter.ImageFoldersAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageFolder;
import com.example.chat.events.ImageFolderEvent;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.rxbus.RxBusManager;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/26     22:00
 * QQ:         1981367757
 */

public class CustomBottomFragment extends BottomSheetDialogFragment {


    private SuperRecyclerView display;
    private ImageFoldersAdapter imageFolderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_bottom, container);
        display = view.findViewById(R.id.srcv_fragment_custom_bottom);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration(getContext()));
        imageFolderAdapter = new ImageFoldersAdapter();
        display.setAdapter(imageFolderAdapter);
        imageFolderAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                ImageFolderEvent imageFolderEvent=new ImageFolderEvent(ImageFolderEvent.FROM_FOLDER,imageFolderAdapter.getData(position).getAllImages(),position);
                imageFolderEvent.setImageFolderName(imageFolderAdapter.getData(position).getName());
                RxBusManager.getInstance().post(imageFolderEvent);
                dismiss();
            }
        });
        List<ImageFolder> list = (List<ImageFolder>) getArguments().getSerializable(Constant.DATA);
        int position=getArguments().getInt(Constant.POSITION,0);
        imageFolderAdapter.setCurrentSelectedPosition(position);
        imageFolderAdapter.refreshData(list);
        return view;
    }


    public static CustomBottomFragment newInstance(ArrayList<ImageFolder> data, int position) {
        CustomBottomFragment customBottomFragment = new CustomBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, data);
        bundle.putInt(Constant.POSITION,position);
        customBottomFragment.setArguments(bundle);
        return customBottomFragment;
    }


}
