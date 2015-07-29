package com.nizlumina.frostmodule;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.nizlumina.frostmodule.common.EngineConfig;
import com.nizlumina.frostmodule.common.TorrentEngine;
import com.nizlumina.frostmodule.common.TorrentObject;

public class TorrentService extends Service
{
    public TorrentService()
    {
        mTorrentEngine = new FrostwireEngine(); //Easy to change implementation later.
        mServiceBinder = new TorrentServiceBinder(mTorrentEngine); //A binder delegate to directly access the engine interface
    }

    private static final String ACTION_STARTSERVICE = TorrentService.class.getSimpleName() + "$START_SERVICE";
    private static final String STR_EXTRA_METAFILEPATH = "METAFILEPATH";
    private final TorrentEngine mTorrentEngine;
    private final IBinder mServiceBinder;

    /**
     * A helper to start the service
     * @param context
     */
    public static void startService(Context context)
    {
        context.startService(new Intent(context, TorrentService.class).setAction(ACTION_STARTSERVICE));
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        log("OC");
        initEngine();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        log("OSM");
        handleIntent(intent);
        return Service.START_STICKY;
    }

    private void handleIntent(Intent intent)
    {
        if (intent.getAction().equals(ACTION_STARTSERVICE))
        {
            String path = intent.getStringExtra(STR_EXTRA_METAFILEPATH);
            if (path != null){
                TorrentObject torrentObject = new TorrentObject().setMetafilePath(path);
                mTorrentEngine.addTorrent(torrentObject);
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mTorrentEngine.stopEngine();
        log("OD");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        log("OB");
        return mServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        log("UB");
        if (!mTorrentEngine.isAnyTorrentDownloading())
            stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent)
    {
        super.onRebind(intent);
        log("RB");
    }

    private void log(String s)
    {
        Log.v(getClass().getSimpleName(), s);
    }

    private void initEngine()
    {

        final EngineConfig engineConfig = new EngineConfig.Builder()
                .setMetafileDirectory(getCacheDir())
                .setSaveDirectory(getFilesDir())
                .setPort(6868)
                .build();

        //Concrete implementations. Enums for choosing engine might be implemented later as well.
        mTorrentEngine.initializeEngine(engineConfig);
        mTorrentEngine.setOnNoMoreRunningTaskListener(new Runnable()
        {
            @Override
            public void run()
            {
                stopSelf();
            }
        });

        mTorrentEngine.setOnEngineStartedListener(new Runnable()
        {
            @Override
            public void run()
            {
                if (!mTorrentEngine.isAnyTorrentDownloading())
                    stopSelf();
            }
        });
        //Start as soon as possible
        mTorrentEngine.startEngine();
    }

    private static Intent makeServiceIntent(Context context)
    {
        return new Intent(context.getApplicationContext(), TorrentService.class).setAction("START_SERVICE");
    }

    public static void addNewTorrent(Context context, String localMetafilePath){
        context.startService(makeServiceIntent(context).putExtra(STR_EXTRA_METAFILEPATH, localMetafilePath));
    }

    /**
     * A Binder class following Android docs
     */
    public static class TorrentServiceBinder extends Binder
    {
        private final TorrentEngine torrentEngine;

        public TorrentServiceBinder(TorrentEngine torrentEngine)
        {
            this.torrentEngine = torrentEngine;
        }

        public TorrentEngine getTorrentEngine()
        {
            return torrentEngine;
        }

    }
}
