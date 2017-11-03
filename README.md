# onvifjava
ONVIF java library

This project is deeply reworked viersion of https://github.com/milg0/onvif-java-lib.<br>


Whats done
=============
* Project is mavenized
* "Devices" entities switched to "Services" as ONVIF specifications decsribe them
* Service classes provide needed functionality, splitted according to ONVIF specifications
* Deprecated methods deleted
* Main OnvifDevice class provides access to Services (which provide main functionality) and small set of basic methods
* SOAP interaction is hidden, no direct expose, as previosly OnfifDevice provided
* Logging and Exception processing completely removed - suppose it's subject of more top-level classes where lib will be used

TODOS
=============
* WS-Discovery part!!!!
* Expansion of services functionality - up to complete ONVIF specifications functionality
* Think some refactoring still needs to be done

All subject-related notes and comments are welcome!

