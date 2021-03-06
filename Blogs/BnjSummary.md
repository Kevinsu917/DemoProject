##  Bangnijiao Summary

#### 必备的模块

1. 广告页,引导页模块  

	这个模块使用compile 'com.prolificinteractive:parallaxpager:2.2.1'库来作为引导页的滑动  

	在引导页中的注意点:  

	1. 尽可能的使动画效果强,流畅.  
	2. 兼容性问题,因为手机的比例不同,如果使用整张图片作为背景,会导致在不同的手机显示的效果不同,要不就是截取部分,要不就是压缩了比例.  

	在广告页中需要注意的点:  

	1. 需要判断是否进入引导页  
	2. 需要判断是否通过网络获取广告图  
	3. 跳过广告的逻辑  
	4. 同引导页一样,需要考虑图片大小在不同手机的显示效果.(处理方法,可以请求不同比例的图片)

	**实现广告页面,和引导页面的Demo**

2. 登录, 注册, 忘记密码, 修改密码, 退出登录  

	这一个模块基本上是每个应用都必备的模块  

	注意点:  

	1. 页面之间的切换关系,例如从登录页面跳转到注册页面,在注册页面中注册成功,应该直接跳入主页面.这个时候需要finish登录页面.  
	2. 登录注册对于密码应该MD5,保证密码的安全性.  
	3. 对于使用手机号作为账号,应该考虑国际手机,以及手机号码的有效性.  
	4. 对于登录,注册按钮,应该避免重复多次点击,导致多次登录和多次注册.  
	5. 登录成功后,应该考虑如何保存用户信息.以及对App的各种初始化操作.(用户信息保存中,使用文件保存的方式,但是因为文件保存,需要是序列化的类,这样在扩展性方面需要考虑.如新加字段后,旧的数据将无法读取出来.)  
	6. 考虑自动登录的问题.当上一次登录成功后,记录了当前登录的用户,下次再打开App将在后台自动登录.即使没有网络,也可以进入app.  
	7. 因为没有网络进入,考虑如何在有网络的时候去登录,因为如果没有登录成功的话,其它的api应该也是请求不了的,所以可以通过拦截api返回的结果去做重新登录  
	8. 上面总结,发现有很多地方都可以登录进App,那么就会出现一个问题,在很多地方都需要登录成功后,初始化App的操作,应该考虑如何减少重复代码.  

	**优化工程中的代码**  
	**通过Python实现后台的登录注册代码,同时实现对应的Demo**

3. 相册模块  
	
	现在的应用基本上都有图片功能,相册,拍照等功能.把拍照的按钮安放在相册里面,然图片这个功能更加单一,纯粹.

	注意点:

	1. 通过ContentProvider的方式获取图片的Cursor,以及获取相册的Cursor,来作为ListView,GridView的数据源,这样做可以更加高效.而不是像我在遇见项目里面做的,把所以数据拿出来存放在容器里面.  
	2. 获取数据源后,通过UIL这个第三方库去展示小图,或者浏览大图的方式.注意设置好ImageLoader的参数选项,否者会导致滑动时候不流畅的问题  
	3. 可以把相册的Activity设置为使用新的进程打开,这样就可以把好资源的图片操作,通过新的进程来处理,能够获取更多的内存空间.[android:process],但是在运行的时候无法调试.同时需要考虑进程间通信的问题.  
	4. 视频操作也可以是同理.但是如果通过系统的录像,会导致得到的视频尺寸过大.  

	**Todo:**   
	**把相册功能单独提取出来,同时加入裁剪图片的功能**  
	**实践关于多进程通信的问题**  

4. 数据库模块

	对于Android来说,数据库使用的频率比较低,但是还是会使用到.

	注意点:

	* 使用SQLiteOpenHelper来创建,更新,连接,关闭数据库.  
	* 注意onCreate方法和onUpdate方法.如果数据库未创建,那么就只会调用onCreate,如果数据库已经创建,那么就只会调用onUpdate来更新数据库.应该记录数据库的版本,通过数据库的版本来判断数据库应该更新哪些内容.  
	* 数据库最让人烦恼的问题,莫过于数据库中的数据与实际代码的数据映射关系,实际的数据存在数据库,和数据库的数据转成实际数据,这两个过程会导致每次修改数据库的字段会导致很多地方都需要修改.  
	* 因为上一条的原因,所以出现了关系数据库框架.例如GreenDao...  

