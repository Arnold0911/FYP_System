package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button select_image_button;
    private Button make_prediction;
    private ImageView img_view;
    private TextView text_view;
    private Bitmap bitmap;
//    private Button camerabtn;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void checkAndGetPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
        } else {
            Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_camera, container, false);


        select_image_button = root.findViewById(R.id.button);
        make_prediction = root.findViewById(R.id.button2);
        img_view = root.findViewById(R.id.imageView2);
        text_view = root.findViewById(R.id.textView);
//        camerabtn = root.findViewById(R.id.camerabtn);

//        // handling permissions
        checkAndGetPermissions();

        // need to modify the fileName
        List<String> labels = null;
        try {
            labels = FileUtils.readLabels(getContext(), "labels.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        select_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mssg", "button pressed");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                launcher.launch(intent);
            }
        });

        List<String> finalLabels = labels;
        make_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                    try {
                        Model model =  Model.newInstance(requireContext().getApplicationContext());
                        TensorImage tbuffer = TensorImage.fromBitmap(resized);
                        TensorBuffer byteBuffer = tbuffer.getTensorBuffer();

                        // Create inputs for the model.
                        //TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 28, 28, 3}, DataType.FLOAT32);
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                        inputFeature0.loadBuffer(byteBuffer.getBuffer());

                        // Run the model inference and get the result.
                        Model.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        int max = getMax(outputFeature0.getFloatArray());

                        text_view.setText(finalLabels.get(max));

                        // Release model resources when no longer used.
                        model.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        camerabtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                launcher.launch(camera);
//            }
//        });

        return root;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                                img_view.setImageURI(uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (data.getExtras() != null) {
                            bitmap = (Bitmap) data.getExtras().get("data");
                            img_view.setImageBitmap(bitmap);
                        }
                    }
                }
            }
    );

    private int getMax(float[] arr) {
        int ind = 0;
        float min = 0.0f;
        int length = arr.length - 1;

        for (int i = 0; i <= length; i++) {
            if (arr[i] > min) {
                min = arr[i];
                ind = i;
            }
        }
        return ind;
    }
}
