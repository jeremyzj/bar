//
//  LoginViewController.m
//  barMall
//
//  Created by 魔笛 on 2018/4/21.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "LoginViewController.h"
#import "JSBundleMananger.h"
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>

@interface LoginViewController ()

@property (nonnull, nonatomic, strong) RCTRootView *mRootView;

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
  
  NSURL *jsCodeLocation =  [[JSBundleMananger sharedInstance] jsCodeLocation];
  
  RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
                                                      moduleName:@"barMall"
                                               initialProperties:@{@"page": @"login"}
                                                   launchOptions:nil];
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
  self.mRootView = rootView;
  [self.view addSubview:self.mRootView];
}

- (void)viewDidLayoutSubviews {
  [super viewDidLayoutSubviews];
  self.mRootView.frame = self.view.bounds;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
