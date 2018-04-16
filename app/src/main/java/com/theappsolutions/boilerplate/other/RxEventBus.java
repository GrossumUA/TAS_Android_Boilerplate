package com.theappsolutions.boilerplate.other;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * A simple event bus built with RxJava
 */
@Singleton
public class RxEventBus {

    private final BackpressureStrategy mBackpressureStrategy = BackpressureStrategy.BUFFER;
    private final PublishSubject<Object> mBusSubject;

    @Inject
    public RxEventBus() {
        mBusSubject = PublishSubject.create();
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Flowable<Object> observable() {
        return mBusSubject.toFlowable(mBackpressureStrategy);
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Flowable<T> filteredObservable(final Class<T> eventClass) {
        return mBusSubject.ofType(eventClass).toFlowable(mBackpressureStrategy);
    }

}
