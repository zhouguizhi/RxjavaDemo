package com.simple;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.simple.bean.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;
import rx.observables.GroupedObservable;
import rx.observables.MathObservable;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
    }
    public void send(View view){
        //今日头条报社 被观察者(Observable)
        Observable<String> observable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //onNext就相当于subscriber要发送报纸的
                subscriber.onNext("hello world rxjava");
                subscriber.onError(new NullPointerException());
                subscriber.onNext("再次发送hello world rxjava");
                subscriber.onCompleted();
            }
        });
        //家庭  观察者（Observer）
        Observer<String> observer = new Observer<String>(){
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted");
            }
            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onError");
            }
            @Override
            public void onNext(String s) {
                //接受到被观察者发送的消息
                Log.e(TAG,"接收到被观察者发送的消息是"+s);
            }
        };
        //让观察者和被观察者有关联 就是订阅了
        observable.subscribe(observer);
    }
    public void single(View view){
        Single single = Single.create(new Single.OnSubscribe<String>(){
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                singleSubscriber.onSuccess("发送消息11111111111");
                singleSubscriber.onSuccess("发送消息2222222222222222");
            }
        });
        Observer observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"接收到的消息s="+s);
            }
        };
        single.subscribe(observer);
    }
    //另一种观察者
    public void subscriber(View view){
        Observable observable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("今天周一好蛋疼");
                subscriber.onCompleted();
            }
        });
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"接收到的消息是::::"+s);
            }
        };
        observable.subscribe(subscriber);
    }
    public void onNext(View view){
        Observable observable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("今天周一好蛋疼");
                subscriber.onCompleted();
            }
        });
        Action1 onNext = new Action1<String>(){
            @Override
            public void call(String s) {
                Log.e(TAG,"接收到的消息是:::"+s);
            }
        };
        Action1 onError = new Action1<Throwable>(){
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG,"接收到异常:::"+throwable.toString());
            }
        };
        Action0 onCompleted = new Action0(){
            @Override
            public void call() {
                Log.e(TAG,"消息发送完毕:::");
            }
        };
        Func0 f0 = new Func0<String>(){
            @Override
            public String call() {
                return null;
            }
        };
        Func1 f1 = new Func1<String,Integer>(){
            @Override
            public Integer call(String s) {
                return null;
            }
        };
        //只接受onNext()
        observable.subscribe(onNext);
    }
    public void asyncsubject(View view){
        AsyncSubject<String> subject = AsyncSubject.create();
        subject.onNext("one");
        subject.onNext("two");
        subject.onNext("three");
        subject.onCompleted();
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"接受到的消息是:::"+s);
            }
        };
        subject.subscribe(action1);
    }
    public void behaviorsubject(View view){
        BehaviorSubject<String> subject = BehaviorSubject.create("default");
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"接受到的消息是:::"+s);
            }
        };
        subject.subscribe(action1);
        subject.onNext("two");
        subject.onNext("three");
    }
    public void publishsubject(View view){
        PublishSubject<String> subject = PublishSubject.create();
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"接受到的消息是:::"+s);
            }
        };
        subject.onNext("one");
        subject.onNext("two");
        subject.subscribe(action1);
        subject.onNext("three");
        subject.onNext("four");
        subject.onCompleted();
    }
    public void replaysubject(View view){
        ReplaySubject<String> subject = ReplaySubject.create();
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"接受到的消息是:::"+s);
            }
        };
        subject.onNext("one");
        subject.onNext("two");
        subject.subscribe(action1);
        subject.onNext("three");
        subject.onCompleted();
    }
    public void publish(View view){
        Observable<String> observable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello world rxjava");
                subscriber.onNext("hello world rxjava");
                subscriber.onNext("hello world rxjava");
                subscriber.onCompleted();
            }
        });
        //通过publish操作符变成一个可连接的observable
        ConnectableObservable connectableObservable = observable.publish();
        Observer<String> observer = new Observer<String>(){
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"接收到被观察者发送的消息是"+s);
            }
        };
        connectableObservable.subscribe(observer);
        connectableObservable.connect();
    }
    public void replay(View view){
        Observable<String> observable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello world rxjava");
                subscriber.onCompleted();
            }
        });
        //通过replay操作符变成一个可连接的observable
        ConnectableObservable connectableObservable = observable.replay();
        Observer<String> observer = new Observer<String>(){
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"消息111"+s);
            }
        };
        connectableObservable.subscribe(observer);
        connectableObservable.connect();
        connectableObservable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"消息222"+s);
            }
        });
    }
    public void defer(View view){
        Observable observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                Observable observable = Observable.create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        //发送数据
                        subscriber.onNext("发送消息的数据");
                    }
                });
                Log.e(TAG,"observable:::"+observable);
                return observable;
            }
        });
       Observer observer = new Observer<String>() {
           @Override
           public void onCompleted() {

           }
           @Override
           public void onError(Throwable e) {

           }
           @Override
           public void onNext(String s) {
                Log.e(TAG,"接受到的消息是:::"+s);
           }
       };
        Observer observer1 = new Observer<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"1111接受到的消息是:::"+s);
            }
        };
        observable.subscribe(observer);
        observable.subscribe(observer1);
    }
    public void just(View view){
        Observable
                .just("想让自己内心丰富起来么","那就多读书吧")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"接收到的消息::::="+s);
                    }
                });
    }
    public void from(View view){
        List<String> lists = new ArrayList<>();
        for(int i=0;i<5;i++){
            lists.add("美女--"+i);
        }
        Observable.from(lists)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"onCompleted=");
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"s="+s);
                    }
                });
    }
    public void interval(View view){
        Observable
                .interval(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Long l) {
                Log.e(TAG,"上课倒计时还有：："+l+"秒");
            }
        });
    }
    public void timer(View view){
        Observable.timer(5000,TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Long aLong) {
                Log.e(TAG,"要上课了：："+aLong);
            }
        });
    }
    public void range(View view){
        Observable
             .range(1,6)
             .subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted:::");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Integer integer) {
                Log.e(TAG,"integer:::"+integer);
            }
        });
    }
    public void repeat(View view){
        Observable
        .just("重要的事说三遍")
         .repeat(3)
          .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted:::");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,s);
            }
        });
    }
    public void delay(View view){
        Observable
                .just("等2秒你去完成登录功能","等3秒去完成注册功能")
                .distinct()
                .delay(5,TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted:::");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"今天要干的活---"+s);
            }
        });
    }
    public void distinct(View view){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        Observable
                .from(persons)
                .distinct(new Func1<Person, String>() {
                    @Override
                    public String call(Person person) {
                        return person.getId_card();
                    }
                })
                .subscribe(new Observer<Person>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Person person) {
                Log.e(TAG,"去参数冬游的有："+person);
            }
        });
    }
    public void filter(View view){
        Observable
             .range(-2,5)
             .filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer>0;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Integer integer) {
                Log.e(TAG,integer%2+"");
            }
        });
    }
    public void first(View view){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        Observable.from(persons).first().subscribe(new Observer<Person>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Person person) {
                Log.e(TAG,person.toString());
            }
        });
    }
    public void last(View view){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        Observable
                .from(persons)
                .last().
                subscribe(new Observer<Person>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Person person) {
                Log.e(TAG,person.toString());
            }
        });



    }
    public void elementAt(View view){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        Observable
                .from(persons)
                .elementAt(1).
                subscribe(new Observer<Person>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Person person) {
                        Log.e(TAG,person.toString());
                    }
                });
    }
    public void task(View view){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        Observable
                .from(persons)
                .take(2)
                .subscribe(new Observer<Person>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Person person) {
                        Log.e(TAG,person.toString());
                    }
                });

        Observable.interval(1000,TimeUnit.MILLISECONDS).take(5000,TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.e(TAG,"aLong="+aLong);
            }
        });
    }
    public void takeLast(View view){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        Observable
                .from(persons)
                .takeLast(2).
                subscribe(new Observer<Person>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Person person) {
                        Log.e(TAG,person.toString());
                    }
                });
    }
    public void sample(View view){
        Observable
                .interval(1000,TimeUnit.MILLISECONDS)
                .sample(2000,TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Long aLong) {
                Log.e(TAG,"aLong="+aLong);
            }
        });
    }
    public void ignoreElements(View view){
        Observable
                  .just("发送第一个消息","发送第二个消息","发送第三个消息")
                .ignoreElements()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,s);
                    }
                });
    }
    public void all(View view){
        Observable
            .from(getPersons())
            .all(new Func1<Person, Boolean>() {
            @Override
            public Boolean call(Person person) {
                return person.getAge()>30;
            }
           })
            .subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Boolean aBoolean) {
                Log.e(TAG,"aBoolean="+aBoolean);
            }
        });
    }
    public void amb(View view){
        Observable<String> aObservable = Observable.just("小明先1秒后","跑到了终点");
        Observable<String> bObservable = Observable.just("小王先2秒后","跑到了终点");
        Observable
                .amb(aObservable,bObservable)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG,"s="+s);
                    }
                });
    }
    public void  contains(View view){
        Observable
                .just(0,1,2)
                .contains(0)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.e(TAG,"aBoolean="+aBoolean);
                    }
                });
    }
    public void defaultIfEmpty(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        //必须要调用onCompleted
                        subscriber.onCompleted();
                    }
                })
                .defaultIfEmpty("你输入用户账号")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG,"s="+s);
                    }
                });
    }
    public void sequenceEqual(View view){
        Observable pwd = Observable.just(1,2,3,4,5,6);
        Observable confimwd = Observable.just(1,2,3,4,5,6);
        Observable
                .sequenceEqual(pwd,confimwd)
                .subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.e(TAG,"aBoolean="+aBoolean);
            }
        });
    }
    public void  skip(View view){
        Observable
               .range(1,10)
                .skip(5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"integer="+integer);
                    }
                });
    }
    public void  skipLast(View view){
        Observable
                .range(1,10)
                .skipLast(5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"integer="+integer);
                    }
                });
    }
    public void  skipWhile(View view){
        Observable
                .just(1,2,3,4,5)
                .skipWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer value) {
                        return value<3;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"integer="+integer);
                    }
                });
    }
    public void takeWhile(View view){
        Observable
                .just(1,2,3,4,5)
                .takeWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer value) {
                        return value<3;
                    }
                })
                .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"integer="+integer);
            }
        });
    }
    public void average(View view){
        Observable observable  = Observable.just(1,2,3,4,5,6,7,8,9,10);
        MathObservable.averageInteger(observable).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer aDouble) {
                Log.e(TAG,"平均值为"+aDouble);
            }
        });
    }
    public void max(View view){
        Observable observable  = Observable.just(1,2,3,4,5,6,7,8,9,10);
        MathObservable.max(observable).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                Log.e(TAG,"最大值为"+value);
            }
        });
    }
    public void min(View view){
        Observable observable  = Observable.just(1,2,3,4,5,6,7,8,9,10);
        MathObservable.min(observable).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                Log.e(TAG,"最小值为"+value);
            }
        });
    }
    public void count(View view){
        Observable
                .just(1,2,3,4,5,6,7,8,9,10)
                .count()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer count) {
                        Log.e(TAG,"发送了多少个数据"+count);
                    }
                });
    }
    public void sum(View view){
        Observable observable  = Observable.just(1,2,3,4,5,6,7,8,9,10);
        MathObservable.sumInteger(observable).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                Log.e(TAG,"总和为"+value);
            }
        });
    }
    public void onErrorReturn(View view){
        Observable
                .create(new OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("消息1----");
                    subscriber.onNext("消息2----");
                    subscriber.onError(new NullPointerException("我擦,空指针异常了,怎么写的代码,这么低级的错误"));
                    subscriber.onCompleted();
                  }
               })
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        return "程序异常了,发送一个默认的消息";
                    }
                })
                .subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                    Log.e(TAG,"onCompleted--------------");
                }
                @Override
                public void onError(Throwable e) {
                    Log.e(TAG,"异常信息="+e.getLocalizedMessage());
                }
                @Override
                public void onNext(String s) {
                    Log.e(TAG,s);
                }
        });
    }
    public void onErrorResumeNext(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("消息1----");
                        subscriber.onNext("消息2----");
                        subscriber.onError(new NullPointerException("我擦,空指针异常了,怎么写的代码,这么低级的错误"));
                        subscriber.onCompleted();
                    }
                })
                .onErrorResumeNext(Observable.create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("程序出现了异常发送一个空消息");
                    }
                }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"onCompleted--------------");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"异常信息="+e.getLocalizedMessage());
                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,s);
                    }
                });
    }
    public void onExceptionResumeNext(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("消息1----");
                        subscriber.onNext("消息2----");
                        subscriber.onError(new NullPointerException("我擦,空指针异常了,怎么写的代码,这么低级的错误"));
                        subscriber.onCompleted();
                    }
                })
                .onExceptionResumeNext(Observable.create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("程序出现了异常发送一个空消息");
                    }
                }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"onCompleted--------------");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"异常信息="+e.getLocalizedMessage());
                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,s);
                    }
                });
    }
    public void retry(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("请求服务器详情接口----");
                        subscriber.onError(new TimeoutException("请求超时了"));
                    }
                })
