

# fast-db-template
----
#### Java ORM框架,大幅度提高开发效率,减少编码量
### GitHub: https://github.com/kaixinzyw/fast-db-template
### 码云: https://gitee.com/fast-product/fast-db-template

----
## 功能简介
----
```java
PageInfo<Demo> page = DemoTemplate..create().dao().findPage(1, 10); //例-分页查询: 
Boolean success = DemoTemplate.create().dao().insert(demo); //例-新增数据: 
Integer updateCount = DemoTemplate.create().id(1).dao().update(demo); //例-更新id为1的数据:
Integer delCount = DemoTemplate.create().id(1).dao().delete(); //例-删除id为1的数据:
```
1. 极·简化数据库操作，大幅度提高编码效率,此框架核心价值所在,上手难度非常低;

2. 自动对<创建时间 | 更新时间 | 主键 | 逻辑删除>进行设置;

3. 支持3种数据缓存<本地缓存，Redis缓存，本地和Redis结合缓存>,支持缓存的自动刷新<更新,删除,新增>后会自动刷新缓存的数据;

4. 逻辑删除保护,对逻辑删除标记的数据在更新,查询等操作时候进行过滤;

5. SQL自检,对每一条SQL执行进行日志输出<可设置开启或关闭>,自动拼接SQL语句占位条件<复制即用>,并输出SQL执行效率,执行结果;

7. 快速数据源更换,可以对任意一次操作进行数据源更换,方便快捷;

8. 支持自定义SQL,自动进行类和表之间的映射;

9. 丰富的模板文件生成器设置,满足各种场景的需求

10. 可按需要对框架扩展<任意数据库,任意框架>的快速操作实现<本框架自身实现了对JDBC和 MyBatis的MySql数据库操作扩展实现,可任意选择使用哪一种>;

11. 完美兼容其他ORM框架,可以搭配任意ORM框架一同使用<如:MyBatis>;

----
## 1. 快速开始
----
### 1.1 安装
本项目使用JDK1.8,依赖SpringBoot,使用Maven
```xml
<dependency>
    <groupId>com.github.kaixinzyw</groupId>
    <artifactId>fast-db-template</artifactId>
    <version>3.0.3</version>
</dependency>
```

### 1.2 框架配置

