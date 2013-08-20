package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import java.util.UUID;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.rackspace.cloudfiles.CloudFilesUKProviderMetadata;

public class TestUtil
{
    public static String randomContainerName()
    {
        return randomName();
    }

    public static String randomBlobName()
    {
        return randomName();
    }

    private static String randomName()
    {
        return "autotest-" + randomUUID();
    }

    private static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    public static BlobStoreContext createBlobStoreContext()
    {
        return startBuildingWithCredentials()
            .buildView(BlobStoreContext.class);
    }

    public static ContextBuilder startBuildingWithCredentials()
    {
        CloudFilesUKProviderMetadata providerMetadata = CloudFilesUKProviderMetadata.builder().build();
        final String identity = RackspaceCloudUkCredentials.getIdentity();
        final String credential = RackspaceCloudUkCredentials.getCredential();
        return ContextBuilder.newBuilder(providerMetadata).credentials(identity, credential);
    }
}
