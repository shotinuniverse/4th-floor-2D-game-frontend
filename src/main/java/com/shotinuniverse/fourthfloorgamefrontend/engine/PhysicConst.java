package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PhysicConst {

    public static double characterGravity;
    public static double mobGravity;

    public PhysicConst() throws SQLException {
        ArrayList<Object> constants = SqlQuery.getObjects(getQueryForConstants());
        for(Object constElement: constants) {
            Map<String, Object> map = (HashMap) constElement;
            if (map.get("class").equals(SessionManager.packageProject + ".character")) {
                if (map.get("name").equals("gravity")) {
                    characterGravity = (float) map.get("value");
                }
            } else if(map.get("class").equals(SessionManager.packageProject + ".mob")) {
                if (map.get("name").equals("gravity")) {
                    mobGravity = (float) map.get("value");
                }
            }
        }
    }

    private static String getQueryForConstants() {
        return String.format("""
                select
                    physic_const.*
                from
                    physic_const as physic_const
                """);
    }
}
