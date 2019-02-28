# 快学
### 简介
“快学”是一款针对大学生课后学习缺乏积极性和难找到一起学习的同伴采用知识付费理念而开发的一款助学类应用软件。
越来越多的大学生在学校不会利用课后的时间主动学习，更多原因是不会利用自己已经学到的知识，再加上感受不到周边的学习氛围，就更懒得主动学习了。因此我们HappyBug小组决定以此为切入点，并以此进行扩展，着力开发出了这款"快学"软件。软件把大学的学科分为专题的形式，用户可以查看专题下的各种内容；用户可以通过LIVE交互式学习，时刻感受学霸的学习经历；也可以在地图上看到周边的学习小组，通过与周边的同学一起学习在校园中留下自己的学习痕迹。用户在学习的同时通过积极分享自己的学习经验和优秀笔记，也可以获得一笔回馈来促进持续学习的热情

### 目标群体
“快学”是一款适用于大学生的助学app，以人和知识作为入口，使大学生充分把握课余时间扩展知识面并且找到适合自己的学习圈。每个用户在学习别人经验的同时也可以成为一个分享者，“快学”希望通过“知识付费”的理念，使用户在使用“快学”时能感受到知识的价值和分享的力量，从而改变大学生下课后漫无目的的状态和通过无意义的兼职获取收入的现状。

### 功能介绍
* 注册功能	用户通过注册功能可以拥有自己的账号密码，之后通过自己的信息，进入游戏。
* LIVE对话	用户付一定的费用参与到LIVE后，除了查看主讲人所有的分享内容外，可以和LIVE的主讲人实时对话交互，提出自己的疑惑。
* 语音播放	用户通过点击该功能，应用将会将用户当前参与的LIVE主讲人的所有内容语音读出，无论是文字还是语音，都会自动播放出来。
* 直播和录制	主讲人可以在自己的LIVE中开始直播功能展示自己的学习过程，参与到该LIVE的用户可以使用录制的功能记录下主讲人的分享内容方便以后查看。
* 附近小组	用户可以在地图上看到自己周边的学习小组，点击小组头像可以查看组内的小组信息和学习照片。参与到该小组并完成学习任务后，会获得“证书”并在地图永久的留下标识。
* 悬赏问答	用户可以使用此功能付费提问或者通过回答悬赏问题来获取报酬。
* 资源共享	当用户打开此页面，可以查看并下载其他用户分享的付费或免费的优秀资源。
* 图片搜索	当用户使用此功能搜索时，可以根据图片或条形码的识别结果为用户推送相关的专题。
### 图片展示
<img src="https://github.com/DreamYHD/Leet/blob/master/img/1.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/2.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/3.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/4.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/5.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/6.png?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/7.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/8.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/9.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/10.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/11.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/12.jpg?raw=true" width=250 height=500 />
<img src="https://github.com/DreamYHD/Leet/blob/master/img/13.png?raw=true" width=250 height=500 />

### 作品实现、难点及特色分析
#### 作品实现及难点
*	直播网络请求处理：推流端到服务器再到播放端的视频流处理RTMP协议推送，FLV直播播放，美颜效果处理。
*	地图位置共享：把服务器上的地图坐标传给高德地图，通过后台处理不同的MARKET在地图上显示出不同的小组信息。
*	手写字识别：基于Tensorflow搭建CNN卷积神经网络识别手写字体
*	敏感字处理：使用字典树以及DFA过滤敏感词语
#### 特色分析
* 学科分类：App把大学学科划分为专题的形式，更方便体系化学习。
*	知识变现：用户在使用的过程中“为知识付费，为价值买单”，在分享的过程中收获客观的经济收益。基于这样的循环，促进大学生学习和分享的积极性。
*	图片搜索：利用图片的识别结果，为用户推送相关的学习专题，更加智能、准确。
*	LIVE交互：主讲人可以使用聊天、直播多种形式实时分享。其特色在于可以增加分享者与学习者的交互性，有问题随时问随时解决。
*	附近小组：把用户周边的学习小组展示在地图上。帮助用户找到周边可以一起学习的人并在地图上留下回忆。
### 团队介绍和人员分工
##### 所在学校 中北大学<br>
##### 团队名称 HappyBug小组<br>
##### 团队人员及分工<br>
 * 队长 杨浩东  &nbsp;项目框架搭建及资源模块,地图,直播模块代码编写<br>
 * 队员 岳强   &nbsp;  直播模块,live模块代码以编写<br>
 * 队员 杨逸帆  &nbsp;搜索模块代码编写<br>
 * 队员 赵琦  &nbsp; 美工及应用原型设计<br>
 * 队员 冀永光  &nbsp; 服务器相关模块  <br>
 * 指导教师  &nbsp; 秦品乐

