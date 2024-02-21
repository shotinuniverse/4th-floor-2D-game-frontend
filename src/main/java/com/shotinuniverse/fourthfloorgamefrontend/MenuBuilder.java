package com.shotinuniverse.fourthfloorgamefrontend;

import java.io.IOException;
import java.util.Map;

public interface MenuBuilder {

    public void setResolution();

    public Map<String, Object> getStructureMenu() throws IOException;

    public void paintMenu(Map<String, Object> structureMenu);
}
