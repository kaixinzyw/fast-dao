
>Gitee: [https://gitee.com/fast-product/fast-dao](https://gitee.com/fast-product/fast-dao) 
>
>GitHub: [https://github.com/kaixinzyw/fast-dao](https://github.com/kaixinzyw/fast-dao/) 
>
>作者: 张亚伟
>
>邮箱: 398850094@qq.com
----
@[toc]

---
 Java 全自动 ORM框架 Dao框架 大幅度提高开发效率 减少编码量
 
----

---- 
- 极·简化DAO操作，面向对象的数据库操作方式, 大幅度提高编码效率
- 支持自定义SQL
- 支持Spring事务管理和手动事务
- 支持分布式缓存和本地缓存,支持数据在增删改后主动刷新缓存

----
示例
```java
User user = UserFastDao.create().dao().insert(user); //增,新增成功后主键会在对象中设置
Integer delCount = UserFastDao.create().id(1).dao().delete(); //删,可以选择逻辑删除和物理删除
Integer updateCount = UserFastDao.create().id(1).dao().update(user); //改,操作简单,条件丰富
PageInfo<User> page = UserFastDao.create().dao().findPage(1, 10); //查,分页查询
```
----

## 1. 框架安装

### 1.1 Maven地址
```xml
<dependency>
    <groupId>com.fast-dao</groupId>
    <artifactId>fast-dao</artifactId>
    <version>9.5</version>
</dependency>
```

#### 1.2 框架配置

```java
/**
 * 字段驼峰转换 例 user_name = userName 默认开启
 */
FastDaoConfig.openToCamelCase();
/**
 * 设置SQL日志打印,默认关闭
 * 参数1: 日志打印级别 DEBUG,INFO,OFF
 * 参数2: 是否打印详细SQL日志
 * 参数3: 是否打印SQL执行结果
 */
FastDaoConfig.openSqlPrint(SqlLogLevel.INFO,true, true);
/**
 * 开启自动对数据 新增操作 进行创建时间设置
 * 参数1: 需要设置创建时间的字段名
 */
FastDaoConfig.openAutoSetCreateTime("create_time");
/**
 * 开启自动对数据 更新操作/逻辑删除操作 进行更新时间设置
 * 参数1: 需要设置更新时间的字段名
 */
FastDaoConfig.openAutoSetUpdateTime("update_time");
/**
 * 开启逻辑删除功能,开启后会对逻辑删除标记的数据在 更新|删除|查询 时进行保护,可通过模板进行单次操作逻辑删除保护的关闭
 * 参数1:  逻辑删除字段名
 * 参数2:  逻辑删除标记默认值
 */
FastDaoConfig.openLogicDelete("deleted", Boolean.TRUE);
/**
 * 设置全局默认缓存时间,两种缓存模式(本地缓存，Redis缓存),支持缓存的自动刷新<更新,删除,新增>后会自动刷新缓存的数据
 * Reids缓存需要进行配置
 * 参数1:  默认缓存时间
 * 参数2:  默认缓存时间类型
 */
FastDaoConfig.openCache(10L, TimeUnit.SECONDS);
/**
 * 数据源配置,Spring环境可无需设置可自动识别
 */
FastDaoConfig.dataSource(dataSource);

/**
 * redis缓存配置,Spring环境可无需设置可自动识别
 */
FastDaoConfig.redisConnectionFactory(redisConnectionFactory);
```


### 1.3 文件生成
```java
FileCreateConfig config = new FileCreateConfig();
/**
 * 设置数据库连接信息
 * @param url 数据库连接
 * @param user 用户名
 * @param password 密码
 * @param driverClass 数据库驱动
 */
config.setDBInfo("jdbc:mysql://IP:端口/数据库?useUnicode=true&characterEncoding=utf-8&useInformationSchema=true","账号","密码","驱动(例:com.mysql.cj.jdbc.Driver)");
/**
 * 生成模板的包路径
 * @param basePackage 包路径地址 xxx.xxx.xxx
 */
config.setBasePackage("xxx.xxx.xxx");
/**
 * 需要生成的模板文件类型,使用FileCreateConfig.CodeCreateModule枚举,多个用逗号隔开
 * @param modules 模板文件类型
 */
config.setNeedModules(FileCreateConfig.CodeCreateModule.Base);
/**
 * 是否过滤表前缀信息
 * @param prefix 生成文件时候是否过滤表前缀信息，ord_orders = orders
 * @param prefixFileDir 是否通过前缀信息生成不同的文件目录,ord_orders 会为将orders生成的模板存储在ord目录下
 * @param prefixName 过滤指定前缀,如果不指定传 null
 */
config.setPrefix(false,false,null);
/**
 * 是否使用Lombok插件注解
 * @param useLombok 默认false
 */
config.setUseLombok(false);
/**
 * 是否在DTO上生成Swagger2注解
 * @param useDTOSwagger2 默认false
 */
config.setUseDTOSwagger2(false);
/**
 * 是否对字段和生成的对象进行下划线转换,如 product_sku = ProductSku
 * @param underline2CamelStr 默认true
 */
config.setUnderline2CamelStr(true);
/**
 * 是否覆盖旧文件
 * @param replaceFile 默认true
 */
config.setReplaceFile(true);
/**
 * 需要生成的表名称
 * @param tables 多个表用逗号隔开,如果需要生成数据库中所有的表,参数为all
 */
config.setCreateTables("all");
/**
 * 如果是多模块项目,需要使用此项
 * @param childModuleName 指定在哪个模块下创建模板文件
 */
//config.setChildModuleName("模块名称");
//生成代码
TableFileCreateUtils.create(config);
```
----
## 2. 使用说明

```java
//使用示例
FastUserTestFastDAO query = new FastUserTestFastDAO();
query.userName().likeRight("张");
query.age().less(30);
query.createTime().orderByDesc();
List<FastUserTest> userList = query.dao().findAll();
```

### 2.1 条件设置

```java
//文件生成对象
UserFastDao fastDao = new UserFastDao();
```


|功能|方法|示例|
|---|---|---|
|相等条件设置| fastDao.fieldName(参数...)<br>fastDao.fieldName().notValEqual(参数)|`fastDao.userName(张三,李四)`<br>`fastDao.userName().notValEqual("张三")`|
|模糊匹配条件设置 |fastDao.fieldName().like(参数)<br>fastDao.fieldName().likeLeft(参数)<br>fastDao.fieldName().likeRight(参数)<br><br>fastDao.fieldName().notLike(参数)<br>fastDao.fieldName().notLikeLeft(参数)<br>fastDao.fieldName().notLikeRight(参数)|`fastDao.userName().like("张")`<br>`fastDao.userName().likeLeft("张")`<br>`fastDao.userName().likeRight("三")`<br><br>`fastDao.userName().notLike("张")`<br>`fastDao.userName().notLikeLeft("张")`<br>`fastDao.userName().notLikeRight("三")`|
|IN条件设置|fastDao.fieldName().in("参数1"...) <br>fastDao.fieldName().notIn("参数1"...)|`fastDao.userName().in("张三","李四")`<br>`fastDao.userName().notIn("张三","李四")` |
|范围条件设置|fastDao.fieldName().between(min, max)<br>fastDao.fieldName().notBetween(min, max)|`fastDao.age().between(20, 30)`<br>`fastDao.age().notBetween(20, 30)`|
|大于条件设置|fastDao.fieldName().greater(参数)|`fastDao.age().greater(30)`|
|大于等于条件设置|fastDao.fieldName().greaterOrEqual(参数)|`fastDao.age().greaterOrEqual(30)`|
|小于条件设置|fastDao.fieldName().less(参数)|`fastDao.age().less(10)`|
|小于等于条件设置|fastDao.fieldName().lessOrEqual(参数)|`fastDao.age().lessOrEqual(10)`|
|IsNull条件设置|fastDao.fieldName().isNull()|`fastDao.userName().isNull()`|
|NotNull条件设置|fastDao.fieldName().notNull()|`fastDao.userName().notNull()`|
|排序设置-升序|fastDao.fieldName().orderByAsc()|`fastDao.age().orderByAsc()`|
|排序设置-降序|fastDao.fieldName().orderByDesc()|`fastDao.age().orderByDesc()`|
|对象条件设置|fastDao.equalObject(对象)|`User user = new User;`<br>`user.setName("张三");`<br>`fastDao.equalObject(user )`<br>|
|查询指定字段设置|fastDao.fieldName().showField()|执行查询操作时只查询指定字段,可设置多个<br>`fastDao.id().showField();`<br>`fastDao.userName().showField();`|
|过滤字段设置|fastDao.fieldName().hideField()|查询操作时不查询指定字段,可设置多个<br>`fastDao.password().hideField();`<br>`fastDao.mail().hideField();`|
|字段去重复设置|fastDao.fieldName().distinctField()|`fastDao.userName().distinctField()`|
|字段去求和设置|fastDao.fieldName().sumField()|`fastDao.age().sumField()`|
|字段去求平均值设置|fastDao.fieldName().avgField()|`fastDao.age().avgField()`|
|字段去求最小值设置|fastDao.fieldName().minField()|`fastDao.age().minField()`|
|字段去求最大值设置|fastDao.fieldName().maxField()|`fastDao.age().maxField()`|
|字段自定义更新设置|fastDao.fieldName().customizeUpdateValue()|等同 age=age+5<br>同时可设置其他更新条件或更新参数<br>`fastDao.age().customizeUpdateValue().thisAdd("#{age}",Collections.singletonMap("age",5)).dao().update(null)`|
|自定义SQL条件设置|fastDao.andSql(SQL语句,参数)<br>fastDao.orSql(SQL语句,参数)<br>fastDao.sql(SQL语句,参数)|会在WHERE后拼接自定义SQL语句<br>如果有占位参数需要使用 #{参数名} 声明<br>传递参数MAP集合put(参数名,参数值)<br>`Map<String, Object> params = new HashMap<>();`<br>`params.put("userName", "张三");`<br>`fastDao.andSql("userName = #{userName}",params)`|
|关闭逻辑删除保护|fastDao.closeLogicDeleteProtect()|会对本次执行进行逻辑删除保护关闭<br>关闭后所有操作会影响到被逻辑删除标记的数据|
|OR条件设置|fastDao.fieldName().or()|指定字段OR条件设置 <br>例: 条件为姓名等于张三或为null <br>`fastDao.userName().valEqual("张三").or().isNull()`



### 2.2 Dao执行器
Dao执行器调用:
```java
//文件生成对象
FastDao<User> dao = UserFastDao.create().dao();
```
执行器方法:

|说明|方法名 |示例|
|---|---|---|
|新增|Pojo insert(Pojo pojo)|新增一个用户,新增成功后会进行对象主键字段赋值<br>`User user = UserFastDao.create().dao().insert(user)`|
|查询单条数据|Pojo findOne()|查询用户名为张三的信息<br>`User user = UserFastDao.create().userName("张三").dao().findOne()`|
|查询多条数据|List<Pojo> findAll()|查询年龄在20-30间的所有用户<br>`List<User> list = UserFastDao.create().age().between(20,30).dao().findAll()`|
|查询数量|Integer findCount()|查询一共有多少用户<br>`Integer count = UserFastDao.create().dao().findCount()`|
|分页查询|PageInfo<Pojo> findPage(int pageNum, int pageSize)|分页查询用户,并对年龄进行排序<br>`PageInfo<User> page = UserFastDao.create().age().orderByDesc().findPage(1, 10)`|
|更新数据,对象中参数为空的属性不进行更新|Integer update(Pojo pojo)|更新姓名为张三和李四的用户<br>`Integer count = UserFastDao.create().userName().in("张三","李四").dao().update(user)`|
|更新数据,对象中参数为空的属性也进行更新|Integer updateOverwrite(Pojo pojo)|更新年龄小于30,并且姓张的用户<br>`UserFastDao fastDao = UserFastDao.create();`<br>`fastDao.age().less(30);`<br>`fastDao.userName().like("张");`<br>`Integer count = fastDao.updateOverwrite(user)`|
|通过条件物理删除<br>如果启动了逻辑删除功能<br>本操作会自动将数据删除标记修改,不会进行物理删除<br>除非关闭逻辑删除保护<br>逻辑删除配置<br>FastDaoConfig.openLogicDelete("deleted",true);<br>关闭逻辑删除保护方式请参考条件设置<br>重要!!!如果不进行设置将使用物理删除方式|Integer delete()|删除年龄大于80或为null的用户<br>`Integer count = UserFastDao.create().age().greater(80).or().isNull().delete()`|


### 2.3 自定义SQL
 
多表等复杂SQL操作,可以使用自定义SQL执行器实现,框架会自动进行对象和表进行映射<br>
如果有参数需要使用 #{参数名} 声明,传递参数MAP集合中put(参数名,参数值)<br>
FastCustomSqlDao.create(Class, SQL语句, 参数)

```java
//例:
String sql = "SELECT * FROM user WHERE `user_name` LIKE #{userName}";

HashMap<String, Object> params = new HashMap<>();
params.put("userName","%张亚伟%");

List<User> all = FastCustomSqlDao.create(User.class, sql, params).findAll();
```

### 2.4 缓存使用

开启缓存功能后,可以Bean添加注解的方式启用缓存

```java
/**
 * Redis缓存
 * 当进行使用此框架模板进行操作新增,更新,删除操作时,会自动刷新Redis缓存中的数据
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

### 2.5 数据源切换

可以在任意一次执行时进行数据源更换,更换数据源只对当前线程影响

```java
//例
FastDaoConfig.dataSource(getDataSource());//更换全局数据源
FastDaoConfig.dataSourceThreadLocal(getDataSource());//更换本线程数据源

private static DataSource getDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
    dataSource.setUsername("root");
    dataSource.setPassword("123456");
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
}
```
----

### 2.6 切面
使用切面可以进行很多自定义操作,比如读写分离,CRUD时候添加参数,权限验证等
#### 2.6.1 实现FastDaoExpander接口
```java
public class DemoExpander implements FastDaoExpander {

    /**
     * @param param 封装了DAO所有的执行参数
     * @return 是否执行
     */
    @Override
    public boolean before(FastDaoParam param) {
        System.out.println("DAO执行前");
        return true;
    }

    /**
     * @param param 封装了DAO所有的执行参数
     */
    @Override
    public void after(FastDaoParam param) {
        System.out.println("DAO执行后");
    }

    @Override
    public List<ExpanderOccasion> occasion() {
        //配置DAO切面执行时机
        List<ExpanderOccasion> list = new ArrayList<>();
        list.add(ExpanderOccasion.SELECT);
        list.add(ExpanderOccasion.UPDATE);
        return list;
    }

}
```
#### 2.6.2 配置切面实现,可以添加多个切面
```java
FastDaoConfig.addFastDaoExpander(DemoExpander.class);
```

### 2.7 手动事务管理
```java
FastTransaction.open(); //开启事务
FastTransaction.commit(); //提交
FastTransaction.rollback(); //回滚

//示例
FastTransaction.open(); //开启事务
FastUserTestFastDao.create().dao().insert(user); //新增数据
FastTransaction.commit(); //提交
```

**感谢使用,希望您能提出宝贵的建议,我会不断改进更新**