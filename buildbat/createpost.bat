cd..
cd createpost
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/createpost .
docker push rnjsxorl3075/createpost