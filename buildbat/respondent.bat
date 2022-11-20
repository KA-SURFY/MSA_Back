cd..
cd respondent
cmd /c gradlew clean
cmd /c gradlew bootJar
docker rmi rnjsxorl3075/respondent
docker build -t rnjsxorl3075/respondent .
docker push rnjsxorl3075/respondent