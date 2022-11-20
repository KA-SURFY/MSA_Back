cd..
cd survey
cmd /c gradlew clean
cmd /c gradlew bootJar
docker rmi rnjsxorl3075/survey
docker build -t rnjsxorl3075/survey .
docker push rnjsxorl3075/survey