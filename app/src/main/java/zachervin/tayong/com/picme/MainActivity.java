package zachervin.tayong.com.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button Image,share;
    ImageView ImageView;
    TextView Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Image = (Button) findViewById(R.id.ImageButton);
        ImageView = (ImageView)findViewById(R.id.ImageView);
        Text = (TextView)findViewById(R.id.textView);
        share = (Button)findViewById(R.id.sharebutton);

        share.setVisibility(View.INVISIBLE);
        ImageView.setVisibility(View.INVISIBLE);

        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        int chooser = 0;

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();


            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                chooser =1;
                ImageView.setVisibility(View.VISIBLE);
                Image.setVisibility(View.INVISIBLE);
                Text.setVisibility(View.INVISIBLE);

                ImageView.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

        }
        if(chooser == 1){
            share.setVisibility(View.VISIBLE);

        }



    }
    private Bitmap viewToBitmap(ImageView imageView, int width, int height) {
        Bitmap bitmap =  Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        imageView.draw(canvas);
        return bitmap;
    }


    public void share(View view){
        Bitmap bitmap = viewToBitmap(ImageView,ImageView.getWidth(),ImageView.getHeight());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "ImageDemo.jpeg");
        try{
            file.createNewFile();
            FileOutputStream fileOutput = new FileOutputStream(file);
            fileOutput.write(byteArray.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/ImageDemo.jpg"));
        startActivity(Intent.createChooser(intent, "share Image"));
    }


}
