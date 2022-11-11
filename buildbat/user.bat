cd..
cd user
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/user .
docker push rnjsxorl3075/user