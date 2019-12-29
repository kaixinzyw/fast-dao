package com.fast.dao.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.DaoActuator;
import com.fast.dao.utils.*;
import com.fast.fast.FastDaoParam;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SpringJDBC NamedParameterJdbcTemplate执行器实现
 *
 * @author 张亚伟 https://github.com/kaixinzyw
 */
public class JdbcImpl<T> implements DaoActuator<T> {

    @Override
    public List<T> insert() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastInsertProvider.insert(param);

        if (CollUtil.isNotEmpty(param.getInsertList()) && param.getTableMapper().getPrimaryKeyType() != null && param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO)) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            JdbcConnection.getJdbcTemplate().update(FastSqlUtil.sqlConversion(param.getSql()), new MapSqlParameterSource(param.getParamMap()), keyHolder);
            if (param.getInsertList().size() == 1) {
                BeanUtil.setFieldValue(param.getInsertList().get(0), param.getTableMapper().getPrimaryKeyField(), Objects.requireNonNull(keyHolder.getKey()).longValue());
            } else {
                List<Map<String, Object>> keyList = keyHolder.getKeyList();
                for (int i = 0; i < keyList.size(); i++) {
                    BeanUtil.setFieldValue(param.getInsertList().get(i), param.getTableMapper().getPrimaryKeyField(), Objects.requireNonNull(keyList.get(i).values().iterator().next()));
                }
            }
        } else {
            JdbcConnection.getJdbcTemplate().update(FastSqlUtil.sqlConversion(param.getSql()), param.getParamMap());
        }
        return param.getInsertList();
    }


    @Override
    public List<T> select() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findAll(param);
        return JdbcConnection.getJdbcTemplate().query(FastSqlUtil.sqlConversion(param.getSql()), param.getParamMap(), JdbcRowMapper.init(param));
    }

    @Override
    public Integer count() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastSelectProvider.findCount(param);
        try {
            return JdbcConnection.getJdbcTemplate().queryForObject(FastSqlUtil.sqlConversion(param.getSql()), param.getParamMap(), Integer.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer update() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastUpdateProvider.update(param);
        return JdbcConnection.getJdbcTemplate().update(FastSqlUtil.sqlConversion(param.getSql()), param.getParamMap());
    }

    @Override
    public Integer delete() {
        FastDaoParam<T> param = FastDaoParam.get();
        FastDeleteProvider.delete(param);
        return JdbcConnection.getJdbcTemplate().update(FastSqlUtil.sqlConversion(param.getSql()), param.getParamMap());
    }

}
