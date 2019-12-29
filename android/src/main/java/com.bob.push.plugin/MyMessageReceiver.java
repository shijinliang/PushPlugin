package com.bob.push.plugin;

//import android.content.Context;
//import android.util.Log;
//import com.alibaba.sdk.android.push.MessageReceiver;
//import com.alibaba.sdk.android.push.notification.CPushMessage;
//import com.google.gson.Gson;
//import io.flutter.plugin.common.EventChannel;
//import java.util.Map;
//
//public class MyMessageReceiver extends MessageReceiver {
//    // 消息接收部分的LOG_TAG
//    public static final String REC_TAG = "receiver";
//    public static final String LOG_TAG = "MyMessageReceiver";
//
//    @Override
//    public void onNotification(Context context,
//                               String title,
//                               String summary,
//                               Map<String, String> extraMap) {
//        Log.e(LOG_TAG,
//              "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " +
//              extraMap);
//        Gson gson = new Gson();
//        Log.e(LOG_TAG,
//              "eventSink: " + PushPlugin.getInstance()
//                                        .getEventSink());
//        PushPlugin.getInstance()
//                  .getEventSink()
//                  .success(gson.toJson(new Notification(title, summary, extraMap),
//                                       Notification.class));
//    }
//
//    @Override
//    public void onMessage(Context context, CPushMessage cPushMessage) {
//        Log.e(LOG_TAG,
//              "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " +
//              cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
//        Gson gson = new Gson();
//        Log.e(LOG_TAG,
//              "eventSink: " + PushPlugin.getInstance()
//                                        .getEventSink());
//        Message message = new Message(cPushMessage);
//        EventChannel.EventSink eventSink = PushPlugin.getInstance()
//                                                     .getEventSink();
//        if (eventSink != null) {
//            eventSink.success(gson.toJson(message, Message.class));
//        }
//    }
//
//    @Override
//    public void onNotificationOpened(Context context,
//                                     String title,
//                                     String summary,
//                                     String extraMap) {
//        Log.e(LOG_TAG,
//              "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" +
//              extraMap);
//    }
//
//    @Override
//    protected void onNotificationClickedWithNoAction(Context context,
//                                                     String title,
//                                                     String summary,
//                                                     String extraMap) {
//        Log.e(LOG_TAG,
//              "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary +
//              ", extraMap:" + extraMap);
//    }
//
//    @Override
//    protected void onNotificationReceivedInApp(Context context,
//                                               String title,
//                                               String summary,
//                                               Map<String, String> extraMap,
//                                               int openType,
//                                               String openActivity,
//                                               String openUrl) {
//        Log.e(LOG_TAG,
//              "onNotificationReceivedInApp, title: " + title + ", summary: " + summary +
//              ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" +
//              openActivity + ", openUrl:" + openUrl);
//    }
//
//    @Override
//    protected void onNotificationRemoved(Context context, String messageId) {
//        Log.e(LOG_TAG, "onNotificationRemoved");
//    }
//
//    private class Notification {
//        String type;
//        String title;
//        String summary;
//        Map<String, String> extraMap;
//
//        public Notification(String title, String summary, Map<String, String> extraMap) {
//            this.type = "notification";
//            this.title = title;
//            this.summary = summary;
//            this.extraMap = extraMap;
//        }
//    }
//
//    private class Message {
//        String type;
//        String title;
//        String content;
//
//        public Message(CPushMessage cPushMessage) {
//            this.type = "message";
//            this.title = cPushMessage.getTitle();
//            this.content = cPushMessage.getContent();
//        }
//    }
//}