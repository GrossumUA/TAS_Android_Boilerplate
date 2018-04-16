package com.theappsolutions.boilerplate.util.other_utils;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.theappsolutions.boilerplate.other.functions.Action1;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static io.reactivex.Observable.empty;
import static io.reactivex.Observable.just;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Provides sets of methods that help to manipulate with Optional
 */
public class OptUtils {

    public static <T> Consumer<T> silentConsumer() {
        return value -> {
        };
    }

    /**
     * Replaces observable of objects with observable of optionals.
     * Result observable always contains optionals, Optional.empty() if source observable has no items.
     */
    public static <T> Observable<Optional<T>> toOptObs(Observable<T> source) {
        return source.map(Optional::ofNullable)
            .switchIfEmpty(just(Optional.empty()));
    }

    @SafeVarargs
    public static <T> T firstOrNull(T... args) {
        return Stream.of(args).filter(value -> value != null).findFirst().orElse(null);
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return param -> !predicate.test(param);
    }

    /**
     * 'id' method is an identical mapper, used for shortening and self-documentation in cases where
     * constructs of unusual nature appear, like .map(value -> value).
     * <p>
     * With 'id' method .map(value -> value) would look like '.map(OptUtils::id)', which references
     * to 'id' identity math function (operator).
     */
    public static <T> T id(T value) {
        return value;
    }


    public static <T> Observable<T> ofOptional(Optional<T> optional) {
        return optional.map(Observable::just).orElse(Observable.empty());
    }

    public static <T> Maybe<T> ofOptionalMaybe(Optional<T> optional) {
        return optional.map(Maybe::just).orElse(Maybe.empty());
    }

    public static <T> Observable<T> ofNullable(T value) {
        return ofOptional(Optional.ofNullable(value));
    }


    public static <T> Stream<T> filterOptional(Stream<Optional<T>> source) {
        return source.filter(Optional::isPresent).map(Optional::get);
    }

    public static <T> Observable<T> flatten(Optional<Observable<T>> source) {
        if (source.isPresent()) {
            return source.get();
        } else {
            return empty();
        }
    }

    public static <T> Observable<Optional<T>> permute(Optional<Observable<T>> source) {
        if (source.isPresent()) {
            return source.get().map(Optional::ofNullable);
        } else {
            return just(Optional.empty());
        }
    }

    /**
     * This mapper is used as workaround to implement synchronized side effects (doOnNext might be
     * async dependently on the scheduler).
     * <p>
     * usage with idMapper:
     * .map(idMapper(value -> doOnNextFunction.call(value))
     * <p>
     * equivalent (posly async) usage of doOnNext would be:
     * .doOnNext(value -> doOnNextFunction.call(value))
     */
    public static <T> Function<T, T> idMapper(Action1<T> onNext) {
        return value -> {
            onNext.call(value);
            return value;
        };
    }

    public static <T> Optional<T> blockingOptional(Observable<T> observable) {
        return Stream.of(observable.toList().blockingGet()).findFirst();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }
}
