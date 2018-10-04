
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
  zubnix/jni-cross-compilers:linux-x86_64 \
  g++ -shared -O3 -w \
    -I/usr/include \
    -I/usr/lib/jvm/java-8-openjdk-amd64/include \
    -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux \
    ./native/square_Square.cpp \
    -o libsquare.so

docker run --rm \
  -v "$PWD":/usr/src \
  -w /usr/src \
  zubnix/jni-cross-compilers:linux-x86_64 \
  java -cp square.jar -Djava.library.path=/usr/src square.Demo
```