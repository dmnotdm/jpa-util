package com.zzz.util.jpa.service;

import com.alibaba.fastjson.JSON;
import com.zzz.util.jpa.entity.JpaEntity;
import com.zzz.util.jpa.exception.PrimaryKeyLoseException;
import com.zzz.util.jpa.util.PropertiesUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhizhao.zhang on 2018/11/14 14:44.
 * Description: ID => 对象主键，T => 对象class
 */
@Slf4j
public abstract class MongoService<ID extends Serializable, T extends JpaEntity<ID>> implements IService<ID, T> {

    @Autowired
    protected MongoRepository<T, ID> repository;

    /**
     * 更新对象，
     * 当对象的主键为null时抛出PrimaryKeyLoseException
     */
    @Override
    public T update(T source) {
        ID id = source.primaryKey();
        if (id == null) {
            throw new PrimaryKeyLoseException("primary key not allow null");
        }
        Optional<T> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return null;
        }

        PropertiesUtils.copyLocalPropertiesWithTarget(optional.get(), source, PropertiesUtils.CopyRule.NULL);
        source.setUpdateTime(new Date());
        source = repository.save(source);
        log.info("update {} success, used id:{}", source.getClass().getName(), JSON.toJSONString(id));
        return source;
    }

    /**
     * 根据example查询记录，并根据source进行更新，忽略source中的null
     * 当无法根据example查询到记录时，返回null
     */
    @Override
    public List<T> updateByExample(T example, T source) {
        List<T> all = repository.findAll(Example.of(example));
        if (all.isEmpty()) {
            log.info("update {} failed, not found with example :: {}", example.getClass().getName(), JSON.toJSONString(example));
            return all;
        }

        Date date = new Date();
        for (T ac : all) {
            PropertiesUtils.copyLocalPropertiesWithSource(source, ac, PropertiesUtils.CopyRule.NOT_NULL);
            ac.setUpdateTime(date);
        }

        all = repository.saveAll(all);
        return all;
    }

    /**
     * 根据主键查询
     */
    @Override
    public T findOne(ID id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * 根据example进行分页查询,
     * example为null时，仅进行分页
     */
    @Override
    public List<T> findByExample(T example, Pageable pageable) {
        return (example == null ? repository.findAll(pageable) : repository.findAll(Example.of(example), pageable)).getContent();
    }

    /**
     * 查询所有满足example的记录
     */
    @Override
    public List<T> findByExample(T example) {
        if (example == null) {
            long start = System.currentTimeMillis();
            List<T> all = repository.findAll();
            long end = System.currentTimeMillis();
            log.warn("[{}] find all spend time :: {}, result lines :: {}", this.getClass().getSimpleName(), end - start, all.size());
            return all;
        }
        return repository.findAll(Example.of(example));
    }
}
