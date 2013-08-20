package com.brightinteractive.jclouds;

/*
 * Copyright 2013 Bright Interactive, All Rights Reserved.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PseudoRandomInputStream extends InputStream
{
    long bytesLeft;
    final Random random;
    byte[] singleByteBuffer = new byte[1];

    public PseudoRandomInputStream(long length, long seed)
    {
        bytesLeft = length;
        random = new Random(seed);
    }

    @Override
    public int read() throws IOException
    {
        if (bytesLeft <= 0)
        {
            return -1;
        }
        else
        {
            bytesLeft--;
            random.nextBytes(singleByteBuffer);
            return singleByteBuffer[0];
        }
    }
}
