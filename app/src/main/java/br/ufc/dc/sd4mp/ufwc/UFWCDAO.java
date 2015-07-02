package br.ufc.dc.sd4mp.ufwc;

import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Marcel on 19/06/2015.
 */
public class UFWCDAO extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UFWC.db";
    public static final int DATABASE_VERSION = 4;

    public UFWCDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public UFWCDAO(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();

        // Tabela Usuario
        sql.append("create table usuario (");
        sql.append("id integer primary key,");
        sql.append("email text,");
        sql.append("password text,");
        sql.append("genero text)");
        db.execSQL(sql.toString());

        sql = new StringBuffer();
        // Tabela Banheiro
        sql.append("create table banheiro (");
        sql.append("id integer primary key,");
        sql.append("latitude numeric,");
        sql.append("longitude numeric,");
        sql.append("descricao text,");
        sql.append("genero text)");
        db.execSQL(sql.toString());

        sql = new StringBuffer();
        // Tabela Avaliacao
        sql.append("create table avaliacao (");
        sql.append("id integer primary key autoincrement,");
        sql.append("usuario_id integer,");
        sql.append("banheiro_id integer,");
        sql.append("higienizacao real,");
        sql.append("tem_porta integer,");
        sql.append("tem_espelho integer,");
        sql.append("tem_vaso_sanitario integer,");
        sql.append("tem_papel integer,");
        sql.append("tem_pia integer,");
        sql.append("tem_agua integer,");
        sql.append("tem_sabonete integer,");
        sql.append("tem_chuveiro integer,");
        sql.append("tem_acessibilidade integer)");
        db.execSQL(sql.toString());

        sql = new StringBuffer();
        // Tabela Comentario
        sql.append("create table comentario (");
        sql.append("id integer primary key autoincrement,");
        sql.append("usuario_id integer,");
        sql.append("banheiro_id integer,");
        sql.append("comment text)");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists banheiro");
        db.execSQL("drop table if exists avaliacao");
        db.execSQL("drop table if exists comentario");
        onCreate(db);
    }

    public void deleteAllFromUsuario() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("usuario", null, null);
    }

    public void deleteAllFromBanheiro() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("banheiro", null, null);
    }

    public void deleteAllFromAvaliacao() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("avaliacao", null, null);
    }

    public void deleteAllFromComentario() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("comentario", null, null);
    }

    public void createUsuario(User usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", usuario.getId());
        contentValues.put("email", usuario.getEmail());
        contentValues.put("password", usuario.getPassword());
        contentValues.put("genero", usuario.getGender());
        long id = db.insert("usuario", null, contentValues);
        Log.v("SQLite", "create usuario id = " + id);
    }

    public void createBanheiro(Bathroom banheiro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", banheiro.getId());
        contentValues.put("longitude", banheiro.getLongitude());
        contentValues.put("latitude", banheiro.getLatitude());
        contentValues.put("descricao", banheiro.getDescription());
        contentValues.put("genero", banheiro.getGender());
        long id = db.insert("banheiro", null, contentValues);
        Log.v("SQLite", "create banheiro id = " + id);
    }

    public void createAvaliacao(Review review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario_id", review.getUserId());
        contentValues.put("banheiro_id", review.getBathroomId());
        contentValues.put("higienizacao", review.getHygienizationGrade());
        contentValues.put("tem_porta", review.isHasDoor()? 1:0);
        contentValues.put("tem_espelho", review.isHasMirror()? 1:0);
        contentValues.put("tem_vaso_sanitario", review.isHasToilet()? 1:0);
        contentValues.put("tem_papel", review.isHasPaper()? 1:0);
        contentValues.put("tem_pia", review.isHasWashbasin()? 1:0);
        contentValues.put("tem_agua", review.isHasWater()? 1:0);
        contentValues.put("tem_sabonete", review.isHasSoap()? 1:0);
        contentValues.put("tem_chuveiro", review.isHasShower()? 1:0);
        contentValues.put("tem_acessibilidade", review.isHasAccessibility()? 1:0);
        long id = db.insert("avaliacao", null, contentValues);
        Log.v("SQLite", "create avaliacao id = " + id);
    }

    public void createComentario(Comment comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario_id", comentario.getUserId());
        contentValues.put("banheiro_id", comentario.getBathroomId());
        contentValues.put("comment", comentario.getText());
        long id = db.insert("comentario", null, contentValues);
        Log.v("SQLite", "create comentario id = " + id);
    }

    //recupera usuario pelo id
    public User retrieveUsuario(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, email, password, genero from usuario where id = ?", new String[] { Integer.toString(id) });
        User usuario = null;
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            usuario = new User();
            usuario.setId(result.getInt(0));
            usuario.setEmail(result.getString(1));
            usuario.setPassword(result.getString(2));
            usuario.setGender(result.getString(3));
        }
        return usuario;
    }

    //recupera usuario pelo email
    public User retrieveUsuarioByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, email, password, genero from usuario where email = ?", new String[] { email });
        User usuario = null;
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            usuario = new User();
            usuario.setId(result.getInt(0));
            usuario.setEmail(result.getString(1));
            usuario.setPassword(result.getString(2));
            usuario.setGender(result.getString(3));
        }
        return usuario;
    }

    //recupera banheiro pelo id
    public Bathroom retrieveBanheiro(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, longitude, latitude, descricao, genero from banheiro where id = ?", new String[] { Integer.toString(id) });
        Bathroom banheiro = null;
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            banheiro = new Bathroom();
            banheiro.setId(result.getInt(0));
            banheiro.setLongitude(Double.parseDouble(result.getString(1)));
            banheiro.setLatitude(Double.parseDouble(result.getString(2)));
            banheiro.setDescription(result.getString(3));
            banheiro.setGender(result.getString(4));
        }
        return banheiro;
    }

    //recupera banheiro pela descricao
    public Bathroom retrieveBanheiroByDescricao(String descricao) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, longitude, latitude, descricao, genero from banheiro where descricao = ?", new String[] { descricao });
        Bathroom banheiro = null;
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            banheiro = new Bathroom();
            banheiro.setId(result.getInt(0));
            banheiro.setLongitude(Double.parseDouble(result.getString(1)));
            banheiro.setLatitude(Double.parseDouble(result.getString(2)));
            banheiro.setDescription(result.getString(3));
            banheiro.setGender(result.getString(4));
        }
        return banheiro;
    }

    //recupera banheiro pela descricao e genero
    public Bathroom retrieveBanheiroByDescricaoAndGenero(String descricao, float rotacao) {
        String genero;

        if (rotacao > 0f)
            genero = "Masculino";
        else
            genero = "Feminino";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, longitude, latitude, descricao, genero from banheiro where descricao = ? and genero = ?", new String[] { descricao, genero });
        Bathroom banheiro = null;
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            banheiro = new Bathroom();
            banheiro.setId(result.getInt(0));
            banheiro.setLongitude(Double.parseDouble(result.getString(1)));
            banheiro.setLatitude(Double.parseDouble(result.getString(2)));
            banheiro.setDescription(result.getString(3));
            banheiro.setGender(result.getString(4));
        }
        return banheiro;
    }

    /*
    //recupera avaliacao
    public Review retrieveAvaliacao(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, usuario_id, banheiro_id, higienizacao, tem_porta, tem_espelho,tem_vaso_sanitario, tem_papel, tem_pia, tem_agua, tem_sabonete, tem_chuveiro, tem_acessibilidade) from avaliacao where id = ?", new String[] { Integer.toString(id) });
        Avaliacao avaliacao = null;
        if (result != null && result.getCount() > 0) {
            avaliacao = new Avaliacao();
            avaliacao.setId(result.getInt(0));
            avaliacao.setUsuario_id(Integer.parseInt(result.getString(1)));
            avaliacao.setBanheiro_id(Integer.parseInt(result.getString(2)));
            avaliacao.setHigienizacao(Integer.parseInt(result.getString(3)));
            avaliacao.setTem_porta(Boolean.parseBoolean(result.getString(4)));
            avaliacao.setTem_espelho(Boolean.parseBoolean(result.getString(5)));
            avaliacao.setTem_vaso_sanitario(Boolean.parseBoolean(result.getString(6)));
            avaliacao.setTem_papel(Boolean.parseBoolean(result.getString(7)));
            avaliacao.setTem_pia(Boolean.parseBoolean(result.getString(8)));
            avaliacao.setTem_agua(Boolean.parseBoolean(result.getString(9)));
            avaliacao.setTem_sabonete(Boolean.parseBoolean(result.getString(10)));
            avaliacao.setTem_chuveiro(Boolean.parseBoolean(result.getString(11)));
            avaliacao.setTem_acessibilidade(Boolean.parseBoolean(result.getString(12)));
        }
        return avaliacao;
    }
    */

    //recupera comentario
    public Comment retrieveComentario(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, usuario_id, banheiro_id, comment) from comentario where id = ?", new String[] { Integer.toString(id) });
        Comment comentario = null;
        if (result != null && result.getCount() > 0) {
            comentario = new Comment();
            comentario.setUserId(Integer.parseInt(result.getString(1)));
            comentario.setBathroomId(Integer.parseInt(result.getString(2)));
            comentario.setText(result.getString(3));
        }
        return comentario;
    }

    //update usuario
    public void updateUsuario(User usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", usuario.getEmail());
        contentValues.put("password", usuario.getPassword());
        contentValues.put("genero", usuario.getGender());
        db.update("usuario", contentValues, "id = ? ", new String[] { Integer.toString(usuario.getId()) });
    }

    //update banheiro
    public void updateBanheiro(Bathroom banheiro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("longitude", banheiro.getLongitude());
        contentValues.put("latitude", banheiro.getLatitude());
        contentValues.put("descricao", banheiro.getDescription());
        contentValues.put("genero", banheiro.getGender());
        db.update("banheiro", contentValues, "id = ? ", new String[] { Integer.toString(banheiro.getId()) });
    }

    /*
    //update avaliacao
    public void updateAvaliacao(Avaliacao avaliacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario_id", avaliacao.getUsuario_id());
        contentValues.put("banheiro_id", avaliacao.getBanheiro_id());
        contentValues.put("higienizacao", avaliacao.getHigienizacao());
        contentValues.put("tem_porta", avaliacao.isTem_porta());
        contentValues.put("tem_espelho", avaliacao.isTem_espelho());
        contentValues.put("tem_vaso_sanitario", avaliacao.isTem_vaso_sanitario());
        contentValues.put("tem_papel", avaliacao.isTem_papel());
        contentValues.put("tem_pia", avaliacao.isTem_pia());
        contentValues.put("tem_agua", avaliacao.isTem_agua());
        contentValues.put("tem_sabonete", avaliacao.isTem_sabonete());
        contentValues.put("tem_chuveiro", avaliacao.isTem_chuveiro());
        contentValues.put("tem_acessibilidade", avaliacao.isTem_acessibilidade());
        db.update("avaliacao", contentValues, "id = ? ", new String[]{Integer.toString(avaliacao.getId())});
    }
    */

    /*
    //update banheiro
    public void updateComentario(Comment comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario_id", comentario.getUserId());
        contentValues.put("banheiro_id", comentario.getBathroomId());
        contentValues.put("comment", comentario.getText());
        db.update("comentario", contentValues, "id = ? ", new String[] { Integer.toString(comentario.getId()) });
    }
    */

    //deleta usuario
    public void deleteUsuario(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("usuario", "id = ? ", new String[] { Integer.toString(id) });
    }

    //deleta banheiro
    public void deleteBanheiro(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("banheiro", "id = ? ", new String[] { Integer.toString(id) });
    }

    //deleta Avaliacao
    public void deleteAvaliacao(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("avaliacao", "id = ? ", new String[] { Integer.toString(id) });
    }

    //deleta comentario
    public void deleteComentario(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("comentario", "id = ? ", new String[] { Integer.toString(id) });
    }

    // List Usuarios
    public List<User> listUsuarios() {
        List<User> usuarios = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, email, password, genero from usuario order by id asc", null);
        if (result != null && result.getCount() > 0) {
            usuarios = new ArrayList<User>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                User usuario = new User();
                usuario.setId(result.getInt(0));
                usuario.setEmail(result.getString(1));
                usuario.setPassword(result.getString(2));
                usuario.setGender(result.getString(3));
                usuarios.add(usuario);
                result.moveToNext();
            }
        }
        return usuarios;
    }

    // List Banheiros
    public List<Bathroom> listBanheiros() {
        List<Bathroom> banheiros = new ArrayList<Bathroom>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, longitude, latitude, descricao, genero from banheiro order by id asc", null);
        if (result != null && result.getCount() > 0) {
            banheiros = new ArrayList<Bathroom>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Bathroom banheiro = new Bathroom();
                banheiro.setId(result.getInt(0));
                banheiro.setLongitude(Double.parseDouble(result.getString(1)));
                banheiro.setLatitude(Double.parseDouble(result.getString(2)));
                banheiro.setDescription(result.getString(3));
                banheiro.setGender(result.getString(4));
                banheiros.add(banheiro);
                result.moveToNext();
            }
        }
        return banheiros;
    }

    // List Avaliacoes
    public List<Review> listAvaliacoes() {
        List<Review> avaliacoes = new ArrayList<Review>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, usuario_id, banheiro_id, higienizacao, tem_porta, tem_espelho,tem_vaso_sanitario, tem_papel, tem_pia, tem_agua, tem_sabonete, tem_chuveiro, tem_acessibilidade from avaliacao order by id asc", null);
        if (result != null && result.getCount() > 0) {
            avaliacoes = new ArrayList<Review>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Review avaliacao = new Review();
                //avaliacao.setId(result.getInt(0));
                avaliacao.setUserId(Integer.parseInt(result.getString(1)));
                avaliacao.setBathroomId(Integer.parseInt(result.getString(2)));
                avaliacao.setHygienizationGrade(Double.parseDouble(result.getString(3)));
                avaliacao.setHasDoor(result.getString(4).equals("1") ? true : false);
                avaliacao.setHasMirror(result.getString(5).equals("1") ? true : false);
                avaliacao.setHasToilet(result.getString(6).equals("1") ? true : false);
                avaliacao.setHasPaper(result.getString(7).equals("1") ? true : false);
                avaliacao.setHasWashbasin(result.getString(8).equals("1") ? true : false);
                avaliacao.setHasWater(result.getString(9).equals("1") ? true : false);
                avaliacao.setHasSoap(result.getString(10).equals("1") ? true : false);
                avaliacao.setHasShower(result.getString(11).equals("1") ? true : false);
                avaliacao.setHasAccessibility(result.getString(12).equals("1") ? true : false);
                avaliacoes.add(avaliacao);
                result.moveToNext();
            }
        }
        return avaliacoes;
    }

    // List Avaliacoes pelo id do banheiro
    public List<Review> listAvaliacoesByIdBanheiro(int banheiroId) {
        List<Review> avaliacoes = new ArrayList<Review>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, usuario_id, banheiro_id, higienizacao, tem_porta, tem_espelho,tem_vaso_sanitario, tem_papel, tem_pia, tem_agua, tem_sabonete, tem_chuveiro, tem_acessibilidade from avaliacao where banheiro_id = ? order by id asc", new String[] { Integer.toString(banheiroId) });
        if (result != null && result.getCount() > 0) {
            avaliacoes = new ArrayList<Review>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Review avaliacao = new Review();
                //avaliacao.setId(result.getInt(0));
                avaliacao.setUserId(Integer.parseInt(result.getString(1)));
                avaliacao.setBathroomId(Integer.parseInt(result.getString(2)));
                avaliacao.setHygienizationGrade(Double.parseDouble(result.getString(3)));
                avaliacao.setHasDoor(result.getString(4).equals("1") ? true : false);
                avaliacao.setHasMirror(result.getString(5).equals("1") ? true : false);
                avaliacao.setHasToilet(result.getString(6).equals("1") ? true : false);
                avaliacao.setHasPaper(result.getString(7).equals("1") ? true : false);
                avaliacao.setHasWashbasin(result.getString(8).equals("1") ? true : false);
                avaliacao.setHasWater(result.getString(9).equals("1") ? true : false);
                avaliacao.setHasSoap(result.getString(10).equals("1") ? true : false);
                avaliacao.setHasShower(result.getString(11).equals("1") ? true : false);
                avaliacao.setHasAccessibility(result.getString(12).equals("1") ? true : false);
                avaliacoes.add(avaliacao);
                result.moveToNext();
            }
        }
        return avaliacoes;
    }

    // List Comments
    public List<Comment> listComments() {
        List<Comment> comentarios = new ArrayList<Comment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, usuario_id, banheiro_id, comment from comentario order by id", null);
        if (result != null && result.getCount() > 0) {
            comentarios = new ArrayList<Comment>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Comment comentario = new Comment();
                comentario.setUserId(Integer.parseInt(result.getString(1)));
                comentario.setBathroomId(Integer.parseInt(result.getString(2)));
                comentario.setText(result.getString(3));
                comentarios.add(comentario);
                result.moveToNext();
            }
        }
        return comentarios;
    }

    // List Comments pelo id do banheiro
    public List<Comment> listCommentsByIdBanheiro(int banheiroId) {
        List<Comment> comentarios = new ArrayList<Comment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select id, usuario_id, banheiro_id, comment from comentario where banheiro_id = ? order by id asc", new String[] { Integer.toString(banheiroId) });
        if (result != null && result.getCount() > 0) {
            comentarios = new ArrayList<Comment>();
            result.moveToFirst();
            while (result.isAfterLast() == false) {
                Comment comentario = new Comment();
                comentario.setUserId(Integer.parseInt(result.getString(1)));
                comentario.setBathroomId(Integer.parseInt(result.getString(2)));
                comentario.setText(result.getString(3));
                comentarios.add(comentario);
                result.moveToNext();
            }
        }
        return comentarios;
    }
}
