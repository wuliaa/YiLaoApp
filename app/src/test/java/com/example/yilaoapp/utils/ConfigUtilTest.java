package com.example.yilaoapp.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigUtilTest {

    @Test
    public void isPhoneNum() {
        assertTrue(ConfigUtil.isPhoneNum("18825133593"));
        assertTrue(ConfigUtil.isPhoneNum("13412101248"));
        assertFalse(ConfigUtil.isPhoneNum("01234567899"));
        assertFalse(ConfigUtil.isPhoneNum("12345678910"));
        assertFalse(ConfigUtil.isPhoneNum("11227643210"));
    }

 
}