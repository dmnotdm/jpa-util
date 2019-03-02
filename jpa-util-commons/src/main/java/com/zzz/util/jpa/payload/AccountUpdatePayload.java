package com.zzz.util.jpa.payload;

import lombok.Data;

/**
 * Created by zhizhao.zhang on 2018/12/20 16:46.
 * Description:
 */
@Data
public class AccountUpdatePayload<T> {
    T example;
    T source;
}
