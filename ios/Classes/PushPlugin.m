#import "PushPlugin.h"
#import <CloudPushSDK/CloudPushSDK.h>
#import <MJExtension/MJExtension.h>

@interface PushPlugin() <FlutterStreamHandler>
@property (nonatomic, copy) NSString *url;
@end

//判空类
#define PPIsNull(x)             (!x || [x isKindOfClass:[NSNull class]])
//字符串判空
#define PPIsEmptyString(x)      (PPIsNull(x) || [x isEqual:@""] || [x isEqual:@"(null)"])

@implementation PushPlugin
+ (instancetype)shareInstance {
    static PushPlugin *instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[PushPlugin alloc] init];
    });
    return instance;
}
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {

    FlutterMethodChannel* channel = [FlutterMethodChannel methodChannelWithName:@"push_plugin"
                                                                binaryMessenger:[registrar messenger]];
    PushPlugin* instance = [PushPlugin shareInstance];
    [registrar addMethodCallDelegate:instance channel:channel];
    
    FlutterEventChannel *eventChannel = [FlutterEventChannel eventChannelWithName:@"push_event"
                                                                  binaryMessenger:[registrar messenger]];
    [eventChannel setStreamHandler:instance];
    
    [instance registerMessageReceive];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"getPlatformVersion" isEqualToString:call.method]) {
        result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
    } else if ([call.method isEqualToString:@"bindAccount"]) {
        [self bindAccount:call];
    } else if ([call.method isEqualToString:@"unbindAccount"]) {
        [self unbindAccount:call];
    } else {
        result(FlutterMethodNotImplemented);
    }
}

#pragma mark - FlutterStreamHandler

- (FlutterError* _Nullable)onListenWithArguments:(id _Nullable)arguments
                                       eventSink:(FlutterEventSink)eventSink{
    if (eventSink) {
        self.eventSink = eventSink;
    }
    return nil;
}

- (FlutterError* _Nullable)onCancelWithArguments:(id _Nullable)arguments{
    return nil;
}

/**
 *    注册推送消息到来监听
 */
- (void)registerMessageReceive {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(onMessageReceived:)
                                                 name:@"CCPDidReceiveMessageNotification"
                                               object:nil];
}
/**
 *    处理到来推送消息
 *
 *    @param notification 接口消息
 */
- (void)onMessageReceived:(NSNotification *)notification {
    
    NSLog(@"onMessageReceived userinfo : %@", notification.userInfo);
    
    CCPSysMessage *message = [notification object];
    NSString *title = [[NSString alloc] initWithData:message.title encoding:NSUTF8StringEncoding];
    NSString *body = [[NSString alloc] initWithData:message.body encoding:NSUTF8StringEncoding];
    NSLog(@"Receive message title: %@, content: %@.", title, body);
    
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    // dict[@"title"] = title;
    // dict[@"body"] = body;
    NSString *jsonStr = [[body mj_JSONString] copy];
    self.eventSink(jsonStr);
}

- (void)bindAccount:(FlutterMethodCall *)call {
    NSString *uid = call.arguments[@"uid"];
    NSLog(@"Bind account uid: %@", uid);
    
    if (PPIsEmptyString(uid)) {
        return;
    }
    [CloudPushSDK bindAccount:uid withCallback:^(CloudPushCallbackResult *res) {
        if (res.success) {
            NSLog(@"Bind account %@ success!", uid);
        } else {
            NSLog(@"Bind account %@ failed!  errorData: %@", uid, res.error);
        }
    }];
}

- (void)unbindAccount:(FlutterMethodCall *)call {
    NSLog(@"Unbind account");
    [CloudPushSDK unbindAccount:^(CloudPushCallbackResult *res) {
        if (res.success) {
            NSLog(@"Unbind account success");
        } else {
            NSLog(@"Unbind account failed!  errorData: %@", res.error);
        }
    }];
}

@end
