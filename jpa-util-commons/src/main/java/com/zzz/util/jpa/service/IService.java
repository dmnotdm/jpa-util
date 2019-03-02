package com.zzz.util.jpa.service;

import com.zzz.util.jpa.entity.JpaEntity;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhizhao.zhang on 2018/11/14 14:44.
 * Description: ID => 对象主键，T => 对象class
 */
public interface IService<ID extends Serializable, T extends JpaEntity<ID>> {

    /**
     * 成功时返回记录的对象，
     * 创建失败时返回null，
     * 当不允许创建是抛出runtime exception
     */
    public T createNew(T object);

    /**
     * 更新对象，
     * 当对象的主键为null时抛出PrimaryKeyLoseException
     */
    public T update(T source);

    /**
     * 根据example查询记录，并根据source进行更新，忽略source中的null
     * 当无法根据example查询到记录时，返回null
     */
    public List<T> updateByExample(T example, T source);

    /**
     * 根据主键查询
     */
    public T findOne(ID id);

    /**
     * 根据example进行分页查询,
     * example为null时，仅进行分页
     */
    public List<T> findByExample(T example, Pageable pageable);

    /**
     * 查询所有满足example的记录
     */
    public List<T> findByExample(T example);
}
