package com.hcmus.dreamers.foodmap.fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.AsyncTask.DownloadImageTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.Activity.EditRestaurantActivity;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.R;
import com.hcmus.dreamers.foodmap.common.FoodMapApiManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.define.ConstantValue;
import com.squareup.picasso.Callback;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final int REQUEST_OPEN_GALERY = 12345;
    private static final int REQUEST_TAKE_PHOTO = 11111;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Context context;
    EditRestaurantActivity editRestaurantActivity;

    Restaurant restaurant;

    ScrollView rootLayout;
    EditText txtResName;
    EditText txtAddress;
    EditText txtPhoneNumber;
    TextView lblOpenHour;
    TextView lblCloseHour;
    ImageView imgDescription;
    EditText txtDescription;
    ProgressBar progressBar;


    public RestaurantInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantInfoFragment newInstance(String param1, String param2) {
        RestaurantInfoFragment fragment = new RestaurantInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        context = getActivity();
        editRestaurantActivity =(EditRestaurantActivity) getActivity();

        if (editRestaurantActivity != null)
        {
            restaurant = editRestaurantActivity.restaurant;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = (ScrollView) inflater.inflate(
                R.layout.fragment_restaurant_info, container, false);

        takeReferenceFromResource();
        putDataToViews();
        handleClickEvent();

        return rootLayout;
    }

    private void handleClickEvent() {
        // Cập nhật giờ mở cửa
        lblOpenHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                lblOpenHour.setText(String.format("%02d:%02d", hourOfDay,minute));
                            }
                        },
                        hour, minute,
                        true);

                mTimePicker.show();
            }
        });


        // Cập nhật giờ đóng của
        lblCloseHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                lblCloseHour.setText(String.format("%02d:%02d", hourOfDay,minute));
                            }
                        },
                        hour, minute,
                        true);

                mTimePicker.show();
            }
        });

        imgDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đổi hình đại diện (Từ máy ảnh hoặc thư viện)

                PopupMenu popupMenu = new PopupMenu(context, view);
                // Inflating the popup using xml file
                popupMenu.getMenuInflater().inflate(R.menu.pick_image_menu, popupMenu.getMenu());
                // registering popup with OnMenuItemClickListener

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int itemID = item.getItemId();
                        switch (itemID){
                            case R.id.open_camera:
                                dispatchTakePictureIntent();
                                break;


                            case R.id.open_galery:

                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, REQUEST_OPEN_GALERY);

                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void putDataToViews() {
        DateFormat hourFormat = new SimpleDateFormat("hh:mm");
        String openingHour = hourFormat.format(restaurant.getTimeOpen());
        String closingHour = hourFormat.format(restaurant.getTimeClose());

        txtPhoneNumber.setText(restaurant.getPhoneNumber());
        txtResName.setText(restaurant.getName());
        txtAddress.setText(restaurant.getAddress());
        lblOpenHour.setText(openingHour);
        lblCloseHour.setText(closingHour);
        txtDescription.setText(restaurant.getDescription());

        if (restaurant.getUrlImage().matches("^(http|https)://.*"))
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);

        DownloadImageTask task = new DownloadImageTask(imgDescription,context);
        task.loadImageFromUrl(restaurant.getUrlImage(), new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        imgDescription.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void takeReferenceFromResource() {
        txtAddress = (EditText) rootLayout.findViewById(R.id.txtAddress);
        txtResName = (EditText) rootLayout.findViewById(R.id.txtResName);
        txtPhoneNumber = (EditText) rootLayout.findViewById(R.id.txtPhoneNumber);
        lblCloseHour = (TextView) rootLayout.findViewById(R.id.closeHour);
        lblOpenHour = (TextView) rootLayout.findViewById(R.id.openHour);
        imgDescription = (ImageView) rootLayout.findViewById(R.id.imageView);
        txtDescription = (EditText) rootLayout.findViewById(R.id.txtDescription);
        progressBar = (ProgressBar) rootLayout.findViewById(R.id.progressBar);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean checkValid(){

        boolean isValid = true;

        if (txtResName.length() == 0 && isValid)
        {
            Toast.makeText(getContext(),"Tên nhà hàng trống",Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (txtAddress.length() == 0 && isValid)
        {
            Toast.makeText(getContext(),"Địa chỉ nhà hàng trống",Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (txtPhoneNumber.length() != ConstantValue.PHONE_NUMBER_LENGTH && isValid)
        {
            Toast.makeText(getContext(),"Số điện thoại không đúng", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        String openHour = lblOpenHour.getText().toString();
        String closeHour = lblCloseHour.getText().toString();
        if (openHour.compareTo(closeHour) > 0 && isValid)
        {
            Toast.makeText(getContext(),"Giờ đóng cửa phải lớn hơn giờ mở cửa", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        return isValid;
    }

    private void setDataFromView() {
        // Get Date object
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
        Date openHour = new Date();
        Date closeHour = new Date();

        try{
            openHour = timeFormatter.parse(lblOpenHour.getText().toString());
            closeHour = timeFormatter.parse(lblCloseHour.getText().toString());
        }catch (Exception e){
            //This line should never run
        }


        restaurant.setName(txtResName.getText().toString());
        restaurant.setPhoneNumber(txtPhoneNumber.getText().toString());
        restaurant.setAddress(txtAddress.getText().toString());
        restaurant.setTimeOpen(openHour);
        restaurant.setTimeClose(closeHour);
        restaurant.setDescription(txtDescription.getText().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_item_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                //Toast.makeText(context,"Delete button hit",Toast.LENGTH_LONG).show();
                FoodMapApiManager.deleteRestaurant(restaurant, new TaskCompleteCallBack() {
                    @Override
                    public void OnTaskComplete(Object response) {
                        if((int)response == FoodMapApiManager.SUCCESS){
                            Intent intent = new Intent();

                            intent.putExtra("isDelete",true);
                            editRestaurantActivity.setResult(RESULT_OK, intent);
                            editRestaurantActivity.finish();
                        }else if((int)response == ConstantCODE.NOTINTERNET){
                            Toast.makeText(context, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Xóa quán thất bại!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return true;

            case R.id.action_done:
                //Toast.makeText(context,"Done button hit",Toast.LENGTH_LONG).show();
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Updating restaurant");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if(checkValid()){
                    setDataFromView();
                    FoodMapApiManager.updateRestaurant(restaurant, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                            progressDialog.dismiss();

                            if((int)response == FoodMapApiManager.SUCCESS) {
                                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                Gson gson = new Gson();
                                Intent intent = new Intent();
                                intent.putExtra("restJSON", gson.toJson(restaurant));

                                editRestaurantActivity.setResult(RESULT_OK, intent);
                                editRestaurantActivity.finish();
                            }else if((int)response == ConstantCODE.NOTINTERNET){
                                Toast.makeText(context, "Không có kết nối INTERNET!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Cập nhật nhà hàng thất bại!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    //Toast.makeText(context, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    Uri photoURI;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("CreateImageFile", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(context,
                        "com.hcmus.dreamers.foodmap",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            startCropImageActivity(photoURI);
        }

        if (requestCode == REQUEST_OPEN_GALERY && resultCode == RESULT_OK)
        {
            // Chuỗi URI trả về có dạng content://<path>
            Uri imageUri = Uri.parse(data.getDataString());
            String str = imageUri.getPath();
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgDescription.setImageURI(resultUri);
                uploadImageToServer(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .start(getContext(),this);
    }

    private void uploadImageToServer(Uri imageUri)
    {
        if (imageUri != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            // Upload hình lên Server
            FoodMapApiManager.uploadImage(getContext(),
                    restaurant.getId(), imageUri, new TaskCompleteCallBack() {
                        @Override
                        public void OnTaskComplete(Object response) {
                           progressBar.setVisibility(View.INVISIBLE);

                            String strResponse = (String) response;
                            if (strResponse.matches("^(http|https)://.*"))
                            {
                                restaurant.setUrlImage(strResponse);
                            }
                            else
                            {
                                Toast.makeText(getContext(), strResponse, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
