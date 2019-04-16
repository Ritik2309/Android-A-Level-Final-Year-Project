package com.ritik.finalproject;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class PhotosFragement extends Fragment {

    @Nullable
    Button takePhotoBTN, addPhotoBTN, changePhotoBTN;
    ImageView photoImageView;
    public String pathOfFile;
    Bitmap bitmap;
    TextView questionNumber;



    PhotosDBHelper MyImagesDB;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photos, container, false);
        MyImagesDB = new PhotosDBHelper(getActivity());
//Initializing the different buttons on the XML file so they can be used in this java
        takePhotoBTN = v.findViewById(R.id.takePhotoBtn);
        photoImageView = v.findViewById(R.id.PhotoImageView);
        questionNumber = v.findViewById(R.id.editQuestionNum);
        addPhotoBTN = v.findViewById(R.id.addPhotoBtn);
        changePhotoBTN = v.findViewById(R.id.changePhotoBtn);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        takePhotoBTN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePictureAction();

                    }
                }
        );

     addImage();
     changeImage();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //This method is called when we take a picture
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                bitmap = BitmapFactory.decodeFile(pathOfFile);
                photoImageView.setImageBitmap(bitmap);



            }
        }
    }

    public void addImage()
    {
        //This method is called in the onCreateView so that when the add photo button is pressed the user data
        //is taken and a method from the PhotosDBHelper is called.
        addPhotoBTN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = MyImagesDB.insertData(questionNumber.getText().toString(),
                                getPictureByteOfArray(bitmap));
                        //The method called from PhotosDBHelper returns a boolean and if it is returned as true then a success message is displayed otherwise an unsuccessful one is displayed.
                        if (isInserted == true)
                        {
                            Toast.makeText(getActivity(),"Image added!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"image not added!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );


    }

    public void changeImage()
    {
        //This method is called in the onCreateView so that when the change photo button is pressed the user data
        //is taken and a method from the PhotosDBHelper is called.
        changePhotoBTN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = MyImagesDB.updateData(questionNumber.getText().toString(),
                                getPictureByteOfArray(bitmap));
                        //The PhotoDBHelper method returns a boolean value that is used to determine if the change was successful or not.
                        if (isInserted == true)
                        {
                            Toast.makeText(getActivity(),"Image changed!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"image not changed!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }

    private void takePictureAction() {
        //This method is used to call the in built camera of the phone to take a picture.
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();
//This code below find the patchable for where the photo taken is stored and then calls the method startActivityForResult which gets the photo from its storage and then places in the ImageView of the application.
            if (photoFile != null) {
                pathOfFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.ritik.finalproject.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }

        }
    }

    private File createPhotoFile() {
        // This method is used to make sure that the app will have access to the storage on the phone and make sure that photos taken through the app are stored under 'pictures'
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //Sets up the naming of the images
        File storeageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //grants access to storage
        File image = null;
        // try catch to make sure that erroes are handled and then returns the image.
        try {
            image = File.createTempFile(name, ".jpg", storeageDir);
        } catch (IOException e) {
            Log.d("mylog", "Excep: " + e.toString());
        }
        return image;
    }

    public static byte[] getPictureByteOfArray(Bitmap bitmaps) {
        //This method is used to convert bitmap data to byte array data that can be stored ina  database.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //creates a new bytearraystream
        bitmaps.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);// compress bitmap data and converts it to byte array
        return byteArrayOutputStream.toByteArray();
    }

}
