# Java Z-Wave Library
This library interfaces with PC Z-Wave controllers using as pure a Java interface/design as possible.
The project is based on [open-zwave](https://code.google.com/p/open-zwave/) and uses the same config files.

As of June-2013, this project is not yet feature complete with open-zwave.

## Hardware
jzwave has been tested successfully with the following controllers:

* [Aeon Labs Z-Stick 2](http://www.amazon.com/Aeon-Labs-Z-Wave-Z-Stick-Series/dp/B003MWQ30E)

jzwave has been able to communicate with the following devices:

* [Aeon Labs (0086) Multi Sensor (00020005)](http://www.amazon.com/Aeon-Labs-DSB05106-ZWUS-Z-Wave-Multi-sensor/dp/B008D5TYGU/)
* [GE (0063) 45603 Plugin Appliance Module (52503030)](http://www.amazon.com/GE-45603-Technology-Fluorescent-Appliance/dp/B0013V58HU/)
* [GE (0063) 45604 Outdoor Module (52503130)](http://www.amazon.com/GE-45604-Technology-Outdoor-Lighting/dp/B0013V8K3O/)
* [GE (0063) 45609 On/Off Relay Switch (52573533)](http://www.amazon.com/GE-45612-Wireless-Lighting-Control/dp/B006LQFHN2/)

## Building
jzwave requires Gradle to build

1. Optional: ```gradle eclipse``` to generate Eclipse classpath files with Gradle dependencies
2. ```gradle build```

## Transports
### socat
Useful if the controller is not connected to the development machine. Utilizing 'socat', create a TCP bridge from remote serial device to socket.

Run on host machine (using port 54321):

```
socat tcp-l:54321,fork,reuseaddr /dev/ttyUSB0,raw,nonblock,b115200
```

On development machine:

```
new ZWaveManager("socket://host:port");
```

### serial
Assumes that the controller is connected directly to the host machine. 

```
// where PORT = COM10 or PORT = /dev/ttyUSB0
new ZWaveManager("serial://PORT");
```

## Usage
 
