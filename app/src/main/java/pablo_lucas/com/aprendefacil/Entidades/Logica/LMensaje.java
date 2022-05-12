package pablo_lucas.com.aprendefacil.Entidades.Logica;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pablo_lucas.com.aprendefacil.Entidades.FireBase.Mensaje;

public class LMensaje {

    private String key;
    private Mensaje mensaje;
    private LUsuario lusuario;

    public LMensaje(String key, Mensaje mensaje) {
        this.key = key;
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public long getCreatedTimestampLong(){
        return (Long)mensaje.getCreatedTimestamp();
    }

    public LUsuario getLusuario() {
        return lusuario;
    }

    public void setLusuario(LUsuario lusuario) {
        this.lusuario = lusuario;
    }

    public String fechaDeCreacionMensaje(){
        Date date = new Date(getCreatedTimestampLong());
        PrettyTime prettyTime = new PrettyTime(new Date(),Locale.getDefault());
        return prettyTime.format(date);
        /*Date date = new Date(getCreatedTimestampLong());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());//a pm o am
        return sdf.format(date); */

    }


}
