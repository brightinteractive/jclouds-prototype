package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.rackspace.cloudfiles.CloudFilesUKProviderMetadata;
import org.junit.Test;

public class PutGetDeleteTest
{
    @Test
    public void putGetDelete() throws IOException
    {
        CloudFilesUKProviderMetadata providerMetadata = CloudFilesUKProviderMetadata.builder().build();
        final String identity = RackspaceCloudUkCredentials.getIdentity();
        final String credential = RackspaceCloudUkCredentials.getCredential();
        BlobStoreContext context = ContextBuilder.newBuilder(providerMetadata).credentials(identity, credential)
                 .buildView(BlobStoreContext.class);
        try
        {
            BlobStore blobStore = context.getBlobStore();
            final String container = RackspaceCloudUkCredentials.getTestContainerName();
            ensureContainerExists(blobStore, container);
            String blobName = randomBlobName();

            putBlob(blobStore, container, blobName);

            final InputStream gotData = blobStore.getBlob(container, blobName).getPayload().getInput();
            try
            {
                final InputStream data = getClass().getResourceAsStream("small.txt");
                try
                {
                    assertTrue(IOUtils.contentEquals(data, gotData));
                }
                finally
                {
                    data.close();
                }
            }
            finally
            {
                gotData.close();
            }

            assertTrue(blobStore.blobExists(container, blobName));
            blobStore.removeBlob(container, blobName);
            assertFalse(blobStore.blobExists(container, blobName));
        }
        finally
        {
            context.close();
        }
    }

    private void putBlob(BlobStore blobStore, String container, String blobName) throws IOException
    {
        final InputStream data = getClass().getResourceAsStream("small.txt");
        try
        {

            Blob blob = blobStore
                .blobBuilder(blobName)
                .payload(data)
                .build();

            blobStore.putBlob(container, blob);
        }
        finally
        {
            data.close();
        }
    }

    private String randomBlobName()
    {
        return UUID.randomUUID().toString();
    }

    private static void ensureContainerExists(BlobStore blobStore, String container)
    {
        if (!blobStore.containerExists(container))
        {
            blobStore.createContainerInLocation(null, container);
        }
    }
}
