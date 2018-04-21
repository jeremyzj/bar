//
//  LoginManager.m
//  barMall
//
//  Created by 魔笛 on 2018/4/21.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "LoginManager.h"

@interface LoginManager()

@property (nonatomic, strong) NSString *loginToken;

@end

@implementation LoginManager

+ (instancetype)sharedInstance
{
  static LoginManager *instance = nil;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    instance = [[self alloc] init];
  });
  return instance;
}

+ (NSString *)loginToken {
  return [[LoginManager sharedInstance] loginToken];
}


- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()
RCT_EXPORT_METHOD(loginSuccess:(NSString *)loginData)
{
  
}

@end
