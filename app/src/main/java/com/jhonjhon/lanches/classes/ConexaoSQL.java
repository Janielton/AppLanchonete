package com.jhonjhon.lanches.classes;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexaoSQL extends SQLiteOpenHelper{
    private static ConexaoSQL INSTANCIA_CONEXAO;
    private static final int VERSAO_DB = 1;
    private Context contexto;
    private static final String NOME_DB = "dbappjhon.sqlite";
    private static String DB_PATH = "/data/data/com.jhonjhon.lanches/databases/";
   // private static final String DATABASE_ALTER_PRODUTO_1 = "ALTER TABLE item_venda ADD COLUMN itens_extras TEXT;";
    //private static final String DATABASE_ALTER_PRODUTO_2 = "ALTER TABLE produto ADD COLUMN categoria TEXT;";

    public ConexaoSQL(Context context) {
        super(context, NOME_DB, null, VERSAO_DB);
        this.contexto = context;
    }

    public static ConexaoSQL getInstance(Context context){

        if(INSTANCIA_CONEXAO == null){
            INSTANCIA_CONEXAO = new ConexaoSQL(context);
        }
        return INSTANCIA_CONEXAO;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TabelaPedidos =
                "CREATE TABLE IF NOT EXISTS pedidos" +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "items TEXT," +
                "data TEXT," +
                "quantidade INTEREGER," +
                "valortotal REAL" +
                ")";
        sqLiteDatabase.execSQL(TabelaPedidos);

        String TabelaItems =
                "CREATE TABLE IF NOT EXISTS dadositems" +
                        "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "iditem TEXT," +
                        "nome TEXT," +
                        "resumo TEXT," +
                        "data_salvo TEXT," +
                        "foto BLOB," +
                        "valor REAL," +
                        "categoria INTEGER" +
                        ")";

        sqLiteDatabase.execSQL(TabelaItems);

        String TabelaIngre =
                "CREATE TABLE IF NOT EXISTS ingredientes" +
                        "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT," +
                        "valor REAL" +
                        ")";

        sqLiteDatabase.execSQL(TabelaIngre);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        if (oldVersion < 2) {
//         db.execSQL(TabelaCategoria);
//         db.execSQL(DATABASE_ALTER_PRODUTO_1);
//        }
//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_PRODUTO_2);
//        }
    }
}