只需正常设置数据库连接信息即可
```bash
#例:
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.druid.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 1.3 模板文件生成

```java
public static void main(String[] args) {
    FileCreateConfig config= new FileCreateConfig();
    config.setDBInfo("数据库连接","用户名","密码","驱动"); //数据库连 例:("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8","root","123456","com.mysql.jdbc.Driver");
    config.setBasePackage("包路径"); //文件生成的包路径 例:("com.db.test")
    TableFileCreateUtils.create(config); //生成代码
}
```
----
## 2. 使用说明
----
### 2.1 条件设置

```java
UserTemplate template = UserTemplate.create();
```
| 功能 |方法 |示例|
|--|--|--|
| 相等条件设置| template.字段名().valEqual(参数)|`template.userName().valEqual("张三")`<br>或调用时候指定<br>`user.userName("张三")`|
|大于条件设置|template.字段名().greater(参数)|`template.age().greater(30)`|
|大于等于条件设置|template.字段名().greaterOrEqual(参数)|`template.age().greaterOrEqual(30)`|
|小于条件设置|template.字段名().less(参数)|`template.age().less(10)`|
|小于等于条件设置|template.字段名().lessOrEqual(参数)|`template.age().lessOrEqual(10)`|
|模糊匹配条件设置 |template.字段名().like(参数)<br>template.字段名().likeLeft(参数)<br>template.字段名().likeRight(参数)|`template.userName().like("张")`<br>`template.userName().likeLeft("张")`<br>`template.userName().likeRight("三")`|
|范围条件设置|template.字段名().between(min, max)|`template.age().between(20, 30)`|
|IN条件设置|template.字段名().in("参数1","参数2"...) <br>template.字段名().in(List)|`template.userName().in("张三","李四")` |
|IsNull条件设置|template.字段名().isNull()|`template.userName().isNull()`|
|NotNull条件设置|template.字段名().notNull()|`template.userName().notNull()`|
|排序设置-升序|template.字段名().orderByAsc()|`template.age().orderByAsc()`|
|排序设置-降序|template.字段名().orderByDesc()|`template.age().orderByDesc()`|
|对象条件设置|template.equalPojo(对象)|`User user = new User;`<br>`user.setName("张三");`<br>`template.equalPojo(user )`<br>或创建模板时指定<br>`UserTemplate.create(user);`|
|指定字段查询设置|template.字段名().showField()|执行查询操作时只查询指定字段,可设置多个<br>`template.id().showField();`<br>`template.userName().showField();`|
|过滤字段查询设置|template.字段名().hideField()|查询操作时不查询指定字段,可设置多个<br>`template.password().hideField();`<br>`template.mail().hideField();`|
|字段去重复设置|template.字段名().distinctField()|`template.userName().distinctField()`|
|自定义SQL条件设置|template.andSql(SQL语句,参数)<br>template.orSql(SQL语句,参数)|会在WHERE后拼接自定义SQL语句<br>如果有参数需 要使用 #{参数名}占位<br>在参数值MAP集合put(参数名,参数值)<br>`Map<String, Object> params = new HashMap<>();`<br>`params.put("userName", "张三");`<br>`template.andSql("userName = #{userName}",params)`|
|关闭逻辑删除保护|template.closeLogicDeleteProtect()|会对本次执行进行逻辑删除保护关闭<br>关闭后所有操作会影响到被逻辑删除标记的数据|
|OR条件设置|template.字段名().or()|指定字段OR条件设置 <br>例: 条件为姓名等于张三或为null <br>`template.userName().valEqual("张三").or().isNull()`



### 2.2 Dao执行器
Dao执行器调用:
```java
//模板直接调用
FastDao<User> dao = UserTemplate.create().dao();
//字段条件操作时调用
FastDao<User> dao = UserTemplate.create().字段名().isNull().dao();
//例:如果单字段查询可以直接快速得到结果
List<User> list = UserTemplate.create().userName().isNull().dao().findAll();
```
执行器方法:

|说明|方法名 |示例|
|--|--|--|
|新增|Boolean insert(Pojo pojo)|新增一个用户<br>`Boolean success = UserTemplate.create().dao().insert(user)`|
|查询单条数据|Pojo findOne()|查询用户名为张三的信息<br>`User user = UserTemplate.create().userName("张三").dao().findOne()`|
|查询多条数据|List<Pojo> findAll()|查询年龄在20-30间的所有用户<br>`List<User> list = UserTemplate.create().age().between(20,30).dao().findAll()`|
|查询数据数量|Integer findCount()|查询一共有多少用户<br>`Integer count = UserTemplate.create().dao().findCount()`|
|分页查询|PageInfo<Pojo> findPage(int pageNum, int pageSize)|分页查询用户,并对年龄进行排序<br>`PageInfo<User> page = UserTemplate.create().age().orderByDesc().findPage(1, 10)`|
|更新数据,对象中参数为空的属性不进行更新|Integer update(Pojo pojo)|更新姓名为张三和李四的用户<br>`Integer count = UserTemplate.create().userName().in("张三","李四").dao().update(user)`|
|更新数据,对象中参数为空的属性也进行更新|Integer updateOverwrite(Pojo pojo)|更新年龄小于30,并且姓张的用户<br>`UserTemplate template = UserTemplate.create();`<br>`template.age().less(30);`<br>`template.userName().like("张");`<br>`Integer count = template.updateOverwrite(user)`|
|逻辑删除<br>本操作会自动将数据进行逻辑删除标记<br>使用逻辑删除功能需要在properties中配置<br>fast.db.set.delete=列名|Integer delete()|删除年龄大于80或为null的用户<br>`Integer count = UserTemplate.create().age().greater(80).or().isNull().delete()`
|物理删除|Integer deleteDisk()|删除id等于12的用户<br>`Integer count = UserTemplate.create().id(12).dao().deleteDisk()`|


 #### 2.3 自定义SQL
 
多表等复杂SQL操作,可以使用自定义SQL执行器实现,框架会自动进行对象和表进行映射

FastCustomSqlDao<操作类> dao = FastCustomSqlDao.create(操作类, SQL语句, 参数)

```java
//例:
String sql = "SELECT u2.`name` as u2Name, u1.`name` as u1Name " +
             "FROM user_test2 u2 LEFT JOIN user_test u1 " +
             "WHERE u2.`AND u1.id = u2.user_test_id " +
             "AND u2.name = #{u2Name}";

Map<String, Object> params = new HashMap<>();
params.put("u2Name", "张三");

List<UserTest2Dto> all = FastCustomSqlDao.create(UserTest2Dto.class, sql, params).findAll();
```

#### 2.4 缓存使用

开启缓存功能后,可以操作类上添加注解的方式使用三种不同的缓存使用

```java
/**
 * Redis缓存,当开启缓存并操作对象配置此注解时,会将查询到的数据缓存到redis中
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新Redis缓存中的数据
 * 此实现使用了StringRedisTemplate 依赖spring-boot-starter-data-redis
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * FastRedisCache(Long 秒) 如@FastRedisCache(60L) 缓存60秒
 * FastRedisCache(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 */
