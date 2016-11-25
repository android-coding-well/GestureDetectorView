# GestureDetectorView
支持8方位的滑动手势检测和缩放手势检测

![image](https://github.com/huweijian5/GestureDetectorView/blob/master/screenshots/device-2016-11-26-001439.png)

###示例如下：
```
binding.gdvGesture.setOnGestureListener(new GestureDetectorView.OnGestureListener() {
            /**
             * @param gesture  手势
             * @param velocity 缩放手势表示范围，滑动手势表示速率
             */
            @Override
            public void onGesture( int gesture, float velocity) {
                switch (gesture) {
                    case GestureDetectorView.GESTURE_SLIDE_UP:
                        Toast.makeText(MainActivity.this, "up", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_DOWN:
                        Toast.makeText(MainActivity.this, "down", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_LEFT:
                        Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_RIGHT:
                        Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_LEFT_UP:
                        Toast.makeText(MainActivity.this, "left_up", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_LEFT_DOWN:
                        Toast.makeText(MainActivity.this, "left_down", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_RIGHT_UP:
                        Toast.makeText(MainActivity.this, "right_up", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SLIDE_RIGHT_DOWN:
                        Toast.makeText(MainActivity.this, "right_down", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SCALE_ZOOMIN:
                        Toast.makeText(MainActivity.this, "zoomin", Toast.LENGTH_SHORT).show();
                        break;
                    case GestureDetectorView.GESTURE_SCALE_ZOOMOUT:
                        Toast.makeText(MainActivity.this, "zoomout", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
  ```      
        
### 如果需要用到lib_common,做法如下：
* Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```	
* and then,add the dependecy:
```
dependencies {
	        compile 'com.github.huweijian5:GestureDetectorView:latest_version'
}
```
* 其中latest_version请到[releases](https://github.com/huweijian5/GestureDetectorView/releases)中查看
