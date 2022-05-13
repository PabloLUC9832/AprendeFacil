package pablo_lucas.com.aprendefacil.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import pablo_lucas.com.aprendefacil.Persistencia.Usuario;
import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;
import pablo_lucas.com.aprendefacil.R;
import pablo_lucas.com.aprendefacil.Utilidades.Constantes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActualizarPerfilActivity extends AppCompatActivity {

    EditText etNombre,etCorreo,etPassword,etPasswordRepetida,etFechaDeNacimiento;
    RadioButton rdHombre,rdMujer;
    long fechaDeNacimiento;
    ImagePicker imagePicker;
    Uri fotoPerfilUri;
    Button btnActualizar;
    CircleImageView fotoPerfil;
    String pickerPath;
    CameraImagePicker cameraPicker;

    private FirebaseAuth mAuth;// ...
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    FirebaseUser userFB;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);
        //https://stackoverflow.com/questions/49357150/how-to-update-email-from-firebase-in-android
        uid = FirebaseAuth.getInstance().getUid();

        fotoPerfil = findViewById(R.id.fotoPerfilA);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etPasswordRepetida = findViewById(R.id.etPasswordRepetida);
        etFechaDeNacimiento = findViewById(R.id.etFechaDeNacimiento);

        rdHombre =  findViewById(R.id.rdHombreA);
        rdMujer =  findViewById(R.id.rdMujerA);

        btnActualizar = findViewById(R.id.btnActualizar);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);

        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        obtenerDatos();

        userFB = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path = list.get(0).getOriginalPath();
                    fotoPerfilUri = Uri.parse(path);
                    fotoPerfil.setImageURI(fotoPerfilUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(ActualizarPerfilActivity.this,"Error: "+s,Toast.LENGTH_SHORT).show();
            }
        });

        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(0).getOriginalPath();
                fotoPerfilUri = Uri.fromFile(new File(path));
                fotoPerfil.setImageURI(fotoPerfilUri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(ActualizarPerfilActivity.this,"Error: "+s,Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ActualizarPerfilActivity.this);
                dialog.setTitle("Foto de Perfil");

                String[] items = {"Galería","Cámara"};
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 : //Click galeria
                                imagePicker.pickImage();
                                break;
                            case 1://Click camara
                                pickerPath = cameraPicker.pickImage();
                                break;
                        }
                    }
                });
                AlertDialog dialogoConstruido = dialog.create();
                dialogoConstruido.show();
            }
        });

        etFechaDeNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog =new DatePickerDialog(ActualizarPerfilActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                        Calendar calendarResultado = Calendar.getInstance();
                        calendarResultado.set(Calendar.YEAR,year);
                        calendarResultado.set(Calendar.MONTH,mes);
                        calendarResultado.set(Calendar.DAY_OF_MONTH,dia);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = calendarResultado.getTime();
                        String fechaDeNacimientoTexto = simpleDateFormat.format(date);
                        fechaDeNacimiento = date.getTime();
                        etFechaDeNacimiento.setText(fechaDeNacimientoTexto);
                    }
                },calendar.get(Calendar.YEAR)-20,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String correo = etCorreo.getText().toString();
                final String nombre = etNombre.getText().toString();
                final String genero;
                if (rdHombre.isChecked()){
                    genero = "Hombre";
                }else{
                    genero = "Mujer";
                }

                    if(isValidEmail(correo) && validadPassword() && validarNombre(nombre)){

                        String contraseña = etPassword.getText().toString() ;

                        String key = mDatabase.child("Usuarios").push().getKey();


                                Usuario user = new Usuario(uid,nombre,correo,genero,fotoPerfilUri.toString(),fechaDeNacimiento);
                                //Usuario user = new Usuario(uid,nombre,correo,genero,url,fechaDeNacimiento);
                                Map<String,Object> userValues = user.toMap();

                                Map<String,Object> childUpdates = new HashMap<>();
                                childUpdates.put("Usuarios/"+uid,userValues);
                                //childUpdates.put("Usuarios/8NSEfhpSjMP0ooy6OFBAfkLTQcV2",userValues);

                                mDatabase.updateChildren(childUpdates);

                                //ACTUALIZARRR
                                SharedPreferences prefe=getSharedPreferences("correo", Context.MODE_PRIVATE);
                                SharedPreferences prefe2=getSharedPreferences("password", Context.MODE_PRIVATE);
                                String correoA = prefe.getString("correoA","no correo");
                                String passwordA = prefe2.getString("passwordA","no password");
                                //Toast.makeText(view.getContext(),correoA+"/"+passwordA,Toast.LENGTH_LONG).show();

                                AuthCredential credential = EmailAuthProvider.getCredential(correoA,passwordA);
                                userFB.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view.getContext(),"Usuario autenticado",Toast.LENGTH_LONG).show();
                                        FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                                        //UPDATE CORREO

                                        user2.updateEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(view.getContext(),"Se actualizo el corroe",Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(view.getContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                        user2.updatePassword(contraseña).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(view.getContext(),"Se actualizo la contraseña",Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(view.getContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });


                                    }
                                });

                                Toast.makeText(ActualizarPerfilActivity.this,"Se ha actualizado con exito",Toast.LENGTH_LONG).show();










                ////////AAA
