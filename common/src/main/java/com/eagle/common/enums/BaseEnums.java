package com.eagle.common.enums;

import com.eagle.common.exception.BasicException;

/**
 * some custom exceptions or Enums can implements this to simply throw or get
 * example:{@link BasicException} construct method with MsgInterface parameter
 */
public interface BaseEnums<T> {
    /**
     * status code;support integer and string
     */
    T getCode();

    /**
     * message or description
     */
    String getMessage();
}
