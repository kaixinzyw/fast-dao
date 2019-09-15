package com.fast.db.template.implement.mybatis;

import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;
import com.fast.db.template.implement.spring_jdbc.FastSqlUtil;
import com.fast.db.template.utils.FastSQL;

/**
 * 扩展MyBatis 查询方法Sql语句拼接
 * @author 张亚伟 398850094@qq.com
 */
public class FastMyBatisSelectProvider {

    public String findByPrimaryKey(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);
        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(util.selectShowField(null))
                .FROM(fastClassTableMapper.getTableName())
                .WHERE(util.fieldPackage(fastClassTableMapper.getPrimaryKeyTableField()) + " = #{primaryKey} ");

        util.deleteWhere(fastSQL);
        String sql = fastSQL.toString();
        //System.out.println(sql);
        return sql;
    }



    public String findOne(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        CompoundQuery compoundQuery = param.getCompoundQuery();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(util.selectShowField(compoundQuery))
                .FROM(fastClassTableMapper.getTableName()).WHERE(util.whereSql(compoundQuery, param.getParamList(), "#"));
        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(param.getParamList()));
        return sql;
    }


    public String findAll(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        CompoundQuery compoundQuery = param.getCompoundQuery();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT(util.selectShowField(compoundQuery))
                .FROM(fastClassTableMapper.getTableName())
                .WHERE(util.whereSql(compoundQuery, param.getParamList(), "#"));
        util.orderBy(compoundQuery, fastSQL);
        String sql = fastSQL.toString();

        if (compoundQuery.getPage() != null && compoundQuery.getSize() != null) {
            sql += " limit #{page} , #{size} ";
            param.setPage(compoundQuery.getPage());
            param.setSize(compoundQuery.getSize());
        }

        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(param.getParamList()));
        return sql;
    }


    public String findCount(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        CompoundQuery compoundQuery = param.getCompoundQuery();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.SELECT("COUNT(" + util.fieldPackage(fastClassTableMapper.getPrimaryKeyTableField()) + ")")
                .FROM(fastClassTableMapper.getTableName())
                .WHERE(util.whereSql(compoundQuery, param.getParamList(), "#"));
        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(param.getParamList()));
        return sql;
    }
}
