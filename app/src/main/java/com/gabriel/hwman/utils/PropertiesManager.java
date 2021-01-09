package com.gabriel.hwman.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesManager {

    private static Context context;

    public PropertiesManager(Context context) {
        this.context = context;
    }

    // create/reset properties file
    public boolean propertiesFileExists() {
        InputStream input = null;
        try {
            input = context.openFileInput("runebreaker.properties");
            System.out.println("Properties file exists.");
            return true;
        } catch (IOException io) {
            System.out.println("Properties file not found.");
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createPropertiesFile() {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = context.openFileOutput("gabrielhw.properties", Context.MODE_PRIVATE);
            prop.setProperty("DATABASE_VERSION", "2");
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearProperties() {
        createPropertiesFile();
    }

    // manipulate properties file
    public static void setProps(String propname, String value) {
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        try {
            input = context.openFileInput("gabrielhw.properties");
            prop.load(input);
            input.close();
            output = context.openFileOutput("gabrielhw.properties", Context.MODE_PRIVATE);
            prop.setProperty(propname, value);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProps(String propname) {
        if (context != null) {
            Properties prop = new Properties();
            InputStream input = null;
            String value = "";

            try {
                input = context.openFileInput("gabrielhw.properties");
                prop.load(input);
                value = prop.getProperty(propname);
                return value;
            } catch (IOException io) {
                io.printStackTrace();
                return "0";
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return null;
        }
    }


    // manipulate database info
    public static int getDbVersionNumber() {
        return Integer.parseInt(PropertiesManager.getProps("DATABASE_VERSION"));
    }

    public void upgradeDbVersionNumber() {
        int oldVersion = Integer.parseInt(PropertiesManager.getProps("DATABASE_VERSION"));
        int newVersion = oldVersion + 1;
        PropertiesManager.setProps("DATABASE_VERSION", String.valueOf(newVersion));
    }

}
