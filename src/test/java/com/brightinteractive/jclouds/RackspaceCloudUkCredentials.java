package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

public class RackspaceCloudUkCredentials
{
    public static String getIdentity()
    {
        return System.getProperty("com.brightinteractive.jclouds.rscloud.identity");
    }

    public static String getCredential()
    {
        return System.getProperty("com.brightinteractive.jclouds.rscloud.credential");
    }
}
