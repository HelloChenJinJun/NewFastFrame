package com.example.commonlibrary.net.okhttpconfig;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.OkHttpClient;


public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {


    private final Call.Factory client;

    public OkHttpUrlLoader(Call.Factory client) {
        this.client=client;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl glideUrl, int width, int height, @NonNull Options options) {
        return new LoadData<>(glideUrl, new OkHttpStreamFetcher(client, glideUrl));
    }

    @Override
    public boolean handles(@NonNull GlideUrl glideUrl) {
        return false;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private static volatile Call.Factory internalClient;
        private Call.Factory client;

        public Factory() {
            this(getInternalClient());
        }


        public Factory(Call.Factory client) {
            this.client = client;
        }

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        internalClient = new OkHttpClient();
                    }
                }
            }
            return internalClient;
        }



        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new OkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {
        }
    }
}
