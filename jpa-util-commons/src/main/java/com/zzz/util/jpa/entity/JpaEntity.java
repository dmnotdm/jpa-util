package com.zzz.util.jpa.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhizhao.zhang on 2018/12/27 15:54.
 * Description: T:主键类型
 */
public interface JpaEntity<T> extends Serializable {
    /**
     * 返回主键对象
     */
    T primaryKey();

    /**
     * 没有updateTime字段使用空实现
     */
    void setUpdateTime(Date updateTime);
}
