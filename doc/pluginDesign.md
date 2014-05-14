Plugin Implementation Architecture
====================


## description file##

初定走动态配置的通道拉下插件配置，原先的url的方式被废掉。

### json###



	{
        Plugins: [
			{"name":"shoot",
			"url":"http://106.187.52.67:8080/plugin/",
			"md5":"b7cc3f312c4dc50d3d266f1c966fb123",
			"version":"1",
	        "size":xxxx,
			"downloadType":1},
			{"name":"shoot1",
			"url":"http://106.187.52.67:8080/plugin/",
			"md5":"b7cc3f312c4dc50d3d266f1c966fb123",
			"version":"1",
	        "size":xxxx,
			"downloadType":1},
		]
	}


### xml###



	"<Plugins>"
			"<Plugin> <url>test</url> <name>shoot</name> <md5>1234</md5> <version>1</version> <size>1234</size> <downloadType>1</downloadType></Plugin>"
			"<Plugin> <url>test1</url> <name>shoot1</name> <md5>12341</md5> <version>11</version> <size>12341</size> <downloadType>12</downloadType></Plugin>"
 			"<Plugin> <url>test2</url> <name>shoot2</name> <md5>12342</md5> <version>12</version> <size>12342</size> <downloadType>11</downloadType></Plugin>"
	"</Plugins>";



主要字段描述：

* `name`: 插件名字
* `url`:拉去插件jar的url
* `md5`: 拉去插件的md5
* `version`: 插件版本号
* `size`: 插件大小
* `downloadType`: 插件推荐的拉去网络环境


## Client##
	
![load plugin sequence](https://raw.githubusercontent.com/simpleton/android_misc/master/doc/sequence_chat.png)

![Start Plugin Activity](https://raw.githubusercontent.com/simpleton/android_misc/master/doc/hwouoQ.png)
