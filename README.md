# MvpApp

##### 练习MVP架构开发的App，算是对自己学过的知识做一个总结，做了有一段时间，界面比较多，代码量还是有的，里面做了大量封装，整体代码整理得很干净，这个我已经尽力整理了- -。

## Screenshot

##### 项目结构：

![](https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/project.png)

##### 应用截图：

<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E4%B8%BB%E9%A1%B5.png" height = "400" alt="主页" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E6%96%B0%E9%97%BB%E4%B8%BB%E9%A1%B5.png" height = "400" alt="新闻主页" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E6%96%B0%E9%97%BB%E8%AF%A6%E6%83%85.png" height = "400" alt="新闻详情" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E4%B8%93%E9%A2%98.png" height = "400" alt="专题" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E6%A0%8F%E7%9B%AE%E7%AE%A1%E7%90%86.png" height = "400" alt="栏目管理" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E5%9B%BE%E7%89%87%E6%96%B0%E9%97%BB.png" height = "400" alt="图片新闻" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E5%9B%BE%E7%89%87%E5%88%97%E8%A1%A8.png" height = "400" alt="图片列表" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E5%A4%A7%E5%9B%BE.png" height = "400" alt="大图" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E4%B8%8B%E8%BD%BD%E7%AE%A1%E7%90%86.png" height = "400" alt="下载管理" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E8%A7%86%E9%A2%91%E6%92%AD%E6%94%BE.png" height = "400" alt="视频播放" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E5%9B%BE%E7%89%87%E5%8A%A8%E5%9B%BE.gif" height = "400" alt="图片动图" align=center />
<img src="https://raw.githubusercontent.com/Rukey7/ScreenShot/master/MvpApp/%E4%B8%8B%E8%BD%BD%E5%8A%A8%E5%9B%BE.gif" height = "400" alt="下载管理" align=center />

## 用到的第三方库

- [leakcanary](https://github.com/square/leakcanary)
	：检测应用内存泄漏问题，这个都知道吧
- [butterknife](https://github.com/JakeWharton/butterknife)
	：帮助Android控件和回调的进行依赖注入，JakeWharton大神的力作
- [dagger2](https://github.com/google/dagger)
	：Android和Java依赖注入库
- [rxjava](https://github.com/ReactiveX/RxJava)
	：一个实现异步操作的库，现在非常火
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
	：用于Android的Rxjava绑定库
- [RxBinding](https://github.com/JakeWharton/RxBinding)
	：配合Rxjava处理控件异步调用
- [RxLifecycle](https://github.com/trello/RxLifecycle)
	：防止RxJava中subscription导致内存泄漏
- [RxPermissions](https://github.com/tbruyelle/RxPermissions)
	：基于RxJava开发的用于帮助在Android 6.0中处理运行时权限检测
- [retrofit](https://github.com/square/retrofit)
	：目前最好用的网络通讯库，应该都用过吧
- [okhttp](https://github.com/square/okhttp)
	：okhttp和retrofit做网络通讯是绝配
- [greenDAO](https://github.com/greenrobot/greenDAO)
	：ORM数据库，能配合rxjava使用
- [logger](https://github.com/orhanobut/logger)
	：Log库，让打印的Log变得非常漂亮
- [glide](https://github.com/bumptech/glide)
	：Google出品的图片加载库，这里有非常好的指导文档：https://mrfu.me/2016/02/27/Glide_Getting_Started/
- [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
	：很好用的RecyclerView多功能适配器库，项目里我并没有直接用这个库，而是按我自己使用习惯在它较早的代码上做了些改动
- [recyclerview-animators](https://github.com/wasabeef/recyclerview-animators)
	：RecyclerView的动画库，内置了非常多的动画效果
- [CircleImageView](https://github.com/hdodenhof/CircleImageView)
	：非常常用的用来显示圆形头像的库
- [PhotoView](https://github.com/chrisbanes/PhotoView)
	：可根据手势进行缩放的图像库，这个也很常见
- [AndroidImageSlider](https://github.com/daimajia/AndroidImageSlider)
	：展示头部Banner的库，动画效果很多，就是需要依赖picasso和nineoldandroids这两个库
- [NumberProgressBar](https://github.com/daimajia/NumberProgressBar)
	：性感的数字进度条
- [FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)
	：样式比TabLayout多样的Tab库
- [FlycoDialog](https://github.com/H07000223/FlycoDialog_Master)
	：多功能的Dialog
- [FlycoLabelView](https://github.com/H07000223/FlycoLabelView)
	：添加角标的库
- [gson](https://github.com/google/gson)
	：帮助Json和Object转换，这个也常用
- [fastjson](https://github.com/alibaba/fastjson)
	：功能同gson，有些地方gson不能处理可以试试这个，阿里巴巴出品
- [ijkplayer](https://github.com/Bilibili/ijkplayer)
	：B站出品的视频解码库
- [DanmakuFlameMaster](https://github.com/Bilibili/DanmakuFlameMaster)
	：同样B站出品的弹幕库
- [ShineButton](https://github.com/ChadCSong/ShineButton)
	：炫酷效果的点击按钮，主要用于显示收藏之类的动画
- [RichText](https://github.com/zzhoujay/RichText)
	：富文本的处理库，用起来挺方便就是有内存泄漏- -
- [Android-SpinKit](https://github.com/ybq/Android-SpinKit)
	：集成多种动画效果的Drawable，之前有看源码觉得代码封装得挺好，动画不仅仅只能用在View上
- [filepicker](https://github.com/Angads25/android-filepicker)
	：这个是用来处理PreferenceScreen的文件选中库，PreferenceScreen感觉平时不怎么看到使用，用法到时挺特别
- [tinker](https://github.com/Tencent/tinker)
	：微信Android热补丁方案，功能强大，和其它热修补方案对比看这里[wiki](https://github.com/Tencent/tinker/wiki)
- [DragSlopLayout](https://github.com/Rukey7/DragSlopLayout)
	：一个辅助开发拖拽功能的库，这是我为了做这个App的某些功能封装的库- -，现在倒也有用在工作的项目上
- [IjkPlayerView](https://github.com/Rukey7/IjkPlayerView)
	：基于ijkplayer开发的播放器，也是为了做这个App的视频播放功能封装的库- -，里面加了弹幕功能，感兴趣可以看下
- [TagLayout](https://github.com/Rukey7/TagLayout)
	：这个库代码有点厚重，有时间再重构下
	
Thanks
---

- 感谢所有优秀的开源项目
- [OuNews](https://github.com/oubowu/OuNews)
	：最初是看了这个项目才开始做的，学了不少东西
