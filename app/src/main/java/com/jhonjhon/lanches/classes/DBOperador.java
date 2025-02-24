package com.jhonjhon.lanches.classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.jhonjhon.lanches.models.IngreModel;
import com.jhonjhon.lanches.models.osPedidosFeitos;

import java.util.ArrayList;
import java.util.List;

public class DBOperador {
    private final ConexaoSQL conectaSQL;

    public DBOperador(ConexaoSQL conectaSQL) {
        this.conectaSQL = conectaSQL;
    }

    //pedidos
    public long salvarPedidosDAO(osPedidosFeitos pedido) {
        SQLiteDatabase db = null;
        try {
            db = conectaSQL.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("items", pedido.getItems());
            values.put("data", pedido.getData());
            values.put("quantidade", pedido.getQuantidadeitens());
            values.put("valortotal", pedido.getValorTotal());
            long IdRegistro = db.insert("pedidos", null, values);
            return IdRegistro;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return 0;
    }

    public int getNumeroPedidos(){
        int numero = 0;
        SQLiteDatabase db = this.conectaSQL.getReadableDatabase();
        try {
            numero = (int) DatabaseUtils.queryNumEntries(db, "pedidos");
        }catch (Exception e){
            Log.e("ERRO", "getNumeroPedidos");
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return numero;
    }

    public List<osPedidosFeitos> getListaPedidos() {
        List<osPedidosFeitos> ListadeProdutos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM pedidos;";
        int i = 0;
        try {
            db = this.conectaSQL.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                osPedidosFeitos produtoTemp = null;
                do {
                    produtoTemp = new osPedidosFeitos();
                    produtoTemp.setId(cursor.getInt(0));
                    produtoTemp.setItems(cursor.getString(1));
                    produtoTemp.setData(cursor.getString(2));
                    produtoTemp.setQuantidadeitens(cursor.getInt(3));
                    produtoTemp.setValorTotal(cursor.getDouble(4));
                    i++;
                    ListadeProdutos.add(produtoTemp);
                } while (i < 200 && cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Erro", "Erro ao listar pedidos");
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return ListadeProdutos;
    }


    //Itens
    public long salvarItensDAO(osPedidosFeitos pedido) {
        SQLiteDatabase db = null;
        try {
            db = conectaSQL.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("iditem", pedido.getIDitems());
            values.put("nome", pedido.getItems());
            values.put("resumo", pedido.getResumo());
            values.put("data_salvo", pedido.getData());
            values.put("foto", pedido.getImagem());
            values.put("valor", pedido.getValorTotal());
            values.put("categoria", pedido.getCategoria());
            long IdRegistro = db.insert("dadositems", null, values);
            return IdRegistro;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return 0;
    }

    public List<osPedidosFeitos> getListaItens(int cat) {
        List<osPedidosFeitos> ListadeProdutos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM dadositems WHERE categoria = " + cat + "";
        try {

            db = this.conectaSQL.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                osPedidosFeitos produtoTemp = null;
                do {
                    produtoTemp = new osPedidosFeitos();
                    produtoTemp.setId(cursor.getInt(0));
                    produtoTemp.setIDitems(cursor.getString(1));
                    produtoTemp.setItems(cursor.getString(2));
                    produtoTemp.setResumo(cursor.getString(3));
                    produtoTemp.setData(cursor.getString(4));
                    produtoTemp.setImagem(cursor.getBlob(5));
                    produtoTemp.setValorTotal(cursor.getDouble(6));
                    produtoTemp.setCategoria(cursor.getInt(7));
                    ListadeProdutos.add(produtoTemp);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Erro", "Erro ao listar dados itens");
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return ListadeProdutos;
    }

    public List<osPedidosFeitos> getDetalheItens(int idItem) {
        List<osPedidosFeitos> ListadeProdutos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM dadositems WHERE id = " + idItem + "";
        try {
            db = this.conectaSQL.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                osPedidosFeitos produtoTemp = null;
                produtoTemp = new osPedidosFeitos();
                produtoTemp.setId(cursor.getInt(0));
                produtoTemp.setIDitems(cursor.getString(1));
                produtoTemp.setItems(cursor.getString(2));
                produtoTemp.setResumo(cursor.getString(3));
                produtoTemp.setData(cursor.getString(4));
                produtoTemp.setImagem(cursor.getBlob(5));
                produtoTemp.setValorTotal(cursor.getDouble(6));
                produtoTemp.setCategoria(cursor.getInt(7));
                ListadeProdutos.add(produtoTemp);

            }
        } catch (Exception e) {
            Log.e("Erro", "Erro ao listar dados itens");
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return ListadeProdutos;
    }

    public byte[] ImagemBlob(int id) {
        byte[] img = "Any".getBytes();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT foto FROM dadositems WHERE id = " + id + "";
        try {
            db = this.conectaSQL.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                img = cursor.getBlob(0);

            }
        } catch (Exception e) {
            Log.e("Erro", "Erro em ImagemBlob");
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return img;

    }

    //Ingredientes
    public long salvarIngredientesDB(IngreModel dado) {
        SQLiteDatabase db = null;
        try {
            db = conectaSQL.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nome", dado.getNome());
            values.put("valor", dado.getValor());
            long IdRegistro = db.insert("ingredientes", null, values);
            return IdRegistro;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<IngreModel> getListaIngre() {
        List<IngreModel> ListadeProdutos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM ingredientes";

        try {
            db = this.conectaSQL.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                IngreModel produtoTemp = null;
                do {
                    produtoTemp = new IngreModel();
                    produtoTemp.setId(cursor.getInt(0));
                    produtoTemp.setNome(cursor.getString(1));
                    produtoTemp.setValor(cursor.getString(2));

                    ListadeProdutos.add(produtoTemp);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Erro", "Erro ao listar ingredientes");
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return ListadeProdutos;
    }


    //operadorTodos
    public void LimparTabela(String nome) {
        SQLiteDatabase db = null;
        try {
            db = this.conectaSQL.getWritableDatabase();
            String query = "DELETE FROM " + nome + "";
            String query2 = "DELETE FROM sqlite_sequence WHERE name='" + nome + "'";
            String query3 = "VACUUM";
            db.execSQL(query);
            db.execSQL(query2);
            db.execSQL(query3);

        } catch (Exception e) {
            Log.d("ItemsTabela", "Não foi possivel limpar itens");
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }


}
