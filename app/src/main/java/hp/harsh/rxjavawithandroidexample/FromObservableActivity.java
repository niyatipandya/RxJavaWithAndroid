/*
* Copyright 2016 Harsh Patel
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package hp.harsh.rxjavawithandroidexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FromObservableActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "FromObservable";

    private Subscription mSubscription;

    private TextView mTxtResult;
    private Button mBtnFrom1, mBtnFrom2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_observable);

        mTxtResult = (TextView) findViewById(R.id.txtResult);

        mBtnFrom1 = (Button) findViewById(R.id.btnFrom1);
        mBtnFrom2 = (Button) findViewById(R.id.btnFrom2);

        mBtnFrom1.setOnClickListener(this);
        mBtnFrom2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Clear current subscription
        clearSubscription();

        // Clear previous result to show latest result
        mTxtResult.setText("");
        switch (view.getId()) {
            case R.id.btnFrom1:
                getIntegers();
                break;
            case R.id.btnFrom2:
                getStrings();
                break;
        }
    }

    private void getIntegers() {
        // Observable which emits each integer one by one
        Observable<Integer> observable = Observable.from(new Integer[]{1, 2, 3, 4, 5, 6});

        // Observer catch emitted data or error.
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onCompleted() {
                // Called when the observable has no more data to emit
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                // Called when the observable encounters an error
                Log.i(TAG, "onError" + e);
            }

            @Override
            public void onNext(Integer i) {
                // Called each time the observable emits data
                Log.i(TAG, "Emitted Observer " + i);

                // Print result
                mTxtResult.setText(mTxtResult.getText().toString() + "\n" + i);
            }
        };

        // Subscribe observer. Observable emits data from io thread and emitted data is observe from main thread
        mSubscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void getStrings() {
        // Observable which emits each string one by one
        Observable<String> observable = Observable.from(new String[]{"USA", "CANADA", "INDIA", "AUSTRALIA", "UAE", "UK"});

        // Observer catch emitted data or error.
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                // Called when the observable has no more data to emit
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                // Called when the observable encounters an error
                Log.i(TAG, "onError" + e);
            }

            @Override
            public void onNext(String s) {
                // Called each time the observable emits data
                Log.i(TAG, "Emitted Observer " + s);

                // Print result
                mTxtResult.setText(mTxtResult.getText().toString() + "\n" + s);
            }
        };

        // Subscribe observer. Observable emits data from io thread and emitted data is observe from main thread
        mSubscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void clearSubscription() {
        // To detach an observer from its observable while the observable is still emitting data, need to unsubscribe
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        // Clear current subscription
        clearSubscription();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Back to caller Activity
        startActivity(new Intent(FromObservableActivity.this, MainActivity.class));
        finish();
    }
}
