# Java 极·简ORM框架
>官方主页: [http://www.fast-dao.com](http://www.fast-dao.com)
>
>作者: 张亚伟
>
>邮箱: 398850094@qq.com

----
- 极简化SQL操作
- 支持链式语法操作
- 全自动对象映射
- 支持多表查询
- 支持分布式缓存和内存缓存
- 支持自定义SQL查询
- 支持逻辑删除
- 支持切面
- 数据源切换
- 支持手动事务管理

**示例**
```java
User user = UserFastDao.create().dao().insert(user); //增,新增成功后主键会在对象中设置
Integer delCount = UserFastDao.create().dao().delete(); //删,可以选择逻辑删除和物理删除
Integer updateCount = UserFastDao.create().dao().update(user); //改,操作简单,条件丰富
PageInfo<User> page = UserFastDao.create().dao().findPage(1, 10); //查,分页查询
```

# 快速入门

## 测试表
```sql
create table user
(
    id          bigint auto_increment comment '主键' primary key,
    type_id     bigint null comment '用户类型',
    user_name   varchar(255) null comment '用户名',
    age         int(10) null comment '年龄',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    deleted     bit null comment '是否删除'
) comment '用户';


create table user_log
(
    id          bigint auto_increment comment '主键' primary key,
    user_id     bigint null comment '用户ID',
    log_info    varchar(255) null comment '日志内容',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    deleted     bit null comment '是否删除'
) comment '用户日志';

create table user_type
(
    id          bigint auto_increment comment '主键' primary key,
    type_name   varchar(255) null comment '用户名',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '更新时间',
    deleted     bit null comment '是否删除'
) comment '用户类型';

INSERT INTO user
(`id`, `type_id`, `user_name`, `age`, `create_time`, `update_time`, `deleted`)
VALUES (1, 1, 'User1', 1, '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (2, 1, 'User2', 2, '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (3, 2, 'User3', 3, '2021-09-14 16:46:11', '2021-09-14 16:46:11', false);

INSERT INTO user_type
    (`id`, `type_name`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'Type1', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (2, 'Type2', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false);

INSERT INTO user_log
    (`id`, `user_id`, `log_info`, `create_time`, `update_time`, `deleted`)
VALUES (1, 1, 'Log1', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false),
       (2, 1, 'Log2', '2021-09-14 16:46:11', '2021-09-14 16:46:11', false);
```

## Maven安装
```xml
<dependency>
    <groupId>com.fast-dao</groupId>
    <artifactId>fast-dao</artifactId>
    <version>9.8.5</version>
</dependency>
```

## 文件生成
```java
public static void main(String[] args) {
    //数据库连接 --改为自己数据库的连接
    String mysqlUrl ="jdbc:mysql://127.0.0.1:3306/my_test?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名 --改为自己数据库用户名
    String userName = "root";
    //数据库密码 -改为自己数据库密码
    String password = "kaixinzyw";
    //数据库驱动 --改为自己数据库驱动
    String drive = "com.mysql.cj.jdbc.Driver";
    //文件生成的包路径 --改为自己项目包路径
    String packagePath = "com.fast.test";
    //生成的表名 --改为自己所需要生成的表
    String[] tableNames = {"user","user_log","user_type"};

    FileCreateConfig config = new FileCreateConfig();
    //数据库连接
    config.setDBInfo(mysqlUrl,userName,password,drive);
    //文件生成的包路径
    config.setBasePackage(packagePath);
    //选择生成的文件
    config.setNeedModules(FileCreateConfig.CodeCreateModule.Base);
    //需要生成的表名
    config.setCreateTables(tableNames);
    //生成代码
    TableFileCreateUtils.create(config);
}
```
### 其他配置
#### 生成DTO
```java
//生成DTO
config.setNeedModules(FileCreateConfig.CodeCreateModule.DTO);
//DOT是否继承POJO
config.setDtoExtendsPOJO(true);
```
#### 表前缀设置
```java
//是否生成表前缀
config.setPrefix(false,false,null);
```
#### lombok注解
```java
//是否使用lombok插件,默认false
config.setUseLombok(true);
```
#### Swagger2注解
```java
//是否在DTO上使用Swagger2注解,默认false
config.setUseDTOSwagger2(true);
//是否在POJO上使用Swagger2注解,默认false
config.setUsePOJOSwagger2(true);
```
## 框架配置

### Spring环境
Spring环境下无需任何配置即可使用,框架可自动识别Spring配置的数据源信息

### 非Spring环境
```java
public static void fastDaoConfig() {
    /**
     * 数据源配置
     */
    FastDaoConfig.dataSource(getDataSource());
}
//数据源信息
private static DataSource getDataSource() {
    //数据库连接 -改为自己数据库的连接
    String mysqlUrl ="jdbc:mysql://127.0.0.1:3306/my_test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&useOldAliasMetadataBehavior=true";
    //数据库用户名 -改为自己数据库用户名
    String userName = "root";
    //数据库密码 -改为自己数据库密码
    String password = "kaixinzyw";
    //数据库驱动 改为自己数据库驱动
    String drive = "com.mysql.cj.jdbc.Driver";
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl(mysqlUrl);
    dataSource.setUsername(userName);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(drive);
    return dataSource;
}
```
### 其他配置
#### 日志输出
```java
/**
 * 设置SQL日志打印,默认关闭
 * 参数1: 日志打印级别 DEBUG,INFO,OFF
 * 参数2: 是否打印详细SQL日志
 * 参数3: 是否打印SQL执行结果
 */
FastDaoConfig.openSqlPrint(SqlLogLevel.INFO,true, true);
```
#### 逻辑删除
```java
/**
 * 开启逻辑删除功能,开启后会对逻辑删除标记的数据在 更新|删除|查询 时进行保护,可通过模板进行单次操作逻辑删除保护的关闭
 * 参数1:  逻辑删除字段名
 * 参数2:  逻辑删除标记默认值
 */
FastDaoConfig.openLogicDelete("deleted", Boolean.TRUE);
```
#### 自动设置时间
```java
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
```

## 示例

### 新增
```
注意:
主键如果为数字类型默认自增,自增后ID会Set到新增的对象中
主键如果为字符串框架会自动进行设置ObjectId 设置后的ID会Set到新增的对象中
ObjectId由以下几部分组成：
1. Time 时间戳。
2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
3. PID 进程ID。确保同一机器中不冲突
4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
```
```java
User user = new User();
user.setUserName("张三");
user.setAge(20);
user.setDeleted(false);
UserFastDAO.create().dao().insert(user);
```
```SQL输出```
```sql
INSERT INTO user SET user_name = '张三' , age = 20 , deleted = false
```
```执行结果```
```json
[{"age":20,"deleted":false,"id":189,"userName":"张三"}]
```

#### 批量新增
```java
User user1 = new User();
user1.setUserName("张三");
user1.setAge(20);
user1.setDeleted(false);

User user2 = new User();
user2.setUserName("李四");
user2.setAge(30);
user2.setDeleted(false);

List<User> userList = new ArrayList<>();
userList.add(user1);
userList.add(user2);
List<User> ls = UserFastDAO.create().dao().insertList(userList);
```
```SQL输出```
```sql
INSERT INTO user
(`id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`) VALUES
(null , null , '张三' , 20 , null , null , false ),
(null , null , '李四' , 30 , null , null , false )
```

### 删除
```
注意
如果设置逻辑删除则调用Updata
如果没设置逻辑删除则会调用Delete
返回值为删除条目数
```
#### 通过主键删除
```java
UserFastDAO.create().dao().deleteByPrimaryKey(1L);
```
```SQL输出-物理删除```
```sql
DELETE FROM user WHERE `id` = 1
```
```SQL输出-逻辑删除```
```sql
UPDATE user SET `deleted` = true WHERE `deleted` = false AND `id` = 1
```

#### 通过条件删除
```java
Integer delete = UserFastDAO.create().userName("张三").dao().delete()
```
```SQL输出-物理删除```
```sql
DELETE FROM user WHERE `user_name` = '张三'
```
```SQL输出-逻辑删除```
```sql
UPDATE user SET  `deleted` = true  WHERE `deleted` = false AND `user_name` = '张三'
```
### 更新
```
更新最少必须设置一个条件
无论是通过主键更新或条件更新 一但更新的对象主键字段不为空则优先将主键参数设为条件
返回值为更新条目数
```
#### 通过主键更新
```字段参数为空则不更新```
```java
User user = new User();
user.setId(1L);
user.setUserName("更新姓名");
UserFastDAO.create().dao().updateByPrimaryKey(user);
```
```SQL输出```
```sql
UPDATE user SET  `id` = 1 , `user_name` = '更新姓名' WHERE `id` = 1
```
#### 通过主键全字段更新
```字段参数为空也进行更新```
```java
User user = new User();
user.setId(1L);
user.setUserName("更新姓名");
UserFastDAO.create().dao().updateByPrimaryKeyOverwrite(user);
```
```SQL输出```
```sql
UPDATE user SET
`id` = 1 , `type_id` = null , `user_name` = '更新姓名' , `age` = null , `create_time` = null , `update_time` = null , `deleted` = null
WHERE `id` = 1
```

#### 通过条件更新
```java
User user = new User();
user.setUserName("更新姓名");
UserFastDAO.create().age().less(10).dao().update(user);
```
```SQL输出```
```sql
UPDATE user SET
`user_name` = '更新姓名'
WHERE `age` < 10
```

### 查询
#### 通过ID查询
```java
User user = UserFastDAO.create().dao().findByPrimaryKey(1L);
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `id` = 1
LIMIT 1
```
```执行结果```
```json
[{"age":10,"createTime":1632150396000,"deleted":true,"id":1,"typeId":1,"updateTime":1632150399000,"userName":"张三"}]
````
#### 通过条件查询
```java
List<User> userList = UserFastDAO.create().userName().like("张").dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` LIKE '%张%'
```
```执行结果```
```json
[{"age":10,"createTime":1632150396000,"deleted":true,"id":1,"typeId":1,"updateTime":1632150399000,"userName":"张三"}]
```
#### 分页查询
```java
PageInfo<User> pageInfo = UserFastDAO.create().userName().like("张").dao().findPage(1, 10);
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` LIKE '%张%'
LIMIT 0 , 10
```
```执行结果```
```json
{
	"endRow":1,
	"hasNextPage":false,
	"hasPreviousPage":false,
	"isFirstPage":true,
	"isLastPage":true,
	"list":[
		{
			"age":10,
			"createTime":1632150396000,
			"deleted":true,
			"id":1,
			"typeId":1,
			"updateTime":1632150399000,
			"userName":"张三"
		}
	],
	"navigateFirstPage":1,
	"navigateLastPage":1,
	"navigatePages":9,
	"navigatepageNums":[1],
	"nextPage":0,
	"pageNum":1,
	"pageSize":10,
	"pages":1,
	"prePage":0,
	"size":1,
	"startRow":1,
	"total":1
}
```

### 自定义SQL
```java
String sql = "SELECT u.id,u.user_name,u.type_id,t.id,t.type_name FROM user u LEFT JOIN user_type t on u.type_id = t.id where u.id=${id}";
Map<String, Object> data = new HashMap<>();
data.put("id", 1);
List<CustomSqlUserDTO> all = FastCustomSqlDao.create(CustomSqlUserDTO.class, sql, data).findAll();
```

```SQL输出```
```sql
SELECT u.id,u.user_name,u.type_id,t.id,t.type_name FROM user u LEFT JOIN user_type t on u.type_id = t.id where u.id=1
```
```执行结果```
```json
[
	{
		"id":1,
		"typeId":1,
		"userName":"User1",
		"userType":{
			"id":1,
			"typeName":"Type1"
		}
	}
]
```
```java
/**
 * 自定义SQL测试DTO
 */
@Table(name = "u")
public class CustomSqlUserDTO extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @FastJoinQuery(thisTableAlias = "u",thisColumnName = "type_id", joinTableAlias = "t", joinColumnName = "id")
    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
```

# 多表查询
```注解```
```java
//可以使用在类上或字段上
//使用在类上的时候可在多表查询时对主表别名进行设置
//使用在字段上时可表示去指定别名表中的数据
@TableAlias(表别名)

//可以使用在字段上 表示需要对连表查询时的数据进行封装
//可选参数 thisTableAlias 查询表别名
//可选参数 thisColumnName 查询表映射列名
//可选参数 joinTableAlias 连接表别名
//可选参数 joinColumnName 连接表映射列名
//可选参数 默认值 连接表别名
@FastJoinQuery
//对此字段不进行查询
@NotQuery
```
```java
JoinFastDao.create(查询返回对象, 主表条件).leftJoin(连接表条件)//可选rightJoin,innerJoin
    .on(连接表映射字段名, 查询表映射字段名).and(on后条件)
    .orderBy(排序字段名).desc()
```
```示例```
```java
//设置用户查询条件
UserFastDAO userFastDAO = UserFastDAO.create().userName().like("User").age().greaterOrEqual(1).id().between(1,10);
//设置用户类型查询条件
UserTypeFastDAO typeFastDAO = UserTypeFastDAO.create().typeName().likeRight("Type").deleted(false);
//设置用户日志1查询条件
UserLogFastDAO logFastDAO = UserLogFastDAO.create();
//设置用户日志2查询条件
UserLogFastDAO logFastDAO2 = UserLogFastDAO.create().id(2L);
//多表查询
List<UserDTO> userList = JoinFastDao.create(UserDTO.class, userFastDAO)
        .leftJoin(typeFastDAO).on(typeFastDAO.id(), userFastDAO.typeId())
        .leftJoin(logFastDAO).on(logFastDAO.userId(), userFastDAO.id())
        .leftJoin(logFastDAO2,"log2").on(logFastDAO.userId(), userFastDAO.id()).and("log2.`deleted` = ${log2Deleted}", MapUtil.of("log2Deleted", false))
        .dao().findAll();
```
```SQL输出```
```sql
SELECT user_type.`type_name`,user_type.`id` AS userTypeId,user_test.`id`,user_test.`type_id`,user_test.`user_name`,user_test.`age`,user_test.`create_time`,user_test.`update_time`,
       user_type.`id`,user_type.`type_name`,user_type.`create_time`,user_type.`update_time`,user_type.`deleted`,
       user_log.`id`,user_log.`user_id`,user_log.`log_info`,user_log.`create_time`,user_log.`update_time`,user_log.`deleted`,
       log2.`id`,log2.`user_id`,log2.`log_info`,log2.`create_time`,log2.`update_time`,log2.`deleted`
FROM user AS user_test
         LEFT JOIN user_type AS user_type ON user_type.`id` = user_test.`type_id`
         LEFT JOIN user_log AS user_log ON user_log.`user_id` = user_test.`id`
         LEFT JOIN user_log AS log2 ON log2.`user_id` = user_test.`id` AND log2.`deleted` = false
WHERE user_test.`deleted` = false
  AND user_test.`user_name` LIKE '%User%'
  AND user_test.`age` >= 1
  AND user_test.`id` BETWEEN 1 AND 10
  AND user_type.`deleted` = false
  AND user_type.`type_name` LIKE 'Type%'
  AND user_type.`deleted` = false
  AND user_log.`deleted` = false
  AND log2.`deleted` = false
  AND log2.`id` = 2 
```
```执行结果```
```json
[
	{
		"age":1,
		"createTime":1631609171000,
		"id":1,
		"typeId":1,
		"typeName":"Type1",
		"updateTime":1631609171000,
		"userLogList":[
			{
				"createTime":1631609171000,
				"deleted":false,
				"id":1,
				"logInfo":"Log1",
				"updateTime":1631609171000,
				"userId":1
			},
			{
				"createTime":1631609171000,
				"deleted":false,
				"id":2,
				"logInfo":"Log2",
				"updateTime":1631609171000,
				"userId":1
			}
		],
		"userLogList2":[
			{
				"createTime":1631609171000,
				"deleted":false,
				"id":2,
				"logInfo":"Log2",
				"updateTime":1631609171000,
				"userId":1
			}
		],
		"userName":"User1",
		"userType":{
			"createTime":1631609171000,
			"deleted":false,
			"id":1,
			"typeName":"Type1",
			"updateTime":1631609171000
		}
	}
]
```
```java
/**
 * 用户多表查询DTO
 */
@TableAlias("user")
public class UserDTO extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotQuery
    private Boolean deleted;

    @FastJoinQuery
    private List<UserLog> userLogList;

    @FastJoinQuery("log2")
    private List<UserLog> userLogList2;

    @FastJoinQuery("user_type")
    private UserTypeDTO userType;

    @TableAlias("user_type")
    private String typeName;

    public List<UserLog> getUserLogList() {
        return userLogList;
    }

    public void setUserLogList(List<UserLog> userLogList) {
        this.userLogList = userLogList;
    }

    public UserTypeDTO getUserType() {
        return userType;
    }

    public void setUserType(UserTypeDTO userType) {
        this.userType = userType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<UserLog> getUserLogList2() {
        return userLogList2;
    }

    public void setUserLogList2(List<UserLog> userLogList2) {
        this.userLogList2 = userLogList2;
    }
}
```

# 执行器

|说明|方法名 |示例|
|---|---|---|
|新增|Pojo insert(Pojo pojo)|新增一个用户,新增成功后会进行对象主键字段赋值<br>`User user = UserFastDao.create().dao().insert(user)`|
|批量新增|List insertList(List pojoList)|批量信息用户,新增成功后会进行对象主键字段赋值,可选每次新增数<br>`List<User> userList = UserFastDao.create().dao().insertList(list)`|
|通过主键查询|Pojo findByPrimaryKey(参数)|查询用ID为1的信息<br>`User user = UserFastDAO.create().dao().findByPrimaryKey(1);`|
|查询单条数据|Pojo findOne()|查询用户名为张三的信息<br>`User user = UserFastDao.create().userName("张三").dao().findOne()`|
|查询多条数据|List findAll()|查询年龄在20-30间的所有用户<br>`List<User> list = UserFastDao.create().age().between(20,30).dao().findAll()`|
|查询数量|Integer findCount()|查询一共有多少用户<br>`Integer count = UserFastDao.create().dao().findCount()`|
|分页查询|PageInfo findPage(int pageNum, int pageSize)|分页查询用户,并对年龄进行排序<br>`PageInfo<User> page = UserFastDao.create().age().orderByDesc().findPage(1, 10)`|
|通过主键更新数据,对象中参数为空的属性不进行更新|Integer updateByPrimaryKey(Pojo pojo)|通过主键更新数据<br>`Integer count = UserFastDao.create().dao().updateByPrimaryKey(user)`|
|通过主键更新数据,对象中参数为空的属性也进行更新|Integer updateByPrimaryKeyOverwrite(Pojo pojo)|通过主键更新数据<br>`Integer count = UserFastDao.create().dao().updateByPrimaryKeyOverwrite(user)`|
|更新数据,对象中参数为空的属性不进行更新|Integer update(Pojo pojo)|更新姓名为张三和李四的用户<br>`Integer count = UserFastDao.create().userName().in("张三","李四").dao().update(user)`|
|更新数据,对象中参数为空的属性也进行更新|Integer updateOverwrite(Pojo pojo)|更新年龄小于30,并且姓张的用户<br>`UserFastDao fastDao = UserFastDao.create();`<br>`fastDao.age().less(30);`<br>`fastDao.userName().like("张");`<br>`Integer count = fastDao.updateOverwrite(user)`|
|通过主键删除数据|Integer deleteByPrimaryKey(参数)|删除ID为1的用户<br>`Integer count = UserFastDao.create().deleteByPrimaryKey(1)`|
|通过条件物理删除<br>如果启动了逻辑删除功能<br>本操作会自动将数据删除标记修改,不会进行物理删除<br>除非关闭逻辑删除保护<br>逻辑删除配置<br>FastDaoConfig.openLogicDelete("deleted",true);<br>关闭逻辑删除保护方式请参考条件设置<br>重要!!!如果不进行设置将使用物理删除方式|Integer delete()|删除年龄大于80或为null的用户<br>`Integer count = UserFastDao.create().age().greater(80).or().isNull().delete()`|


# 功能介绍
```创建示例对象```
```java
UserFastDao fastDao = UserFastDAO.create();
```
## 条件设置
### 对象条件
```对象内不为空的字段会作为查询条件```
```java
//语法
fastDao.equalObject(对象);
```
```示例```
```java
//对象条件设置
User query = new User();
query.setUserName("User1");
//执行查询
User user = UserFastDAO.create().equalObject(query).dao().findOne();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` = 'User1'
LIMIT 1
```

### 相等条件
```java
fastDao.字段名(参数);
//或
fastDao.字段名().valEqual(参数);
```
```示例```
```java
User user = UserFastDAO.create().userName("User1").dao().findOne();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` = '张三'
LIMIT 1
```
### 不相等条件
```java
fastDao.字段名().notValEqual(参数);
```
```示例```
```java
User user = UserFastDAO.create().userName().notValEqual("张三").dao().findOne();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` != '张三'
LIMIT 1
```

### 模糊匹配条件
```java
//两边模糊匹配
fastDao.字段名().like(参数);
//左模糊匹配
fastDao.字段名().likeLeft(参数);
//右模糊匹配
fastDao.字段名().likeRight(参数);
```
```示例```
```java
User user = UserFastDAO.create().userName().like("张三").dao().findOne();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` LIKE '%张三%'
LIMIT 1
```

### 模糊排除条件
```java
//两边模糊排除
fastDao.字段名().notLike(参数);
//左模糊排除
fastDao.字段名().notLikeLeft(参数);
//右模糊排除
fastDao.字段名().notLikeRight(参数);
```
```示例```
```java
User user = UserFastDAO.create().userName().notLike("张三").dao().findOne();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` NOT LIKE '%张三%'
LIMIT 1
```

### 包含条件
```java
fastDao.字段名().in(参数1,参数2);
//或
fastDao.字段名().in(集合);
```
```示例```
```java
List<User> userList = UserFastDAO.create().userName().in("张三", "李四").dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` IN ('张三','李四')
```
### 不包含条件
```java
fastDao.字段名().notIn(参数1,参数2);
//或
fastDao.字段名().notIn(集合);
```
```示例```
```java
List<User> userList = UserFastDAO.create().userName().notIn("张三", "李四").dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` NOT IN ('张三','李四')
```

### 范围条件
```java
fastDao.字段名().between(起始值,结束值);
```
```示例```
```java
List<User> userList = UserFastDAO.create().age().between(2, 5).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `age` BETWEEN 2 AND 5
```
### 范围排除条件
```java
fastDao.字段名().notBetween(起始值,结束值);
```
```示例```
```java
List<User> userList = UserFastDAO.create().age().notBetween(2, 5).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `age` NOT BETWEEN 2 AND 5
```

### 大于条件
```java
fastDao.字段名().greater(参数);
```
```示例```
```java
List<User> userList = UserFastDAO.create().age().greater(2).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `age` > 2
```
### 大于等于条件
```java
fastDao.字段名().greaterOrEqual(参数);
```
```示例```
```java
List<User> userList = UserFastDAO.create().age().greaterOrEqual(2).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `age` >= 2
```

### 小于条件
```java
fastDao.字段名().less(参数);
```
```示例```
```java
List<User> userList = UserFastDAO.create().age().less(5).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `age` < 5
```
### 小于等于条件
```java
fastDao.字段名().lessOrEqual(参数);
```
```示例```
```java
List<User> userList = UserFastDAO.create().age().lessOrEqual(5).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `age` <= 5
```

### IsNull条件
```java
fastDao.字段名().isNull();
```
```示例```
```java
List<User> userList = UserFastDAO.create().typeId().isNull().dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `type_id` IS NULL
```
### NotNull条件
```java
fastDao.字段名().notNull();
```
```示例```
```java
List<User> userList = UserFastDAO.create().typeId().notNull().dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `type_id` IS NOT NULL
```

### 排序
```java
//升序
fastDao.字段名().orderByAsc();
//降序
fastDao.字段名().orderByDesc();
```
```示例```
```java
List<User> userList = UserFastDAO.create().createTime().orderByDesc().dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
ORDER BY `create_time` DESC
```

### 字段过滤
```java
//查询时只查询指定字段
fastDao.字段名().showField();
//查询时不查询指定字段
fastDao.字段名().hideField();
```
```示例```
```java
List<User> userList = UserFastDAO.create().userName().showField().dao().findAll();
```
```SQL输出```
```sql
SELECT `user_name` FROM user
```

### 字段去重复
```java
fastDao.字段名().distinctField();
```
```示例```
```java
List<User> userList = UserFastDAO.create().userName().distinctField().dao().findAll();
```
```SQL输出```
```sql
SELECT DISTINCT `user_name` FROM user
```

### 聚合函数
```java
//求和
fastDao.字段名().sumField();
//求平均值
fastDao.字段名().avgField();
//求最小值
fastDao.字段名().minField();
//求最大值
fastDao.字段名().maxField();
```
```示例```
```java
User user = UserFastDAO.create().age().maxField().dao().findOne();
```
```SQL输出```
```sql
SELECT MAX(`age`) `age` FROM user LIMIT 1
```

### 自定义条件
```java
//会在WHERE后拼接自定义SQL语句 如果有占位参数需要使用${参数名}声明 传递参数MAP集合put(参数名,参数值)
fastDao.andSql(SQL语句,参数)//拼接AND
fastDao.orSql(SQL语句,参数)//拼接OR
fastDao.sql(SQL语句,参数)//需要自己拼接连接条件
```
```示例```
```java
Map<String,Object> paramMap = new HashMap<>();
paramMap.put("age",20);
List<User> userList = UserFastDAO.create().userName().likeRight("张").andSql("age > ${age}", paramMap).dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` LIKE '张%'
AND age > 20
```

### 自定义更新
```java
//(字段=字段+值)加法运算 可以使用SQL占位符, (${参数名},Map<参数名,数据>)
fastDao.字段名.customizeUpdateValue().thisAdd(参数);
//(字段=字段-值)减法运算 可以使用SQL占位符, (${参数名},Map<参数名,数据>)
fastDao.字段名.customizeUpdateValue().thisSbu(参数);
//(字段=字段*值)乘法运算 可以使用SQL占位符, (${参数名},Map<参数名,数据>)
fastDao.字段名.customizeUpdateValue().thisMul(参数);
//(字段=字段/值)除法运算 可以使用SQL占位符, (${参数名},Map<参数名,数据>)
fastDao.字段名.customizeUpdateValue().thisDiv(参数);
//(字段=字段%值)取模运算 可以使用SQL占位符, (${参数名},Map<参数名,数据>)
fastDao.字段名.customizeUpdateValue().thisModulo(参数);
//字段自定义运算 可以使用SQL占位符, (${参数名},Map<参数名,数据>) 如:customize("user_age + 1") 则 tableColumnName = user_age + 1
fastDao.字段名.customizeUpdateValue().customize(SQL,参数);
```
```示例```
```java
UserFastDAO.create().typeId(1L)
        .age().customizeUpdateValue().thisAdd(1)
        .dao().update(null);
```
```SQL输出```
```sql
UPDATE user SET  `age` = `age` + 1
WHERE `type_id` = 1
```

### 关闭逻辑删除
```java
//如果开启了逻辑删除功能 可以通过此设置关闭逻辑删除
fastDao.closeLogicDeleteProtect();
```
```示例```
```java
List<User> userList = UserFastDAO.create().closeLogicDeleteProtect().dao().findAll();
```

### OR条件
```java
fastDao.字段名().or();
```
```示例```
```java
List<User> userList = UserFastDAO.create()
    .userName().likeRight("张")
    .age().or().greater(10)
    .dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `user_name` LIKE '张%'
OR `age` > 10
```

### 添加括号
```java
//AND左括号
fastDao.andLeftBracket();
//OR左括号
fastDao.orLeftBracket();
//右括号
fastDao.rightBracket();

```
```示例```
```java
List<User> userList = UserFastDAO.create().typeId().notNull()
        .orLeftBracket()
        .userName().likeRight("张").age().or().greater(10)
        .rightBracket()
        .dao().findAll();
```
```SQL输出```
```sql
SELECT `id`,`type_id`,`user_name`,`age`,`create_time`,`update_time`,`deleted`
FROM user
WHERE `type_id` IS NOT NULL
OR  (`user_name` LIKE '张%' OR `age` > 10 )
```



# 自定义SQL

多表等复杂SQL操作,可以使用自定义SQL执行器实现,框架会自动进行对象和表进行映射<br>
如果有参数需要使用 ${参数名} 声明,传递参数MAP集合中put(参数名,参数值)<br>
FastCustomSqlDao.create(Class, SQL语句, 参数)

```java
//例:
String sql = "SELECT * FROM user WHERE `user_name` LIKE ${userName}";

HashMap<String, Object> params = new HashMap<>();
params.put("userName","%张亚伟%");

List<User> all = FastCustomSqlDao.create(User.class, sql, params).findAll();
```

# 缓存使用

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

# 数据源切换

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

# 切面
使用切面可以进行很多自定义操作,比如读写分离,CRUD时候添加参数,权限验证等
## 实现FastDaoExpander接口
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
## 配置切面实现,可以添加多个切面
```java
FastDaoConfig.addFastDaoExpander(DemoExpander.class);
```

# 手动事务管理
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