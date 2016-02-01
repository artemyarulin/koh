#import "JSEnvironment.h"
#import "RNMEvaluator.h"
#import "RCTRootView.h"

@implementation JSEnvironment

RCTRootView* rootView;

+(RCTBridge*)bridgeWithURL:(NSURL*)bundleURL
{
    RCTBridge* bridge = [[RCTBridge alloc] initWithBundleURL:bundleURL
                                             moduleProvider:nil
                                              launchOptions:nil];
    rootView = [[RCTRootView alloc] initWithBridge:bridge
                                        moduleName:@"app"
                                 initialProperties:nil];
    return bridge;
}


+(ExecutorHandler)executorWithBridge:(RCTBridge*)bridge
{
    return ^(NSString *funcName, NSArray *args, ExecutorCallback cb) {
        [RNMEvaluator callAsyncFunction:bridge
                                   name:funcName
                                   args:args
                                     cb:cb];
    };
}

+(void)resetView { rootView = NULL; }

@end
