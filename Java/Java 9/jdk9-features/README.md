
# Java 9

## JDK Enhancement Proposals

- [JEP 110: HTTP/2 Client (Incubator)](https://openjdk.org/jeps/110)
- [JEP 213: Milling Project Coin](https://openjdk.org/jeps/213)
- [JEP 222: jshell: The Java Shell (Read-Eval-Print Loop)](https://openjdk.org/jeps/222)
- [JEP 261: Module System](https://openjdk.org/jeps/261)
- [JEP 269: Convenience Factory Methods for Collections](https://openjdk.org/jeps/269)


### Java as REPL (JEP 222)

Java can be prototyped by using the interactive tool *jshell*. These are some examples:

Terminal:
```sh
jshell
jshell> System.out.println("Hello from JShell on terminal!");
Hello from JShell on terminal!

jshell> /exit
```

As part of a script file:
```sh
#!/bin/bash
jshell <<EOF
System.out.println("Hello from Embedded JShell!");
int a = 5;
int b = 10;
System.out.println("a + b = " + (a + b));
EOF
```

## Resources

- [Java® Platform, Standard Edition & Java Development Kit Version 9 API Specification](https://docs.oracle.com/javase/9/docs/api/overview-summary.html)
- [Java Platform, Standard Edition Java Shell User’s Guide](https://docs.oracle.com/javase/9/jshell/introduction-jshell.htm)
- [Java Versions and Features](https://www.marcobehler.com/guides/a-guide-to-java-versions-and-features)
