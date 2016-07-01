package com.vexonelite.rxjava.demo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class ForTestOne {

    public static void main(String[] args) {

        List<String> arrayList = new ArrayList<String>();
        arrayList.add("Krzysztof");
        arrayList.add("Karol");
        arrayList.add("Irek");

        //final ActionHandler handler = new ActionHandler();
        Observable.from(arrayList)
                //.subscribe(url -> System.out.println(url));
                .subscribe(new ActionHandler());
    }

    private static class ActionHandler implements Action1<String> {
        private int number = 1;
        @Override
        public void call(String result) {
            System.out.println(Integer.toString(number) + ": " + result);
            number++;
        }
    }

}
