
# 组件化框架项目


## 简介
该项目目前集成现在主流的开发框架和技术，包括okhttp、rxjava、retrofit、glide、greendao、dagger、mvp、md风格、皮肤插件、热修复tinker,bugly、友盟数据统计和组件化等。

采用组件化开发框架，可以使模块单独编译调试，可以有效地减少编译的时间，更好地进行并发开发，从而极大的提高了并行开发效率。



## 项目详解地址为

https://juejin.im/entry/5a1cca70f265da432652923f

http://www.jianshu.com/p/e6eb9c8d120f

https://juejin.im/post/5c2d8fe46fb9a049c30b5d4b

## 本次更新内容(2019-1-3)：
#### 1.内部更新编译SDK版本到28.0.0，google的support库迁移到androidx；
#### 2.在线下载皮肤插件，无需重启更新全局。
#### 3.基类库集成字体库、友盟页面数据统计、bugly（bug监控、升级和热修复）等功能。
#### 4.改版音乐模块，功能界面简洁优美。
#### 5.新增vip电影模块，数据来源于腾讯视频接口以及网上的vip视频解析接口。
#### 6.改版帖子发布UI界面，包括纯文本、图文、视频等格式，界面类似于微博。
#### 7.改版聊天界面，新增聊天背景图，完善UI细节。
#### 8.基类库添加今日头条适配方案。只需要在基类库中填写相应的设计图尺寸便可，亲测在公司项目上已适配成功，适配成本低。
#### 9.改版图片浏览界面，滑动渐隐删除、并伴随共享动画效果，效果类似于微信朋友圈。
#### 10.评论界面、帖子展示界面、个人中心界面等添加共享动画效果。
#### 11.新增系统反馈和关于界面。
#### 12.基类库新增保活Service基类，（包括目前比较主流的保活策略，如：JobService、onStartCommend返回Sticky，onDestroy重新创建，一像素activity保活、系统广播保活、系统漏洞startForeground等）
#### 13.基类库新增音乐播放和视频播放管理类。
#### 14.各个三方库基本上更新到最新版本，主要是为了与androidx进行交接。
#### 15.内部模块之间的通信已经抛弃了阿里开源的Arouter，采用自己搭建的路由框架和RxBus来进行模块通信
#### 16.新增沉浸式状态栏，适配activity和fragment，已经封装到基类库
#### 17.修复图片选择器大图加载的bug
#### 18.统一使用DefaultModel来替代全局的mvp架构中的M模块
#### 19.新增后台推送数据管理app ，属于其中的manager模块。
#### 20.移除直播模块，数据来源于全民直播。至于为什么用不了（你们懂的_）
#### 21....太多了，列举不了这么多，具体效果请看app。

## app下载地址：
http://bmob-cdn-17771.b0.upaiyun.com/2019/01/04/e97fd73e40819993806c379e2a6bc79f.apk


#### 由于该项目是本人一个人独立开发的，所以我这边不仅仅考虑技术上的问题，还得考虑UI设计等方面，由于我用的三方bmob后台，所以不需要考虑后台的开发，但任务还是挺繁重的，接下去要做的事情还有很多，主要有如下：

#### 1.新增手机号一键登陆注册功能。
#### 2.密码找回界面，包括手机号、邮箱等方式,以及密码修改等服务。
#### 3.改版皮肤插件更新模式，新增多种皮肤插件。
#### 4.整合聊天界面和帖子界面的数据交互。
#### 5.帖子界面新增分享新闻、音乐、vip电影的功能。
#### 6.打通音乐、视频、新闻模块的用户信息，包括用户的浏览历史记录以及对相应用户进行消息推送。
#### 7.优化app的电量管理以及内存管理。
#### 8.权限管理各版本兼容等。
#### 9.音乐模块上新增推荐歌曲、歌单界面以及歌手分类界面。
#### 10......等等，后续会一直维护更新，敬请期待！！！。

## screenshots & Video
### 新闻模块 
视频展示地址：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-13-57-37-115.mp4

