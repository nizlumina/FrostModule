/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Nizlumina Studio (Malaysia)
 *
 * Unless specified, permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.nizlumina.frostmodule.common;

import java.io.File;

/**
 * A Config class to be use by {@link TorrentEngine}.
 * This class is initialized in a Service (and utilize SharedPrefs) and passed as parameter for {@link TorrentEngine} initialization.
 * <p/>
 * Use a {@link Builder} for building it.
 */
public final class EngineConfig
{
    private int connectionCountLimit;
    private int uploadBandwithLimit;
    private int downloadBandwithLimit;
    private int maxRunningUploads;
    private int maxRunningDownloads;
    private int maxRunningTorrent;
    private File downloadDirectory;
    private File metafileDirectory;
    private File privateDirectory;
    private int port;

    public EngineConfig(int connectionCountLimit, int uploadBandwithLimit, int downloadBandwithLimit, int maxRunningUploads, int maxRunningDownloads, int maxRunningTorrent, File downloadDirectory, File metafileDirectory, File privateDirectory, int port)
    {
        this.connectionCountLimit = connectionCountLimit;
        this.uploadBandwithLimit = uploadBandwithLimit;
        this.downloadBandwithLimit = downloadBandwithLimit;
        this.maxRunningUploads = maxRunningUploads;
        this.maxRunningDownloads = maxRunningDownloads;
        this.maxRunningTorrent = maxRunningTorrent;
        this.downloadDirectory = downloadDirectory;
        this.metafileDirectory = metafileDirectory;
        this.privateDirectory = privateDirectory;
        this.port = port;
    }

    public EngineConfig() {}

    public File getPrivateDirectory()
    {
        return privateDirectory;
    }

    public File getMetafileDirectory()
    {
        return metafileDirectory;
    }

    public int getConnectionCountLimit()
    {
        return connectionCountLimit;
    }

    public int getUploadBandwithLimit()
    {
        return uploadBandwithLimit;
    }

    public int getDownloadBandwithLimit()
    {
        return downloadBandwithLimit;
    }

    public int getMaxRunningUploads()
    {
        return maxRunningUploads;
    }

    public int getMaxRunningDownloads()
    {
        return maxRunningDownloads;
    }

    public int getMaxRunningTorrent()
    {
        return maxRunningTorrent;
    }

    public File getDownloadDirectory()
    {
        return downloadDirectory;
    }

    public int getPort()
    {
        return port;
    }

    public static class Builder
    {
        private int connectionCountLimit;
        private int uploadBandwithLimit;
        private int downloadBandwithLimit;
        private int maxRunningUploads;
        private int maxRunningDownloads;
        private int maxRunningTorrent;
        private File saveDirectory;
        private int port;
        private File metafileDirectory;
        private File privateDirectory;

        public Builder setPrivateDirectory(File privateDirectory)
        {
            this.privateDirectory = privateDirectory;
            return this;
        }

        public Builder setConnectionCountLimit(int connectionCountLimit)
        {
            this.connectionCountLimit = connectionCountLimit;
            return this;
        }

        public Builder setUploadBandwithLimit(int uploadBandwithLimit)
        {
            this.uploadBandwithLimit = uploadBandwithLimit;
            return this;
        }

        public Builder setDownloadBandwithLimit(int downloadBandwithLimit)
        {
            this.downloadBandwithLimit = downloadBandwithLimit;
            return this;
        }

        public Builder setMaxRunningUploads(int maxRunningUploads)
        {
            this.maxRunningUploads = maxRunningUploads;
            return this;
        }

        public Builder setMaxRunningDownloads(int maxRunningDownloads)
        {
            this.maxRunningDownloads = maxRunningDownloads;
            return this;
        }

        public Builder setMaxRunningTorrent(int maxRunningTorrent)
        {
            this.maxRunningTorrent = maxRunningTorrent;
            return this;
        }

        public Builder setSaveDirectory(File saveDirectory)
        {
            this.saveDirectory = saveDirectory;
            return this;
        }

        public Builder setPort(int port)
        {
            this.port = port;
            return this;
        }

        public Builder setMetafileDirectory(File metafileDirectory)
        {
            this.metafileDirectory = metafileDirectory;
            return this;
        }

        public EngineConfig build()
        {
            return new EngineConfig(connectionCountLimit, uploadBandwithLimit, downloadBandwithLimit, maxRunningUploads, maxRunningDownloads, maxRunningTorrent, saveDirectory, metafileDirectory, privateDirectory, port);
        }
    }
}
