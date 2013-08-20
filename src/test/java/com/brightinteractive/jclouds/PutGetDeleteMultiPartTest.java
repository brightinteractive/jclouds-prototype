package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.brightinteractive.common.lang.ClassUtil;
import org.apache.log4j.Logger;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.io.Payload;
import org.jclouds.io.payloads.InputStreamPayload;
import org.jclouds.openstack.swift.blobstore.strategy.MultipartUpload;
import org.junit.Test;

public class PutGetDeleteMultiPartTest extends TempContainerTest
{
    private static final Logger log = Logger.getLogger(ClassUtil.currentClassName());

    /**
	 * Use the minimum part size to minimise the file size that we have to upload to get a given number of chunks and
	 * thereby make the test run faster
	 */
    private static final long PART_SIZE = MultipartUpload.MIN_PART_SIZE;

    private static final String TEST_FILE = "boring.dat";

    private final long testFileLength;

    public PutGetDeleteMultiPartTest() throws IOException
    {
        testFileLength = testFileLength();
    }

    @Test
    public void putGetDelete() throws IOException
    {
        Properties overrides = new Properties();
        // This property controls the number of parts being uploaded in parallel, the default is 4
        // overrides.setProperty("jclouds.mpu.parallel.degree", "5");

        // This property controls the size (in bytes) of parts being uploaded in parallel, the default is 33554432 bytes = 32 MB
        overrides.setProperty("jclouds.mpu.parts.size", String.valueOf(PART_SIZE));

        BlobStoreContext context = TestUtil.startBuildingWithCredentials()
            .overrides(overrides)
            .buildView(BlobStoreContext.class);
        try
        {
            BlobStore blobStore = context.getBlobStore();
            String blobName = TestUtil.randomBlobName();

            assertEquals(0, blobStore.countBlobs(container));

            putMultiPartBlob(blobStore, container, blobName);

            assertTrue("There should be more than one blob in the container because it should have been split up into " +
                       numPartsExpected() + " parts",
                       blobStore.countBlobs(container) > 1);

            final InputStream data = openTestFile();
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

            assertEquals("No blobs should be left behind after the main blob has been deleted",
                         0, blobStore.countBlobs(container));
        }
        finally
        {
            context.close();
        }
    }

    private long numPartsExpected() throws IOException
    {
        long contentLength = testFileLength;
        long parts = contentLength / PART_SIZE;
        if (parts * PART_SIZE < contentLength)
        {
            parts += 1;
        }
        return parts;
    }

    private InputStream openTestFile()
    {
        return getClass().getResourceAsStream(TEST_FILE);
    }

    private long testFileLength() throws IOException
    {
        final URL testFileUrl = getClass().getResource(TEST_FILE);
        URLConnection conn = testFileUrl.openConnection();
        final long contentLength = conn.getContentLengthLong();
        log.debug("Content length: " + contentLength);
        return contentLength;
    }

    private void putMultiPartBlob(BlobStore blobStore, String container, String blobName) throws IOException
    {
        final InputStream data = openTestFile();
        try
        {
            Payload payload = new InputStreamPayload(data);
            payload.getContentMetadata().setContentLength(testFileLength);

            Blob blob = blobStore
                .blobBuilder(blobName)
                .payload(payload)
                .build();

            blobStore.putBlob(container, blob, PutOptions.Builder.multipart());
        }
        finally
        {
            data.close();
        }
    }
}
