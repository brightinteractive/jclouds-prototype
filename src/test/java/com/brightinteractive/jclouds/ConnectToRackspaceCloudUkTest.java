package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.rackspace.cloudfiles.CloudFilesUKProviderMetadata;
import org.junit.Test;

public class ConnectToRackspaceCloudUkTest
{
    @Test
    public void connectToRackspaceCloudUk()
    {
        CloudFilesUKProviderMetadata providerMetadata = CloudFilesUKProviderMetadata.builder().build();
        final String identity = System.getProperty("com.brightinteractive.jclouds.rscloud.identity");
        final String credential = System.getProperty("com.brightinteractive.jclouds.rscloud.credential");
        BlobStoreContext context = ContextBuilder.newBuilder(providerMetadata).credentials(identity, credential)
                 .buildView(BlobStoreContext.class);
        try
        {
            BlobStore blobStore = context.getBlobStore();
            final String container = "auto-test";
            blobStore.countBlobs(container);
        }
        finally
        {
            context.close();
        }
    }
}
