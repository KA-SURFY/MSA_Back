cd..
cd post
cmd /c gradlew clean
cmd /c gradlew bootJar
docker rmi rnjsxorl3075/post
docker build -t rnjsxorl3075/post .
docker push rnjsxorl3075/post