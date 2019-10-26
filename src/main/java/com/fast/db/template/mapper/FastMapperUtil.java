package com.fast.db.template.mapper;

import com.fast.db.template.cache.DataCache;
import com.fast.db.template.config.AutomaticParameterAttributes;
import com.fast.db.template.dao.DaoActuator;
import com.fast.db.template.template.FastDaoParam;
import com.fast.db.template.template.*;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * 表和对象关系映射生成工具
 *
 * @author 张亚伟 https://github.com/kaixinzyw/fast-db-template
 */
public class FastMapperUtil<Pojo> {

    /**
     * 线程缓存
     */
    private static final FastThreadLocal<FastMapperUtil> dataUtilThread = new FastThreadLocal<>();

    /**
     * 模板执行器
     */
    private FastDao<Pojo> fastDao;
    /**
     * 自定义SQL执行器
     */
    private FastCustomSqlDao<Pojo> fastCustomSqlDao;
    /**
     * 模板操作执行器
     */
    private DaoTemplate<Pojo> daoTemplate;
    /**
     * ORM实现执行器
     */
    private DaoActuator<Pojo> daoActuator;
    /**
     * 属性和表的映射
     */
    private TableMapper<Pojo> tableMapper;
    /**
     * 数据缓存执行器
     */
    private DataCache<Pojo> dataCache;
    /**
     * 条件封装器
     */
    private FastDaoParam<Pojo> daoParam;
    /**
     * 条件设置器
     */
    private FastExample<Pojo> fastExample;

    /**
     * 初始化FastDao执行器
     * @param clazz 需要操作的对象
     * @param fastExample 条件封装
     * @param <T> 操作对象的泛型
     * @return 初始化
     */
    public static <T> FastDao<T> fastDao(Class<T> clazz, FastExample<T> fastExample) {
        FastMapperUtil<T> dataUtil = FastMapperUtil.init(clazz, fastExample);
        return dataUtil.fastDao;
    }

    /**
     * 初始化FastDao执行器
     * @param clazz 需要操作的对象
     * @param <T> 操作对象的泛型
     * @return 初始化
     */
    public static <T> FastDao<T> fastDao(Class<T> clazz) {
        FastMapperUtil<T> dataUtil = FastMapperUtil.init(clazz, new FastExample<>(clazz));
        return dataUtil.fastDao;
    }

    /**
     * 初始化自定义SQL执行器
     * @param clazz 需要操作的对象
     * @param fastExample 条件封装
     * @param <T> 操作对象的泛型
     * @return 初始化
     */
    public static <T> FastCustomSqlDao<T> fastCustomSqlDao(Class<T> clazz, FastExample<T> fastExample) {
        FastMapperUtil<T> dataUtil = FastMapperUtil.init(clazz, fastExample);
        return dataUtil.fastCustomSqlDao;
    }

    /**
     * 获取执行器封装信息
     * @param <T> 执行器需要操作的对象
     * @return 初始化
     */
    public static <T> FastMapperUtil<T> get() {
        return dataUtilThread.get();
    }

    /**
     * 执行器封装创建
     * @param <T> 执行器需要操作的对象
     * @return 初始化
     */
    public static <T> FastMapperUtil<T> create() {
        FastMapperUtil<T> fastMapperUtil = new FastMapperUtil<>();
        fastMapperUtil.daoParam = new FastDaoParam<>();
        fastMapperUtil.fastDao = new FastDao<>();
        fastMapperUtil.fastCustomSqlDao = new FastCustomSqlDao<>();
        if (AutomaticParameterAttributes.isOpenCache) {
            fastMapperUtil.dataCache = new DataCache<>();
        }
        try {
            fastMapperUtil.daoActuator = AutomaticParameterAttributes.getDBActuator().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fastMapperUtil.daoTemplate = new DaoTemplate<>(fastMapperUtil.daoActuator);
        return fastMapperUtil;
    }

    /**
     * 模板所有执行器初始化
     * @param clazz 执行器操作的Class
     * @param fastExample 条件封装
     * @param <T> 执行器操作的对象泛型
     * @return 初始化
     */
    private static <T> FastMapperUtil<T> init(Class<T> clazz, FastExample<T> fastExample) {
        FastMapperUtil<T> fastMapperUtil = dataUtilThread.get();
        if (fastMapperUtil == null) {
            try {
                fastMapperUtil = FastMapperUtilFactory.getUtil();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            dataUtilThread.set(fastMapperUtil);
        }
        fastMapperUtil.fastExample = fastExample;
        if (fastMapperUtil.tableMapper != null && fastMapperUtil.tableMapper.getClassName().equals(clazz.getSimpleName())) {
            return fastMapperUtil;
        }
        fastMapperUtil.tableMapper = TableMapperUtil.getTableMappers(clazz);
        return fastMapperUtil;
    }

    public DaoTemplate<Pojo> getDaoTemplate() {
        return daoTemplate;
    }

    public FastDao<Pojo> getFastDao() {
        return fastDao;
    }

    public DaoActuator<Pojo> getDaoActuator() {
        return daoActuator;
    }

    public TableMapper<Pojo> getTableMapper() {
        return tableMapper;
    }

    public DataCache<Pojo> getDataCache() {
        return dataCache;
    }

    public FastDaoParam<Pojo> getDaoParam() {
        return daoParam;
    }

    public FastExample<Pojo> getFastExample() {
        return fastExample;
    }

    public FastCustomSqlDao<Pojo> getFastCustomSqlDao() {
        return fastCustomSqlDao;
    }
}
