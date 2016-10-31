package com.example.ivyli.security.presenters;


public abstract class BasePresenter<T> {
    T mTarget;

    public void takeTarget(T target) {
        mTarget = target;
    }

    public void dropTarget() {
        if (mTarget != null) {
            mTarget = null;
        }
    }

    public T getTarget() {
        return mTarget;
    }

}
