package com.csc.spring.common.enums;

/**
 * some custom exceptions or Enums can implements this to simply throw or get
 * example:{@link com.csc.spring.common.exception.BasicException} construct method with MsgInterface parameter
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
