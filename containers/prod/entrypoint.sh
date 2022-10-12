#Build jar
./gradlew bootJar
cp ./build/libs/*.jar ./

#Run jar
java -jar *.jar