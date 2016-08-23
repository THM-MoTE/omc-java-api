# OMC CORBA-interface for Java
A simple java implementation of [OpenModelica](https://openmodelica.org/)'s CORBA interface.
Java-clients should use this library to communicate with ```omc```.

# Common workflow
A common workflow example:
```java
OMCInterface omci = new OMCClient();
//1. connect to server; now omci is usable
omci.connect();

//2. start communication
Result res = omc.sendExpression("model abc Real x=1; end abc;");
System.out.println(res);

//3. disconnect from server;
//omci isn't usable until a call to connect()
omci.disconnect();
```

# Helpers, Converters
We provide converters which makes the creation of an Modelica-expression easier.
For example if you want to create a modelica-array from a list:
```java
List<Integer> xs = Arrays.toList(1,2,3,4);
String modelicaArray = ScriptingHelper.asArray(xs);
```
The helpers avoid common pitfalls like missing ```"```, ```,``` ```{```, etc.

# Logging
This library uses the [slf4j](http://www.slf4j.org/) api for logging.
Provide a proper logging-framework for controlling logging output, for example [logback](http://logback.qos.ch/).

# License
Because of OpenModelica's restrictions this library is distributed under
the terms of the **GNU General Public License Version 3.0**.
For more information see the LICENSE and the
[OpenModelica License](https://github.com/OpenModelica/OMCompiler/blob/master/COPYING).

# Notes
- This implementation is based on OpenModelica's
  [System Documentation V. 2013-01-28](https://openmodelica.org/svn/OpenModelica/tags/OPENMODELICA_1_9_0_BETA_4/doc/OpenModelicaSystem.pdf)
- omc's scripting-API is documented at
  [API](https://build.openmodelica.org/Documentation/OpenModelica.Scripting.html)
- If an ```omc```-instance gets started as subprocess
  ```stdin``` and ```stdout``` are redirected into
  ```$TMP/omc_home/omc.log```.