//                .observeOn(Schedulers.io())
                .retry(2)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"异常-"+e.getLocalizedMessage());
                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,s);
                    }
                });

//
//        Observable
//                .create(new OnSubscribe<String>() {
//                    @Override
//                    public void call(Subscriber<? super String> subscriber) {
//                        subscriber.onNext("请求服务器详情接口----");
//                        subscriber.onError(new TimeoutException("请求超时了"));
//                    }
//                })
//                .retry(2)
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Log.e(TAG,s);
//                    }
//                });
    }
    public void observeOn(View view){
        Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("发送消息");
                Log.e(TAG,"被观察者所在的线程名字是="+Thread.currentThread().getName());
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"观察者所在的线程名字是="+Thread.currentThread().getName());
            }
        });
    }
    public void subscribeOn(View view){
        Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("发送消息");
                Log.e(TAG,"被观察者所在的线程名字是="+Thread.currentThread().getName());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"观察者所在的线程名字是="+Thread.currentThread().getName());
                    }
                });
    }
    public void thread_scheduler(View view){
        //在子线程中创建一个执行任务 可以看作一个Thread或者Runnable
       final Scheduler.Worker worker =  Schedulers.io().createWorker();
        worker.schedule(new Action0() {
            @Override
            public void call() {
                //在这执行你的业务 这个call方法所在线程就是子线程
                //........
                worker.unsubscribe();
            }
        });
    }
    public void doOnEach(View view){
        Observable
                .create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("现在事件是"+System.currentTimeMillis());
                subscriber.onCompleted();
            }
        })
                .doOnEach(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"开始执行观察者中的onCompleted方法");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"开始执行观察者中的onNext方法");
            }
        })
                .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"接收到消息是:"+s);
            }
        });
    }
    public void doOnNext(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("现在事件是"+System.currentTimeMillis());
                        subscriber.onCompleted();
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG,"开始执行观察者中的onNext方法");
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"接收到消息是:"+s);
                    }
                });
    }
    public void doOnSubscribe(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("现在事件是"+System.currentTimeMillis());
                        subscriber.onCompleted();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e(TAG,"开始订阅");
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"接收到消息是:"+s);
                    }
                });
    }
    public void doOnUnsubscribe(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("现在事件是"+System.currentTimeMillis());
                        subscriber.onCompleted();
                    }
                })
                .doOnUnsubscribe(new Action0() {//解绑订阅的监听
                    @Override
                    public void call() {
                        Log.e(TAG,"解绑订阅了");
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"收到onCompleted事件");
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"接收到消息是:"+s);
                    }
                });
    }
    public void serialize(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        subscriber.onNext("请客户端等待");
                        subscriber.onCompleted();
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                subscriber.onNext("去后台请求一个接口,返回一个json,大概耗时10秒");
                            }
                        }.start();
                        subscriber.onCompleted();
                    }
                })
                .serialize()
                .unsafeSubscribe(new Subscriber<String>(){//不安全的订阅
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,s);
                    }
                });
    }
    public void timeout(View view){
        Observable
                .create(new OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        subscriber.onNext("网络请求成功了");
                    }
                })
                .delay(4,TimeUnit.SECONDS)//模拟网络请求
                .timeout(2,TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"异常超时了");
                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,s);
                    }
                });
    }
    public void using(View view){
        Observable observable = Observable.using(new Func0<String>() {
            @Override
            public String call() {
                return "返回一个cursor对象,记得用完要close";
            }
        }, new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String s) {//s就是一次性资源 通过Observable发送事件
                return Observable.just(s);
            }
        }, new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"要销毁这个资源");//在这里资源我们是用字符串模拟了
                s = null;
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"事件完毕onCompleted");
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"接收到的消息是:::"+s);
            }
        });
    }
    public void buffer(View view){
        Observable observable = Observable.just("章子怡","范冰冰","刘茹英","刘德华","梁朝伟");
        Observable<List<String>> bufferObservable = observable.buffer(2);//缓存2个
        bufferObservable.subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted:::");
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(List<String> strings) {
                Log.e(TAG,"--------------华丽的分割线-----------");
                for(String str:strings){
                    Log.e(TAG,"接收到的消息是:::"+str);
                }
            }
        });
    }
    public void window(View view){
        Observable observable = Observable.just("章子怡","范冰冰","刘茹英","刘德华","梁朝伟");
        Observable<Observable<String>> windowObservable = observable.window(2);
        windowObservable.subscribe(new Observer<Observable<String>>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Observable<String> stringObservable) {
                Log.e(TAG,"stringObservable="+stringObservable);
                stringObservable.subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG,"--------------华丽的分割线-----------");
                        Log.e(TAG,"接收到事件="+s);
                    }
                });
            }
        });
    }
    public void map(View view){
        Observable
            .create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("http://www.baidu.com");
             }
           })
           .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        if(s.equals("http://www.baidu.com")){
                            return "http://www.qq.com";
                        }
                        return null;
                    }
                })
            .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                Log.e(TAG,"你访问的网址是::"+s);
            }
        });
    }
    public void scan(View view){
        Observable.just(1,5,10,15,20)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i1, Integer i2) {
                        return i1+i2;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,integer+"");
            }
        });
    }
    public void cast(View view){
        Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("11111");

            }
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,s);
            }
        });
    }
    public void toList(View view){
        Observable.just(1,2,3,4,5).toList().subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                for(Integer i:integers){
                    Log.e(TAG,i+"");
                }
            }
        });
    }
    public void toSortedList(View view){
        Observable
                .just(2,4,1,3,5)
                .toSortedList(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer2-integer;
                    }
                })
                .subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                for(Integer i:integers){
                    Log.e(TAG,i+"");
                }
            }
        });
    }
    public void toMap(View view){
        Observable
            .just("time","sort","name","pwd")
            .toMap(new Func1<String, String>() {
                @Override
                public String call(String s) {
                    return s;
                }
            }).subscribe(new Action1<Map<String, String>>() {
            @Override
            public void call(Map<String, String> stringStringMap) {
                for (Map.Entry<String, String> entry: stringStringMap.entrySet()) {
                    Log.e(TAG,"key="+entry.getKey()+"::"+"value="+entry.getValue());
                }
            }
        });
    }
    public void toMultiMap(View view){
        Observable
                .just("time","sort","name","pwd")
                .toMultimap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s;
                    }
                }).subscribe(new Action1<Map<String, Collection<String>>>() {
            @Override
            public void call(Map<String, Collection<String>> stringCollectionMap) {
                Log.e(TAG,"stringCollectionMap="+stringCollectionMap);
            }
        });
    }
    public void flatMap(View view){
        Observable.just(1,2,3,4,5)
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Integer integer) {
                        return Observable.create(new OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                subscriber.onNext(integer*2+10);
                            }
                        });
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Integer value) {
                Log.e(TAG,"value="+value);
            }
        });
    }
    public void flatMapIterable(View view){
        Observable.just(1,2,3,4,5)
                .flatMapIterable(new Func1<Integer, Iterable<String>>() {
                    @Override
                    public Iterable<String> call(Integer integer) {
                        return Arrays.asList(integer*2+"");
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"value="+s);
            }
        });
    }
    public void concatMap(View view){
        Observable
                .from(getPersons())
                .concatMap(new Func1<Person, Observable<String>>() {
                    @Override
                    public Observable<String> call(Person person) {
                        return Observable.just(person.getName());
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG,"value="+s);
                    }
                });
    }
    public void groupBy(View view){
        Observable
                .just(1,2,3,4,5)
                .groupBy(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer%2==0?"男":"女";
            }
        })
                .subscribe(new Action1<GroupedObservable<String, Integer>>() {
            @Override
            public void call(final GroupedObservable<String, Integer> stringIntegerGroupedObservable) {
                stringIntegerGroupedObservable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,stringIntegerGroupedObservable.getKey()+":"+integer.toString()); //偶数：2，奇数：3，...
                    }
                });
            }
        });
    }
    public void switchMap(View view){
        
    }
    public List<Person> getPersons(){
        List<Person>  persons = new ArrayList<>();
        persons.add(new Person("章子怡","34183987778925",40));
        persons.add(new Person("范冰冰","34183986678925",35));
        persons.add(new Person("刘茹英","341839855558925",30));
        persons.add(new Person("刘茹英","341839855558925",35));
        return persons;
    }
}
