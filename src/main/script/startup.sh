
export JAVA_OPTS="-server -Xms1048m -Xmx1048m -XX:MaxPermSize=256m -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

java -Xms1048m -Xmx1048m -XX:MaxPermSize=256m -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -jar  dmall-blas-collect.jar > /dev/null 2>&1 &

