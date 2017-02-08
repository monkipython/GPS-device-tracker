//
//  GPSChat.cpp
//  GPS connect
//
//  Created by Tony Lu on 9/23/16.
//  Copyright © 2016 Tony Lu. All rights reserved.
//

#include "GPSChat.hpp"

    Client::Client(boost::asio::io_service& svc, std::string const& host, int port)
    : io_service(svc), socket(io_service), Host(host), Port(port)
    {
        memset(data_,'\0',sizeof(data_));
        
        tcp::resolver resolver(io_service);
        tcp::resolver::iterator endpoint = resolver.resolve(tcp::resolver::query(host, std::to_string(port)));
        connect(socket, endpoint);
        
        cout << "connect to" << host << ":" << std::to_string(port) << " !!!";
    }
    
    void Client::_connect(){
        if(!isConnect()){
            tcp::resolver resolver(io_service);
            tcp::resolver::iterator endpoint = resolver.resolve(tcp::resolver::query(Host, std::to_string(Port)));
            connect(socket, endpoint);
            cout << "connect to" << Host << ":" << std::to_string(Port) << " !!!";
        }
    }
    
    bool Client::handle_write(const std::string& message)
    {
        boost::system::error_code e;
        socket.write_some(buffer(message), e);
        if(e){
            return false;
        }
        return true;
        
    }
    
    void Client::read_handler(std::function<void(void)> func)
    {
        unsigned int interval = INCOMING_MESSAGE_INTERVAL;
        std::thread([func, interval]() {
            while (true)
            {
                func();
                std::this_thread::sleep_for(std::chrono::milliseconds(interval));
            }
        }).detach();
    }
    
    std::string Client::handle_read()
    {
        boost::system::error_code e;
        char buf[RECV_BUFFER_SIZE];
        size_t len=socket.read_some(buffer(buf), e);
        std::string s(buf, len);
        return len > 0 ? s: "";
    }
    
    void Client::send(std::string const& message) {
        
        Client::handle_write(message);
        cout << "send message: " << message << "\n";
    }
    
    void Client::login(std::string const& user_id){
        boost::system::error_code e;
        boost::format fmt = boost::format("{\"request_type\":\"login\", \"user_id\":\"%s\"}") % user_id;
        std::string message = fmt.str();
        //json data with request_type = login
        Client::send(message);
    }
    
    bool Client::logout(std::string const& user_id){
        boost::system::error_code e;
        boost::format fmt = boost::format("{\"request_type\":\"logout\", \"user_id\":\"%s\"}") % user_id;
        std::string message = fmt.str();
        //json data with request_type = login
        Client::send(message);
        usleep(300);
        
        socket.close();
        
        if(!socket.is_open()){
            cout << "logout!\n";
            return true;
        }else{
            cout << "logout failed!\n";
            return false;
        }
        
    }
    
    bool Client::isConnect(){
        return socket.is_open();
    }
