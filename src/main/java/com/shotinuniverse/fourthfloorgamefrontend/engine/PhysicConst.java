package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PhysicConst {

    public static int characterGravity;
    public static int mobGravity;

    public PhysicConst() throws SQLException {
        ArrayList<Object> consts = SqlQuery.getObjects(getQueryForConsts());
        for(Object constElement: consts) {
            Map<String, Object> map = (HashMap) constElement;
            if (map.get("class").equals(SessionManager.packageProject + ".character")) {
                if (map.get("name").equals("gravity")) {
                    characterGravity = (Integer) map.get("value");
                }
            } else if(map.get("class").equals(SessionManager.packageProject + ".mob")) {
                if (map.get("name").equals("gravity")) {
                    mobGravity = (Integer) map.get("value");
                }
            }
        }
    }

    private static String getQueryForConsts() {
        return String.format("""
                select
                    physic_const.*
                from
                    physic_const as physic_const
                """);
    }
}
