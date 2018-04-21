//
//  JSBundleMananger.h
//  barMall
//
//  Created by 魔笛 on 2018/4/5.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JSBundleMananger : NSObject

@property (nonnull, nonatomic, strong) NSURL *jsCodeLocation;

+ (instancetype _Nullable )sharedInstance;

@end
