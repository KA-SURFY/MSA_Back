cd..
cd respondent
cmd /c gradlew clean
cmd /c gradlew bootJar
docker build -t rnjsxorl3075/respondent .
docker push rnjsxorl3075/respondent