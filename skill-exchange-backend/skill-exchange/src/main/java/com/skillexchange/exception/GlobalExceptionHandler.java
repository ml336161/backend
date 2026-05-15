package com.skillexchange.exception;

import com.skillexchange.common.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 1. 导入 SQL 异常类
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        return Result.error(400, message);
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数绑定失败";
        return Result.error(400, message);
    }

    // 2. 新增：处理数据库唯一键冲突（如邮箱重复、用户名重复）
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<Void> handleDuplicateKeyException(SQLIntegrityConstraintViolationException e) {
        String message = e.getMessage();

        // 根据报错信息判断是哪个字段重复了
        if (message.contains("uk_email")) {
            return Result.error("该邮箱已被注册，请直接登录");
        }
        if (message.contains("uk_username")) {
            return Result.error("该用户名已被占用");
        }

        // 其他唯一键冲突
        return Result.error("数据重复，请检查输入信息");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("系统异常：" + e.getMessage());
    }
}