/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

class Emojifier {

    private static final String LOG_TAG = Emojifier.class.getSimpleName();
    private static Context con;
    /**
     * Method for detecting faces in a bitmap.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    static void detectFaces(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        con=context;

        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                // Log the classification probabilities for each face.
                whichEmoji(face);
            }

        }


        // Release the detector
        detector.release();
    }


    /**
     * Method for logging the classification probabilities.
     *
     * @param face The face to get the classification probabilities.
     */
    private static void whichEmoji(Face face) {
        // Log all the probabilities
        double smile_prob = face.getIsSmilingProbability();
        double left_eye_prob = face.getIsLeftEyeOpenProbability();
        double right_eye_prob = face.getIsRightEyeOpenProbability();
        Log.d(LOG_TAG, "whichEmoji: smilingProb = " + smile_prob);
        Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = "
                + left_eye_prob);
        Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = "
                + right_eye_prob);

        final double THRESHOLD_SMILE = 0.1;
        final double THRESHOLD_EYE = 0.25;
        boolean smiling = false;
        boolean left_eye_open = false;
        boolean right_eye_open = false;
        Emoji emoji;
        if (smile_prob >= THRESHOLD_SMILE)
            smiling = true;
        if (left_eye_prob >= THRESHOLD_EYE)
            left_eye_open = true;
        if (right_eye_prob >= THRESHOLD_EYE)
            right_eye_open = true;
        if (smiling && left_eye_open && right_eye_open)
            emoji = Emoji.smiling;
        else if (smiling && left_eye_open)
            emoji = Emoji.right_wink;
        else if (smiling && right_eye_open)
            emoji = Emoji.left_wink;
        else if (smiling)
            emoji = Emoji.closed_eye_smiling;
        else if (left_eye_open && right_eye_open)
            emoji = Emoji.frowning;
        else if (left_eye_open)
            emoji = Emoji.right_wink_frowning;
        else if (right_eye_open)
            emoji = Emoji.left_wink_frowning;
        else
            emoji = Emoji.close_eye_frowning;
        Toast.makeText(con, emoji.toString(), Toast.LENGTH_LONG).show();
    }


    enum Emoji {
        smiling, frowning, left_wink, right_wink, left_wink_frowning, right_wink_frowning, closed_eye_smiling, close_eye_frowning;
    }
}
