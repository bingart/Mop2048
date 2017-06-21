package com.mopinfo.mop2048.util;

import com.mopinfo.lib.util.UserAgentHelper;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by feisun on 2017/6/19.
 */
public class UserAgentHelperTest {

    @Test
    public void testGetUserAgent() throws Exception {
        String uid = "1122334455";
        String userAgent = UserAgentHelper.getUserAgent(uid);
        Assert.assertNotNull(userAgent);

        uid = "1122334450";
        String userAgent2 = UserAgentHelper.getUserAgent(uid);
        Assert.assertNotNull(userAgent2);
        Assert.assertFalse(userAgent.equalsIgnoreCase(userAgent2));
    }
}