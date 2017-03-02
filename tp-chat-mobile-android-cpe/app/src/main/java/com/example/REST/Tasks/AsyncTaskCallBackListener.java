package com.example.REST.Tasks;

// Implement a callback mecanism on task start and complete to execute
// UI actions on main thread when executing an async task using this listener
public interface AsyncTaskCallBackListener<T> {

    void onTaskStart();

    void onTaskComplete(T result);
}
