package com.bob.push.plugin;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * PushPlugin
 */
public class PushPlugin implements MethodCallHandler, EventChannel.StreamHandler {
    public static final String TAG = "PushPlugin";
    
    private EventChannel.EventSink eventSink;
    private CloudPushService pushService;
    private String pushMessage;
    
    private static class PushPluginHolder {
        public static PushPlugin pushPlugin = new PushPlugin();
    }
    
    public static PushPlugin getInstance() {
        //        return pushPlugin;
        return PushPluginHolder.pushPlugin;
    }
    
    private PushPlugin() {
        pushService = PushServiceFactory.getCloudPushService();
    }
    
    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }
    
    public EventChannel.EventSink getEventSink() {
        return this.eventSink;
    }
    
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        Log.e(TAG, "registerWith() called with: registrar = [" + registrar + "]");
        final MethodChannel methodChannel = new MethodChannel(registrar.messenger(), "push_plugin");
        final EventChannel eventChannel = new EventChannel(registrar.messenger(), "push_event");
        Log.e(TAG, "eventChannel called with: registrar = [" + eventChannel + "]");
        methodChannel.setMethodCallHandler(getInstance());
        eventChannel.setStreamHandler(getInstance());
    }
    
    @Override
    public void onMethodCall(MethodCall call, Result result) {
        Log.e(TAG, "onMethodCall() called with: call = [" + call + "], result = [" + result + "]");
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE + "packageName" +
                           BuildConfig.APPLICATION_ID);
        } else if (call.method.equals("bindAccount")) {
            toBindAccount(call);
        } else if (call.method.equals("unbindAccount")) {
            toUnbindAccount(call);
        } else {
            result.notImplemented();
        }
    }
    
    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
        if (!TextUtils.isEmpty(pushMessage)) {
            eventSink.success(pushMessage);
            pushMessage = null;
        }
    }
    
    @Override
    public void onCancel(Object o) {
        eventSink = null;
    }
    
    private void toBindAccount(MethodCall call) {
        final String uid = call.argument("uid");
        
        pushService.bindAccount(uid, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "bind account " + uid + " success\n");
            }
            
            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.e(TAG,
                      "bind account " + uid + " failed." + "errorCode: " + errorCode +
                      ", errorMsg:" + errorMsg);
            }
        });
    }

    private  void toUnbindAccount(MethodCall call) {
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "unbind account success\n");
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.e(TAG,
                        "unbind account failed." + "errorCode: " + errorCode +
                                ", errorMsg:" + errorMsg);
            }
        });
    }
}
