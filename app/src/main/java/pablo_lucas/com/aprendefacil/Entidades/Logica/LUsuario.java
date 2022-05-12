package pablo_lucas.com.aprendefacil.Entidades.Logica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pablo_lucas.com.aprendefacil.Entidades.FireBase.Usuario;
import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;

//L DE logica
public class LUsuario {

    private String key;
    private Usuario usuario;

    public LUsuario(String key, Usuario usuario) {
        this.key = key;
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String obtenerFechaDeCreacion(){
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        Date date =new Date(UsuarioDAO.getInstancia().fechaDeCreacion());
        return simpleDateFormat.format(date);
    }

    public String obtenerFechaDeUltimaVezQueSeLogueo(){
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        Date date =new Date(UsuarioDAO.getInstancia().fechaDeUltimaVezQueSeLogeoLong());
        return simpleDateFormat.format(date);
    }
}
