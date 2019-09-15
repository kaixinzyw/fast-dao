package com.fast.db.template.implement.mybatis;

import com.fast.db.template.mapper.FastClassTableMapper;
import com.fast.db.template.template.CompoundQuery;
import com.fast.db.template.implement.spring_jdbc.FastSqlUtil;
import com.fast.db.template.utils.FastSQL;

import java.util.List;

/**
 * 扩展MyBatis 删除方法Sql语句拼接
 *
 * @author 张亚伟 398850094@qq.com
 */
public class FastMyBatisDeleteProvider {

    public String delete(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);
        CompoundQuery compoundQuery = param.getCompoundQuery();
        List<Object> paramList = param.getParamList();

        FastSQL fastSQL = new FastSQL();
        fastSQL.DELETE_FROM(fastClassTableMapper.getTableName());
        fastSQL.WHERE(util.whereSql(compoundQuery, paramList, "#"));
        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(JSONObject.toJSONString(paramList));
        return sql;
    }

    public String deleteByPrimaryKey(FastMybatisParam param) {
        FastClassTableMapper fastClassTableMapper = param.getFastClassTableMapper();
        FastSqlUtil util = new FastSqlUtil(fastClassTableMapper);

        FastSQL fastSQL = new FastSQL();
        fastSQL.DELETE_FROM(fastClassTableMapper.getTableName());

        fastSQL.WHERE(util.fieldPackage(fastClassTableMapper.getPrimaryKeyTableField()) + " = #{primaryKey}");
        String sql = fastSQL.toString();
        //System.out.println(sql);
        //System.out.println(param.getPrimaryKey());
        return sql;
    }
}
