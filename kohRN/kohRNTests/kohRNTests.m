#import <XCTest/XCTest.h>
#import "JSEnvironment.h"

@interface kohRNTests : XCTestCase

@end

@implementation kohRNTests

-(void)runJSTest:(NSString*)testSuite
{
    NSString* testPath = [[NSBundle bundleForClass:[self class]] pathForResource:@"js/tests" ofType:@"js"];
    XCTestExpectation* exp = [self expectationWithDescription:testSuite];
    ExecutorHandler executor = [JSEnvironment executorWithBridge:[JSEnvironment bridgeWithURL:[NSURL fileURLWithPath:testPath]]];
    executor([NSString stringWithFormat:@"(function(cb) {					             \
              process = {exit: function(exitCode) {	     \
              if (exitCode){				             \
              cb(new Error('Test failed'),null) }	     \
              else {					                 \
              cb(null,0) }			                 \
              }};					                     \
              test.runner.%@()()})",testSuite],@[],
             ^(NSString* error, id returnValue){
                 XCTAssertNil(error);
                 XCTAssertNil(returnValue);
                 [exp fulfill];
             });
    [self waitForExpectationsWithTimeout:100 handler:^(NSError*  error) {
        XCTAssertNil(error);
    }];
}

-(void)testJSEnvironmentUnitTests
{
    [self runJSTest:@"test_unit"];
}

-(void)testJSEnvironmentIntegrationTests
{
    [self runJSTest:@"test_integration"];
}

@end
