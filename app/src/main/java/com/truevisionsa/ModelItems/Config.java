package com.truevisionsa.ModelItems;

public class Config {

    private String ServerIp , ServerPort , ServerUserName , ServerPassword , DefaultSchema  , WebIp , WebPort;

    public Config(String serverIp, String serverPort, String serverUserName, String serverPassword, String defaultSchema , String webIp , String webPort) {
        ServerIp = serverIp;
        ServerPort = serverPort;
        ServerUserName = serverUserName;
        ServerPassword = serverPassword;
        DefaultSchema = defaultSchema;
        WebIp = webIp;
        WebPort = webPort;
    }

    public Config() {
    }


    public static final String TABLE_NAME = "config";
    public static final String COLUMN_SERVER_IP = "SERVER_IP";
    public static final String COLUMN_SERVER_PORT = "SERVER_PORT";
    public static final String COLUMN_SERVER_USERNAME = "SERVER_USERNAME";
    public static final String COLUMN_SERVER_PASSWORD = "SERVER_PASSWORD";
    public static final String COLUMN_DEFAULT_SCHEMA = "DEFAULT_SCHEMA";
    public static final String COLUMN_WEB_IP = "WEB_IP";
    public static final String COLUMN_WEB_PORT = "WEB_PORT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_SERVER_IP + " TEXT,"
                    + COLUMN_SERVER_PORT + " TEXT,"
                    + COLUMN_SERVER_USERNAME + " TEXT,"
                    + COLUMN_SERVER_PASSWORD + " TEXT,"
                    + COLUMN_DEFAULT_SCHEMA + " TEXT,"
                    + COLUMN_WEB_IP + " TEXT,"
                    + COLUMN_WEB_PORT + " TEXT"
                    + ")";

    public String getServerIp() {
        return ServerIp;
    }

    public void setServerIp(String serverIp) {
        ServerIp = serverIp;
    }

    public String getServerPort() {
        return ServerPort;
    }

    public void setServerPort(String serverPort) {
        ServerPort = serverPort;
    }

    public String getServerUserName() {
        return ServerUserName;
    }

    public void setServerUserName(String serverUserName) {
        ServerUserName = serverUserName;
    }

    public String getServerPassword() {
        return ServerPassword;
    }

    public void setServerPassword(String serverPassword) {
        ServerPassword = serverPassword;
    }

    public String getDefaultSchema() {
        return DefaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        DefaultSchema = defaultSchema;
    }

    public String getWebIp() {
        return WebIp;
    }

    public String getWebPort() {
        return WebPort;
    }

    public void setWebIp(String webIp) {
        WebIp = webIp;
    }

    public void setWebPort(String webPort) {
        WebPort = webPort;
    }
}
