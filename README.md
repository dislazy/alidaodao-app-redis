## 简单介绍
alidaodao-app-redis是一款基于JedisPool的动态多redisDB获取实例，执行完命令自动回收资源的简单工具，旨在让redis的接入更简单便捷。

## 基本原理
利用JedisPool的池化技术，将对应的链接封装到Map中，可以自由根据DB取用，执行对应的redis命令，执行完毕会自动释放链接回到连接池。

## 引入alidaodao-app-redis使用redisClient（手工配置）

### 引入依赖
```
<dependency>
  <groupId>com.alidaodao.app</groupId>
  <artifactId>alidaodao-app-redis</artifactId>
  <version>{maven仓库中最新版本即可}</version>
</dependency>
#如果不生效可以引用以下的包
<dependency>
    <groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
	<version>2.11.1</version>
</dependency>
```

### 初始化对象

```
@Configuration
public class RedisConfiguration {

    @Value(value = "${spring.redis.host}")
    private String host;
    @Value(value = "${spring.redis.port}")
    private int port;
    @Value(value = "${spring.redis.password}")
    private String password;
    //需要实例化的db列表(必须包含默认db)
    @Value(value = "${spring.redis.indexes}")
    private String indexes;
    //默认db
    @Value(value = "${spring.redis.default.index}")
    private int defaultIndex;

    @Bean("redisClient")
    public RedisClient redisClient() {
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setRedisHost("127.0.0.1");
        redisConfig.setRedisPort(6379);
        redisConfig.setRedisPwd("test");
         //此处为需要创建的redis db实例
        Set<Integer> redisIndexes = new HashSet<>();
        RedisClient redisClient = new RedisClient(redisConfig, redisIndexes);
        redisClient.setDefaultIndex(defaultIndex);
        return redisClient;
    }
}
```
## 引入spring-boot-redis-starter方式（自动配置）

### 引入依赖
```
<dependency>
  <groupId>com.alidaodao.app</groupId>
  <artifactId>spring-boot-redis-starter</artifactId>
  <version>{maven仓库中最新版本即可}</version>
</dependency>
```
### 配置
```
#redis
#开启redisClient
spring.redis.enable=true
# redisHost
spring.redis.host=redis.xxxx.com
# RedisPort
spring.redis.port=6379
#redis password
spring.redis.password=xxxxxx
# 使用的默认DB
spring.redis.default-index=1
# 使用的DB池，必须包含默认DB在内
spring.redis.indexes=1,2,3,4,5
```

## 使用RedisClient

```
    @Autowired
    private RedisClient redisClient;
     //使用默认的db相关方法
    //String data = redisClient.getByDefault().get(redisKey);
    //get指定db相关方法
    //String data = redisClient.get(index).get(redisKey);
```

## 致谢
- 感谢 JetBrains 提供的免费开源 License：
<img src="https://images.gitee.com/uploads/images/2020/0406/220236_f5275c90_5531506.png" alt="图片引用自lets-mica" style="float:left;">

到此结束
