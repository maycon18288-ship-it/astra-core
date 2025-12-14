package astra.core.mysql.data.container;

import astra.core.Manager;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.interfaces.AbstractContainer;
import org.json.simple.JSONObject;

public class SkinsContainer extends AbstractContainer {
    public SkinsContainer(DataContainer dataContainer) {
        super(dataContainer);
    }

    public void setOriginalSkin(String name) {
        if (name != null && !name.isEmpty()) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            String value = Manager.getSkin(name, "value");
            String signature = Manager.getSkin(name, "signature");
            if (value != null && signature != null) {
                selected.put("name", name);
                selected.put("value", value);
                selected.put("signature", signature);
                this.dataContainer.set(selected.toString());
            }

            selected.clear();
        }
    }

    public void setSkin(String name, String value, String signature) {
        if (name != null && value != null && signature != null) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            selected.put("name", name);
            selected.put("value", value);
            selected.put("signature", signature);
            this.dataContainer.set(selected.toString());
            selected.clear();
        }
    }

    public String getSkin() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (String)json.get("name") : null;
    }

    public String getValue() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (String)json.get("value") : null;
    }

    public void setValue(String value) {
        if (value != null) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            selected.put("value", value);
            this.dataContainer.set(selected.toString());
            selected.clear();
        }
    }

    public void setSignature(String signature) {
        if (signature != null) {
            JSONObject selected = this.dataContainer.getAsJsonObject();
            selected.put("signature", signature);
            this.dataContainer.set(selected.toString());
            selected.clear();
        }
    }

    public String getSignature() {
        JSONObject json = this.dataContainer.getAsJsonObject();
        return json != null ? (String)json.get("signature") : null;
    }


    public void addSkin(String name) {
        if (name != null) {
            String value = Manager.getSkin(name, "value");
            String signature = Manager.getSkin(name, "signature");
            if (value != null && signature != null) {
                JSONObject selected = this.dataContainer.getAsJsonObject();
                selected.put(name, System.currentTimeMillis() + ":" + value);
                this.dataContainer.set(selected.toString());
                selected.clear();
            }

        }
    }
}