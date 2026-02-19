
# Java 18

## JDK Enhancement Proposals

- [JEP 400: UTF-8 by Default](https://openjdk.org/jeps/400)
- [JEP 408: Simple Web Server](https://openjdk.org/jeps/408)
- [JEP 413: Code Snippets in Java API Documentation](https://openjdk.org/jeps/413)
- [JEP 417: Vector API (Third Incubator)](https://openjdk.org/jeps/417)
- [JEP 420: Pattern Matching for switch (Second Preview)](https://openjdk.org/jeps/420)


### Set a different file encoding (JEP 400)

The default value for `file.encoding` can be changed by:

```sh
java -Dfile.encoding=COMPAT AnyJavaProgram
```


### Simple Web Server (JEP 408)

Serve static files only by starting a minimal web server on command-line tool:

```sh
jwebserver -p 8081 -d path/html
```


## Resources

- [Java® Platform, Standard Edition & Java Development Kit Version 18 API Specification](https://docs.oracle.com/en/java/javase/18/docs/api/index.html)
- [Core Libraries](https://docs.oracle.com/en/java/javase/18/core/)
- [Java Versions and Features](https://www.marcobehler.com/guides/a-guide-to-java-versions-and-features)
