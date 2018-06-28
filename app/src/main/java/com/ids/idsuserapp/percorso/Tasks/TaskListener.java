package com.ids.idsuserapp.percorso.Tasks;

public interface TaskListener<Result> {
    String TAG = TaskListener.class.getName();

    void onTaskSuccess(Result result);

    void onTaskError(Exception e);

    void onTaskComplete();

    void onTaskCancelled();
}
