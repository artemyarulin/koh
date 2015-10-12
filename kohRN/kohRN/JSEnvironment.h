#import "RCTBridge.h"

typedef void (^ExecutorCallback)(NSString *error, id returnValue);
typedef void (^ExecutorHandler)(NSString *funcName, NSArray *args, ExecutorCallback cb);

@interface JSEnvironment: NSObject

+(RCTBridge*)bridgeWithURL:(NSURL*)bundleURL;
+(ExecutorHandler)executorWithBridge:(RCTBridge*)bridge;
+(void)resetView;

@end
