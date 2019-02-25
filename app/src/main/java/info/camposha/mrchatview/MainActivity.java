package info.camposha.mrchatview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<ChatModel> data = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChatAdapter chatAdapter;
    FrameLayout frameLayout;
    EditText editText;
    /**
     * Camera Attachment
     */
    private static final int REQ_CAMERA_IMAGE = 101;
    private static final int RUNTIME_PERMISSIONS = 100;
    ImageView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_chat_screen);
        recyclerView = findViewById(R.id.rvMessages);
        cameraView = findViewById(R.id.imageView1);
        frameLayout = findViewById(R.id.frameLayout_chat_send);
        editText = findViewById(R.id.chat_input_editText);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(this, data);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    String currentTime = timeFormat.format(calendar.getTime());

                    ChatModel chatModel = new ChatModel();
                    chatModel.setId(Constants.SENDER);
                    chatModel.setMessageType("text");
                    chatModel.setTime(currentTime);
                    chatModel.setMessage(editText.getText().toString());
                    data.add(chatModel);


                    /**Receive same reply current static same reply
                     * return
                     */
                    ChatModel chatModel1 = new ChatModel();
                    chatModel1.setId(Constants.RECEIVER);
                    chatModel1.setTime(currentTime);
                    chatModel1.setMessage(editText.getText().toString());
                    chatModel1.setMessageType("text");
                    data.add(chatModel1);
                    editText.setText("");
                    chatAdapter.notifyDataSetChanged();

                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Can't send empty message", Toast.LENGTH_LONG).show();
                }
            }
        });


        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //check runtime permissions
                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        if (!hasPermissions(MainActivity.this, permissions)) {
                            ActivityCompat.requestPermissions(MainActivity.this, permissions, RUNTIME_PERMISSIONS);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQ_CAMERA_IMAGE);
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQ_CAMERA_IMAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * @param requestCode RUNTIME_PERMISSIONS = 100
     * @param permissions Camera,Storage
     * @param grantResults open camera
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RUNTIME_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CAMERA_IMAGE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
        try {
            if (requestCode == REQ_CAMERA_IMAGE && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data1.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                File finalFile = new File(getRealPathFromURI(tempUri));

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                String currentTime = timeFormat.format(calendar.getTime());
                Log.e("MainActivity", currentTime);

                ChatModel chatModel = new ChatModel();
                chatModel.setId(Constants.SENDER);
                chatModel.setMessageType("img");
                chatModel.setMessage(finalFile.getAbsolutePath());
                chatModel.setBitmap(photo);
                chatModel.setTime(currentTime);
                data.add(chatModel);

                /**Receive same image current same reply
                 */
                ChatModel chatModel1 = new ChatModel();
                chatModel1.setId(Constants.RECEIVER);
                chatModel1.setTime(currentTime);
                chatModel1.setMessage(finalFile.getAbsolutePath());
                chatModel1.setMessageType("img");
                chatModel1.setBitmap(photo);
                data.add(chatModel1);
            } else {
                Toast.makeText(this,
                        "Ooops ..! something happen", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = "";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        int index = 0;
        Cursor cursor =
                getContentResolver().query(uri, null, null, null, null);
        try {
            cursor.moveToFirst();
            index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor.getString(index);
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String perm : permissions) {
                if (ActivityCompat.checkSelfPermission(context, perm) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
//end