//
//  LoginManager.h
//  barMall
//
//  Created by 魔笛 on 2018/4/21.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTLog.h>
#import <UIKit/UIKit.h>

@interface LoginManager : NSObject<RCTBridgeModule>

+ (NSString *)loginToken;

@end
