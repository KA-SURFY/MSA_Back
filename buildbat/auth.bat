cd..
cd auth
cmd /c gradlew clean
cmd /c gradlew bootJar
docker rmi rnjsxorl3075/auth
docker build -t rnjsxorl3075/auth .
docker push rnjsxorl3075/auth