5. App升级模块

	App升级除了在应用商店升级外,通常我们还会在App中做内部升级,因为应用商店升级需要等审核通过.

	注意点:

	* 移动网络和Wifi网络下,提醒功能  
	* 提供静默升级与否  
	* 强制升级的功能  
	* 是否提供断点下载  
	* 升级提示内容Html化  
	* 升级过程Notification提醒  

	**整理代码,做出demo,查阅网上的其它做法**  

#### 常用的模块

1. 对于App主界面,常见的布局不外乎两种.

	* Android类型的,**Navigation Drawer**,侧边栏切换页面.国外应用使用的多  
	* IOS类型的,**Bottom Tab**,底部切换页面.国内应用基本这个套路.  
	* 当然更有甚者,把两种结合起来...例如最新的google+....  

	在我的项目当中.使用Bottom Tab的模式.通过把4个Fragment附着在MainActivity上,通过点击底部的Tab来切换Fragment.需要注意的是,在切换Fragment的时候,应当尽可能保留原来的Fragment状态,所以在每次点击切换的时候,通过隐藏和显示其他的Fragment来处理切换的效果,当然需要知道Fragment是否已经被OS回收.  
	另外如果希望Fragment可以通过滑动来切换的话,可以使用ViewPage+Fragment的模式.  

	**通过demo实现可滑动可点击切花的MainActivity,类似微信**

2. 下拉刷新,上拉加载的控件.

	这个功能基本上每个app都要使用到.而且第三方控件也很多  

	在项目中,我使用了SwipeRefreshLayout,这个Google原生的控件.这个控件简洁明了,已经满足一般开发的需求了.但是SwipeRefreshLayout只有下拉的操作.通常还会有加载更多新数据的操作,而这通常都是通过ListView或者GridView显示更多数据,所以我自己通过继承SwipeRefreshLayout,实现加载更多数据的功能.其中还加入了数据请求异常的情况.

	著名的第三方控件:  

	* [Android-PullToRefresh](https://github.com/chrisbanes/Android-PullToRefresh)
	* [Taurus](https://github.com/Yalantis/Taurus)一款带动画效果的下拉刷新
	* [BaiduGoingRefreshLayout](https://github.com/Hankkin/BaiduGoingRefreshLayout)仿照百度的下拉刷新动画

	在这里就不一一列举,如果有新的有意思的再补充.  
	我觉得尽量用Google原生的比较好,既简单,又符合Google的设计要求....

	**使用一个Demo,整理以上几种下拉刷新上拉加载的功能**

3. 广告轮播栏.
	
	广告轮播也在很多App上有,可以用来宣传自己的app,也可以用来作为广告位盈利.  

	广告组成部分: ViewPage + ImageView + Indicator

	注意点:  

	1. 轮播的图片从服务端获取,同时应该保存一份在本地,以防止在没有网络无法加载的时候,没图片显示.  
	2. 轮播可以实现无限循环  

	**实现一个无限轮播的Demo**  


4. 联系人列表.

	如果App是有联系人页面的,而且联系人的数量稍微有点多.这个时候如果能够快速索引,或者搜索.那就非常方便了.

	注意点:

	1. 实现索引功能  
	2. 实现搜索页面  
	3. 还可通过pingyin4j的jar包来实现汉字对拼音的索引


5. 一个App应该是拥有相同风格页面的.

	统一的风格包括:
	
	* 颜色:字体颜色,背景颜色....
	* 字体
	* 大小:
	* 按钮: Button, CheckBox, 
	* Dialog
	* actionBar
	* 分割线
	* Toast
	* 图标

	页面ActionBar一般分为:

	* 没有ActionBar的页面,如引导页  
	* 拥有标题描述的actionbar  
	* 拥有标题,功能按键的actionbar  
	* 拥有标题,功能键,还有下拉菜单的actionbarbar  
	**功能键可以是图片也可以是文字**

	可以通过style.xml去控制整个App的样式

	**整理项目中的style**  
	**研究一下系统定义的style,做一个统一风格的demo**  


项目难点亮点:

* 课表功能 ScheduleView  
* 地图切换 SwitchMap  


#### 接入的库

* Google地图, 高德地图  
* 支付宝支付, 微信支付  
* 门锁uclbrt   
* 友盟统计, 友盟分享  
* 信鸽推送   


#### 常用的第三方库

网络:

* Volley (使用)  
* OkHttp  
* Retrofit  
* android-async-http(使用)  

图片加载:

* UIL (使用)  
* Picasso  
* Fresco  

注解:

* Dragger2  
* ButterKnife  
* AndroidAnnotations  

数据库:

* GreenDao  

其它:

* 红点提示 BadgeView  
* 二维码扫描 ZXing  
* RxJava  
* 流式布局 com.wefika:flowlayout  
