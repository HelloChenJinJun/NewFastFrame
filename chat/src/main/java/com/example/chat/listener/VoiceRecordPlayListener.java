package com.example.chat.listener;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import com.example.chat.R;
import com.example.chat.bean.BaseMessage;
import com.example.chat.bean.MessageContent;
import com.example.chat.manager.DownLoadManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.FileUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.bmob.v3.exception.BmobException;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/21      22:17
 * QQ:             1981367757
 */
public class VoiceRecordPlayListener implements View.OnClickListener {
        private static boolean isPlaying = false;
        private BaseMessage mMessage;
        private Context mContext;
        private static VoiceRecordPlayListener currentListener;
        private AnimationDrawable mAnimationDrawable;
        private MediaPlayer mPlayer;
        private OnDownLoadFileListener listener = null;
        private ImageView display;

        public VoiceRecordPlayListener(Context context, ImageView imageView, BaseMessage message, OnDownLoadFileListener listener) {
                this.mContext = context.getApplicationContext();
                this.listener = listener;
                this.mMessage = message;
                this.display = imageView;
                if (currentListener == null) {
                        currentListener = this;
                }
        }

        @Override
        public void onClick(final View v) {
                if (isPlaying) {
                        if (currentListener.hashCode() == hashCode()) {
                                LogUtil.e("播放时按自身的重复语音");
                                stopAnimation();
                                stopRecord();
                                currentListener = null;
                                return;
                        }
                        currentListener.stopRecord();
                        currentListener.stopAnimation();
                        LogUtil.e("播放时按其他的语音");
                }
                LogUtil.e("这里开始播放声音");
                if (mMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        String localPath = BaseApplication
                                .getAppComponent().getGson().fromJson(mMessage.getContent(), MessageContent.class)
                                .getUrlList().get(0);
                        LogUtil.e("这里localPath" + localPath);

                        if (new File(localPath).exists()) {
                                startRecord(localPath, true);
                        } else {
                                if (listener != null) {
                                        DownLoadManager.getInstance().downFile(mMessage, new OnDownLoadFileListener() {
                                                @Override
                                                public void onStart() {
                                                        listener.onStart();
                                                }

                                                @Override
                                                public void onProgress(int value) {
                                                        listener.onProgress(value);
                                                }

                                                @Override
                                                public void onSuccess(String localPath) {
                                                        listener.onSuccess(localPath);
                                                        startRecord(localPath, true);
                                                }

                                                @Override
                                                public void onFailed(BmobException e) {
                                                        listener.onFailed(e);
                                                }
                                        });
                                }
                        }

                } else {
//                        别人发过来的,如果存在直接获取，否则在线下载存储到当地路径
                        if (!FileUtil.isExistFileLocalPath(mMessage.getBelongId(), mMessage.getCreateTime())) {
                                if (listener != null) {
                                        DownLoadManager.getInstance().downFile(mMessage, new OnDownLoadFileListener() {
                                                @Override
                                                public void onStart() {
                                                        listener.onStart();
                                                }

                                                @Override
                                                public void onProgress(int value) {
                                                        listener.onProgress(value);
                                                }

                                                @Override
                                                public void onSuccess(String localPath) {
                                                        listener.onSuccess(localPath);
                                                        startRecord(localPath, true);

                                                }

                                                @Override
                                                public void onFailed(BmobException e) {
                                                        listener.onFailed(e);
                                                }
                                        });
                                } else {
                                        LogUtil.e("监听为空111");
                                }
                        } else {
                                startRecord(FileUtil.getUserVoiceFilePath(mMessage.getBelongId(), mMessage.getCreateTime()), true);
                        }
                }
        }


        /**
         * 开始记录语音
         *
         * @param localPath 本地的文件路径
         * @param b         设置语音的风格  是否使用扬声器
         */
        private void startRecord(String localPath, boolean b) {
                File file = new File(localPath);
                if (!file.exists()) {
                        LogUtil.e("文件不存在");
                        return;
                }

                LogUtil.e("这里啦啦啦");
                AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mPlayer = new MediaPlayer();
                if (b) {
                        audioManager.setMode(AudioManager.MODE_NORMAL);
                        audioManager.setSpeakerphoneOn(true);
                        mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                } else {
                        audioManager.setMode(AudioManager.MODE_IN_CALL);
                        audioManager.setSpeakerphoneOn(false);
                        mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                }
                mPlayer.reset();
                try {
                        FileInputStream stream = new FileInputStream(file);
                        mPlayer.setDataSource(stream.getFD());
                        mPlayer.setOnPreparedListener(mp -> {
                                isPlaying = true;
                                startAnimation();
                                mp.start();
                        });
                        mPlayer.setOnCompletionListener(mp -> {
                                stopAnimation();
                                stopRecord();
                                LogUtil.e("播放完成,置空");
                                currentListener = null;
                        });
                        mPlayer.prepare();
                        currentListener = this;
                } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.e("io异常" + e.getMessage());
                }
        }

        private void stopAnimation() {
                if (mMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        display.setImageResource(R.drawable.voice_left3);
                } else {
                        display.setImageResource(R.drawable.voice_right3);
                }
                if (mAnimationDrawable != null) {
                        mAnimationDrawable.stop();
                }
        }

        private void startAnimation() {
                if (mMessage.getBelongId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        display.setImageResource(R.drawable.animationlist_chat_voice_left);
                } else {
                        display.setImageResource(R.drawable.animationlist_chat_voice_right);
                }
                mAnimationDrawable = (AnimationDrawable) display.getDrawable();
                mAnimationDrawable.start();
        }

        private void stopRecord() {
                if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = null;
                }
                isPlaying = false;
        }
}
