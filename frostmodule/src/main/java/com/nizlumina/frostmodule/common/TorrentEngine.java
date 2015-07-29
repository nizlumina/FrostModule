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

import java.util.List;

/**
 * An interface for implementating a generic TorrentEngine.
 * Each method is called from the main thread hence concrete implementation
 * must delegate method calls into its own thread managers.
 */
public interface TorrentEngine
{
    /**
     * Called after the framework fully received a raw metafile/magnet/etc. Callers must set the TorrentObject path to the metafile.
     * Id can also be optionally set.
     * <p />
     * For the concrete implementers, the TorrentObject must have an id (check for existing id/null before setting one) to sync the known shared ids with the the callers.
     * <p />
     * In a sense, this method hands off the raw source to the engine and expect its implementation to fully initialize it.
     * See also {@link TorrentObject} for the basic provided properties.
     * @param torrentObjects TorrentObject(s) created from any valid source (metafile/magnet/etc).
     */
    void addTorrent(TorrentObject... torrentObjects);

    /**
     * Resume torrents of the given ids.
     */
    void resumeTorrent(String... ids);

    /**
     * Pause torrents with the given ids.
     */
    void pauseTorrent(String... ids);

    /**
     * Remove torrents with the given ids
     */
    void removeTorrent(String... ids);

    /**
     * Get the ids as represented by the Engine. Implementation must return exactly as the items are ordered.
     */
    List<String> getTorrentIds();

    boolean isAnyTorrentDownloading(); //reconsidering neccessity of this method

    String getTorrentName(String id);

    String getTorrentDetails(String id);

    /**
     * Set the listener for the torrent. This is intended for UI adapters where each torrent in a list will be given a corresponding listener.
     * @param id The torrent id to be passes to the engine for retrieval of its corresponding torrent.
     * @param listener The listener to be set on the given torrent.
     */
    void setTorrentListener(String id, TorrentObject.TorrentListener listener);

    void setOnNoMoreRunningTaskListener(Runnable listener);

    void setOnEngineStartedListener(Runnable listener);

    /**
     * Starts the engine.
     */
    void startEngine();

    /**
     * Fully stops the engine. There's no actual pausing provided.
     * If all torrents are paused, the engine (and most likely the service) is still running.
     *
     * Shutting down the service via the normal UI interaction/IPC will also fire this method.
     */
    void stopEngine();

    /**
     * Initialize engine with the given config. This is called before {@link #startEngine}.
     * @param engineConfig The config passed as parameter.
     */
    void initializeEngine(EngineConfig engineConfig);

    void setEngineListener(Listener engineListener);

    interface Listener
    {
        void onTorrentAdded(String torrentId);
        void onEngineStarted();
        void onNoMoreTaskRunning();
    }

}
