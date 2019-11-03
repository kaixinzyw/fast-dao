[toc]
----
#### Java ORM框架 大幅度提高开发效率 减少编码量
### GitHub: https://github.com/kaixinzyw/fast-dao

#### 作者: 张亚伟
#### 邮箱: 398850094@qq.com
#### QQ交流群: 554127796

---
 功能简介
----
1. 极·简化DAO操作，大幅度提高编码效率;

2. 支持自定义SQL,自动映射;

3. 支持Redis缓存和内存缓存,自动更新缓存;

5. 支持MyBatis
----
示例
```java
Boolean success = UserFastDao.create().dao().insert(user); //增,新增成功后主键会在对象中设置
Integer delCount = UserFastDao.create().id(1).dao().delete(); //删,可以选择逻辑删除和物理删除
Integer updateCount = UserFastDao.create().id(1).dao().update(user); //改,操作简单,条件丰富
PageInfo<User> page = UserFastDao.create().dao().findPage(1, 10); //查,分页查询
```
----

## 1. 快速开始

### 1.1 安装
```xml
<dependency>
    <groupId>com.fast-dao</groupId>
    <artifactId>fast-dao</artifactId>
    <version>4.0.0</version>
</dependency>
```

### 1.2 文件生成
```java
public static void main(String[] args) {
    FileCreateConfig config= new FileCreateConfig();
    config.setDBInfo("数据库连接","用户名","密码","驱动"); //数据库连 例:("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC","root","123456","com.mysql.jdbc.Driver");
    config.setBasePackage("包路径"); //文件生成的包路径 例:("com.dao.test")
    TableFileCreateUtils.create(config); //生成代码
}
```
----
## 2. 使用说明

### 2.1 条件设置

```java
UserTemplate template = UserTemplate.create();
```


|功能|方法|示例|
|---|---|---|
|相等条件设置| template.fieldName(参数...)<br>template.fieldName().notValEqual(参数)|`template.userName(张三,李四)`<br>`template.userName().notValEqual("张三")`|
|模糊匹配条件设置 |template.fieldName().like(参数)<br>template.fieldName().likeLeft(参数)<br>template.fieldName().likeRight(参数)<br><br>template.fieldName().notLike(参数)<br>template.fieldName().notLikeLeft(参数)<br>template.fieldName().notLikeRight(参数)|`template.userName().like("张")`<br>`template.userName().likeLeft("张")`<br>`template.userName().likeRight("三")`<br><br>`template.userName().notLike("张")`<br>`template.userName().notLikeLeft("张")`<br>`template.userName().notLikeRight("三")`|
|IN条件设置|template.fieldName().in("参数1"...) <br>template.fieldName().notIn("参数1"...)|`template.userName().in("张三","李四")`<br>`template.userName().notIn("张三","李四")` |
|范围条件设置|template.fieldName().between(min, max)<br>template.fieldName().notBetween(min, max)|`template.age().between(20, 30)`<br>`template.age().notBetween(20, 30)`|
|大于条件设置|template.fieldName().greater(参数)|`template.age().greater(30)`|
|大于等于条件设置|template.fieldName().greaterOrEqual(参数)|`template.age().greaterOrEqual(30)`|
|小于条件设置|template.fieldName().less(参数)|`template.age().less(10)`|
|小于等于条件设置|template.fieldName().lessOrEqual(参数)|`template.age().lessOrEqual(10)`|
|IsNull条件设置|template.fieldName().isNull()|`template.userName().isNull()`|
|NotNull条件设置|template.fieldName().notNull()|`template.userName().notNull()`|
|排序设置-升序|template.fieldName().orderByAsc()|`template.age().orderByAsc()`|
|排序设置-降序|template.fieldName().orderByDesc()|`template.age().orderByDesc()`|
|对象条件设置|template.equalPojo(对象)|`User user = new User;`<br>`user.setName("张三");`<br>`template.equalPojo(user )`<br>|
|查询指定字段设置|template.fieldName().showField()|执行查询操作时只查询指定字段,可设置多个<br>`template.id().showField();`<br>`template.userName().showField();`|
|过滤字段设置|template.fieldName().hideField()|查询操作时不查询指定字段,可设置多个<br>`template.password().hideField();`<br>`template.mail().hideField();`|
|字段去重复设置|template.fieldName().distinctField()|`template.userName().distinctField()`|
|自定义SQL条件设置|template.andSql(SQL语句,参数)<br>template.orSql(SQL语句,参数)|会在WHERE后拼接自定义SQL语句<br>如果有参数需 要使用 #{参数名}占位<br>在参数值MAP集合put(参数名,参数值)<br>`Map<String, Object> params = new HashMap<>();`<br>`params.put("userName", "张三");`<br>`template.andSql("userName = #{userName}",params)`|
|关闭逻辑删除保护|template.closeLogicDeleteProtect()|会对本次执行进行逻辑删除保护关闭<br>关闭后所有操作会影响到被逻辑删除标记的数据|
|OR条件设置|template.fieldName().or()|指定字段OR条件设置 <br>例: 条件为姓名等于张三或为null <br>`template.userName().valEqual("张三").or().isNull()`



