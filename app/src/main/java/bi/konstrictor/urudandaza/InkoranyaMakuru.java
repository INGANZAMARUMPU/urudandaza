package bi.konstrictor.urudandaza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import bi.konstrictor.urudandaza.models.Account;
import bi.konstrictor.urudandaza.models.ActionStock;
import bi.konstrictor.urudandaza.models.Cloture;
import bi.konstrictor.urudandaza.models.ClotureProduit;
import bi.konstrictor.urudandaza.models.Liquide;
import bi.konstrictor.urudandaza.models.Remboursement;
import bi.konstrictor.urudandaza.models.Personne;
import bi.konstrictor.urudandaza.models.Produit;
import bi.konstrictor.urudandaza.models.ProxyAction;
import bi.konstrictor.urudandaza.models.Signature;

public class InkoranyaMakuru extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "ringtone.mp3";
    private static final int DB_VERSION = 1;
    Context context;

    public InkoranyaMakuru(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, ClotureProduit.class);
            TableUtils.createTableIfNotExists(connectionSource, Produit.class);
            TableUtils.createTableIfNotExists(connectionSource, Personne.class);
            TableUtils.createTableIfNotExists(connectionSource, ActionStock.class);
            TableUtils.createTableIfNotExists(connectionSource, Cloture.class);
            TableUtils.createTableIfNotExists(connectionSource, Liquide.class);
            TableUtils.createTableIfNotExists(connectionSource, ProxyAction.class);
            TableUtils.createTableIfNotExists(connectionSource, Remboursement.class);
            TableUtils.createTableIfNotExists(connectionSource, Account.class);
            TableUtils.createTableIfNotExists(connectionSource, Signature.class);

            getLatestCloture();

        } catch (Exception e) {
            Log.e("INKORANYAMAKURU", e.getMessage());
        }
    }

    public Cloture getLatestCloture(){
        try {
            List<Cloture> clotures = getDao(Cloture.class).queryBuilder()
                    .where().eq("compiled", false).query();
            if (clotures.size() > 1) {
                for (int i = 0; i < clotures.size() - 1; i++) {
                    Cloture cloture = clotures.get(i);
                    cloture.compiled = true;
                    cloture.update(context);
                }
                return clotures.get(clotures.size() - 1);
            } else if (clotures.size() == 1) {
                return clotures.get(0);
            } else {
                Cloture cloture = new Cloture();
                cloture.create(context);
                return cloture;
            }
        }catch (SQLException e){
            Log.e("INKORANYAMAKURU ERREUR", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}
