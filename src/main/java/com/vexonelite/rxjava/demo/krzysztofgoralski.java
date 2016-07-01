package com.vexonelite.rxjava.demo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @see <a href=https://krzysztofgoralski.wordpress.com/2016/02/28/rxjava-basics/>Reference</a>
 */
public class krzysztofgoralski {

    public static void main(String[] args) {

        // 1. verbose example
        Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> sub) {
                sub.onNext(extracted());
                sub.onCompleted();
            }

            private String extracted() {
                return "1. Hello, world!";
            }
        });

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            public void onCompleted() {
                System.out.println("1. the end");
            }

            public void onError(Throwable e) {
                System.out.println("error");
            }

            public void onNext(String s) {
                System.out.println(s);
            }
        };
        myObservable.subscribe(mySubscriber);

        // 2. almost same thing as above
        //Observable.just("2. Hello, world!")
        //      .subscribe(s -> System.out.println(s + "-Krzysztof"));
        Observable.just("2. Hello, world!")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s + "-Krzysztof");
                    }
                });

        // 3. map operator - transforming
        //Observable.just("3. Hello, world!")
        //      .map(s -> s + "-Krzysztof")
        //      .subscribe(s -> System.out.println(s));
        // http://reactivex.io/documentation/operators/map.html
        Observable.just("3. Hello, world!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "-Krzysztof";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });


        // 4. subscriber receive Integer
        //Observable.just("4. Hello, world!").map(s -> s.hashCode())
        //        .subscribe(i -> System.out.println("4. " + Integer.toString(i)));
        Observable.just("4. Hello, world!")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer i) {
                        System.out.println("4. " + Integer.toString(i));
                    }
                });

        // 5. subscriber shouldn't have much logic so...
        //Observable.just("5. Hello, world!")
        //      .map(s -> s.hashCode())
        //      .map(i -> Integer.toString(i))
        //      .subscribe(s -> System.out.println("5. " + s));
        Observable.just("5. Hello, world!")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer i) {
                        return Integer.toString(i);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("5. " + s);
                    }
                });

        // 6. some transformations
        //Observable.just("6. Hello, world!")
        //      .map(s -> s + " -Krzysztof")
        //      .map(s -> s.hashCode())
        //      .map(i -> Integer.toString(i))
        //      .subscribe(s -> System.out.println("6. " + s));
        Observable.just("6. Hello, world!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "-Krzysztof";
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer i) {
                        return Integer.toString(i);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("6. " + s);
                    }
                });

        List<String> arrayList = new ArrayList<String>();
        arrayList.add("Krzysztof");
        arrayList.add("Karol");
        arrayList.add("Irek");

        // 7. from operator emits one element each time
        //Observable.just(arrayList)
        //        .subscribe(names -> {
        //            Observable.from(names).subscribe(name -> System.out.println("7. Name: " + name));
        //        });
        // http://reactivex.io/documentation/operators/from.html
        Observable.just(arrayList)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> names) {
                        Observable.from(names).subscribe(
                                new Action1<String>() {
                                    @Override
                                    public void call(String name) {
                                        System.out.println("7. Name: " + name);
                                    }
                                }
                        );
                    }
                });

        // 8. list observable transformed into observable which emits single
        // result with after change name, filtering and taking entry at the end
        //Observable.just(arrayList)
        //        .flatMap(names -> Observable.from(names))
        //        .flatMap(name -> Observable.just(name + " is awesome"))
        //        .filter(nameChange -> !"Karol is awesome".equals(nameChange))
        //        .take(1)
        //        .subscribe(result -> System.out.println("8. " + result + "- this is the result"));
        // http://reactivex.io/documentation/operators/flatmap.html
        Observable.just(arrayList).
                flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> names) {
                        return Observable.from(names);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String name) {
                        return Observable.just(name + " is awesome");
                    }
                })
                /*
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String nameChange) {
                        return ( !("Karol is awesome".equals(nameChange)) );
                    }
                })
                .take(1)
                */
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        System.out.println("8. " + result + "- this is the result");
                    }
                });

        // 9. doOnNext() allows us to add extra behavior each time an item is
        // emitted, in this case singing 'lalala'
        //Observable.just(arrayList)
        //      .flatMap(names -> Observable.from(names))
        //      .flatMap(name -> Observable.just(name + " is awesome"))
        //      .filter(nameChange -> !"Irek is awesome".equals(nameChange)).take(1)
        //      .doOnNext(nameChange -> System.out.println("9. lalalla"))
        //      .subscribe(result -> System.out.println("9. " + result + "- this is the result"));
        // http://reactivex.io/documentation/operators/filter.html
        // http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnNext(rx.functions.Action1)
        Observable.just(arrayList)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> names) {
                        return Observable.from(names);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String name) {
                        return Observable.just(name + " is awesome");
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String nameChange) {
                        return ( !("Irek is awesome".equals(nameChange)) );
                    }
                })
                .take(1)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String nameChange) {
                        System.out.println("9. lalalla");
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        System.out.println("9. " + result + "- this is the result");
                    }
                });

        // 10. error example
