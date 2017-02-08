//
//  GPSChat.hpp
//  GPS connect
//
//  Created by Tony Lu on 9/23/16.
//  Copyright © 2016 Tony Lu. All rights reserved.
//

#ifndef GPSChat_hpp
#define GPSChat_hpp

#include <boost/asio.hpp>
#include <boost/thread.hpp>
#include <boost/format.hpp>
#include <boost/array.hpp>
#include <boost/bind.hpp>
#include <iostream>
#include <chrono>
#include <string>
#include <iterator>
#include <thread>
#include <functional>
#include <stdio.h>
#include "MD5.hpp"

#define RECV_BUFFER_SIZE 1024 //incoming message size - bytes

#define INCOMING_MESSAGE_INTERVAL 300; //incoming message interval - Milliseconds

#define MAX_NUM 10000 //incoming message size - bytes

using namespace boost::asio::ip;
using namespace boost::asio;
using namespace std;

class Client
{
private:
    char data_[RECV_BUFFER_SIZE];
    string Host;
    int Port;
    io_service& io_service;
    tcp::socket socket;
public:
    Client(boost::asio::io_service& svc, std::string const& host, int port);
    void _connect();
    bool handle_write(const std::string& message);
    void read_handler(std::function<void(void)> func);
    std::string handle_read();
    void send(std::string const& message);
    void login(std::string const& user_id);
    bool logout(std::string const& user_id);
    bool isConnect();
};

#endif /* GPSChat_hpp */
