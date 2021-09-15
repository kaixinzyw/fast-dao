package com.fast.fast;

import com.fast.condition.ConditionPackages;
import com.fast.condition.FastExample;
import com.fast.utils.page.PageInfo;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.List;

/**
 * Dao执行类
 * 创建可执行Dao必须使用FastDao.create
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class FastDao<Pojo> {

    private FastDao() {
    }

    private static final FastThreadLocal<FastDao> fastDaoThreadLocal = new FastThreadLocal<>();

    public static <Pojo> FastDao<Pojo> init(ConditionPackages<Pojo> conditionPackages) {
        FastDao<Pojo> fastDao = fastDaoThreadLocal.get();
        if (fastDao == null) {
            fastDao = new FastDao<>();
            fastDaoThreadLocal.set(fastDao);
        }
        fastDao.conditionPackages = conditionPackages;
        fastDao.conditionPackages.setSize(null);
        fastDao.conditionPackages.setPage(null);
        fastDao.conditionPackages.setLimit(null);
        return fastDao;
    }

    private ConditionPackages<Pojo> conditionPackages;

    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据,会对框架设置的主键字段进行赋值
     * @return 是否新增成功
     */
    public Pojo insert(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).insert(pojo);
    }

    /**
     * 批量新增数据
     *
     * @param pojos 需要新增的数据,会对框架设置的主键字段进行赋值
     * @return 是否新增成功
     */
    public List<Pojo> insertList(List<Pojo> pojos) {
        return DaoTemplate.init(conditionPackages).insertList(pojos);
    }

    /**
     * 批量新增数据,分多次插入
     *
     * @param pojos 需要新增的数据,会对框架设置的主键字段进行赋值
     * @param size  每次插入条数
     * @return 是否新增成功
     */
    public List<Pojo> insertList(List<Pojo> pojos, Integer size) {
        return DaoTemplate.init(conditionPackages).insertList(pojos, size);
    }

    /**
     * 新增或更新操作
     * @param pojo 如果主键有值则进行更新操作,主键为空则进行新增操作,参数为空不进行更新
     * @return 新增结果
     */
    public Pojo insertOrUpdateByPrimaryKey(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).insertOrUpdateByPrimaryKey(pojo, Boolean.TRUE);
    }

    /**
     * 新增或更新操作
     * @param pojo 如果主键有值则进行更新操作,主键为空则进行新增操作,参数为空则也进行更新
     * @return 新增结果
     */
    public Pojo insertOrUpdateByPrimaryKeyOverwrite(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).insertOrUpdateByPrimaryKey(pojo, Boolean.FALSE);
    }



    /**
     * 通过主键查询数据
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKeyValue 主键参数
     * @return 查询到的数据结果
     */
    public Pojo findByPrimaryKey(Object primaryKeyValue) {
        return DaoTemplate.init(conditionPackages).findByPrimaryKey(primaryKeyValue);
    }

    /**
     * 通过查询条件查询一条数据
     *
     * @return 数据结果
     */
    public Pojo findOne() {
        return DaoTemplate.init(conditionPackages).findOne();
    }

    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @return 数据结果
     */
    public List<Pojo> findAll() {
        return DaoTemplate.init(conditionPackages).findAll();
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @return 查询到的数据条数
     */
    public Integer findCount() {
        return DaoTemplate.init(conditionPackages).findCount();
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum       页数
     * @param pageSize      每页条数
     * @param navigatePages 页面数量
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(Integer pageNum, Integer pageSize, Integer navigatePages) {
        return DaoTemplate.init(conditionPackages).findPage(pageNum, pageSize, navigatePages);
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页,默认9页
     *
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(Integer pageNum, Integer pageSize) {
        return findPage(pageNum, pageSize, 9);
    }

    /**
     * 通过对象中的主键更新数据,参数为空则不进行更新
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKey(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).updateByPrimaryKey(pojo, Boolean.TRUE);
    }

    /**
     * 通过对象中的主键更新数据,参数为空则也进行更新
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param pojo 需要更新的数据,对象中必须有主键参数
     * @return 是否更新成功
     */
    public Boolean updateByPrimaryKeyOverwrite(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).updateByPrimaryKey(pojo, Boolean.FALSE);
    }

    /**
     * 通过条件更新数据, 参数为空则的字段不会进行更新
     * 如果条件参数为空,则获取主键作为条件参数,条件参数至少存在1个,否则抛出异常
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer update(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).update(pojo, true);
    }

    /**
     * 通过条件更新数据,参数为空则的字段也会进行更新
     * 如果条件参数为空,则获取主键作为条件参数,条件参数至少存在1个,否则抛出异常
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer updateOverwrite(Pojo pojo) {
        return DaoTemplate.init(conditionPackages).update(pojo, false);
    }

    /**
     * 通过主键删除
     * 如果设置逻辑删除,对逻辑删除的数据不进行操作
     *
     * @param primaryKey 主键ID
     * @return 是否删除成功
     */
    public Boolean deleteByPrimaryKey(Object primaryKey) {
        return DaoTemplate.init(conditionPackages).deleteByPrimaryKey(primaryKey);
    }

    /**
     * 通过条件物理删除,如果启动了逻辑删除功能,本操作会自动将数据删除标记修改,不会进行物理删除,除非关闭逻辑删除保护
     * 条件参数至少存在1个,否则抛出异常
     *
     * @return 删除影响到的数据条数
     */
    public Integer delete() {
        return DaoTemplate.init(conditionPackages).delete();
    }


}
