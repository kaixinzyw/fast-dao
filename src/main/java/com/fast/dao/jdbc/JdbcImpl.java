package com.fast.dao.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fast.aspect.ExpanderOccasion;
import com.fast.aspect.FastDaoExpanderRunner;
import com.fast.condition.ConditionPackages;
import com.fast.config.FastDaoAttributes;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.*;
import com.fast.dao.many.JSONResultUtil;
import com.fast.dao.many.JdbcJoinQueryRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SpringJDBC NamedParameterJdbcTemplate执行器实现
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class JdbcImpl {
    public static <T> List<T> insert(ConditionPackages<T> conditionPackages) {
        try {
            execution(conditionPackages);
            FastDaoExpanderRunner.runBeforeFastDaoExpander(conditionPackages, ExpanderOccasion.INSERT.method);
            FastInsertProvider.insert(conditionPackages);
            if (CollUtil.isNotEmpty(conditionPackages.getInsertList()) && conditionPackages.getTableMapper().getPrimaryKeyType() != null && conditionPackages.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO)) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                FastDaoAttributes.getJdbcTemplate().update(FastSqlUtil.sqlConversion(conditionPackages.getSql()), new MapSqlParameterSource(conditionPackages.getParamMap()), keyHolder);
                if (conditionPackages.getInsertList().size() == 1 && keyHolder.getKey() != null) {
                    BeanUtil.setFieldValue(conditionPackages.getInsertList().get(0), conditionPackages.getTableMapper().getPrimaryKeyField(), Objects.requireNonNull(keyHolder.getKey()).longValue());
                } else {
                    List<Map<String, Object>> keyList = keyHolder.getKeyList();
                    for (int i = 0; i < keyList.size(); i++) {
                        BeanUtil.setFieldValue(conditionPackages.getInsertList().get(i), conditionPackages.getTableMapper().getPrimaryKeyField(), Objects.requireNonNull(keyList.get(i).values().iterator().next()));
                    }
                }
            } else {
                FastDaoAttributes.getJdbcTemplate().update(FastSqlUtil.sqlConversion(conditionPackages.getSql()), conditionPackages.getParamMap());
            }
            return extracted(conditionPackages, conditionPackages.getInsertList(), ExpanderOccasion.INSERT.method);
        } finally {
            FastSqlPrintLog.printSql(conditionPackages);
        }
    }

    public static <T> List<T> select(ConditionPackages<T> conditionPackages) {
        try {
            execution(conditionPackages);
            FastDaoExpanderRunner.runBeforeFastDaoExpander(conditionPackages, ExpanderOccasion.SELECT.method);
            FastSelectProvider.findAll(conditionPackages);
            if (CollUtil.isEmpty(conditionPackages.getFastJoinQueryInfoList())) {
                return extracted(conditionPackages,
                        FastDaoAttributes.getJdbcTemplate().query(FastSqlUtil.sqlConversion(conditionPackages.getSql()),
                                conditionPackages.getParamMap(),
                                new JdbcRowMapper(conditionPackages)),
                        ExpanderOccasion.SELECT.method);
            } else {
                List<Map<String, JSONObject>> query = FastDaoAttributes.getJdbcTemplate().query(FastSqlUtil.sqlConversion(conditionPackages.getSql()),
                        conditionPackages.getParamMap(), new JdbcJoinQueryRowMapper());
                List<JSONObject> resultJsonList = JSONResultUtil.change(conditionPackages, conditionPackages.getFastJoinQueryInfoList(), query);
                if (resultJsonList == null) {
                    return extracted(conditionPackages, new ArrayList<>(), ExpanderOccasion.SELECT.method);
                }
                return extracted(conditionPackages,
                        JSONObject.parseArray(resultJsonList.toString(), conditionPackages.getTableMapper().getObjClass()),
                        ExpanderOccasion.SELECT.method);
            }
        } finally {
            FastSqlPrintLog.printSql(conditionPackages);
        }

    }

    public static Integer count(ConditionPackages conditionPackages) {
        try {
            execution(conditionPackages);
            FastDaoExpanderRunner.runBeforeFastDaoExpander(conditionPackages, ExpanderOccasion.COUNT.method);
            FastSelectProvider.findCount(conditionPackages);
            return extracted(conditionPackages,
                    FastDaoAttributes.getJdbcTemplate().queryForObject(FastSqlUtil.sqlConversion(conditionPackages.getSql()), conditionPackages.getParamMap(), Integer.class),
                    ExpanderOccasion.COUNT.method);
        } finally {
            FastSqlPrintLog.printSql(conditionPackages);
        }
    }

    public static Integer update(ConditionPackages conditionPackages) {
        try {
            execution(conditionPackages);
            FastDaoExpanderRunner.runBeforeFastDaoExpander(conditionPackages, ExpanderOccasion.UPDATE.method);
            FastUpdateProvider.update(conditionPackages);
            return extracted(conditionPackages,
                    FastDaoAttributes.getJdbcTemplate().update(FastSqlUtil.sqlConversion(conditionPackages.getSql()), conditionPackages.getParamMap()),
                    ExpanderOccasion.UPDATE.method);
        } finally {
            FastSqlPrintLog.printSql(conditionPackages);
        }
    }

    public static Integer delete(ConditionPackages conditionPackages) {
        try {
            execution(conditionPackages);
            FastDaoExpanderRunner.runBeforeFastDaoExpander(conditionPackages, ExpanderOccasion.DELETE.method);
            FastDeleteProvider.delete(conditionPackages);
            return extracted(conditionPackages,
                    FastDaoAttributes.getJdbcTemplate().update(FastSqlUtil.sqlConversion(conditionPackages.getSql()), conditionPackages.getParamMap()),
                    ExpanderOccasion.DELETE.method);
        } finally {
            FastSqlPrintLog.printSql(conditionPackages);
        }

    }

    private static <T> T extracted(ConditionPackages conditionPackages, T result, String methodName) {
        conditionPackages.setReturnVal(result);
        FastDaoExpanderRunner.runAfterFastDaoExpander(conditionPackages, methodName);
        return result;
    }

    private static void execution(ConditionPackages conditionPackages) {
        conditionPackages.setSqlTime(System.currentTimeMillis());
        conditionPackages.setSql(null);
    }

}