//        Observable.just(arrayList)
//                .flatMap(names -> Observable.from(names))
//                .flatMap(name -> Observable.just(name + " is awesome"))
//                .filter(nameChange -> getError(nameChange).equals("anything"))
//                .take(1)
//                .doOnNext(nameChange -> System.out.println("10. lalalla"))
//                .onErrorReturn(new Func1<Throwable, String>() {
//                    @Override
//                    public String call(Throwable t) {
//                        return "error";
//                    }
//                })
//                .subscribe(result -> System.out.println("10. " + result + " - this is the result"));
        Observable.just(arrayList)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> names) {
                        return Observable.from(names);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String name) {
                        return Observable.just(name + " is awesome");
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String nameChange) {
                        return ( "anything".equals(getError(nameChange)) );
                    }
                })
                .take(1)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String nameChange) {
                        System.out.println("10. lalalla");
                    }
                })
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable t) {
                        return "error";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        System.out.println("10. " + result + "- this is the result");
                    }
                });

        // test error
        /*
        Observable.just(arrayList)
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> names) {
                        return Observable.from(names);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String name) {
                        return Observable.just(name + " is awesome");
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String nameChange) {
                        return ( "anything".equals(getError(nameChange)) );
                    }
                })
                .take(1)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String nameChange) {
                        System.out.println("10. lalalla");
                        Observable.error()
                    }
                })
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable t) {
                        return "error";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        System.out.println("10. " + result + "- this is the result");
                    }
                });
        */

        Observable.just(1, 2, 3, 4, 5, 6) // add more numbers
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer value) {
                        return value % 2 == 1;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Complete!");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext: " + value);
                    }
                });
        // Outputs:
        // onNext: 1
        // onNext: 3
        // onNext: 5
        // Complete!


        Observable.just(1, 2, 3, 4, 5, 6) // add more numbers
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer value) {
                        return value % 2 == 1;
                    }
                })
                .map(new Func1<Integer, Double>() {
                    @Override
                    public Double call(Integer value) {
                        return Math.sqrt(value);
                    }
                })
                .subscribe(new Subscriber<Double>() { // notice Subscriber type changed to
                    @Override
                    public void onCompleted() {
                        System.out.println("Complete!");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Double value) {
                        System.out.println("onNext: " + value);
                    }
                });
        // Outputs:
        // onNext: 1.0
        // onNext: 1.7320508075688772
        // onNext: 2.23606797749979
        // Complete!

        // 11. Schedulers: easy threading
        // myObservableServices.retrieveImage(url)
        // .subscribeOn(Schedulers.io())
        // .observeOn(AndroidSchedulers.mainThread())
        // .subscribe(bitmap -> myImageView.setImageBitmap(bitmap));

        // subscribeOn(Scheduler scheduler): Asynchronously subscribes Observers
        // to this Observable on the specified Scheduler.

        // observeOn(Scheduler scheduler): Modifies an Observable to perform its
        // emissions and notifications on a specified Scheduler, asynchronously
        // with a bounded buffer.

        // check: http://reactivex.io/documentation/operators/subscribeon.html

    }

    private static String getError(String name) {
        throw new RuntimeException(name);
    }
}
