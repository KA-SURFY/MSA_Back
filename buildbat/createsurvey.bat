cd..
cd createsurvey
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/createsurvey .
docker push rnjsxorl3075/createsurvey