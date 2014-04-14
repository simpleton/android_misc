# Plugin Implementation Architecture#

## Host##
1. config url:

	http://xxx/{Client Version}/{Plugin_Name}_config.json

```
	{
		"id":"com.tencent.mm.plugin.shoot-20140413-1",
		"url":"http://106.187.52.67:8080/plugin/com.tencent.mm.plugin.shoot-20140413-1.jar",
		"md5":"b7cc3f312c4dc50d3d266f1c966fb123",
		"version":"1"
	}
```


2. plugin file url:

	http://106.187.52.67:8080/plugin/com.tencent.mm.plugin.shoot-20140413-1.


## Client##
	
![load plugin sequence](http://www.websequencediagrams.com/cgi-bin/cdraw?lz=ClVzZXItPlBsdWdpbkhlbHBlcjogAAgGIFJlcXVlc3QKABEMACMITWFuYWdlcjogcgAfBiBwAD0FACUHABcHAFAITG9hZGVyOiBsb2FkABsOABQGLT4rUmV0cmlldmVXb3JrACIJY29uZmlnCgAODi0tPi0ASQ4AJyUAgRoGIGlmIG5lZWRlZAA2IAoAgRMPAIFpDgCBWBAAgQsHAIIOCHZlcmlmeQCCQQcAgX4WAIJlBwCCTg9VcwCCHwlsaWIKCgo&s=qsd)