/*                if(isValidEmail(correo) && validadPassword() && validarNombre(nombre)){

                    String contraseña = etPassword.getText().toString() ;

                    String key = mDatabase.child("Usuarios").push().getKey();
                    Usuario user = new Usuario(uid,nombre,correo,genero,Constantes.URL_FOTO_POR_DEFECTO_USUARIOS,fechaDeNacimiento);
                    Map<String,Object> userValues = user.toMap();

                    Map<String,Object> childUpdates = new HashMap<>();
                    childUpdates.put("Usuarios/"+uid,userValues);
                    //childUpdates.put("Usuarios/8NSEfhpSjMP0ooy6OFBAfkLTQcV2",userValues);

                    mDatabase.updateChildren(childUpdates);

                    //ACTUALIZARRR
                    SharedPreferences prefe=getSharedPreferences("correo", Context.MODE_PRIVATE);
                    SharedPreferences prefe2=getSharedPreferences("password", Context.MODE_PRIVATE);
                    String correoA = prefe.getString("correoA","no correo");
                    String passwordA = prefe2.getString("passwordA","no password");
                    //Toast.makeText(view.getContext(),correoA+"/"+passwordA,Toast.LENGTH_LONG).show();

                    AuthCredential credential = EmailAuthProvider.getCredential(correoA,passwordA);
                    userFB.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(),"Usuario autenticado",Toast.LENGTH_LONG).show();
                            FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                            //UPDATE CORREO
                            user2.updateEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(view.getContext(),"Se actualizo el corroe",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(view.getContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            user2.updatePassword(contraseña).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(view.getContext(),"Se actualizo la contraseña",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(view.getContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }
                    });

                    Toast.makeText(ActualizarPerfilActivity.this,"Se ha actualizado con exito",Toast.LENGTH_LONG).show();*/

                    ///AAAAAAAAAAAAA


                }else{
                    Toast.makeText(ActualizarPerfilActivity.this,"Ha ocurrido un error",Toast.LENGTH_SHORT).show();
                }


            }
        });

        Glide.with(this).load(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS).into(fotoPerfil);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imagePicker.submit(data);
        }else if(requestCode==Picker.PICK_IMAGE_CAMERA && resultCode==RESULT_OK){
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }

    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validadPassword(){
        String password, passwordRepetida;
        password = etPassword.getText().toString();
        passwordRepetida = etPasswordRepetida.getText().toString();
        if (password.equals(passwordRepetida)){
            if (password.length()>=6 && password.length()<=16){
                return true;
            }else return false;
        }else return false;
    }

    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void obtenerDatos(){
        Bundle datos = getIntent().getExtras();
        String nombre = datos.getString("nombre");
        String correo = datos.getString("correo");
        String genero = datos.getString("genero");
        String fotoPerfilURL = datos.getString("fotoPerfilURL");
        String fechaDeNacimiento = datos.getString("fechaDeNacimiento");
        String msj = nombre + "/" + correo + "/" + genero + "/" + fotoPerfilURL + "/" + fechaDeNacimiento + "/" ;
        Toast.makeText(this,msj,Toast.LENGTH_LONG).show();
    }

}