# ATCWidget
AnimationRelativeLayout,ThumbTextView,CarryFragmentLayout
[Github 地址链接](https://github.com/zzz40500/ATCWidget)
###今天分享3个自定义组件使用:
 先放3个效果图
![ThumbTextView.gif](http://upload-images.jianshu.io/upload_images/166866-0eec8d54a811cb5a.gif)

![AnimationRelativeLayout.gif](http://upload-images.jianshu.io/upload_images/166866-1126f05d01da7e73.gif)

![carryFragmentLayout.gif](http://upload-images.jianshu.io/upload_images/166866-90b5a5ca9d2df926.gif)
>三个自定义组件
 1. ThumbTextView
 * AnimationRelativeLayout
 *  CarryFragmentLayout

###usage:###

#####ThumbTextView#####
布局文件:
~~~
<com.mingle.widget.ThumbTextView
                android:id="@+id/thumbTextView"

                app:content="5】尼玛，这招也太狠了！ 这次绝逼是友尽了！希望情人节全国停电，气死那些唱歌喝酒泡吧看电影的，再来一场鹅毛大雪，冻死那些牵手逛街的，最后警察集体搜查，憋死那些想开房的~反正情人节我在家，不过了！【13】多大仇多大怨？为什么没有红烧前男友？差评！（转）眼见杯子要被打翻在地，主人急忙高呼住爪！！[lxhx惊]于是喵星人真的住爪了……一下。。。。"

                app:mainTitle="主题"
                android:layout_width="fill_parent"
                android:layout_height="wrap_content">
                </com.mingle.widget.ThumbTextView>
~~~

自定义属性说明:
`mainTitle`:标题;
`content`:正文内容
`maxNumOfContent`:最大字数限制,超出这个字数,显示缩略文字,
`titleAppearance`: 标题 TextView 的 TextAppearance
`contentAppearance`:正文 TextView 的 TextAppearance
使用注意:

ThumbTextView可以被 ScrollView下的 LinearLayout
和 RelativeLayout 布局包裹,也可以直接被LinearLayout和 RelativeLayout 包裹着.


###### AnimationRelativeLayout#####
这个比较简单的一个,他是继承自 RelativeLayout 布局  所以你可以当它是有开场动画的 RelativeLayout使用,被它包裹的布局第一次会有动画的出现



###### CarryFragmentLayout#####

布局文件:
~~~
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background_material_light"
    tools:context="com.vserve.ottodemo.SecondActivity">


    <com.mingle.widget.CarryFragmentLayout
        android:id="@+id/carryFragment"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent">

        <ScrollView
            android:layout_width="fill_parent"
            android:layout_height="fill_parent">

            <RelativeLayout
                android:layout_width="fill_parent"
                android:layout_height="wrap_content"
                >


                <TextView
                    android:id="@+id/button"
                    android:paddingTop="8dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:textSize="22dp"
                    android:paddingLeft="13dp"
                    android:text="人物:" />

                <TextView
                    android:id="@+id/button1"
                    android:paddingTop="8dp"
                    android:paddingLeft="13dp"
                    android:textSize="22dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:layout_below="@+id/button"
                    android:text="时间" />

                <TextView
                    android:id="@+id/button2"
                    android:paddingTop="8dp"
                    android:textSize="22dp"
                    android:paddingLeft="13dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:layout_below="@+id/button1"
                    android:text="地点" />

                <TextView
                    android:id="@+id/button3"
                    android:textSize="22dp"
                    android:paddingTop="8dp"
                    android:paddingLeft="13dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:layout_below="@+id/button2"
                    android:text="天气" />

                <TextView
                    android:id="@+id/button4"
                    android:textSize="22dp"
                    android:paddingTop="8dp"
                    android:paddingLeft="13dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:layout_below="@+id/button3"
                    android:text="省份" />

                <TextView
                    android:id="@+id/button5"
                    android:textSize="22dp"
                    android:paddingTop="8dp"
                    android:paddingLeft="13dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:layout_below="@+id/button4"
                    android:text="房子" />

                <TextView
                    android:id="@+id/button6"
                    android:textSize="22dp"
                    android:paddingLeft="13dp"
                    android:paddingTop="8dp"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:layout_below="@+id/button5"
                    android:onClick="onClick"
                    android:text="媳妇" />
                <TextView
                    android:layout_below="@+id/button6"
                    android:layout_width="fill_parent"
                    android:layout_alignParentBottom="true"
                    android:layout_height="200dp"/>
            </RelativeLayout>
        </ScrollView>

    </com.mingle.widget.CarryFragmentLayout>
</LinearLayout>
~~~
1. 将布局包裹在CarryFragmentLayout中
* 使用   carryFragmentLayout.openFragment(getSupportFragmentManager(),fragment,view)方法显示 view 对应出现的 Fragment.
* 使用  carryFragmentLayout.closeFragment();恢复初始样子

至于 Fragment 与 Activity 的通信,你可以使用接口的方式实现,也可以使用 Otto, 和 EventBus 实现,个人推荐 Otto(原因不爱告诉你) ,你可以按你自己喜欢使用.follow your heart










