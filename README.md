# puregym-book-class
This application is a scheduler that uses a docker container with firefox and selenium to book classes on the puregym website

How to run
Change application.yml file and set the name of the classes you would like to book for each day. 
If you dont want to book a class for that day set it to "" e.g   sunday: ""
Build the project runing "./gradlew clean build"
Run docker build with the appropriate tag e.g myproject/gymbooking "docker build . -t myproject/gymbooking"
Run the docker image passing your user, password and frequency in cron format that the class booking logic will run
"docker run -it -e USER="myUser" -e PASS="myPass" -e FREQ="* * * * * ?" myproject/gymbooking"
By default it will run at midninght 0 * 0 * * ?
