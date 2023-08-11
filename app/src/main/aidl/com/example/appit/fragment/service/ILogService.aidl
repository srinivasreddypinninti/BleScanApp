// ILogService.aidl
package com.example.appit.fragment.service;

// Declare any non-default types here with import statements

interface ILogService {

   void log_d(String tag, String message);
   void log(in Message message);
}