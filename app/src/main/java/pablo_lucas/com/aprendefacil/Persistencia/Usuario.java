package pablo_lucas.com.aprendefacil.Persistencia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    //String uid,nombre,correo,genero,fotoPerfilURL,fechaDeNacimiento;
    String uid,nombre,correo,genero,fotoPerfilURL;
    long fechaDeNacimiento;

    public Usuario(){

    }

    public Usuario(String uid,String nombre,String correo,String genero,String fotoPerfilURL,long fechaDeNacimiento){
        this.uid = uid;
        this.nombre = nombre;
        this.correo = correo;
        this.genero = genero;
        this.fotoPerfilURL = fotoPerfilURL;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("uid",uid);
        result.put("nombre",nombre);
        result.put("correo",correo);
        result.put("genero",genero);
        result.put("fotoPerfilURL",fotoPerfilURL);
        result.put("fechaDeNacimiento",fechaDeNacimiento);

        return result;
    }

}
