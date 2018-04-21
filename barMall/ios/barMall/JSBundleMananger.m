//
//  JSBundleMananger.m
//  barMall
//
//  Created by 魔笛 on 2018/4/5.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "JSBundleMananger.h"
#import <React/RCTBundleURLProvider.h>

@implementation JSBundleMananger

+ (instancetype)sharedInstance {
  static JSBundleMananger *sharedInstance;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    sharedInstance = [self new];
  });
  
  return sharedInstance;
}

- (NSURL *)jsCodeLocation {
  if (_jsCodeLocation == nil) {
    _jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
  }
  return _jsCodeLocation;
}


@end
