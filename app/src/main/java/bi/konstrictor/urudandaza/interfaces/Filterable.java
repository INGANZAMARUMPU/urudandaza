package bi.konstrictor.urudandaza.interfaces;

public interface Filterable {
    public void performFiltering(Boolean in, Boolean out, Boolean dette, Boolean perimee);
    public void cancelFiltering();
}
