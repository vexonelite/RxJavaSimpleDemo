package com.vexonelite.rxjava.demo;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;

public class ErrorHandlingTestOne {

    public static void main(String[] args) {

        System.out.println("1. Unchecked Exception Test:");
        Observable.just("Hello!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String input) {
                        throw new RuntimeException("This is a Unchecked Exception!");
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String nameChange) {
                        System.out.println("doOnNext - call: lalalla");
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext: " + s);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e.getLocalizedMessage());
                    }
                });

        System.out.println("\n2. Checked Exception Test: by map");
//        Observable.just("Hello!")
//                .map(input -> {
//                    try {
//                        return transform(input);
//                    } catch (Throwable t) {
//                        throw Exceptions.propagate(t);
//                    }
//                });
        Observable.just("Hello!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String input) {
                        int end;
                        try {
                            if (Math.random() < .5) {
                                end = 4;
                            } else {
                                end = 10;
                            }
                            String tmp = input.substring(0, end);
                            System.out.println("map 1 - tmp: " + tmp);
                            return "This is a valid String";
                        } catch (Throwable t) {
                            System.out.println("Throwable t is caught! --> Exceptions.propagate(t)");
                            throw Exceptions.propagate(t);
                        }
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String nameChange) {
                        System.out.println("doOnNext - call: lalalla");
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext: " + s);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e.getLocalizedMessage());
                    }
                });

        System.out.println("\n3. Checked Exception Test: by flatMap");
//        Observable.just("Hello!")
//            .flatMap(input -> {
//                try {
//                    return Observable.just(transform(input));
//                } catch (Throwable t) {
//                    return Observable.error(t);
//                }
//            })
        Observable.just("Hello!")
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String input) {
                        int end;
                        try {
                            if (Math.random() < .5) {
                                end = 4;
                            } else {
                                end = 10;
                            }
                            String tmp = input.substring(0, end);
                            System.out.println("map 1 - tmp: " + tmp);
                            return Observable.just("This is a valid String");
                        }
                        catch (Throwable t) {
                            System.out.println("Throwable t is caught! --> Observable.error(t)");
                            return Observable.error(t);
                        }
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String nameChange) {
                        System.out.println("doOnNext - call: lalalla");
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext: " + s);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError: " + e.getLocalizedMessage());
                    }
                });
    }


}
