package com.example.commonlibrary.net.upload;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by COOTEK on 2017/8/4.
 */

public class UpLoadRequestBody extends RequestBody {


    private UpLoadProgressListener listener;
    private RequestBody delegate;


    public UpLoadRequestBody(UpLoadProgressListener listener, RequestBody delegate) {
        this.listener = listener;
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CustomSink customSink = new CustomSink(sink);
        BufferedSink bufferedSink = Okio.buffer(customSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.buffer();
    }


    @Override
    public long contentLength() throws IOException {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class CustomSink extends ForwardingSink {
        private long currentSize;
        private long totalSize;

        public CustomSink(Sink delegate) throws IOException {
            super(delegate);
            totalSize = contentLength();
        }


        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            currentSize += byteCount;
            listener.onUpdate(currentSize,totalSize);
        }
    }
}
