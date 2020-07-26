package bi.konstrictor.urudandaza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;

public class InkoranyaMakuru extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "ringtone.mp3";
    private static final int DB_VERSION = 1;
    Context context;
    private Dao<Produit, Integer> dao_produit;
    private Dao<Personne, Integer> dao_client;
    private Dao<ActionStock, Integer> dao_stock;

    public InkoranyaMakuru(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public Dao<Produit, Integer> getDaoProduit() throws SQLException {
        return getDao(Produit.class);
    }
    public Dao<Personne, Integer> getDaoPersonne() throws SQLException {
        return getDao(Personne.class);
    }
    public Dao<ActionStock, Integer> getDaoActionStock() throws SQLException {
        return getDao(ActionStock.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Produit.class);
            TableUtils.createTableIfNotExists(connectionSource, Personne.class);
            TableUtils.createTableIfNotExists(connectionSource, ActionStock.class);

            database.execSQL("create trigger insertion_stock " +
                    "after insert on ActionStock for each row begin " +
                    "update Produit set quantite = quantite+NEW.quantite," +
                    " prix = NEW.prix where id = NEW.produit_id; end;");

            database.execSQL("create trigger modification_stock " +
                    "after update on ActionStock for each row begin " +
                    "update Produit set quantite = quantite-OLD.quantite+NEW.quantite, " +
                    "prix = NEW.prix where id = NEW.produit_id; end;");

        } catch (Exception e) {
            Log.e("INKORANYAMAKURU", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}
