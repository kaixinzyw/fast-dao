package com.fast.dao.jdbc;

import cn.hutool.core.bean.BeanUtil;
import com.fast.config.PrimaryKeyType;
import com.fast.dao.DaoActuator;
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
    public Integer insert() {
        FastDaoParam<T> param = FastDaoParam.get();
        String jdbcSql = param.getSql();
        int insertCount;
        if (param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO)) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            insertCount = JdbcConnection.getJdbcTemplate().update(jdbcSql, new MapSqlParameterSource(param.getParamMap()), keyHolder);
            BeanUtil.setFieldValue(param.getInsert(), param.getTableMapper().getPrimaryKeyField(), Objects.requireNonNull(keyHolder.getKey()).longValue());
        } else {
            insertCount = JdbcConnection.getJdbcTemplate().update(jdbcSql, param.getParamMap());
        }
        return insertCount;

    }

    @Override
    public Integer insertList() {
        FastDaoParam<T> param = FastDaoParam.get();
        String jdbcSql = param.getSql();
        int insertCount;
        if (param.getTableMapper().getPrimaryKeyType().equals(PrimaryKeyType.AUTO)) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            insertCount = JdbcConnection.getJdbcTemplate().update(jdbcSql, new MapSqlParameterSource(param.getParamMap()), keyHolder);
            List<Map<String, Object>> keyList = keyHolder.getKeyList();
            for (int i = 0; i < keyList.size(); i++) {
                BeanUtil.setFieldValue(param.getInsertList().get(i), param.getTableMapper().getPrimaryKeyField(), Objects.requireNonNull(keyList.get(i).values().iterator().next()));
            }
        } else {
            insertCount = JdbcConnection.getJdbcTemplate().update(jdbcSql, param.getParamMap());
        }
        return insertCount;
    }

    @Override
    public List<T> findAll() {
        FastDaoParam<T> param = FastDaoParam.get();
        return JdbcConnection.getJdbcTemplate().query(param.getSql(), param.getParamMap(), JdbcRowMapper.init(param));
    }

    @Override
    public Integer findCount() {
        FastDaoParam<T> param = FastDaoParam.get();
        try {
            return JdbcConnection.getJdbcTemplate().queryForObject(param.getSql(), param.getParamMap(), Integer.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer update() {
        FastDaoParam<T> param = FastDaoParam.get();
        return JdbcConnection.getJdbcTemplate().update(param.getSql(), param.getParamMap());
    }

    @Override
    public Integer delete() {
        FastDaoParam<T> param = FastDaoParam.get();
        return JdbcConnection.getJdbcTemplate().update( param.getSql(), param.getParamMap());
    }

}
