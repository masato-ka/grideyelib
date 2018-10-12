# Grid-Eye Android Things Library

## Overview

You can use Grid-Eye that Infrared Array sensor with Android Things using this library.
The Grid-Eye sensor module have 8x8 Infrared sensor. And you can get Infrared image (64 pixels) throw I2C bus.
This driver completely integrated Grid-Eye sensor as User-space-driver. 

Grid-Eye [Detail in ....](https://github.com/jodalyst/AMG8833/blob/master/Grid-EYE-Datasheet.pdf)

I checked this software with only AGM8833 what a type of GridType.

## install

Add repository your build system configuration.
```
https://dl.bintray.com/masato-ka/android-things-support/
```

Please write to your project pom file.
```gradle
<dependency>
  <groupId>ka.masato.library.device</groupId>
  <artifactId>grideyelib</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

If you use gradle.

```
implementation compile 'ka.masato.library.device:grideyelib:1.0.0'


```


## Usage

Registering driver for your application.

```java

PasoriReadCallback pasoriReadCallback = new PasoriReadCallback() {
    @Override
    public void pollingRecieve(String idmString, String pmmString) {
        Log.i("InstrumentedTest", "IDM: " + idmString + " PMm: " + pmmString);
        status = false;
    }
};

HandlerThread handlerThread = new HandlerThread("polling");
handlerThread.start();
Handler handler = new Handler(handlerThread.getLooper());
Log.i("InstrumentedTest", "Please touch your IC card on Pasori.");
pasoriDriverTypeF.startPolling(handler, pasoriReadCallback);

while (status) {
    try {
        Log.i("InstrumentedTest", "Loop");
        Thread.sleep(1000L);
    } catch (InterruptedException e) {
       e.printStackTrace();
    }
}


pasoriDriverTypeF.stopPolling();

```

## Version
      
* 2018/10/xx  Version x.x.x
     

## Author

Name : masato-ka
E-mai: jp6uzv at gmail.com
Twitter: @masato_ka

## LICENCE

MIT


&copy; 2018 masato-ka All Rights Reserved.
