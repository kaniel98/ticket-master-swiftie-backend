package com.oop.ticketmasterswiftiebackend.common;

import com.oop.ticketmasterswiftiebackend.common.constants.CommonError;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommonUtils {
    public static BaseException commonExceptionHandler(Exception e) {
        if (e instanceof BaseException baseException) {
            return (BaseException) e;
        }
        if (e instanceof DataIntegrityViolationException) {
            return new BaseException(CommonError.DATABASE_ERROR_FOREIGN_KEY_CONSTRAINT.getCode(), CommonError.DATABASE_ERROR_FOREIGN_KEY_CONSTRAINT.getBusinessCode(), CommonError.DATABASE_ERROR_FOREIGN_KEY_CONSTRAINT.getDescription());
        }
        if (e instanceof DataAccessException) {
            return new BaseException(CommonError.DATABASE_ERROR.getCode(), CommonError.DATABASE_ERROR.getBusinessCode(), CommonError.DATABASE_ERROR.getDescription());
        }
        return new BaseException(CommonError.INTERNAL_SERVER_ERROR.getCode(), CommonError.INTERNAL_SERVER_ERROR.getBusinessCode(), CommonError.INTERNAL_SERVER_ERROR.getDescription());
    }

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