### 2.2 Dao执行器
Dao执行器调用:
```java
FastDao<User> dao = UserTemplate.create().dao();
```
执行器方法:

|说明|方法名 |示例|
|---|---|---|
|新增|Boolean insert(Pojo pojo)|新增一个用户,新增成功后会进行对象主键字段赋值<br>`Boolean success = UserTemplate.create().dao().insert(user)`|
|查询单条数据|Pojo findOne()|查询用户名为张三的信息<br>`User user = UserTemplate.create().userName("张三").dao().findOne()`|
|查询多条数据|List<Pojo> findAll()|查询年龄在20-30间的所有用户<br>`List<User> list = UserTemplate.create().age().between(20,30).dao().findAll()`|
|查询数量|Integer findCount()|查询一共有多少用户<br>`Integer count = UserTemplate.create().dao().findCount()`|
|分页查询|PageInfo<Pojo> findPage(int pageNum, int pageSize)|分页查询用户,并对年龄进行排序<br>`PageInfo<User> page = UserTemplate.create().age().orderByDesc().findPage(1, 10)`|
|更新数据,对象中参数为空的属性不进行更新|Integer update(Pojo pojo)|更新姓名为张三和李四的用户<br>`Integer count = UserTemplate.create().userName().in("张三","李四").dao().update(user)`|
|更新数据,对象中参数为空的属性也进行更新|Integer updateOverwrite(Pojo pojo)|更新年龄小于30,并且姓张的用户<br>`UserTemplate template = UserTemplate.create();`<br>`template.age().less(30);`<br>`template.userName().like("张");`<br>`Integer count = template.updateOverwrite(user)`|
|逻辑删除<br>本操作会自动将数据进行逻辑删除标记<br>SpringBoot环境需要在properties中配置<br>fast.db.set.delete=列名<br>其他环境使用Bean配置<br>FastDaoConfig.openLogicDelete("deleted",true);<br>重要!!!如果不进行设置将使用物理删除方式|Integer delete()|删除年龄大于80或为null的用户<br>`Integer count = UserTemplate.create().age().greater(80).or().isNull().delete()`
|物理删除|Integer deleteDisk()|删除id等于12的用户<br>`Integer count = UserTemplate.create().id(12).dao().deleteDisk()`|


#### 2.3 自定义SQL
 
多表等复杂SQL操作,可以使用自定义SQL执行器实现,框架会自动进行对象和表进行映射

FastCustomSqlDao.create(Class, SQL语句, 参数)

```java
//例:
String sql = "SELECT * FROM user WHERE `user_name` LIKE #{userName}";

HashMap<String, Object> params = new HashMap<>();
params.put("userName","%张亚伟%");

List<User> all = FastCustomSqlDao.create(User.class, sql, params).findAll();
```

