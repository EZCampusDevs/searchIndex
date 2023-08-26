package org.ezcampus.search.System;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class GlobalSettings
{
    public static final String BRAND = "searchIndex";
    public static final String BRAND_LONG = "SchedulePlatform-" + BRAND;
    public static boolean IS_DEBUG = false;

    public static final int THUMBNAIL_SIZE = 256;


    public static String Log_Dir = Paths.get(".", "logs").toString();
    public static String Log_File = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.log").format(new java.util.Date());

    public static String Token_File_Path = "./token.json";

    public static Boolean Is_Linux = false;
    
    public static String DB_User = "";
    public static String  DB_Password = "";
    
    public static String DB_Port = "";
    public static String  DB_Name = "";
    public static String  DB_Host = "";
    


}
