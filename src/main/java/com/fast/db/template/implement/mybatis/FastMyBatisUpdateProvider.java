package com.fast.db.template.implement.mybatis;

import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;
import com.fast.db.template.implement.spring_jdbc.FastSqlUtil;
import com.fast.db.template.utils.FastSQL;

import java.util.List;
/**
 * 扩展MyBatis 更新方法Sql语句拼接
 * @author 张亚伟 398850094@qq.com
 */
public class FastMyBatisUpdateProvider {

    public String update(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);
        CompoundQuery compoundQuery = param.getCompoundQuery();
        Object pojo = param.getPojo();
        List<Object> paramList = param.getParamList();
        boolean isSelective = param.getSelective();

        FastSQL fastSQL = new FastSQL();
        fastSQL.UPDATE(fastClassTableMapper.getTableName());

        util.updateSql(fastSQL, pojo, paramList, isSelective, "#");
        fastSQL.WHERE(util.whereSql(compoundQuery, paramList, "#"));
        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(pojo) + ":" + JSONObject.toJSONString(paramList));
        return sql;
    }

    public String updateByPrimaryKey(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);
        Object pojo = param.getPojo();
        List<Object> paramList = param.getParamList();
        boolean isSelective = param.getSelective();

        FastSQL fastSQL = new FastSQL();
        fastSQL.UPDATE(fastClassTableMapper.getTableName());

        util.updateSql(fastSQL, pojo, paramList, isSelective, "#");
        fastSQL.WHERE(util.fieldPackage(fastClassTableMapper.getPrimaryKeyTableField()) + " = #{pojo." + fastClassTableMapper.getPrimaryKeyField() + "}");
        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(param.getPojo()));
        return sql;
    }
}
