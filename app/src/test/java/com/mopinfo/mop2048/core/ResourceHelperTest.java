package com.mopinfo.mop2048.core;

import com.mopinfo.lib.json.resource.UrlResponse;
import com.mopinfo.lib.logic.ResourceHelper;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ResourceHelperTest {

    @Test
    public void testGetResource() throws Exception {
        String host = "http://localhost:8000/service/resource.php";
        UrlResponse rsp = ResourceHelper.getResource(host, "U");
        Assert.assertNotNull(rsp);
        Assert.assertNotNull(rsp.getUrlItemList());
        Assert.assertTrue(rsp.getUrlItemList().size() > 0);
    }
}