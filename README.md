### 引入依赖
```
<dependency>
  <groupId>com.alidaodao.app</groupId>
  <artifactId>alidaodao-app-redis</artifactId>
  <version>{maven仓库中最新版本即可}</version>
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
    @NacosValue(value = "${spring.redis.default.index}")
    private int defaultIndex;

    @Bean("redisClient")
    public RedisClient redisClient() {
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);
         //此处为需要创建的redis db实例
        Set<Integer> redisIndexes = new HashSet<>();
        RedisClient redisClient = new RedisClient(redisConfig, redisIndexes);
        redisClient.setDefaultIndex(defaultIndex);
        return redisClient;
    }
}
```

### 使用RedisClient

```
    @Autowired
    private RedisClient redisClient;
     //使用默认的db相关方法
    //String data = redisClient.getByDefault().get(redisKey);
    //get指定db相关方法
    //String data = redisClient.get(index).get(redisKey);
```
到此结束
