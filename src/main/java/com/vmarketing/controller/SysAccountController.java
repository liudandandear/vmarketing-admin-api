package com.vmarketing.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.RandomUtil;
import com.vmarketing.core.constant.CacheConstant;
import com.vmarketing.core.sdk.sms.Upyun;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vmarketing.core.api.Result;
import com.vmarketing.core.constant.JwtConstant;
import com.vmarketing.core.constant.RedisConstant;
import com.vmarketing.core.api.ResultCode;
import com.vmarketing.core.db.RedisClient;
import com.vmarketing.core.util.JwtUtil;
import com.vmarketing.dto.LoginReq;
import com.vmarketing.dto.RegisterReq;
import com.vmarketing.dto.SendCodeReq;
import com.vmarketing.entity.SysUser;
import com.vmarketing.service.SysUserService;

import cn.hutool.crypto.SecureUtil;

/**
 * 鉴权控制器
 *
 * @author liudandandear
 */
@RestController
public class SysAccountController {

    @Value("${config.refreshToken-expireTime}")
    private String refreshTokenExpireTime;

    @Autowired
    private RedisClient redis;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录
     *
     * @param loginDto
     * @param response
     * @return
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginReq loginReq, HttpServletResponse response) {
        try {
            String username = loginReq.getUsername();
            String password = loginReq.getPassword();
            SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", username));
            Assert.notNull(sysUser, "账号有误，请重新输入！");
            if (!sysUser.getUsername().equals(username)
                    || !sysUser.getPassword().equals(SecureUtil.md5(password + sysUser.getSalt()))) {
                return new Result(ResultCode.PASSWORD_ERROR, "用户名与密码不匹配！（username or password error.）");
            }

            // 清除可能存在的shiro权限信息缓存
            if (redis.hasKey(RedisConstant.PREFIX_SHIRO_CACHE + username)) {
                redis.del(RedisConstant.PREFIX_SHIRO_CACHE + username);
            }

            // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            redis.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username, currentTimeMillis,
                    Integer.parseInt(refreshTokenExpireTime));

            // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
            String token = JwtUtil.sign(username, currentTimeMillis);
            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            return new Result().OK();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultCode.ERROR, e.getMessage());
        }
    }

    /**
     * 退出
     *
     * @return
     */
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        try {
            String token = "";
            // 获取头部信息
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                if ("Authorization".equalsIgnoreCase(key)) {
                    token = value;
                }
            }
            // 校验tok
            if (StringUtils.isBlank(token)) {
                return new Result(ResultCode.PARAM_ERROR, "token 不能为空");
            }
            String username = JwtUtil.getClaim(token, JwtConstant.ACCOUNT_KEY);
            if (StringUtils.isBlank(username)) {
                return new Result(ResultCode.NOT_LOGIN, "token失效或不正确.");
            }
            // 清除shiro权限信息缓存
            if (redis.hasKey(RedisConstant.PREFIX_SHIRO_CACHE + username)) {
                redis.del(RedisConstant.PREFIX_SHIRO_CACHE + username);
            }
            // 清除RefreshToken
            redis.del(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username);

            return new Result().OK();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ResultCode.ERROR, e.getMessage());
        }
    }

    /**
     * 注册
     *
     * @param loginReq
     * @param response
     * @return
     */
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterReq registerReq, HttpServletResponse response) {
        try {
            String phone = registerReq.getPhone();
            String password = registerReq.getPassword();
            Integer code = registerReq.getCode();
            // 检查账号是否存在
            SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", phone));
            if (sysUser != null) {
                return new Result(ResultCode.ALREADY_EXIST, "账号已存在,请重新输入！");
            }
            // 检查验证码是否正确
            Integer redis_code = (Integer) redis.get(registerReq.getCodeKey(phone));
            if (!redis_code.equals(code)) {
                return new Result(ResultCode.PARAM_ERROR, "验证码不正确！");
            }
            // 检查密码是否规范

            return new Result().OK();
        } catch (Exception e) {
            return new Result(ResultCode.ERROR, e.getMessage());
        }
    }

    /**
     * 发送验证码
     *
     * @param sendCodeReq
     * @return
     */
    @PostMapping("/send_code")
    public Result sendCode(@Validated @RequestBody SendCodeReq sendCodeReq) throws IOException {
        // 写入redis
        String phone = sendCodeReq.getPhone();
        //6位随机数
        String captcha = RandomUtil.randomNumbers(6);
        //请求验证码服务
        int overdue_minute = 2;
        //短信参数：验证+过期时间
        String vars = captcha + "|" + overdue_minute;
        Upyun upyun = new Upyun();
        Boolean status = upyun.send(phone, vars);
        if (status) {
            //存redis（过期时间）
            redis.set(CacheConstant.SYS_PHONE_CODE + phone, captcha, overdue_minute * 60);
            return new Result().OK();
        } else {
            return new Result(ResultCode.PARAM_ERROR, "验证码发送有误！");
        }
    }
}
