cd..
cd surveyresult
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/surveyresult .
docker push rnjsxorl3075/surveyresult