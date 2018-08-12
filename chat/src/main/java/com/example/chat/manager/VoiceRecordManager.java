package com.example.chat.manager;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/18      10:07
 * QQ:             1981367757
 */

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

import com.example.chat.base.Constant;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 录制声音管理
 */
public class VoiceRecordManager {
        /**
         * 编码比特率
         */
        private static final int RECORDING_BITRATE = 12200;
        private static final int VOICE_TIME_CHANGE = 0;
        public static final int MAX_RECORD_TIME = 60;
        private static Object LOCK = new Object();
        private static VoiceRecordManager INSTANCE;
        private MediaRecorder mMediaRecorder;
        private OnVoiceChangerListener mListener;
        private volatile boolean isRecording = false;
        private String voiceRecordPath = "";
        private long startTime;
        /**
         * 缓存线程池
         */
        private ExecutorService mExecutorService;
        private Handler updateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                        switch (msg.what) {
                                case VOICE_TIME_CHANGE:
                                        if (mListener != null) {
                                                mListener.onVoiceColumnChange(msg.arg1);
                                                if (msg.arg2 % 10 == 0) {
                                                        mListener.onRecordTimeChange(msg.arg2 / 10, voiceRecordPath);
                                                }
                                        }
                                        break;
                        }
                }
        };
        private File voiceFile;

        private VoiceRecordManager() {
                mExecutorService = Executors.newCachedThreadPool();

        }

        public static VoiceRecordManager getInstance() {
                if (INSTANCE == null) {
                        synchronized (LOCK) {
                                if (INSTANCE == null) {
                                        INSTANCE = new VoiceRecordManager();
                                }
                        }
                }
                return INSTANCE;
        }

        public void setOnVoiceChangeListener(OnVoiceChangerListener listener) {
                mListener = listener;
        }

        /**
         * 以用户ID为文件目录开始记录声音
         *
         * @param uid 用户ID
         */
        public void startRecording(String uid) {
                if (mMediaRecorder == null) {
                        mMediaRecorder = new MediaRecorder();
//                       设置音频的输出格式
                        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        设置为单通道
                        mMediaRecorder.setAudioChannels(1);
                        //                        设置编码格式
                        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//                        设置编码器
                        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        设置音频编码录音比特率
                        mMediaRecorder.setAudioEncodingBitRate(RECORDING_BITRATE);
                        mMediaRecorder.setOnErrorListener((mr, what, extra) -> LogUtil.e("录制声音出错"));
                } else {
//                        mMediaRecorder.stop();
//                        mMediaRecorder.reset();
                        stopRecord();
                        return;
                }
                voiceRecordPath = getVoiceRecordPath(uid);
                voiceFile = new File(voiceRecordPath);
                mMediaRecorder.setOutputFile(voiceFile.getAbsolutePath());
                try {
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                        isRecording = true;
                        this.startTime = System.currentTimeMillis();
                        mExecutorService.execute(new VoiceRecordChangeListener());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        /**
         * 停止录音
         *
         * @return 返回录制时长
         */
        public int stopRecord() {
                if (mMediaRecorder != null) {
                        isRecording = false;
                        mMediaRecorder.stop();
//                        记得释放资源
                        mMediaRecorder.release();
                        mMediaRecorder = null;
                        return (int) ((System.currentTimeMillis() - startTime) / 1000);
                }
                return 0;
        }

        /**
         * 取消录制
         */
        public void cancelRecord() {
                if (mMediaRecorder != null) {
                        isRecording = false;
                        mMediaRecorder.stop();
                        mMediaRecorder.release();
                        mMediaRecorder = null;
                        if (voiceFile != null && voiceFile.exists() && !voiceFile.isDirectory()) {
                                voiceFile.delete();
                        }
                }
        }


        /**
         * 根据UID获取和当前时间获取用户此次录制声音的路径
         *
         * @param uid 用户ID
         * @return 路径
         */
        private String getVoiceRecordPath(String uid) {
                String localId = CommonUtils.md5(UserManager.getInstance().getCurrentUserObjectId());
                File dir = new File(Constant.VOICE_CACHE_DIR + localId + File.separator + uid);
                if (!dir.exists()) {
                        dir.mkdirs();
                }
                File voiceFile = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".amr");
                if (!voiceFile.exists()) {
                        try {
                                voiceFile.createNewFile();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return voiceFile.getAbsolutePath();
        }

        public String getVoiceFilePath() {
                return voiceRecordPath;
        }

        /**
         * 录制声音的接口
         */
        public interface OnVoiceChangerListener {
                void onVoiceColumnChange(int value);

                void onRecordTimeChange(int time, String localVoicePath);
        }

        /**
         * 声音记录改变监听器
         */
        private class VoiceRecordChangeListener implements Runnable {
                @Override
                public void run() {
//                        单位为秒
                        int currentRecordTime = 0;
                        while (isRecording) {
                                int volume = mMediaRecorder.getMaxAmplitude();
//                                将音量控制到0~5之间
                                int endVolume = volume * 5 / 32768;
                                if (endVolume > 5) {
                                        endVolume = 5;
                                }
                                Message message = new Message();
                                message.arg1 = endVolume;
                                message.arg2 = currentRecordTime;
                                message.what = VOICE_TIME_CHANGE;
                                updateHandler.sendMessage(message);
                                try {
                                        Thread.sleep(100);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        Thread.currentThread().interrupt();
                                }
                                currentRecordTime++;
                        }
                }
        }
}
