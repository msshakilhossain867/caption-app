package com.citomoltd.romanticstatusandcaptions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_image extends AppCompatActivity {
    public static String EDITTEXT_ = "";

    TextView editText, profileNameId, socialAppName;
    CardView editTExtForDesgin, sizeTextLayout, bgFile_get, bgSizeId, download_image, copyText;
    Dialog dialog;
    Spinner spinner;
    RelativeLayout statusDropDown, logoDropDown, bgRelativeSize, waterMarkId, waterMarkId2;
    AppCompatImageView bgSet;
    float xDown = 0, yDown = 0;
    int file2 = 0;
    View nextImage;

    int imaegarr01[] = {
            R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3, R.drawable.bg_4, R.drawable.bg_5,
            R.drawable.bg_6, R.drawable.bg_7, R.drawable.bg_8, R.drawable.bg_9, R.drawable.bg_10,
            R.drawable.bg_11, R.drawable.bg_12, R.drawable.bg_13, R.drawable.bg_15, R.drawable.bg_16,
            R.drawable.bg_17, R.drawable.bg_18, R.drawable.bg_19, R.drawable.bg_20
    };
    int index = 0;

    private static final String KEY_PROFILE_NAME = "ProfileName";
    private static final String KEY_SOCIAL_NAME = "SocialName";
    private static final String KEY_FONT_SIZE = "FontSize";
    static final String KEY_FONT_FAMILY="FontFamily";

    private static final String SHARE_PRE_NAME = "MyFont";
    private static final String SHARE_PRE_NAME2 = "MyFont2";

    CircleImageView profileChangeId;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;

    SharedPreferences sharedPreferences_socialName;
    SharedPreferences.Editor editor_socialName;



    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AdHelper.initializeAd(this);




        initViews();
        initSharedPreferences();
        setupListeners();
        loadPreferences();
        setKeyFontFamily();

        editText.setText(EDITTEXT_);










    }//end method======================================================================

    private void initViews() {
        editText = findViewById(R.id.editText);
        editTExtForDesgin = findViewById(R.id.editTExtForDesgin);
        profileNameId = findViewById(R.id.profileNameId);
        profileChangeId = findViewById(R.id.profileChangeId);
        sizeTextLayout = findViewById(R.id.sizeTextLayout);
        spinner = findViewById(R.id.spinner);
        bgSet = findViewById(R.id.bgSet);
        nextImage = findViewById(R.id.nextImage);
        statusDropDown = findViewById(R.id.statusDropDown);
        logoDropDown = findViewById(R.id.logoDropDown);
        socialAppName = findViewById(R.id.socialAppName);
        bgFile_get = findViewById(R.id.bgFile_get);
        bgSizeId = findViewById(R.id.bgSizeId);
        bgRelativeSize = findViewById(R.id.bgRelativeSize);
        download_image = findViewById(R.id.download_image);
        waterMarkId = findViewById(R.id.waterMarkId);
        waterMarkId2 = findViewById(R.id.waterMarkId2);
        copyText = findViewById(R.id.copyText);
    }
    private void initSharedPreferences() {
        sharedPreferences2 = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor2 = sharedPreferences2.edit();

        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sharedPreferences_socialName = getSharedPreferences(SHARE_PRE_NAME2, MODE_PRIVATE);
        editor_socialName = sharedPreferences_socialName.edit();
    }
    private void setupListeners() {
        profileChangeId.setOnClickListener(v -> openProfilePicker());

        editTExtForDesgin.setOnClickListener(v -> showEditTextDialog());

        profileNameId.setOnClickListener(v -> showProfileNameDialog());

        socialAppName.setOnClickListener(v -> showSocialNameDialog());

        sizeTextLayout.setOnClickListener(v -> showSizeDialog());

        bgSet.setImageResource(R.drawable.bg_5);

        nextImage.setOnClickListener(v -> changeBackgroundImage());

        statusDropDown.setOnTouchListener(createDragTouchListener(statusDropDown));
        logoDropDown.setOnTouchListener(createDragTouchListener(logoDropDown));

        bgFile_get.setOnClickListener(v -> openBgPicker());

        bgSizeId.setOnClickListener(v -> showBgSizeDialog());

        download_image.setOnClickListener(v -> showDownloadDialog());

        waterMarkId2.setOnClickListener(v -> showWaterMarkToast());

        copyText.setOnClickListener(v -> copyToClipboard(EDITTEXT_));
    }

    private void loadPreferences() {
        String profileName = sharedPreferences.getString(KEY_PROFILE_NAME, "your name");
        profileNameId.setText(profileName);

        String socialName = sharedPreferences_socialName.getString(KEY_SOCIAL_NAME, "SocialName");
        socialAppName.setText(socialName);

        int fontSize = sharedPreferences.getInt(KEY_FONT_SIZE, 14);
        editText.setTextSize(fontSize);
    }

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener createDragTouchListener(View view) {
        return (v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    xDown = event.getX();
                    yDown = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float movedX = event.getX();
                    float movedY = event.getY();
                    float distanceX = movedX - xDown;
                    float distanceY = movedY - yDown;
                    view.setX(view.getX() + distanceX);
                    view.setY(view.getY() + distanceY);
                    break;
            }
            return true;
        };
    }

    // Dialog Methods

    private void showEditTextDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_text_desgin);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        ImageView deleteDialog = dialog.findViewById(R.id.deleteDialog);
        TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
        TextView okDialog = dialog.findViewById(R.id.okDialog);
        EditText editTextDialog = dialog.findViewById(R.id.editTextDialog);

        editTextDialog.setText(EDITTEXT_);

        deleteDialog.setOnClickListener(v -> editTextDialog.setText(""));
        cancelDialog.setOnClickListener(v -> dialog.dismiss());
        okDialog.setOnClickListener(v -> {
            if (!editTextDialog.getText().toString().trim().isEmpty()) {
                EDITTEXT_ = editTextDialog.getText().toString();
                editText.setText(EDITTEXT_);
                dialog.dismiss();
            } else {
                editTextDialog.setError("দয়া করে স্ট্যাটাস লিখুন");
                Toast.makeText(this, "দয়া করে স্ট্যাটাস লিখুন", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showProfileNameDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_name_get);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        EditText useNameEditText = dialog.findViewById(R.id.useNameEditText);
        Button userNameOk = dialog.findViewById(R.id.userNameOk);

        userNameOk.setOnClickListener(v -> {
            String name = useNameEditText.getText().toString().trim();
            if (!name.isEmpty()) {
                editor.putString(KEY_PROFILE_NAME, name);
                editor.apply();
                profileNameId.setText(name);
                dialog.dismiss();
            } else {
                useNameEditText.setError("Please enter your name");
            }
        });

        dialog.show();
    }

    private void showSocialNameDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.social_name_get);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        EditText useNameEditText2 = dialog.findViewById(R.id.useNameEditText2);
        Button userNameOk2 = dialog.findViewById(R.id.userNameOk2);

        userNameOk2.setOnClickListener(v -> {
            String name = useNameEditText2.getText().toString().trim();
            if (!name.isEmpty()) {
                editor_socialName.putString(KEY_SOCIAL_NAME, name);
                editor_socialName.apply();
                socialAppName.setText(name);
                dialog.dismiss();
            } else {
                useNameEditText2.setError("Please enter your name");
            }
        });

        dialog.show();
    }

    private void showSizeDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.size_font_layout_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        SeekBar seekbar = dialog.findViewById(R.id.seekbar);
        TextView fonSizeCont = dialog.findViewById(R.id.fonSizeCont);

        int intFontSize = sharedPreferences.getInt(KEY_FONT_SIZE, 14);
        fonSizeCont.setText(String.valueOf(intFontSize));
        editText.setTextSize(intFontSize);
        seekbar.setProgress(intFontSize);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
                fonSizeCont.setText(String.valueOf(position));
                editText.setTextSize(position);
                editor.putInt(KEY_FONT_SIZE, position);
                editor.apply();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        dialog.show();
    }

    private void changeBackgroundImage() {
        index++;
        if (index >= imaegarr01.length) index = 0;
        bgSet.setImageResource(imaegarr01[index]);
    }

    private void showBgSizeDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.size_layout_);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        SeekBar seekbar = dialog.findViewById(R.id.seekbar2);
        TextView fonSizeCont = dialog.findViewById(R.id.fonSizeCont2);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
                fonSizeCont.setText(String.valueOf(position));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, position);
                bgRelativeSize.setLayoutParams(layoutParams);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        dialog.show();
    }

    private void showDownloadDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.water_mark_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView withoutWaterMark = dialog.findViewById(R.id.withoutWaterMark);
        TextView withWaterMark = dialog.findViewById(R.id.withWaterMark);
        TextView exitId = dialog.findViewById(R.id.exitId);

        withoutWaterMark.setOnClickListener(v -> {

            AdHelper.showRewardedAd(Edit_image.this,() -> {
                waterMarkId.setVisibility(View.GONE);
                saveImage();
                dialog.dismiss();
            });

        });

        withWaterMark.setOnClickListener(v -> {
            saveImage();
            dialog.dismiss();
        });

        exitId.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showWaterMarkToast() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.water_mark_toast_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }




    // font family array==============
    private final SparseArray<Integer> fontMap = new SparseArray<>();

    private void initFontMap() {
        fontMap.put(1, R.font.normal);
        fontMap.put(2, R.font.italic_normal);
        fontMap.put(3, R.font.font_1_);
        fontMap.put(4, R.font.font_3);
        fontMap.put(5, R.font.font_4);
        fontMap.put(6, R.font.font_6);
        fontMap.put(7, R.font.font_7);
        fontMap.put(8, R.font.font_9);
        fontMap.put(9, R.font.font_10);
        fontMap.put(10, R.font.font_12);
        fontMap.put(11, R.font.font_15);
        fontMap.put(12, R.font.font_17);
        fontMap.put(13, R.font.font_20);
        fontMap.put(14, R.font.font_22);
        fontMap.put(15, R.font.font_23);
        fontMap.put(16, R.font.font_24);
    }

    private void setKeyFontFamily() {
        initFontMap();

        final String[] fontList = new String[]{"Select Font", "normal", "italic_normal", "font-3", "font-4", "font-5", "font-6", "font-7", "font-8", "font-9", "font-10", "font-11", "font-12",
                "font-13", "font-14", "font-15", "font-16"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fontList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        int savedPosition = sharedPreferences2.getInt(KEY_FONT_FAMILY, 0);
        spinner.setSelection(savedPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean firstCall = true; // spinner setSelection এ একবার ট্রিগার হয়, সেটা এড়াতে

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstCall) {
                    firstCall = false;
                    return;
                }
                if (position != 0) {
                    editor2.putInt(KEY_FONT_FAMILY, position);
                    editor2.apply();

                    Integer fontResId = fontMap.get(position);
                    if (fontResId != null) {
                        editText.setTypeface(ResourcesCompat.getFont(Edit_image.this, fontResId));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    //geImage profile===============================================================================================

    // 1. Profile image picker launcher (Android 13+)
    private final ActivityResultLauncher<PickVisualMediaRequest> profilePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    profileChangeId.setImageURI(uri);
                }
            });

    // 2. BG image picker launcher (Android 13+)
    private final ActivityResultLauncher<PickVisualMediaRequest> bgPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    bgSet.setImageURI(uri);
                }
            });

    // 3. পুরনো ভার্সনের জন্য fallback launcher
    private final ActivityResultLauncher<Intent> legacyProfilePicker =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uriImage = result.getData().getData();
                    profileChangeId.setImageURI(uriImage);
                }
            });

    private final ActivityResultLauncher<Intent> legacyBgPicker =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uriImage = result.getData().getData();
                    bgSet.setImageURI(uriImage);
                }
            });

    // 4. প্রোফাইল ইমেজ ওপেন মেথড
    public void openProfilePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (Photo Picker)
            profilePickerLauncher.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        } else {
            // Android 6 থেকে 12
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            legacyProfilePicker.launch(intent);
        }
    }

    // 5. ব্যাকগ্রাউন্ড ইমেজ ওপেন মেথড
    public void openBgPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (Photo Picker)
            bgPickerLauncher.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        } else {
            // Android 6 থেকে 12
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            legacyBgPicker.launch(intent);
        }
    }


    //download image code================================
    //download image code================================
    private void saveImage() {
        try {
            int width = bgRelativeSize.getWidth();
            int height = bgRelativeSize.getHeight();

            // Limit max size (e.g., 2000 px)
            int maxSize = 2000;
            float scale = Math.min(
                    (float) maxSize / width,
                    (float) maxSize / height
            );

            // নতুন width/height
            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

            // Bitmap বানাও
            Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(scale, scale);
            bgRelativeSize.draw(canvas);

            saveBitmapToDownloads(bitmap);

            bitmap.recycle(); // মেমরি free
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveBitmapToDownloads(Bitmap bitmap) {
        String fileName = "Status_" + System.currentTimeMillis() + ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API 29+ : Scoped Storage
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            values.put(MediaStore.Downloads.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            try {
                if (uri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                    Toast.makeText(this, "Saved to Downloads", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // API 23–28: পুরনো পদ্ধতি
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            File file = new File(downloadsDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Toast.makeText(this, "Saved to Downloads", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //permission==============================
    //permission==============================



    //copy text======================



}


