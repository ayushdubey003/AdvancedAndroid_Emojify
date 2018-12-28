package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class Emojifier {
    public static void detectfaces(Context context, Bitmap bitmap)
    {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);
        int l=faces.size();
        if(l==0)
            Toast.makeText(context,"No face detected",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,Integer.toString(l)+" face(s) detected",Toast.LENGTH_SHORT).show();
    }
}
