cd..
cd loadsurvey
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/loadsurvey .
docker push rnjsxorl3075/loadsurvey