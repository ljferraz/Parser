# Parser Project

Spring Boot JPA application which parses an acces log file specified in command line argument (accesslog), saves the whole file content on MySQL Database and also checks
for blocked ips depending of the number of requests passed in command line argument (threshold).

## Running Application Samples ##

The tool takes 'accesslog', 'startDate', 'duration' and 'threshold' as command line arguments. 'accesslog' is the log file absolute path, 'startDate' is of 'yyyy-MM-dd.HH:mm:ss' format,
'duration' can take only 'hourly', 'daily' as inputs and 'threshold' can be an integer.

java -jar parser-1.0.jar --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250

In the case that database connection parameters are not set under application.properties, you can add them on command line arguments. For example:
java -jar parser-1.0.jar --startDate=2017-01-01.00:00:11 --duration=hourly --threshold=100 --accesslog=/path/to/file --spring.datasource.url=jdbc:mysql://Server:Port/DatabaseName?useTimezone=true&serverTimezone=UTC --spring.datasource.username=user --spring.datasource.password=password