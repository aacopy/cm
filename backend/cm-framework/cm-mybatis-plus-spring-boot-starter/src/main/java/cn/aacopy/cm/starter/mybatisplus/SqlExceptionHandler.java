package cn.aacopy.cm.starter.mybatisplus;

import cn.aacopy.cm.starter.web.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.sql.SQLException;

/**
 * @author cmyang
 * @date 2026/2/17
 */
@RestControllerAdvice
@Slf4j
public class SqlExceptionHandler {

    @ExceptionHandler({SQLException.class, DataAccessException.class, DuplicateKeyException.class})
    public Result<?> handleSQLException(SQLException e) {
        log.error(e.getMessage(), e);
        return Result.failure(2001, "system error");
    }
}
