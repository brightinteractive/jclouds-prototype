package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.junit.After;
import org.junit.Before;

public class TempContainerTest
{
    protected String container;

    @Before
    public void createContainer()
    {
        final BlobStoreContext context = TestUtil.createBlobStoreContext();
        try
        {
            container = TestUtil.randomContainerName();
            context.getBlobStore().createContainerInLocation(null, container);
        }
        finally
        {
            context.close();
        }
    }

    @After
    public void deleteContainer()
    {
        final BlobStoreContext context = TestUtil.createBlobStoreContext();
        try
        {
            context.getBlobStore().deleteContainer(container);
        }
        finally
        {
            context.close();
        }
    }

    public void assertBlobContentEquals(BlobStore blobStore, String blobName, InputStream expected) throws IOException
    {
        final InputStream gotData = blobStore.getBlob(container, blobName).getPayload().getInput();
        try
        {
            assertTrue(IOUtils.contentEquals(expected, gotData));
        }
        finally
        {
            gotData.close();
        }
    }
}
