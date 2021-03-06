package com.zzz.util.jpa.client;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.zzz.util.jpa.entity.JpaEntity;
import com.zzz.util.jpa.exception.PrimaryKeyLoseException;
import com.zzz.util.jpa.payload.AccountUpdatePayload;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhizhao.zhang on 2018/12/20 15:01.
 * Description:
 */
public interface BaseClient<ID extends Serializable, T extends JpaEntity<ID>> {

    /**
     * 成功时返回记录的对象，
     * 创建失败时返回null，
     * 当不允许创建是抛出runtime exception
     */
    @PostMapping(value = "create", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public T create(@RequestBody T source);

    /**
     * 更新对象，
     * 当对象的主键为null时抛出 {@link PrimaryKeyLoseException}
     */
    @PostMapping(value = "update", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public T update(@RequestBody T source);

    /**
     * 根据example查询记录，并根据source进行更新，忽略source中的null
     * 当无法根据example查询到记录时，返回null
     */
    @PostMapping(value = "update-by-example", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<T> updateByExample(@RequestBody AccountUpdatePayload<T> payload);

    /**
     * 根据example进行分页查询,
     * example为null时，仅进行分页
     */
    @PostMapping(value = "find", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<T> find(
        @RequestBody(required = false) T example,
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "size", required = false) Integer size);

    /**
     * 根据主键查询
     */
    @PostMapping(value = "find-one", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public T findOne(@RequestBody ID id);

}
