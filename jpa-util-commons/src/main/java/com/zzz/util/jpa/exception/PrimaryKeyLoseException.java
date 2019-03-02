package com.zzz.util.jpa.exception;

/**
 * Created by zhizhao.zhang on 2018/12/20 17:07.
 * Description:
 */
public class PrimaryKeyLoseException extends RuntimeException {
    public PrimaryKeyLoseException() {
        super();
    }

    public PrimaryKeyLoseException(String var1) {
        super(var1);
    }
}
