package com.simpleweather.android.view.horizon_view;

public interface ScrollWatched {

    void addWatcher(ScrollWatcher watcher);

    void removeWatcher(ScrollWatcher watcher);

    void notifyWatcher(int x);

}
