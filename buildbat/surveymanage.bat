cd..
cd surveymanage
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/surveymanage .
docker push rnjsxorl3075/surveymanage