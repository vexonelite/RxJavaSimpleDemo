package com.vexonelite.rxjava.demo;


import rx.Observable;
import rx.functions.Action1;

public class TestObservableConcat {

    public static void main(String[] args) {

        //final ActionHandler handler = new ActionHandler();
        Observable.concat(
                Observable.just("Krzysztof"),
                Observable.just("Karol"),
                Observable.just("Irek"))
                .subscribe(new Action1<String> () {
                    @Override
                    public void call(String result) {
                        System.out.println("ActionHandler");
                        System.out.println(result);
                    }
                });
    }



}
