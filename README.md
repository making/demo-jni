
```
rm -rf classes native *.so *.jar
mkdir -p classes
javac -sourcepath src -d classes src/square/*.java
jar -cvf square.jar -C classes .
javac src/square/Square.java -h native
```

```c++
cat <<'EOF' > native/square_Square.cpp
#include "square_Square.h"

JNIEXPORT jint JNICALL Java_square_Square_square
  (JNIEnv* env, jclass clazz, jint x) {
  return x * x;
}
EOF
```

```
docker run --rm \
  -v "$PWD":/usr/src \
  -w /usr/src \
  jokester/alpine-jni-build \
  g++ -shared -O3 -w \
    -I/usr/include \
    -I$JAVA_HOME/include \
    -I$JAVA_HOME/include/linux \
    ./native/square_Square.cpp \
    -o libsquare.so

docker run --rm \
  -v "$PWD":/usr/src \
  -w /usr/src \
  jokester/alpine-jni-build \
  java -cp square.jar -Djava.library.path=/usr/src square.Demo
```