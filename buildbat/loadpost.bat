cd..
cd loadpost
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/loadpost .
docker push rnjsxorl3075/loadpost