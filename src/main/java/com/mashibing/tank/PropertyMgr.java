package com.mashibing.tank;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Xiao
 */
public class PropertyMgr {
    private static Properties props = null;

    static {

    }

    private PropertyMgr(){
    }

    public static Properties getProps(){
        if (props == null){
            props = new Properties();
            try {
                props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }


    public static Object get(String key){
        if (props == null){
            return null;
        }
        return props.get(key);
    }

    public static void main(String[] args) {
        System.out.println(PropertyMgr.getProps().get("initTankCount"));
    }

}
