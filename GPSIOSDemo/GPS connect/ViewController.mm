//
//  ViewController.m
//  GPS connect
//
//  Created by Tony Lu on 9/17/16.
//  Copyright © 2016 Tony Lu. All rights reserved.
//

#import "ViewController.h"
#include <stdio.h>
#include <string>
#include <boost/asio.hpp>
#include <boost/thread/thread.hpp>
#include "GPSChat.hpp"

using namespace boost::asio::ip;
using namespace boost::asio;
using namespace std;

string HOST = "192.168.0.12";
int PORT    = 10008;
io_service service;

Client client(service, HOST, PORT);

@interface ViewController (){
    NSString *log_str;
}
@property (weak, nonatomic) IBOutlet UITextField *userID;
@property (weak, nonatomic) IBOutlet UITextField *toUserID;
@property (weak, nonatomic) IBOutlet UITextField *message;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    client._connect();
    client.read_handler(read_incoming_message);
    
    _userID.delegate = self;
    _toUserID.delegate = self;
    _message.delegate = self;
    
}
- (IBAction)disconnect:(id)sender {
    
    std::string uid = [[_userID text] cStringUsingEncoding:[NSString defaultCStringEncoding]];
    client.logout(uid);
}
- (IBAction)connect:(id)sender {
    if(!client.isConnect()){
        client._connect();
    }
    
    std::string uid = [[_userID text] cStringUsingEncoding:[NSString defaultCStringEncoding]];
    client.login(uid);
}
- (IBAction)petLost:(id)sender {
    
    NSString *str = [NSString stringWithFormat:@"{\"request_type\":\"pet_lost\",\"user_id\":\"%@\", \"dev_id\":\"%@\", \"lon\":\"111.518\",\"lat\":\"36.096\",\"datetime\":\"%@\"}", [_userID text], [_userID text], [NSDate date]];
    
    std::string json = [str cStringUsingEncoding:[NSString defaultCStringEncoding]];
    
    client.send(json);
    
}
- (IBAction)send:(id)sender {
    NSString *str = [NSString stringWithFormat:@"{\"request_type\":\"send\",\"user_id\":\"%@\", \"to_user_id\":\"%@\",\"message\":\"%@\",\"datetime\":\"%@\"}", [_userID text], [_toUserID text], [_message text], [NSDate date]];
    
    std::string json = [str cStringUsingEncoding:[NSString defaultCStringEncoding]];

    client.send(json);
}

- (IBAction)updateLocation:(id)sender {
    
    NSString *str = [NSString stringWithFormat:@"{\"request_type\":\"setup_wifi\", \"user_id\":\"%@\", \"dev_id\":\"%@\", \"wifi_name\":\"ee\",\"mac_address\":\"12:12:12:12:12\",\"lat\":\"70.232\",\"lon\":\"132.222\"}", [_userID text], [_userID text]];
    
    std::string json = [str cStringUsingEncoding:[NSString defaultCStringEncoding]];
    
    client.send(json);
    
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

void read_incoming_message() {
    std::string data = client.handle_read();
    NSString *incoming_msg = [NSString stringWithCString:data.c_str() encoding:[NSString defaultCStringEncoding]];
    //如果坚挺的消息是空字符串就代表服务器挂掉了！ 需要这边重启reconnect！！
    if([NSStringFromClass([incoming_msg class]) isEqualToString:@"__NSCFConstantString"]){
        client._connect();
    }
    NSLog(@"incoming message: %@ message_type: %@", incoming_msg, NSStringFromClass([incoming_msg class]));
}

@end
