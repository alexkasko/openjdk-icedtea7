OpenJDK 7 source code with IcedTea patches
------------------------------------------

This repository contains [OpenJDK7](http://openjdk.java.net/projects/jdk7u/) source code with
applied patches from [IcedTea](http://icedtea.classpath.org/wiki/Main_Page) project.

To obtain these sources IcedTea has been configured with the following options:

    ./configure \
    --with-jdk-home=/home/alex/java/openjdk7_u6 \
    --with-rhino=/home/alex/projects/openjdk/drops/rhino-1.7.4.jar \
    --disable-bootstrap \
    --disable-system-zlib \
    --disable-system-jpeg \
    --disable-system-png \
    --disable-system-gif \
    --disable-system-lcms \
    --disable-compile-against-syscalls \
    --disable-nss

License information
-------------------

[GNU GPL v. 2 with classpath exception](http://hg.openjdk.java.net/jdk7u/jdk7u/raw-file/da55264ff2fb/LICENSE).
