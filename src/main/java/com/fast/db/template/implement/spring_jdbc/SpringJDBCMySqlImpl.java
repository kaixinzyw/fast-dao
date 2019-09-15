package com.fast.db.template.implement.spring_jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.fast.db.template.implement.FastDBActuator;
import com.fast.db.template.template.CompoundQuery;
import com.fast.db.template.utils.FastSQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SpringJDBC fastDB执行器实现
 * @author 张亚伟 398850094@qq.com
 */
public class SpringJDBCMySqlImpl<Pojo> extends FastDBActuator<Pojo> {


    @Override
    public Integer insert(Pojo pojo) {

        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.INSERT_INTO(fastClassTableMapper.getTableName());

        List<String> fieldNames = fastClassTableMapper.getFieldNames();
        Map<String, String> fieldTableNames = fastClassTableMapper.getFieldTableNames();

        List<Object> list = new ArrayList<>();
        for (String fieldName : fieldNames) {
            Object fieldValue = BeanUtil.getFieldValue(pojo, fieldName);
            if (fieldValue != null) {
                list.add(fieldValue);
                fastSQL.VALUES(util.fieldPackage(fieldTableNames.get(fieldName)), "?");
            }
        }
        int update = SpringJDBCMySqlDBConnection.getJdbcTemplate().update(fastSQL.toString(), list.toArray());
        return update;

    }

    @Override
    public Pojo findByPrimaryKey(Object primaryKey) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(util.selectShowField(null))
                .FROM(fastClassTableMapper.getTableName())
                .WHERE(util.fieldPackage(fastClassTableMapper.getPrimaryKeyTableField()) + " = ? ");
        util.deleteWhere(fastSQL);
        Object[] params = new Object[]{primaryKey};
        try {
            Pojo pojo = SpringJDBCMySqlDBConnection.getJdbcTemplate().queryForObject(fastSQL.toString(), params, new SpringJDBCMySqlRowMapper<>(fastClassTableMapper));
            return pojo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Pojo findOne(CompoundQuery compoundQuery) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        List<Object> paramList = new ArrayList<>();
        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(util.selectShowField(compoundQuery))
                .FROM(fastClassTableMapper.getTableName()).WHERE(util.whereSql(compoundQuery, paramList, "?"));
        String sql = fastSQL.toString();
        //System.out.println(sql);
        Object[] params = ArrayUtil.toArray(paramList, Object.class);
        try {
            Pojo pojo = SpringJDBCMySqlDBConnection.getJdbcTemplate().queryForObject(sql, params, new SpringJDBCMySqlRowMapper<>(fastClassTableMapper));
            return pojo;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Pojo> findAll(CompoundQuery compoundQuery) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        List<Object> paramList = new ArrayList<>();
        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(util.selectShowField(compoundQuery))
                .FROM(fastClassTableMapper.getTableName())
                .WHERE(util.whereSql(compoundQuery, paramList, "?"));
        util.orderBy(compoundQuery, fastSQL);

        String sql = fastSQL.toString();

        if (compoundQuery.getPage() != null && compoundQuery.getSize() != null) {
            sql += " limit ? , ? ";
            paramList.add(compoundQuery.getPage());
            paramList.add(compoundQuery.getSize());
        }

        //System.out.println(sql);

        Object[] params = ArrayUtil.toArray(paramList, Object.class);
        try {
            List<Pojo> query = SpringJDBCMySqlDBConnection.getJdbcTemplate().query(sql, params, new SpringJDBCMySqlRowMapper<>(fastClassTableMapper));
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Integer findCount(CompoundQuery compoundQuery) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        List<Object> paramList = new ArrayList<>();
        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT("count(*)").FROM(fastClassTableMapper.getTableName()).WHERE(util.whereSql(compoundQuery, paramList, "?"));
        String sql = fastSQL.toString();
        Object[] params = ArrayUtil.toArray(paramList, Object.class);
        try {
            Integer count = SpringJDBCMySqlDBConnection.getJdbcTemplate().queryForObject(sql, params, Integer.class);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Integer update(Pojo pojo, CompoundQuery compoundQuery, boolean isSelective) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        List<Object> paramList = new ArrayList<>();

        FastSQL fastSQL = new FastSQL();
        fastSQL.UPDATE(fastClassTableMapper.getTableName());

        util.updateSql(fastSQL, pojo, paramList, isSelective, "?");

        fastSQL.WHERE(util.whereSql(compoundQuery, paramList, "?"));

        Object[] params = ArrayUtil.toArray(paramList, Object.class);

        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(params));

        return SpringJDBCMySqlDBConnection.getJdbcTemplate().update(sql, params);
    }

    @Override
    public Integer updateByPrimaryKey(Pojo pojo, boolean isSelective) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        List<Object> paramList = new ArrayList<>();
        FastSQL fastSQL = new FastSQL();
        fastSQL.UPDATE(fastClassTableMapper.getTableName());
        util.updateSql(fastSQL, pojo, paramList, isSelective, "?");
        fastSQL.WHERE(util.fieldPackage(fastClassTableMapper.getPrimaryKeyTableField()) + " = ? ");
        paramList.add(BeanUtil.getFieldValue(pojo, fastClassTableMapper.getPrimaryKeyTableField()));
        Object[] params = ArrayUtil.toArray(paramList, Object.class);
        return SpringJDBCMySqlDBConnection.getJdbcTemplate().update(fastSQL.toString(), params);
    }

    @Override
    public Integer delete(CompoundQuery compoundQuery) {
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        List<Object> paramList = new ArrayList();
        FastSQL fastSQL = new FastSQL();
        fastSQL.DELETE_FROM(fastClassTableMapper.getTableName());
        fastSQL.WHERE(util.whereSql(compoundQuery, paramList, "?"));
        Object[] params = ArrayUtil.toArray(paramList, Object.class);
        return SpringJDBCMySqlDBConnection.getJdbcTemplate().update(fastSQL.toString(), params);
    }

    @Override
    public Integer deleteByPrimaryKey(Object primaryKey) {
        FastSQL fastSQL = new FastSQL();
        fastSQL.DELETE_FROM(fastClassTableMapper.getTableName());
        fastSQL.WHERE(" " + fastClassTableMapper.getTableName() + "." + fastClassTableMapper.getPrimaryKeyTableField() + " = ? ");
        Object[] params = new Object[]{primaryKey};
        return SpringJDBCMySqlDBConnection.getJdbcTemplate().update(fastSQL.toString(), params);
    }

}
