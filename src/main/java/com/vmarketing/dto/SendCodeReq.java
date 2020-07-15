package com.vmarketing.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.vmarketing.core.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;

import com.vmarketing.core.db.RedisClient;

import lombok.Data;

//发送验证码参数校验类
@Data
public class SendCodeReq implements Serializable {

    @Autowired
    RedisClient redis;

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    private int code;

}
