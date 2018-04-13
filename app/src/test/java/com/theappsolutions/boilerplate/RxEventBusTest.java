package com.theappsolutions.boilerplate;


import com.theappsolutions.boilerplate.other.RxEventBus;
import com.theappsolutions.boilerplate.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.subscribers.TestSubscriber;

public class RxEventBusTest {

    @Rule
    // Must be added to every test class that targets app code that uses RxJava
    public final RxSchedulersOverrideRule mOverrideSchedulersRule =
        new RxSchedulersOverrideRule();
    private RxEventBus mEventBus;

    @Before
    public void setUp() {
        mEventBus = new RxEventBus();
    }

    @Test
    public void postedObjectsAreReceived() {
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        mEventBus.observable().subscribe(testSubscriber);

        Object event1 = new Object();
        Object event2 = new Object();
        mEventBus.post(event1);
        mEventBus.post(event2);

        testSubscriber.assertValues(event1, event2);
    }

    @Test
    public void filteredObservableOnlyReceivesSomeObjects() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        mEventBus.filteredObservable(String.class).subscribe(testSubscriber);

        String stringEvent = "Event";
        Integer intEvent = 20;
        mEventBus.post(stringEvent);
        mEventBus.post(intEvent);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(stringEvent);
    }
}