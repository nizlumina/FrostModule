package com.nizlumina.frostmodule;

import com.frostwire.bittorrent.BTContext;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Fingerprint;
import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.nizlumina.frostmodule.common.EngineConfig;
import com.nizlumina.frostmodule.common.TorrentEngine;
import com.nizlumina.frostmodule.common.TorrentObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In a sense this is a functionality copy of FrostWire BTEngine but aimed towards decoupling those to conform a generic {@link TorrentEngine} interface instead.
 * <p/>
 * Moving forward, we will slowly remove dependency on Frostwire BTEngine (and its common packages) and become into its own implementation ala Glide (which started as Picasso fork).
 * Also, this is my humble attempt at making sense of how Frostwire implemented theirs. All help and fixes are very much appreciated.
 * <p/>
 * Also for the uninitiated, management of torrents is handled by {@link Session} inside the actual libtorrent library.
 * Checkout <a href="http://www.libtorrent.org/manual-ref.html">the original libtorrent manual</a> to see how to utilize it
 * (which also did well to explain what's underneath in general).
 */
public class FrostwireEngine implements TorrentEngine
{

    private EngineConfig mEngineConfig;
    //private ArrayList<String> mTorrentIds;
    private DHT mDht;
    private ConcurrentHashMap<String, TorrentHandle> mTorrentMap;

    private ExecutorService mSessionUpdateExecutor;
    private Listener mEngineListener;

    /**
     * {@inheritDoc}
     * <p/>
     * This implentation process the torrents addition asynchronously. Any id used by the passed {@link TorrentObject} will be overwritten by the internal engine.
     * <p/>
     * You can retrieve the ids via
     *
     * @param torrentObjects TorrentObject(s) created from any valid source (metafile/magnet/etc).
     */
    @Override
    public void addTorrent(final TorrentObject... torrentObjects)
    {
        mSessionUpdateExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                for (TorrentObject torrentObject : torrentObjects)
                {
                    if (torrentObject != null)
                    {
                        final TorrentHandle torrentHandle = BTEngine.getInstance().getSession().addTorrent(torrentObject.getMetafile(), mEngineConfig.getDownloadDirectory());
                        mTorrentMap.put(torrentHandle.getInfoHash().toString(), torrentHandle); //id = infohash
                    }
                }
            }
        });
    }

    @Override
    public void resumeTorrent(final String... ids)
    {
        mSessionUpdateExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                for (String id : ids)
                {
                    if (id != null)
                    {

                    }
                }
            }
        });
    }

    @Override
    public void pauseTorrent(final String... ids)
    {
        mSessionUpdateExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                for (String id : ids)
                {
                    if (id != null)
                    {

                    }
                }
            }
        });
    }

    @Override
    public void removeTorrent(final String... ids)
    {

    }

    /**
     * This use libtorrent own fast resume method for torrents that was paused.
     */
    private void libtorrentFastResume()
    {

    }

    @Override
    public List<String> getTorrentIds()
    {
        return null;
    }

    @Override
    public boolean isAnyTorrentDownloading()
    {
        return false;
    }

    @Override
    public String getTorrentName(String id)
    {
        return null;
    }

    @Override
    public String getTorrentDetails(String id)
    {
        return null;
    }

    @Override
    public void setTorrentListener(String id, TorrentObject.TorrentListener listener)
    {

    }

    @Override
    public void removeTorrentListener(String id)
    {

    }

    @Override
    public void setOnNoMoreRunningTaskListener(Runnable listener)
    {

    }

    @Override
    public void setOnEngineStartedListener(Runnable listener)
    {

    }

    @Override
    public void startEngine()
    {
        BTEngine btEngine = BTEngine.getInstance();
        btEngine.start();

        mTorrentMap = new ConcurrentHashMap<>(32, 0.9f, 2); //Using 2 for concurrency level for add and remove
        mSessionUpdateExecutor = Executors.newFixedThreadPool(2);
        final List<TorrentHandle> torrentHandles = btEngine.getSession().getTorrents();
        mSessionUpdateExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                for (TorrentHandle torrentHandle : torrentHandles)
                {
                    torrentHandle.getInfoHash().toString();
                }
            }
        });

        //Turn on DHT by default.
        mDht = new DHT(btEngine.getSession());
        mDht.start();
    }

    @Override
    public void stopEngine()
    {
        mTorrentMap.clear();
        mDht.stop();
        BTEngine.getInstance().stop();
    }

    @Override
    public void initializeEngine(EngineConfig engineConfig)
    {
        mEngineConfig = engineConfig;
        //This whole block is almost a carbon copy of Frostwire Android implementation
        BTEngine.ctx = new BTContext();
        BTEngine btEngine = BTEngine.getInstance();
        btEngine.reloadBTContext(
                engineConfig.getMetafileDirectory(),
                engineConfig.getDownloadDirectory(),
                new File(engineConfig.getPrivateDirectory(), "libtorrent"),
                engineConfig.getPort(), //Currently, even Frostwire actual JNI wrapper (jlibtorrent) doesn't even use it but we just provide it just in case. Yeah, total wtf there.
                engineConfig.getPort(), //Yeap, same as above.
                "0.0.0.0", //...and same as above. And it still works for downloading actual torrent. What is this sorcery!?
                false,
                false);

        BTEngine.ctx.optimizeMemory = true;

        //Our own implementation

    }

    @Override
    public void setEngineListener(Listener engineListener)
    {
        mEngineListener = engineListener;
    }

    // Even though Session itself is the "engine" (especially if you read libtorrent docs), encapsulating Session from the
    // TorrentEngine implementation help us to be insulated from any breaking changes in the official jlibtorrent lib.
    private static class SessionManager
    {
        public Session getLibtorrentSession()
        {
            return mLibtorrentSession;
        }

        private Session mLibtorrentSession;
        private final ReentrantLock sessionStartLocker = new ReentrantLock();
        private File mSavedState;

        private void start(EngineConfig engineConfig)
        {
            //Albeit almost a direct copy for Frostwire BTEngine, we assume future Session class may have throwable errors via JNI/inside jlibtorrent hence the same try-finally block
            sessionStartLocker.lock();
            try
            {
                mLibtorrentSession = new Session(
                        new Fingerprint(),
                        new Pair<>(engineConfig.getPort(), engineConfig.getPort()),
                        "0.0.0.0");
            }
            finally
            {
                sessionStartLocker.unlock();
            }
        }

        private void resumeSession()
        {

        }

        private void loadSavedState()
        {
            getLibtorrentSession().loadState();
        }

        private void loadSettings(Session session)
        {

        }

    }


}
