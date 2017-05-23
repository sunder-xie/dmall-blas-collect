#!/usr/bin/env bash


cd target
rm -r dmall-blas-collect
tar -xzvf dmall-blas-collect-bin.tar.gz
cd dmall-blas-collect
java -Xms1048m -Xmx1048m -XX:MaxPermSize=256m -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -jar  dmall-blas-collect.jar
