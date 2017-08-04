package com.example.commonlibrary.net;

import com.example.commonlibrary.net.db.NewFileInfo;

import retrofit2.Retrofit;

/**
 * Created by COOTEK on 2017/8/3.
 */

public class WrappedDownFile {
    private Retrofit retrofit;
    private NewFileInfo newFileInfo;



    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public NewFileInfo getNewFileInfo() {
        return newFileInfo;
    }

    public void setNewFileInfo(NewFileInfo newFileInfo) {
        this.newFileInfo = newFileInfo;
    }
}
