package com.vexonelite.rxjava.demo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

//      output:
//        ActionHandler
//        1: Krzysztof
//        ActionHandler
//        2: Karol
//        ActionHandler
//        3: Irek
//
//        Test 2:
//
//        ActionHandler2
//        1: Krzysztof
//        2: Karol
//        3: Irek
//
//        Test 3:
//
//        Func1
//        ActionHandler
//        1: Krzysztof
//        ActionHandler
//        2: Karol
//        ActionHandler
//        3: Irek
//
//        Test 4:
//
//        Func1 # 1
//        Func1 # 2
//        ActionHandler
//        1: Show me the money :)
//        Func1 # 2
//        ActionHandler
//        2: I want to eat a lot!
//        Func1 # 2
//        ActionHandler
//        3: Today is a sunny day!

public class TestObservableFrom {

    static Map<String, String> myMap;

    public static void main(String[] args) {

        List<String> arrayList = new ArrayList<String>();
        arrayList.add("Krzysztof");
        arrayList.add("Karol");
        arrayList.add("Irek");

        System.out.println("Test 1:\n");
        //final ActionHandler handler = new ActionHandler();
        Observable.from(arrayList)
                //.subscribe(url -> System.out.println(url));
                .subscribe(new ActionHandler());

        System.out.println("\nTest 2:\n");
        Observable.just(arrayList).
                subscribe(new ActionHandler2());

        System.out.println("\nTest 3:\n");
        Observable.just(arrayList).
                flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        System.out.println("Func1");
                        return Observable.from(strings);
                    }
                })
                .subscribe(new ActionHandler());

        if (null == myMap) {
            myMap = new HashMap();
            myMap.put("Krzysztof", "Show me the money :)");
            myMap.put("Karol", "I want to eat a lot!");
            myMap.put("Irek", "Today is a sunny day!");
        }
        System.out.println("\nTest 4:\n");

        Observable.just(arrayList).
                flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        System.out.println("Func1 # 1");
                        return Observable.from(strings);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String input) {
                        System.out.println("Func1 # 2");
                        return getTitle(input);
                    }
                })
                .subscribe(new ActionHandler());

    }

    private static Observable<String> getTitle(final String input) {
        //return Observable.just(myMap.get(input));
        Observable<String> observable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(myMap.get(input));
                        subscriber.onCompleted();
                    }
                }
        );
        return observable;
    }

    private static class ActionHandler implements Action1<String> {
        private int number = 1;
        @Override
        public void call(String result) {
            System.out.println("ActionHandler");
            System.out.println(Integer.toString(number) + ": " + result);
            number++;
        }
    }

    private static class ActionHandler2 implements Action1<List<String>> {
        @Override
        public void call(List<String> list) {
            System.out.println("ActionHandler2");
            int i = 1;
            for (String str : list) {
                System.out.println(Integer.toString(i) + ": " + str);
                i++;
            }
        }
    }

}
