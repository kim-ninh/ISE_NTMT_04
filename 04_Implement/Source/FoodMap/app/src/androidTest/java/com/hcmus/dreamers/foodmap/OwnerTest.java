package com.hcmus.dreamers.foodmap;

import android.support.test.runner.AndroidJUnit4;

import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OwnerTest {
    int respone = 0;

    @Test
    public void loginSucess() throws InterruptedException {
        respone = FoodMapApiManager.FAIL_INFO;              // Đặt khác giá trị Mong đợi ở dưới
        CountDownLatch signal = new CountDownLatch(1);     //  Mục đích là dùng đồng bộ 2 tiến trình main vs AsyncTask
        FoodMapApiManager.Login("thedreamers", "doanfoodmap", new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                respone = (int) response;
                signal.countDown();
            }
        });
        signal.await();
        assertEquals(FoodMapApiManager.SUCCESS, respone);
    }

    @Test
    public void loginFailed() throws InterruptedException {
        respone = FoodMapApiManager.SUCCESS;
        CountDownLatch signal = new CountDownLatch(1);
        FoodMapApiManager.Login("thedreamers", "doanfoodma", new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                respone = (int) response;
                signal.countDown();
            }
        });
        signal.await();
        assertEquals(FoodMapApiManager.FAIL_INFO, respone);
    }
}