<image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/new_1.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/new_2.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/new_3.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/new_4.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/new_5.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/new_6.png
  width=250 height=450>
 ### 音乐模块(由于为了展示锁屏音乐播放界面，因此录制了两端视频)
第一部分：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-13-43-34-35.mp4

第二部分：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-13-45-10-119.mp4

  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_1.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_2.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_3.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_4.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_5.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_6.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_7.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_8.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_9.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_10.png
  width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_11.png
   width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_12.png
    width=250 height=450>
    <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_13.png
     width=250 height=450>
     <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_14.png
      width=250 height=450>
      <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/music_15.png
       width=250 height=450>
 ### 直播模块
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/live_1.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/live_2.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/live_3.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/live_4.png
 width=250 height=450>
 ### vip电影视频模块
 视频展示地址：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-15-39-39-135.mp4
   
   <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/video_1.png
  width=250 height=450>
    <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/video_2.png
   width=250 height=450>
     <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/video_3.png
    width=250 height=450>
      <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/video_4.png
     width=450 height=250>
       <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/video_5.png
      width=250 height=450>
         <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/video_6.png
            width=250 height=450>


 ### 聊天模块
 独立项目github地址:https://github.com/HelloChenJinJun/TestChat(该项目包括群聊功能的实现)
 
 聊天项目详解地址:http://www.jianshu.com/p/2d76430617ae
 
 
 #### 帖子模块：
http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-14-50-02-348.mp4

#### 主模块:
第一部分：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-15-16-36-145.mp4

第二部分：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-15-18-28-177%280%29.mp4

第三部分：http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-15-24-56-962.mp4

#### 皮肤插件模块：
http://pkqddsu1y.bkt.clouddn.com/Screenrecorder-2019-01-01-15-37-24-991.mp4
 
 
   <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_1.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_2.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_3.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_4.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_5.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_6.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_7.png
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_8.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_9.png
  width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_10.png
   width=250 height=450>
    <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_11.png
    width=250 height=450>
     <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_12.png
     width=250 height=450>
      <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_13.png
      width=250 height=450>
       <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_14.png
       width=250 height=450>
        <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_15.png
        width=250 height=450>
         <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_16.png
         width=250 height=450>
          <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_17.png
          width=250 height=450>
           <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_18.png
           width=250 height=450>
            <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_19.png
            width=250 height=450>
             <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_20.png
             width=250 height=450>
              <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_21.png
              width=250 height=450>
               <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_22.png
                width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_23.png
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_24.png
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_25.png
 width=250 height=450>
     <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_26.png
  width=250 height=450>
    <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/chat_27.png
    width=250 height=450>

### bmob后台系统通知管理模块
后台通知管理app下载地址：

http://bmob-cdn-17771.b0.upaiyun.com/2019/01/03/1afe58254020329d80aa7778747b38d2.apk

 <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/manager_1.png
    width=250 height=450>
     <image src=https://github.com/HelloChenJinJun/NewFastFrame/blob/common/screenshots/manager_2.png
        width=450 height=225>

 ### 视频效果以及apkDemo中的效果可能有时候跟开发进度匹配不上，想看最新效果的，请从最新源码中编译运行观看。

 
 ## 参考的项目
MVPArms
https://github.com/JessYanCoding/MVPArms

全民直播
https://github.com/jenly1314/KingTV

音乐项目
https://github.com/hefuyicoder/ListenerMusicPlayer，
https://github.com/aa112901/remusic

大象：PHPHub客户端
https://github.com/Freelander/Elephant

MvpApp
https://github.com/Rukey7/MvpApp

CloudReader
https://github.com/youlookwhat/CloudReader

非常感谢以上开源项目的作者！谢谢！

# License


    Copyright 2018, chenjinjun       
  
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at 
 
       http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## 以上的vip电影、音乐和新闻模块上的资源都是从网上收集过来的，只用于提供学习交流，切勿商用!!!如有侵权，请联系作者将其删除。

 
 
 
 
