package com.fast.fast;

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

    public static <Pojo> FastDao<Pojo> init(Class<Pojo> clazz, FastExample<Pojo> fastExample) {
        FastDao<Pojo> fastDao = fastDaoThreadLocal.get();
        if (fastDao == null) {
            fastDao = new FastDao<>();
            fastDaoThreadLocal.set(fastDao);
        }
        fastDao.clazz = clazz;
        fastDao.fastExample = fastExample;
        return fastDao;
    }

    private Class<Pojo> clazz;
    private FastExample<Pojo> fastExample;

    /**
     * 新增数据
     *
     * @param pojo 需要新增的数据,会对框架设置的主键字段进行赋值
     * @return 是否新增成功
     */
    public Boolean insert(Pojo pojo) {
        return DaoTemplate.init(clazz, fastExample).insert(pojo) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 新增数据
     *
     * @param pojos 需要新增的数据,会对框架设置的主键字段进行赋值
     * @return 是否新增成功
     */
    public Integer insertList(List<Pojo> pojos) {
        return DaoTemplate.init(clazz, fastExample).insertList(pojos);
    }

    /**
     * 通过查询条件查询一条数据
     *
     * @return 数据结果
     */
    public Pojo findOne() {
        return DaoTemplate.init(clazz, fastExample).findOne();
    }

    /**
     * 通过查询条件查询所符合要求的所有数据
     *
     * @return 数据结果
     */
    public List<Pojo> findAll() {
        return DaoTemplate.init(clazz, fastExample).findAll();
    }

    /**
     * 通过查询条件查询所符合要求的数据数量
     *
     * @return 查询到的数据条数
     */
    public Integer findCount() {
        return DaoTemplate.init(clazz, fastExample).findCount();
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页
     *
     * @param pageNum       页数
     * @param pageSize      每页条数
     * @param navigatePages 页面数量
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(int pageNum, int pageSize, int navigatePages) {
        return DaoTemplate.init(clazz, fastExample).findPage(pageNum, pageSize, navigatePages);
    }

    /**
     * 通过查询条件查询所符合要求的数据,并进行分页,默认9页
     *
     * @param pageNum  页数
     * @param pageSize 条数
     * @return 分页对象, 内包含分页信息和查询到的数据
     */
    public PageInfo<Pojo> findPage(int pageNum, int pageSize) {
        return DaoTemplate.init(clazz, fastExample).findPage(pageNum, pageSize, 9);
    }

    /**
     * 通过条件更新数据, 参数为空则的字段不会进行更新
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer update(Pojo pojo) {
        return DaoTemplate.init(clazz, fastExample).update(pojo, true);
    }

    /**
     * 通过条件更新数据,参数为空则的字段也会进行更新
     *
     * @param pojo 需要更新的数据 (本操作会对更新时间自动赋值)
     * @return 更新影响到的数据
     */
    public Integer updateOverwrite(Pojo pojo) {
        return DaoTemplate.init(clazz, fastExample).update(pojo, false);
    }

    /**
     * 通过主键逻辑删除标记,如果启动了逻辑删除功能,本操作会自动将数据删除标记修改,不会进行物理删除
     *
     * @return 删除影响到的数据条数
     */
    public Integer delete() {
        return DaoTemplate.init(clazz, fastExample).delete(false);
    }

    /**
     * 通过条件物理删除 (本操作会进行物理删除,请谨慎操作)
     *
     * @return 删除影响到的数据条数
     */
    public Integer deleteDisk() {
        return DaoTemplate.init(clazz, fastExample).delete(true);
    }

}
