@echo off
setlocal enabledelayedexpansion

set "CLASSPATH=C:\Users\kang3\Desktop\web系统原理\新建文件夹\web project\microservices\activity-service\target\classes"

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\org\springframework\boot\spring-boot-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\org\springframework\data\spring-data-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\org\springframework\cloud\spring-cloud-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\org\hibernate\hibernate-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\mysql\mysql-connector-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\org\apache\shardingsphere\shardingsphere-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

for /f "delims=" %%i in ('dir /b /s "C:\Users\kang3\.m2\repository\org\projectlombok\lombok-*.jar"') do (
    set "CLASSPATH=!CLASSPATH!;%%i"
)

echo Running Activity Service...
java -cp "!CLASSPATH!" com.example.activityservice.ActivityServiceApplication

endlocal