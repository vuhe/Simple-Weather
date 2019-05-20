package com.simpleweather.android.View.horizon_view;

public interface ScrollWatched {

    void addWatcher(ScrollWatcher watcher);

    void removeWatcher(ScrollWatcher watcher);

    void notifyWatcher(int x);

}
