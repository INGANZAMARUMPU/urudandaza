package bi.konstrictor.urudandaza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.models.Client;
import bi.konstrictor.urudandaza.models.Offre;
import bi.konstrictor.urudandaza.models.Prix;
import bi.konstrictor.urudandaza.models.Produit;
import bi.konstrictor.urudandaza.models.Stock;
import bi.konstrictor.urudandaza.models.Vente;

public class InkoranyaMakuru extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "ringtone.mp3";
    private static final int DB_VERSION = 1;
    Context context;
    private Dao<Produit, Integer> dao_produit;
    private Dao<Prix, Integer> dao_prix;
    private Dao<Offre, Integer> dao_offre;
    private Dao<Client, Integer> dao_client;
    private Dao<Stock, Integer> dao_stock;
    private Dao<Vente, Integer> dao_vente;

    public InkoranyaMakuru(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public Dao<Produit, Integer> getDaoProduit() throws SQLException {
        return getDao(Produit.class);
    }
    public Dao<Prix, Integer> getDaoPrix() throws SQLException {
        return getDao(Prix.class);
    }
    public Dao<Offre, Integer> getDaoOffre() throws SQLException {
        return getDao(Offre.class);
    }
    public Dao<Client, Integer> getDaoClient() throws SQLException {
        return getDao(Client.class);
    }
    public Dao<Stock, Integer> getDaoStock() throws SQLException {
        return getDao(Stock.class);
    }
    public Dao<Vente, Integer> getDaoVente() throws SQLException {
        return getDao(Vente.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Produit.class);
            TableUtils.createTableIfNotExists(connectionSource, Prix.class);
            TableUtils.createTableIfNotExists(connectionSource, Offre.class);
            TableUtils.createTableIfNotExists(connectionSource, Client.class);
            TableUtils.createTableIfNotExists(connectionSource, Stock.class);
            TableUtils.createTableIfNotExists(connectionSource, Vente.class);
        } catch (Exception e) {
            Log.e("INKORANYAMAKURU", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}