#### 2.4 缓存使用

开启缓存功能后,可以操作类上添加注解的方式使用三种不同的缓存使用

```java
/**
 * Redis缓存
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新Redis缓存中的数据
 * 此实现使用了StringRedisTemplate 依赖spring-boot-starter-data-redis
 * 默认参数为框架设置的缓存时间和类型
 * 缓存可选参数
 * FastRedisCache(Long 秒) 如@FastRedisCache(60L) 缓存60秒
 * FastRedisCache(cacheTime = 时间,cacheTimeType = TimeUnit) 如@FastRedisCache(cacheTime =1L,cacheTimeType = TimeUnit.HOURS) 缓存1小时
 */
@FastRedisCache

/**
 1. 内存缓存
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
FastDaoConfig.dataSource(getDataSource());//更换全局数据源
FastDaoConfig.dataSourceThreadLocal(getDataSource());//更换本线程数据源

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
#### 3.1 框架可选配置说明
|功能说明| KEY |限定值|默认值 |示例|Bean配置方式|
|---|---|---|---|---|---|
|框架模式,框架对Jdbc和MyBatis进行封装<br>默认使用JDBC|fast.db.impl|spring-jdbc<br>mybatis|spring-jdbc|fast.db.impl=spring-jdbc=mybatis|FastDaoConfig.daoActuator(FastMyBatisImpl.class)|
|将表列名下划线方式命名的表列名转换为Java驼峰式字段<br>例如:user_name=userName |fast.db.camel|true<br>false|true|fast.db.camel=false|FastDaoConfig.openToCamelCase()|
|打印SQL执行日志|fast.db.sql.log|true<br>false|false|fast.db.sql.log=true|FastDaoConfig.openSqlPrint(true, false)|
|打印SQL执行结果日志|fast.db.sql.log.result|true<br>false|false|fast.db.sql.log.result=true|FastDaoConfig.openSqlPrint(true, true)|
|开启缓存功能并设置时间,单位为秒|fast.db.cache.time|Long类型|无|fast.db.cache.time=60|FastDaoConfig.openCache(10L, TimeUnit.SECONDS)|
|自动设置数据创建时间,只支持datetime类型,<br>默认不进行操作|fast.db.set.create|无|无|fast.db.set.create=my_create_time|FastDaoConfig.openAutoSetCreateTime("create_time")|
|自动设置数据更新时间,只支持datetime类型|fast.db.set.update|无|无|fast.db.set.update=my_update_time|FastDaoConfig.openAutoSetUpdateTime("update_time")|
|开启逻辑删除功能列,只支持bit类型<br>默认无法使用逻辑删除功能|fast.db.set.delete|无|无|fast.db.set.delete=my_deleted|FastDaoConfig.openLogicDelete("deleted", Boolean.TRUE)|
|逻辑删除标记|fast.db.set.delete.val|true<br>false|true|fast.db.set.delete.val=false|FastDaoConfig.openLogicDelete("deleted", Boolean.TRUE)|

#### 3.2 文件生成可选配置说明

```java
public static void main(String[] args) {

    FileCreateConfig config = new FileCreateConfig();
    
    //数据库连接
    config.setDBInfo("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8","root","123456","com.mysql.jdbc.Driver");
    
    //文件生成的包路径
    config.setBasePackage("com.db.test");
    
    //是否过滤表前缀
    config.setPrefix(false,false,null);
    
    //是否使用lombok插件
    config.setUseLombok(true);
    
    //是否下划线转大小写,默认true
    config.setUnderline2CamelStr(true);
    
    //项目多模块路径
    config.setChildModuleName("service");
    
    //需要生成的表名 (可选值,具体表名或all)
    config.setCreateTables("user");
    
    //生成代码
    TableFileCreateUtils.create(config);
}
```

**感谢使用,希望您能提出宝贵的建议,我会不断改进更新**