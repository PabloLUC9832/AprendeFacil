package pablo_lucas.com.aprendefacil.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import pablo_lucas.com.aprendefacil.Entidades.FireBase.Usuario;
import pablo_lucas.com.aprendefacil.Persistencia.UsuarioDAO;
import pablo_lucas.com.aprendefacil.R;
import pablo_lucas.com.aprendefacil.Utilidades.Constantes;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre,txtCorreo,txtContraseña,txtContraseñaRepetida;
    private EditText txtFechaDeNacimiento;
    //private RadioGroup rgGenero;
    private CircleImageView fotoPerfil;
    private Button btnRegistar;
    private FirebaseAuth mAuth;// ...
    private FirebaseDatabase database;
    //private DatabaseReference referenceUsuario;
    private long fechaDeNacimiento;
    private ImagePicker imagePicker;
    private Uri fotoPerfilUri;
    private RadioButton rdHombre,rdMujer;

    private CameraImagePicker cameraPicker;
    private String pickerPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_registro);

        txtNombre = (EditText)findViewById(R.id.idRegistroNombre);
        txtCorreo = (EditText)findViewById(R.id.idRegistroCorreo);
        txtContraseña = (EditText)findViewById(R.id.idRegistroContraseña);
        txtContraseñaRepetida = (EditText)findViewById(R.id.idRegistroContraseñaRepetida);
        btnRegistar = (Button) findViewById(R.id.idRegistroRegistrar);
        txtFechaDeNacimiento = (EditText)findViewById(R.id.txtFechaDeNacimiento);
        //rgGenero = (RadioGroup) findViewById(R.id.rgGenero); //ES LO MISMO = rgGenero =findViewById(R.id.rgGenero);
        fotoPerfil = (CircleImageView)findViewById(R.id.fotoPerfil);
        rdHombre = (RadioButton)findViewById(R.id.rdHombre);
        rdMujer = (RadioButton)findViewById(R.id.rdMujer);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);

        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);


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
                Toast.makeText(RegistroActivity.this,"Error: "+s,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegistroActivity.this,"Error: "+s,Toast.LENGTH_SHORT).show();
            }
        });

        //referenceUsuario = database.getReference("Usuarios");

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegistroActivity.this);
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

        txtFechaDeNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog =new DatePickerDialog(RegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        txtFechaDeNacimiento.setText(fechaDeNacimientoTexto);
                    }
                },calendar.get(Calendar.YEAR)-20,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnRegistar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String correo = txtCorreo.getText().toString();
                final String nombre = txtNombre.getText().toString();

                ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected){
                    //Toast.makeText(view.getContext(),"Conectado a internet",Toast.LENGTH_LONG).show();


                    if(isValidEmail(correo) && validadContraseña() && validarNombre(nombre)){

                        String contraseña = txtContraseña.getText().toString() ;
                        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                                .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                        final String genero;
                                        if (rdHombre.isChecked()){
                                            genero = "Hombre";
                                        }else{
                                            genero = "Mujer";
                                        }

                                        if (fotoPerfilUri!=null) {

                                            UsuarioDAO.getInstancia().subirFotoUri(fotoPerfilUri, new UsuarioDAO.IDevolverUrlFoto() {
                                                @Override
                                                public void devolverUrlString(String url) {
                                                    Toast.makeText(RegistroActivity.this, "Registro exitoso :D", Toast.LENGTH_SHORT).show();
                                                    Usuario usuario = new Usuario();
                                                    usuario.setCorreo(correo);
                                                    usuario.setNombre(nombre);
                                                    usuario.setFechaDeNacimiento(fechaDeNacimiento);
                                                    usuario.setGenero(genero);
                                                    usuario.setFotoPerfilURL(url);
                                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                                    DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
                                                    reference.setValue(usuario);
                                                    finish();

                                                }
                                            });

                                        }else{

                                            Toast.makeText(RegistroActivity.this,"Registro exitoso :D",Toast.LENGTH_SHORT).show();
                                            Usuario usuario = new Usuario();
                                            usuario.setCorreo(correo);
                                            usuario.setNombre(nombre);
                                            usuario.setFechaDeNacimiento(fechaDeNacimiento);
                                            usuario.setGenero(genero);
                                            usuario.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                                            reference.setValue(usuario);
                                            finish();

                                        }


                                        } else {
                                            Toast.makeText(RegistroActivity.this,"Error al registrarse",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(RegistroActivity.this,"Campos vacios",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(view.getContext(),"Ha ocurrido un error, al parecer has perdido tu conexión a internet. Intentalo nuevamente.",Toast.LENGTH_LONG).show();
                }
//                SharedPreferences prefe=getSharedPreferences("correo",Context.MODE_PRIVATE);
//                prefe.getString("correoA","");
//                SharedPreferences preferencias=getSharedPreferences("correo",Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor=preferencias.edit();
//                editor.putString("correoA", correo);
//                editor.commit();
//                finish();
                shared(txtCorreo.getText().toString(),txtContraseña.getText().toString());
            }
        });

        Glide.with(this).load(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS).into(fotoPerfil);

    }

    //https://developer.android.com/training/data-storage/shared-preferences
    public void shared(String correo,String password){
                SharedPreferences preferencias1=getSharedPreferences("correo",Context.MODE_PRIVATE);
                SharedPreferences preferencias2=getSharedPreferences("password",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1=preferencias1.edit();
                SharedPreferences.Editor editor2=preferencias2.edit();
                editor1.putString("correoA", correo);
                editor2.putString("passwordA", password);
                editor1.commit();
                editor2.commit();
                //finish();
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

    public boolean validadContraseña(){
        String contraseña, contraseñaRepetida;
        contraseña = txtContraseña.getText().toString();
        contraseñaRepetida = txtContraseñaRepetida.getText().toString();
        if (contraseña.equals(contraseñaRepetida)){
            if (contraseña.length()>=6 && contraseña.length()<=16){
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


}
