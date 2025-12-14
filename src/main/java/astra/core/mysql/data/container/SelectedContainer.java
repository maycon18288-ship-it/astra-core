package astra.core.mysql.data.container;

import astra.core.api.titles.Title;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.interfaces.AbstractContainer;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class SelectedContainer extends AbstractContainer {

    public SelectedContainer(DataContainer dataContainer) {
        super(dataContainer);
    }

    public void setTitle(Title title) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        if (title == null) {
            selected.remove("titulos");
        } else {
            selected.put("titulos", title.getName());
        }
        this.dataContainer.set(selected.toString());
        selected.clear();
    }

    public void setIcon(String id) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        selected.put("icon", id);
        this.dataContainer.set(selected.toString());
        selected.clear();
    }

    public Title getTitle() {
        Object titleName = this.dataContainer.getAsJsonObject().get("titulos");
        if (titleName == null) {
            return null;
        }
        return Title.getTitle(titleName.toString());
    }
}
