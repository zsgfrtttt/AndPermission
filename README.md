# AndPermission
动态权限申请

### 在根.gradle添加jitpack
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
添加依赖
dependencies {
	        implementation 'com.github.zsgfrtttt:AndPermission:1.1.0'
	}
```
# 基本使用
####
```
PermissionHelper.simpleRequest(this, new ResultCallback() {
            @Override
            public void onFailure(String[] ungrantPermissions) {
                Toast.makeText(MActivity.this, Arrays.toString(ungrantPermissions), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(MActivity.this,"申请权限成功",Toast.LENGTH_LONG).show();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE,
           Manifest.permission.CALL_PHONE,
           Manifest.permission.CAMERA,
           Manifest.permission.READ_CONTACTS,
           Manifest.permission.ACCESS_COARSE_LOCATION,
           Manifest.permission.ACCESS_FINE_LOCATION);
```

# 联系方式 
如果你在使用AndPermission过程中发现任何问题，你可以通过如下方式联系我：
* 邮箱: 1058079995@qq.com
