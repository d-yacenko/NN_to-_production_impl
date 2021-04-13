package com.samsung.itschool.firereconize;

import android.graphics.Bitmap;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.util.Arrays;
import java.util.Collections;

public class Classifier {
    public static final int IMG_SIZE=224;
    public static final String[] IMAGENET_CLASSES ={"fire","nofire"};
    public static final float[] mean = {0.485f, 0.456f, 0.406f};
    public static final float[] std = {0.229f, 0.224f, 0.225f};
    Module model;


    public Classifier(String modelPath){
        model = Module.load(modelPath);
    }

    // приведение размера картинки и конвертация ее в тензор
    public Tensor preprocess(Bitmap bitmap, int size){
        bitmap = Bitmap.createScaledBitmap(bitmap,size,size,false);
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap,this.mean,this.std);
    }

    // найти номер максимального элемента в массиве
    public int argMax(float[] inputs){
        int maxIndex = -1;
        float maxvalue = 0.0f;
        for (int i = 0; i < inputs.length; i++){
            if(inputs[i] > maxvalue) {
                maxIndex = i;
                maxvalue = inputs[i];
            }
        }
        return maxIndex;
    }

    // использование НС
    public String predict(Bitmap bitmap){
        Tensor tensor = preprocess(bitmap,IMG_SIZE);
        IValue inputs = IValue.from(tensor);
        Tensor outputs = model.forward(inputs).toTensor();
        float[] scores = outputs.getDataAsFloatArray();
        int classIndex = argMax(scores);
        return IMAGENET_CLASSES[classIndex];
    }
}