@FastRedisCache

/**
 * Redis和本地内存结合的缓存,在特殊场景使用,数据库中需要实时进行集群同步,数据量大并取用频繁,并且数据修改不频繁的场景,如商品的品牌或类目信息
 * Redis只会存储版本号,本地存储具体数据内容
 * 当开启缓存并操作对象配置此注解时,会将查询到的数据缓存到本地中,同时在Redis中获取数据版本号
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新Redis缓存中的数据数据版本号
 * 此方法使用了RedisConnectionFactory 依赖spring-boot-starter-data-redis
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * FastRedisLocalCache(Long 秒) 如@FastRedisLocalCache(60L) 缓存60秒
 * FastRedisLocalCache(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisLocalCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 */
@FastRedisLocalCache

/**
 1. 纯本地内存缓存,当集群项目部署,不会进行其他服务器的缓存刷新,使用场景需要注意,缓存的数据一般不会变,比如项目存储在数据库中的配置信息等
 2. 当开启缓存并操作对象配置此注解时,会将查询到的数据缓存到本地中
 3. 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新内存中缓存的数据
 4. 默认参数为框架设置的缓存时间和类型
 5. 缓存可选参数
 6. FastStatisCache(Long 秒) 如@FastStatisCache(60L) 缓存60秒
 7. FastStatisCache(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastStatisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 */
@FastStatisCache
```

#### 2.5 数据源切换

可以在任意一次执行时进行数据源更换,更换数据源只对当前线程影响

```java
//例
SpringJDBCMySqlImpl.dataSource(getDataSource());//如果使用mybatis实现需要使用FastMyBatisImpl.dataSource(getDataSource())
public DataSource getDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/demo2");
    dataSource.setUsername("root");
    dataSource.setPassword("123456");
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
}
```
----

### 3. 详细配置介绍
----
#### 3.1 配置参数详解
|功能说明| KEY |限定值|默认值 |示例|
|--|--|--|--|--|
|Dao实现配置,框架对Spring-Jdbc和MyBatis进行封装|fast.db.impl|spring-jdbc<br>mybatis|spring-jdbc|fast.db.impl=spring-jdbc=mybatis|
|将下划线方式命名的表列名转换为Java驼峰式字段<br>例如:列名=user_name<br>转换成Java字段名=userName |fast.db.camel|true<br>false|true|fast.db.camel=false|
|打印SQL执行日志|fast.db.sql.log|true<br>false|false|fast.db.sql.log=true|
|打印SQL执行结果日志|fast.db.sql.log.result|true<br>false|false|fast.db.sql.log.result=true
|开启缓存功能并设置时间,单位为秒|fast.db.cache.time|Long类型|无|fast.db.cache.time=60|
|自动设置数据创建时间列,只支持datetime类型,<br>默认不进行操作|fast.db.set.create|无|无|fast.db.set.create=my_create_time|
|自动设置数据更新时间列,只支持datetime类型|fast.db.set.update|无|无|fast.db.set.update=my_update_time|
|开启逻辑删除功能列,只支持bit类型<br>默认无法使用逻辑删除功能|fast.db.set.delete|无|无|fast.db.set.delete=my_deleted|
|逻辑删除标记|fast.db.set.delete.val|true<br>false|true|fast.db.set.delete.val=false|
#### 3.2 模板文件生成设置详解

```java
public static void main(String[] args) {

    FileCreateConfig config = new FileCreateConfig();
    
    //数据库连接
    config.setDBInfo("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8","root","123456","com.mysql.jdbc.Driver");
    
    //文件生成的包路径
    config.setBasePackage("com.db.test");
    
    //需要生成的文件 (可选值Base<生成Pojo类,Pojo查询模板,Pojo字段集>,Service<Service接口>,ServiceImpl<Service实现类>,Dto<Dto对象>,Dao<Dao对象>, All<上述所有文件>)
    config.setNeedModules(FileCreateConfig.CodeCreateModule.All);
    
    //是否过滤表前缀
    config.setPrefix(false,false,null);
    
    //是否使用lombok插件
    config.setUseLombok(true);
    
    //是否下划线转大小写,默认true
    config.setUnderline2CamelStr(true);
    
    //是否覆盖原文件,默认false
    config.setReplaceFile(false);
    
    //项目多模块空间
    config.setChildModuleName("service");
    
    //需要生成的表名 (可选值,具体表名或all)
    config.setCreateTables("user");
    
    //生成代码
    TableFileCreateUtils.create(config);
}
```

**感谢使用,希望您能提出宝贵的建议,我会不断改进更新**