# WaveView
自定义水波纹控件
### 简介

  这是一个自定义的水波纹，它是圆形扩展的，可以修改水波纹控件的文字，以及水波纹相关的属性。
  
### 集成步骤

####  在app的跟build.gradle下 （Add it in your root build.gradle at the end of repositories）:
      
     	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
      
#### 在app目录下的build.gradle的dependencies中添加如下引用（Add the dependency）：

    	dependencies {
	        compile 'com.github.ZABone:WaveView:v1.0.0'
	}

    
### 2.在布局文件中设置WaveView属性

    <com.meibaa.waveview.view.WaveView
        android:id="@+id/wave_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:text="@string/app_name"
        android:textColor="@color/colorAccent"
        android:textSize="20sp"
        app:wCreateDuration="1000"
        app:wMaxRadiusRate="0.95"
        app:wRunDuration="5000" />
 
 #### text：view中间的文字
 #### textColor：view中间的文字颜色
 #### textSize：view中间的文字的大小
 #### wCreateDuration：水波纹创建的时间间隔
 #### wMaxRadiusRate：最大水波纹相对父控件的大小（0~1）
 #### wRunDuration：水波纹扩散的时间
 
 ### 或者，在代码中设置
 
  #### 文字设置
  #### mWaveView.setTextSize(20);
  #### mWaveView.setText("我看见你在笑，小帅逼");
  
  #### 水波纹设置
  #### mWaveView.setColor(Color.BLACK);
  #### mWaveView.setSpeed(5000);
  #### mWaveView.setDuration(1000);
  #### mWaveView.setMaxRadiusRate(0.9);
 
 
