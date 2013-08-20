package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.junit.Test;

public class PutGetDeleteTest extends TempContainerTest
{
    @Test
    public void putGetDelete() throws IOException
    {
        BlobStoreContext context = TestUtil.createBlobStoreContext();
        try
        {
            BlobStore blobStore = context.getBlobStore();
            String blobName = TestUtil.randomBlobName();

            putBlob(blobStore, container, blobName);

            final InputStream data = getClass().getResourceAsStream("small.txt");
            try
            {
                assertBlobContentEquals(blobStore, blobName, data);
            }
            finally
            {
                data.close();
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
}
