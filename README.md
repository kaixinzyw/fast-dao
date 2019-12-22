
>主页: [http://www.fast-dao.com/](http://www.fast-dao.com/) 
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
- 极·简化DAO操作，面向对象的数据库操作方式, 大幅度提高编码效率
- 支持自定义SQL
- 支持Spring事务管理
- 支持Redis缓存和内存缓存,支持缓存自动刷新

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
    <version>6.3</version>
</dependency>
```

#### 1.2.2 框架配置

```java
//配置数据源
FastDaoConfig.dataSource(getDataSource());

private static DataSource getDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
    dataSource.setUsername("root");
    dataSource.setPassword("123456");
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return dataSource;
}
```


### 1.3 文件生成
```java
public static void main(String[] args) {
    FileCreateConfig config = new FileCreateConfig();
    //数据库连接
    config.setDBInfo("jdbc:mysql://127.0.0.1:3306/user?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC","root","123456","com.mysql.cj.jdbc.Driver");
    //文件生成的包路径
    config.setBasePackage("com.fast.dao.test");
    //生成代码
    TableFileCreateUtils.create(config);
}
```
----
## 2. 使用说明

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
|自定义SQL条件设置|fastDao.andSql(SQL语句,参数)<br>fastDao.orSql(SQL语句,参数)|会在WHERE后拼接自定义SQL语句<br>如果有参数需要使用 :参数名 声明<br>传递参数MAP集合put(参数名,参数值)<br>`Map<String, Object> params = new HashMap<>();`<br>`params.put("userName", "张三");`<br>`fastDao.andSql("userName = :userName",params)`|
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
String sql = "SELECT * FROM user WHERE `user_name` LIKE :userName";

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

**感谢使用,希望您能提出宝贵的建议,我会不断改进更新**