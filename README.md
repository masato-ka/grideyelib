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

You can choose two way. One is use Low level API. Other is Hight Level API as User space drivers.

### Low Lvel API

```java

private static final String I2C_PORT = "I2C1";
private static final int address = 0x68; // Default address of GridEye.
private static GridEyeDriver gridEyeDriver = GridEyeDriver.getInstance();

PeripheralManager mPeripheralManager = PeripheralManager.getInstance();

GridEyeDriver.setmPeripheralManager(mPeripheralManager);
GridEyeDriver.opne(I2C_PORT, address);

float[] temperatures = GridEyeDriver.getTemperature(); //temperatures size is 68 as 8 * 8.  

```

### Hight Level API.

* register sensor.

```java

private static final String I2C_PORT = "I2C1";
private static final int address = 0x68; // Default address of GridEye.
private static GridEyeDriver gridEyeDriver = GridEyeDriver.getInstance();
private UserSensor userSensor;
private UserDriverManager;

PeripheralManager mPeripheralManager = PeripheralManager.getInstance();
gridEyeManager = new GridEyeManager(mPeripheralManager, I2C_PORT, ADDRESS);

userSensor = gridEyeManager.getUserSensor();

mUserDriverManager = UserDriverManager.getInstance();
mUserDriverManager.registerSensor(userSensor);

```

* unregister sensor.

```java

mUserDriverManager.unregisterSensor(userSensor);

```



## Version
      
* 2018/10/21  Version 1.0.0
     

## Author

Name : masato-ka
E-mai: jp6uzv at gmail.com
Twitter: @masato_ka

## LICENCE

MIT


&copy; 2018 masato-ka All Rights Reserved.
