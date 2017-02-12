package com.example.telerik.urbanissues.models;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.Date;
import java.util.UUID;

@ServerType("Users")
public class MyUser extends User {

}