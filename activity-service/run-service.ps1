# 设置类路径
$classPath = "C:\Users\kang3\Desktop\web系统原理\新建文件夹\web project\microservices\activity-service\target\classes"

# 添加Spring Boot核心依赖
$springBootJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\org\springframework\boot" -Recurse -Filter "*.jar"
foreach ($jar in $springBootJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加Spring Cloud依赖
$springCloudJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\org\springframework\cloud" -Recurse -Filter "*.jar"
foreach ($jar in $springCloudJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加Spring Data依赖
$springDataJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\org\springframework\data" -Recurse -Filter "*.jar"
foreach ($jar in $springDataJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加Hibernate依赖
$hibernateJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\org\hibernate" -Recurse -Filter "*.jar"
foreach ($jar in $hibernateJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加MySQL驱动
$mysqlJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\mysql" -Recurse -Filter "*.jar"
foreach ($jar in $mysqlJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加Redis依赖
$redisJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\io\lettuce" -Recurse -Filter "*.jar"
foreach ($jar in $redisJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加ShardingSphere依赖
$shardingSphereJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\org\apache\shardingsphere" -Recurse -Filter "*.jar"
foreach ($jar in $shardingSphereJars) {
    $classPath += ";$($jar.FullName)"
}

# 添加Lombok依赖
$lombokJars = Get-ChildItem -Path "C:\Users\kang3\.m2\repository\org\projectlombok" -Recurse -Filter "*.jar"
foreach ($jar in $lombokJars) {
    $classPath += ";$($jar.FullName)"
}

# 运行服务
Write-Host "Running Activity Service..."
Write-Host "Classpath: $classPath"
java -cp "$classPath" com.example.activityservice.ActivityServiceApplication