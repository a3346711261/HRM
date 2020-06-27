package com.ihrm.common.exception;

/**
 * 全局异常处理
 */
import com.ihrm.common.entity.ResultCode;
import lombok.Getter;
@Getter
public class CommonException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private ResultCode code = ResultCode.SERVER_ERROR;
        public CommonException(){}
        public CommonException(ResultCode resultCode) {
            super(resultCode.message());
            this.code = resultCode;
     }
}