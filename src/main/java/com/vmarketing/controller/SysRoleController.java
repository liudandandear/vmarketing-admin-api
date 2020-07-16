package com.vmarketing.controller;


import com.vmarketing.core.api.Result;
import com.vmarketing.core.api.ResultCode;
import com.vmarketing.core.constant.JwtConstant;
import com.vmarketing.core.util.JwtUtil;
import com.vmarketing.entity.SysRole;
import com.vmarketing.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author liudandan
 * @since 2020-07-16
 */
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;

    // 获取角色列表
    @RequiresAuthentication
    @GetMapping("list")
    public Result list(@RequestParam(name = "token", required = true) String token) {
        SysRole sysRole = new SysRole();
        try {
            String username = JwtUtil.getClaim(token, JwtConstant.ACCOUNT_KEY);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new Result(ResultCode.SUCCESS, "success", sysRole);
    }
}
