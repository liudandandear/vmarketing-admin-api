package com.vmarketing.core.util;

import org.apache.shiro.SecurityUtils;

import com.vmarketing.core.shiro.AccountProfile;

public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
