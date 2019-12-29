#import <Flutter/Flutter.h>

@interface PushPlugin : NSObject<FlutterPlugin>

@property (nonatomic, strong) FlutterEventSink eventSink;

+ (instancetype)shareInstance;

@end
