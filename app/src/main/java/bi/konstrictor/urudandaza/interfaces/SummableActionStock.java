package bi.konstrictor.urudandaza.interfaces;

import bi.konstrictor.urudandaza.models.ActionStock;

public interface SummableActionStock {
    void addToTotals(ActionStock as);
    void refresh();
}
