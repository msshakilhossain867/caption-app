package com.citomoltd.romanticstatusandcaptions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Status_View_activity extends AppCompatActivity {

    RecyclerView statusRecycler;

    Dialog dialog;
    private static final String KEY_FONT_FAMILY_="ts_tu";
    private static final String SHARE_PRE_NAME_St="status";
     int index = 0;
    private static final int REQUEST_WRITE_PERMISSION = 1001;
    private Bitmap bitmapToSave;


    TextView stToolbar;
    ImageView back_arrow;




    int imaegarr01[] = {
            R.font.normal, R.font.italic_normal, R.font.font_1_, R.font.font_3, R.font.font_4, R.font.font_6,
            R.font.font_7, R.font.font_9, R.font.font_10, R.font.font_12, R.font.font_15, R.font.font_17,
            R.font.font_20, R.font.font_22, R.font.font_23, R.font.font_24
    };



    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;





    HashMap<String,String>hashMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        statusRecycler = findViewById(R.id.statusRecycler);
        stToolbar=findViewById(R.id.stToolbar);
        back_arrow=findViewById(R.id.back_arrow);
        AdHelper.initializeAd(this);

        String title = getIntent().getStringExtra("TITTLE");
        int val = getIntent().getIntExtra("val", -1);

        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME_St, MODE_PRIVATE);
        editor = sharedPreferences.edit();




        if (val==0){
            banglaCaption();
        } else if (val==1) {
            romanticPremerChondo();
        } else if (val==2) {
            romanticPremerSMS();
        } else if (val==3) {
            premikaHappy();
        } else if (val==4) {
            monFine();
        } else if (val==5) {
            missSMS();
        } else if (val==6) {
            loveSMS();
        } else if (val==7) {
            loveStatus();
        } else if (val==8) {
            RomanticFbCaption();
        } else if (val==9) {
            premerSokal();
        }else if (val==10) {
            novoborsho();
        }else if (val==11) {
            eidSMS();
        }


        //recycler code

        StatusR statusR = new StatusR();
        statusRecycler.setAdapter(statusR);
        statusRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdHelper.loadBannerAd(this,findViewById(R.id.adContainer));
        ///===============================
        stToolbar.setText(title);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }//onCreate method start==========================================


    //Adapter==================
    private class StatusR extends RecyclerView.Adapter<StatusR.StatusViewHolder> {

        // ViewHolder class
        class StatusViewHolder extends RecyclerView.ViewHolder {

            TextView statusId, useName;
            LinearLayout downloadId, editId, shareId, copyId;
            CircleImageView userProfile;
            LinearLayout saveFile;
            RelativeLayout back_round;

            int fontIndex = 0; // ফন্টের ইন্ডেক্স প্রতিটি আইটেমের জন্য আলাদা

            public StatusViewHolder(@NonNull View itemView) {
                super(itemView);

                userProfile = itemView.findViewById(R.id.userProfile);
                statusId = itemView.findViewById(R.id.statusId);
                useName = itemView.findViewById(R.id.useName);
                downloadId = itemView.findViewById(R.id.downloadId);
                editId = itemView.findViewById(R.id.editId);
                shareId = itemView.findViewById(R.id.shareId);
                copyId = itemView.findViewById(R.id.copyId);
                saveFile = itemView.findViewById(R.id.saveFile);
                back_round = itemView.findViewById(R.id.back_round);

                // Edit click listener
                editId.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String title = arrayList.get(position).get("tittle");
                        Edit_image.EDITTEXT_ = title;
                        Intent intent = new Intent(Status_View_activity.this, Edit_image.class);
                        startActivity(intent);
                    }
                });

                // useName click listener - show dialog for name input
                useName.setOnClickListener(v -> {
                    Dialog dialog = new Dialog(Status_View_activity.this);
                    dialog.setContentView(R.layout.user_name_get);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                    EditText useNameEditText = dialog.findViewById(R.id.useNameEditText);
                    Button userNameOk = dialog.findViewById(R.id.userNameOk);

                    // Pre-fill EditText with saved name if available
                    String savedName = sharedPreferences.getString(KEY_FONT_FAMILY_, "");
                    if (!savedName.isEmpty()) {
                        useNameEditText.setText(savedName);
                    }

                    userNameOk.setOnClickListener(v1 -> {
                        String enteredName = useNameEditText.getText().toString().trim();
                        if (!enteredName.isEmpty()) {
                            editor.putString(KEY_FONT_FAMILY_, enteredName);
                            editor.apply();

                            useName.setText(enteredName);
                            dialog.dismiss();
                        } else {
                            useNameEditText.setError("Please enter your name");
                        }
                    });

                    dialog.show();
                });

                // download click listener
                downloadId.setOnClickListener(v -> {

                    AdHelper.showAdIfNeeded(Status_View_activity.this,() -> {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Deprecated কোড বাদ দিয়ে আধুনিক পদ্ধতি:
                            Bitmap bitmap = Bitmap.createBitmap(saveFile.getWidth(), saveFile.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            saveFile.draw(canvas);

                            saveImageWithPermissionCheck(bitmap);
                        }
                    });

                });


                // font change click listener
                statusId.setOnClickListener(v -> {
                    fontIndex++;
                    if (fontIndex == imaegarr01.length) {
                        fontIndex = 0;
                    }
                    statusId.setTypeface(ResourcesCompat.getFont(Status_View_activity.this, imaegarr01[fontIndex]));

                    if (fontIndex == 5 || fontIndex == 10) {
                        setRateUs();
                    }
                });

                // share click listener
                shareId.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String title = arrayList.get(position).get("tittle");
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, title);
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, "Share By");
                        startActivity(shareIntent);
                    }
                });

                // copy click listener
                copyId.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String title = arrayList.get(position).get("tittle");
                        copyToClipboard(title);
                    }
                });

            }
        }

        @NonNull
        @Override
        public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_status_file, parent, false);
            return new StatusViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
            HashMap<String, String> hashMap = arrayList.get(position);
            String title = hashMap.get("tittle");

            holder.statusId.setText(title);

            // Set useName from SharedPreferences or default
            String savedName = sharedPreferences.getString(KEY_FONT_FAMILY_, "TypeuseName");
            holder.useName.setText(savedName);

            // এখানে fontIndex এর ভিত্তিতে Typeface সেট করতে পারো, যদি চাও
            // holder.statusId.setTypeface(ResourcesCompat.getFont(Status_View_activity.this, imaegarr01[holder.fontIndex]));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }




    //bangla caption======================
    //bangla caption======================



    private void banglaCaption() {
        arrayList.clear();
        String[] captions = {
                "ღ۵༎🌷💜ღ৫༎_ একটি’༎ ভালোবাসার ‘༎সম্পর্ক’༎ হলো’༎ বাতাসের ‘༎মতো যা ‘༎দেখতে ‘༎পাওয়া ‘༎যায় না’༎ তবে ‘༎অনুভব’༎ করতে ‘༎পারা ‘༎যায়-!!-🍒🐰🌈",
                "დ︵♡︎♥︎༊༎তুমি ༅༎আসবে༅༎ বলেই সোনালী ༅༎স্বপ্ন ভীড় ༅༎করে আসে চোখে! তুমি ༅༎আসবে বলেই আগামী বলছে ༅༎দেখতে আসবো ༅༎তোকে🌼💖",
                "__🌺༅༎প্রিয়༅༎🌺বড় ইচ্ছে༅༎ ﹏ღ﹏ করছে আজকে… তোর ༅༎হাতটা ধরে হাঁটতে!༅༎ বড় ইচ্ছে ༅༎করছে আজকে.. ༅༎শুধু তোকেই ভালোবাসতে।-༊࿐💚🌻༅🌺",
                "🔵┊🔵┊┊🔵┊┊┊🔵•──────༏༏🌺༏༏─────কপালটা ༅༎খুবই༅༎ ভালো ༅༎তোমাকে ༅༎ভালোবাসছিলাম༅༎, তা না হলে ༅༎কোনদিনই༅༎ জানতে༅༎ পারতাম༅༎ না তুমি༅༎ এতোটা༅༎ সুন্দর।•──────༏༏🌺༏༏──────•┊┊┊🔵┊┊🔵┊🔵🔵",
                "_ღ۵༎🌷💜ღ৫༎আমার۵✿৫ মনে তোমার ༅༎জন্য যে জায়গাটা আছে সেটা কেউ ༅༎নিতে পারবে༅༎ না।﹏ღ﹏😌🌺💤🌺༅༎💝",
                "_💥🐰🌲ღ༎জীবনে অনেক কিছু বদলাতে দেখেছি༅༎, কিন্তু তোমার ༅༎মনের পবিত্র ভালোবাসা কখনো বদলাতে༅༎ দেখিনি||-🐰😍↗️🌻।",
                "•︵🥿🌸ღ۵༎_যে তোমাকে །প্রকৃত ভালোবাসে །সে তোমাকে །কখনো ভুলে །যাবে না। །তোমার কথা། সবসময় মনে །রাখার །চেষ্টা །করবে।❥━──➸➽❥❥━──➸➽",
                "️█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█আজকেღ۵༎ তুমি রাগ ༎করেছো༎ কষ্ট পাবো তাতে।༎ কালকে ༎যদি না ༎থাকি রাগ ༎দেখাবে ༎কাকে?۞💙█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█💙۞",
                "❥☘◉○❥তাকে༎ রাগাতে ༎আমার খুব ভালো ༎লাগে, রাগ༎ ভাঙ্গানোর পর ওর ওই༎ মুখের হাসিটা༎ আমার ༎খুব প্রিয়༎00:36 ━●━━━━━ 5:00⇆ㅤ◁ㅤ❚❚ㅤ▷",
                "─༅༎༅💙🌼🧷༅༎༅─বাগানের ༎ঝরা ফুল, ༎আকাশের༎ তারা! ༎আমি༎ কি থাকতে ༎পারি༎ তোমাকে༎ ছাড়া।",
                "ლ🥿ლ──🐰💚🌺সঠিক༎মানুষ༎কখনো༎ছেড়ে༎যায়༎না༎তারা༎সব༎সময়༎একটা༎কারণ༎খুঁজে༎কাছে༎থাকার༎জন্য༎<<-)♥️✨🧤🎋🎈",
                "_💥🐰🌲ღ༎আমার ༎হাজার༎ হাজার༎ পছন্দের༎ মানুষ ༎থাকলেও, আমি༎ দিনশেষে༎ তোমাকেই ༎বেছে নিবো।●❯────────────────❮●",
                "•︵🥿🌸ღ۵༎_তুমি༎ আমার শরীরের༎ ভালোবাস༎া হলে তোমাকে অনেক༎ আগেই ছেড়ে চলে যেতাম। ༎তুমি আমার মনের ভালোবাসা༎ তাই তোমাকে ধরে রেখেছি।◁━━━━◈🌸◈━━━━▷",
                "′⌒💜”!!🥿✰დকারো༎ ভালোবাসার মায়ায় ༎পড়ে গেলে সে মায়া কাটিয়ে বের হওয়াটা খুবই༎ কঠিন।༎︵།།ღ💜🍓,,",
                "__🌺༅༎প্রিয়༅༎আমি তোমাকে চাই, আর তুমি ༎অন্য কাউকে চাও, আসলেই ༎চাওয়া পাওয়ার ইচ্ছে গুলো এমনি হয়।🌼💖",
                "_ღ✿❃.•°࿐মানুষ সবচেয়ে বেশি বেহায়া  তার কাছেই হয়, যাকে সে নিজে থেকেও বেশি ভালোবাসে।||-🐰😍↗️🌻",
                "__۵ღ༎🌸যার༎ সাথে༎ বেশি রাগ করে ༎থাকতে ༎পারবেন না, তার সাথেই বেশি ༎রাগারাগি ༎বা ঝগড়া হয়।❥━──➸➽❥❥━──➸➽",
                "┏╮/╱💗╰ 🕘🥿╱/ ┗একবার༎ কারো ༎প্রতি মন আটকে༎ গেলে༎, আর༎ অন্য ༎কারো সৌন্দর্য ༎মনে ধরে ༎না।︵♡️💚🌺",
                "༊═══༆🌺তোমাকে ༎পাওয়ার পর আর কারো ༎কথা ভাবিনি। ༎তোমাকে ভালবেসে༎ আর কারো প্রতি অনুভূতি জাগেনি༎।-!!-🍒🐰🌈",
                "︵♡️💚তুমি༎ আমার ༎প্রয়োজন নও, তুমি আমার ༎অভ্যাস। যে অভ্যাস༎ অপরিবর্তনীয়।__💚🌻༅🌺",
                "┌───🤑♣🤑───┐🌻༆ 𝄞⋆⃝প্রিয় ⋆⃝𝄞༆🌻└───🤑♣🤑───┘তুমি মিশে༎ আছো ༎আমার ༎অস্তিত্বে༎, গেঁথে༎ আছো ༎আমার༎ মনে,༎মগজে।༎彡❤️‍🦹‍♀️🌻",
                "❥☘◉○❥প্রেমে না ༎পড়লে বুঝা ༎যায় না, বুকের ༎ভিতরে কত༎ রকমের অনুভূতি ༎লুকিয়ে থাকে।-!!-🍒🐰🌈",
                "▬▬🥿༄᭄࿐🌿আমি༎ তোমাকে ভেঙ্গে༎ চুরে ভালোবাসবো༎ স্বৈরাচারী প্রেমিকের ༎মতোই। শুধু এক ༎চেটিয়ে ভালোবেসে༎ যাবো।ლ🥿ლ──🐰💚🌺",
                "●───༆༄࿐🍁🌻🥿༄࿐ღ༎আমার তোমাকে চাই ,༎ তুমি ছাড়া আমার ༎এই পৃথিবী শূন্য༎। আমার শুধু তোমাকেই লাগবে༎।💗",
                "শোনো, কাজল চোখের মেয়েআমার দিবস কাটে, বিবশ হয়েতোমার চোখে চেয়ে।\n\n\nদহনের দিনে, কিছু মেঘ কিনেযদি ভাসে মধ্য দুপুরতবু মেয়ে জানে, তার চোখ মানেকারো বুক পদ্মপুকুর।\n\n\nএই যে মেয়ে, কজল চোখতোমার বুকে আমায় চেয়েতীব্র দাবির মিছিল হোক।\n\n\nতাকাস কেন?আঁকাস কেন, বুকের ভেতর আকাশ?কাজল চোখের মেয়েতুই তাকালে থমকে থাকেআমার বুকের বাঁ পাশ",
                "তোমার চোখ চেয়েছি বলে, এমন ডুবল আমার চোখঅমন অথৈ জ্বলে রোজ, আমার ডুব সাঁতারটা হোক।শোনো কাজল চোখের মেয়ে, আমি তোমার হব ঠিকতুমি ভীষণ অকূল পাথার, আমি একরোখা নাবিক।শোনো, জ্বল ছল ছল কাজল চোখের কন্যা সর্বনাশী,আমি তোমায় ভালোবাসি।",
                "დ︵♡︎♥︎༊༎প্রিয় শুনো! নিজের একটু খেয়াল রেখো༎, তোমার কিছু হলে আমাকে༎ এত ভালবাসবে কে!┇┇┇┇♥️┇┇┇💚┇┇💙┇❤️",
                "ღ۵༎🌷💜ღ۵༎আমি জানি༎ আমি ༎তোমার প্রেমে ༎পড়েছি কারণ আমার বাস্তব ༎শেষ পর্যন্ত আমার স্বপ্নের ༎চেয়ে ভালো।_💚🌻༅🌺",
                "ლ🥿ლ──🐰💚🌺যখন একটি মেয়ে প্রেমে পড়ে, আপনি এটি তার হাসিতে দেখতে পারেন, যখন একটি ছেলে প্রেমে পড়েন আপনি তার চোখে দেখতে পারেন।༎彡❤️‍🦹‍♀️🌻",
                "🌼💖আমি তোমার༎ প্রেমে পড়েছি,༎ কেন বা কিভাবে জানি না।༎ তবে༎ তোমার প্রেমে পড়েছি!",
                "<<-)♥️✨🧤🎄আমি আমার জীবনে ༎কোন কিছু দ্বারা༎ সংক্রামিত হতে চাইনি, ༎কিন্তু আমি ༎তোমার অবিরাম༎ ভালোবাসায়༎ আক্রান্ত হতে চাই।✧🌺✧                 ┊┊┊⇣❥                  ┊┊⇣❥                  ┊⇣❥                   ⇣💖",
                "༎彡❤️‍🦹‍♀️🌻তোমার༎ ভালোবাসা ছাড়া আমি কখনও༎ সম্পন্ন হতে༎ পারতাম না।●❯────────────────❮●",
                "__🌺༅༎প্রিয়༅༎আপনার ༎সাথে দেখা ༎কর༎া ভাগ্য ছিল༎, আপনার বন্ধু হওয়া༎ একটি পছন্দ ছিল, কিন্তু আপনার༎ প্রেমে পড়া༎ আমার নিয়ন্ত্রণের༎ বাইরে ছিল।༎◁━━━━◈🌸◈━━━━▷",
                "_ღ✿❃.•°࿐প্রথম༎ প্রেম ༎হওয়া দুর্দান্ত হতে༎ পারে, ༎তবে শেষ ༎প্রেম নিখুঁত।ლ🥿ল──🐰💚🌺",
                "༊᭄●══ ❥তোমাকে༎ ছাড়া ༎আমি কিছুই না,༎ কিন্তু তোমার༎ সাথে ༎আমি সবকিছু।༎ একসাথে ༎আমরা সবকিছু।﹏ღ﹏😌🌺💤__🌺༅",
                "─༅༎•🌺🌸༅༎•─যে ব্যক্তি༎ আপনাকে সত্যিকারের ༎ভালোবাসে সে আপনাকে পরিস্থিতি যাই༎ হোক না কেন যেতে ༎দেবে না।༉_💚<🐰",
                "__🌺’༉༎তুমি আমার স্বর্গ ༎এবং আমি আনন্দের༎ সাথে༎ তোমার ༎উপর আজীবন আটকে ༎থাকব।||-🐰😍↗️🌻",
                "__৫ღ༎🌸দিনের༎ বেলায়, তুমি আমার জীবনের༎ আলো।༎ রাতের বেলা, তুমি༎ আমার༎ পথের তারা।❥━──➸➽❥❥━──➸➽",
                "__ღ༉৫🥿আমি༎ তোমাকে এতটাই ভালোবাসি যে যদি༎ আমাকে তোমার আর༎ চকলেটের মধ্যে༎ একটা বেছে নিতে হয়, আমি༎ তোমাকে বেছে নেব।00:36 ━●━━━━━ 5:00⇆ㅤ◁ㅤ❚❚ㅤ▷",
                "__💜🌻༅🌺আমি জানি༎ না আমার ভবিষ্যৎ কি ༎আছে, কিন্তু আমি ༎༎আশা করছি ༎তুমি এতে আছো।┇┇┇┇♥️┇┇┇💚┇┇💙┇❤️",
                "┏╮/╱💗╰ 🕘🥿╱/ ┗আমি তোমাকে ༎খুশি দেখতে ভালোবাসি༎ এবং আমার ༎সবচেয়ে বড় পুরস্কার ༎তোমাকে ༎হাসতে দেখা💗💗।",
                "༊═══༆🌺তুমি༎ আমার ༎স্বপ্নের চেয়েও বেশি,༎ তুমি প্রাপ্য༎ থেকেও বেশি এবং আমার প্রয়োজনের চেয়েও বেশি।༎ আমি༎ তোমাকে প্রতিদিন ༎আরও༎ বেশি করে ভালোবাসি।-༊࿐",
                "︵♡️💚তোমার যত্ন আমাকে সবসময় অবাক করে তোলে যে তুমি ছাড়া জীবন কেমন হতো। তুমি আমার হিরো এবং আমি তোমাকে চিরকাল ভালোবাসি।༅༎ ﹏ღ﹏",
                "┌───🤑♣🤑───┐🌻༆ 𝄞⋆⃝প্রিয় ⋆⃝𝄞༆🌻└───🤑♣🤑───┘আপনার হাসি আক্ষরিক অর্থে আমার জীবনে দেখা সবচেয়ে সুন্দর জিনিস।৫✿৫",
                "༊” I 𝑸𝒂𝒍𝒍-‘༊﹏ღ﹏”তোমার জন্য আমার হৃদয় কখনই ভাঙবে না। তোমার জন্য আমার হাসি কখনো ম্লান হবে না। তোমার প্রতি আমার ভালোবাসা কখনো শেষ হবে না। আমি তোমাকে ভালোবাসি!||-🐰😍↗️🌻",
                "▬▬🥿༄᭄࿐🌿আমি জানি না কেন আমি তোমার প্রেমে পড়েছি। কিন্তু আমি নিশ্চিত যে তুমি আমার ভাগ্য।⎯͢⎯⃝🥵⎯͢⎯⃝",
                "৫ღ༎🌷💜ღ৫༎আমি তোমাকে চাই, তোমার ভালোবাসা ছাড়া আমার জীবন শুন্য। আমি শুধু তোমাকে চাই।",
                "ল🥿ল──🐰💚🌺আমি তোমাকে আজীবন ভালোবাসবো, কারণ তুমি আমার জীবনের সবচেয়ে মূল্যবান উপহার।",
                "🌼💖আমি তোমার প্রতি আমার ভালোবাসা প্রকাশ করতে পারি না, কারণ তুমি আমার হৃদয়ের রাজকুমারী।",
                "<<-)♥️✨🧤🎄আমার জীবনের সবকিছু তোমার জন্য। তুমি আমার সবকিছু।",
                "༎彡❤️‍🦹‍♀️🌻তোমার ভালোবাসা আমার জীবনের আলো। আমি তোমাকে চিরদিন ভালোবাসবো।",
                "__🌺༅༎প্রিয়༅༎আমার হৃদয় শুধুমাত্র তোমার জন্য। তুমি আমার সব।",
                "_ღ✿❃.•°࿐আমার প্রথম এবং শেষ ভালোবাসা তুমি।",
                "༊᭄●══ ❥তুমি ছাড়া আমি কিছুই না। তুমি আমার জীবন।",
                "─༅༎•🌺🌸༅༎•─যে ব্যক্তি তোমাকে ভালোবাসে সে কখনো তোমাকে ছাড়বে না।",
                "__🌺’༉༎তুমি আমার স্বপ্ন এবং সুখ। আমি তোমার জন্য আছি।",
                "__৫ღ༎🌸তুমি আমার জীবনের আলো এবং তারারা।",
                "__ღ༉৫🥿আমি তোমাকে চকলেটের মতো পছন্দ করি।",
                "__💜🌻༅🌺আমি তোমার সঙ্গে ভবিষ্যৎ দেখতে চাই।",
                "┏╮/╱💗╰ 🕘🥿╱/ ┗তুমি হাসলে আমি খুশি হই।",
                "༊═══༆🌺তুমি আমার প্রয়োজন এবং প্রিয়। আমি তোমাকে ভালোবাসি।",
                "︵♡️💚তুমি আমার হিরো এবং আমি তোমাকে চিরকাল ভালোবাসবো।",
                "┌───🤑♣🤑───┐🌻༆ তোমার হাসি আমার জীবনের সেরা উপহার।",
                "༊” I 𝑸𝒂𝒍𝒍-‘༊﹏ღ﹏”তোমার জন্য আমার হৃদয় সব সময় খোলা। আমি তোমাকে ভালোবাসি!",
                "▬▬🥿༄᭄࿐🌿তুমি আমার ভাগ্য। আমি তোমাকে কখনো ছাড়বো না।"
        };



        for (String caption : captions) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("tittle", caption);
            arrayList.add(hashMap);
        }
    }


    //romantic premer chondo===================
    //romantic premer chondo===================
    private void romanticPremerChondo(){

        String[] chondoList = {
                "ღ_ღ_ তুমি আমার রঙিন স্বপ্ন, শিল্পীর রঙ্গে ছবি. তুমি আমার চাঁদের আলো, সকাল বেলার রবি.. তুমি আমর নদীর মাঝে একটি মাত্র কূল, তুমি আমার ভালবাসার শিউলি বকুল ফুল…….  \n",
                "ღ_ღ_ দিন যায় দিন আসে, সময়ের স্রোতে ভাসে. কেউ কাঁদে কেউ হাঁসে, তাতে কি যায় আসে. খুঁজে দেখো আশে পাশে, কেউ তোমায় তার জীবনের চেয়ে বেশি ভালবাসে…….   \n",
                "ღ_ღ_ আমার শোকে ছড়িয়ে দিও , জবা ফুলের লাল, বন্ধু আমি তোমার নিশী, জাগবো চির কাল…….  \n",
                "ღ_ღ_ আমার শোকে ছড়িয়ে দিও , জবা ফুলের লাল, বন্ধু আমি তোমার নিশী, জাগবো চির কাল…….  \n",
                "ღ_ღ_ বন্ধুত্ব হলো, হাত এবং চোখের মধ্যে সম্পর্কের মতন. যখন হাতে কোনো আঘাত লাগে, তখন চোখের অশ্রু ঝরে. আবার, যখন চোখের অশ্রু ঝরে, তখন হাত টা মুছে দেয়…..   \n",
                "ღ_ღ_ রাতের আকাশে তাকালে দেখি লক্ষ্য তারার মেলা, এক চাঁদকে ঘিরেই যেন তাদের যত খেলা. বন্ধু অনেক পাওয়া যায় বাড়ালেই হাত, আমার কাছে তুই যে বন্ধু ..ওই আকাশের চাঁদ…..!  \n",
                "ღ_ღ_ আবার যদি রৌদ্র উঠে, মেঘ কেটে যায় মনের.. আমি তোমার সঙ্গী হবো, বন ফুলো বনের….   \n",
                "ღ_ღ_ সারা শহর খুঁজে বেড়াই, তোমার যদি দেখা পাই, চোখ বুজলেই তোমায় দেখি, খুললে দেখি তুমি নাই……  \n",
                "ღ_ღ_ আমি এখন অন্ধকারে মেঘ লেগেছে চাঁদে, তবু আমার বাসরী মন তোমার জন্য কাঁদে…..  \n",
                "ღ_ღ_ আবার যদি বৃষ্টি নামে — আমিই তোমার প্রথম হবো… লেপ্টে যাওয়া শাড়ির মতো — অঙ্গে তোমার জড়িয়ে রবো ……..   \n",
                "ღ_ღ_ মন বলে কিছু কথা, হৃদয়ে আছে গাঁথা, মনের মাঝে লুকিয়ে আছে না বলা অনেক বেথা, যদি সময় থাকে শুনে নিও আমার কিছু কথা.. আমি এখন বড্ড একা……  \n",
                "ღ_ღ_ আজকের এই দিন গুলো কাল সৃতি হয়ে যাবে, মনের খাতায় কোনো পাতায় লেখা হয়ে রবে। কালকে এই পাতা গুলো একটু উল্টে দেখো, আবছা সব সৃতির মাঝে আমায় খুঁজে পাবে…..  \n",
                "ღ_ღ_ হারিয়ে গেছে অনেক কিছু – সকাল থেকে রাত, হারিয়ে গেছে পাশা পাশি আঁকড়ে ধরা হাত. হারিয়ে গেছে প্রথম প্রেমে টুকরো হওয়া মন, চলতে চলতে হারিয়ে গেছে বন্ধু কতোজন।   \n",
                "ღ_ღ_ কখনো যদি দেখা হয়ে যায় দুজনার পথ চলার পথে, সে দিন ও দেখবে তুমি, আমি আছি বসে তোমারি পথো চেয়ে…..   \n",
                "ღ_ღ_ রাতে চাঁদ, দিনে আলো. কেন তোমায় লাগে ভালো ? গোলাপ লাল, কোকিল কালো সবার চেয়ে তুমি ভালো. আকাশ নীল, মেঘ সাদা. গোয়াল ঘরে,তুমি …..বাধা.  \n",
                "ღ_ღ_ যতো দুরে যাও না কেন আছি তোমার পাশে, তাকিয়ে দেখো আকাশ পানে ঘুম যদি না আসে। কাছে আমায় পাবে তুমি হাত বাড়াবে যেই, যদি না পাও জানবে সেদিন আমি যে আর নেই…….    \n",
                "ღ_ღ_ আমার স্বপ্ন জলধারায় তুমি, রিমঝিম সুরে ঝরা বৃষ্টি. আমার হৃদয় canvus জুড়ে, তুমি আমারি অপূর্ব সৃষ্টি.  \n",
                "ღ_ღ_ আমার শীতের মেঠো পথে তুমি, আমারি ছিটানো নিষ্পাপ শিশির. আমার গোধুলির আকাশে তুমি, আমারি রাঙানো রক্তিম আবির.. আমার জোসনার সোনালী আলোয় তুমি, আমারি বাজানো বাশির সুর. আমার গ্রীষ্মের তীব্রতায় তুমি, ভালবাসার ক্লান্ত অলস দুপুর…   \n",
                "ღ_ღ_ মন খোঁজে সারাক্ষণ মনের মত মন, মনের আশা পূরণ করতে তোমায় প্রয়োজন. শূন্য মনে লুকিয়ে আছে অনেক গুলো আশা, তার মধ্যে উননতম তোমার ভালবাসা। \n",
                "ღ_ღ_ তাকেই ভালবাস… যে তোমাকে কষ্ট দেয় … তাকে কষ্ট দিও না … যে তোমায় ভালবাসে … হয়ত পৃথিবীর কাছে তুমি কিছুই না … কিন্তু কারো কারো কাছে তুমিই তার পৃথিবী………   \n"
        };

        arrayList.clear();
        for (String chondo : chondoList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", chondo);
            arrayList.add(map);
        }
    }




    private void romanticPremerSMS() {
        arrayList.clear();

        String[] smsArray = {
                "ღ_ღ_ তুমি ফুল আমি কলি***\n" +
                        "নাম্বার দাও কথা বলি***\n" +
                        "তুমি চাঁদ আমি আলো***\n" +
                        "তোমাকে আমার লাগে ভালো***\n" +
                        "তুমি মেঘ আমি বৃষ্টি***\n" +
                        "তোমার জন্য আমার সৃষ্টি***\n" +
                        "আমি গোলাপ তুমি জবা***\n" +
                        "তুমি কি আমার হবা***\n" +
                        "যদি হতে চাও তাহলে দুটি হাত বাড়াও,,,,  \n",

                "ღ_ღ_ জীবনকে খুজতে গিয়ে তোমাকে পেয়েছি,নিজেকে ভালবাসতে গিয়ে তোমার প্রেমে পরেছি,,জানতাম না কাকে বলে ভালো ভালবাসা,সিখিয়েছ তুমি।…   \n",

                "ღ_ღ_ হঠাৎ এসেছিলে চোখের আলোতে,হারিয়ে ফেলেছি এক ঝলকে, তবুওতুমি ছিলে চোখের কোণে,আগলে রেখেছি বড় যতনে,ভালোবেসেছি তোমাকে প্রথম,চোখের আলোতে এসেছ যখন;ছিলে হৃদয় জুড়ে প্রতিক্ষণে ভালবাসা তো হয়না মনের বিপরীতে।।।  \n",

                "ღ_ღ_ কেউ যদি অভিমানে তোমার সাথে কথা না বলে,, বুঝে নিবে সে তোমায় আড়ালে মিস করে.. আর কেউ যদি না দেখে কাঁদে,, বুঝে নিবে সে তোমায় ভীষণ ভালবাসে..!!   \n",

                "ღ_ღ_ ভুলতে পারিনা তারে,, ভালবাসি আমি যারে.. মনে পড়ে তারে,, শুধু বারেবারে.. জানিনা সে কত দূরে,, তবুও আছে মন জুরে.. এখনো যে ভাবি তারে,, সে কি আজো ভালবাসে আমারে..??  \n",

                "ღ_ღ_ একটা আঁকাশে অনেক তাঁরা । একটা জীবনে দূঃখ ভরা । অনেক রকম প্রেমের ভুল । ভুলের জন্য জীবন দিবো । তবুও আমি তোমারই রবো ।   \n",

                "ღ_ღ_ রাজার আছে অনেক ধন । আমারআছে একটি মন । পাখির আছে ছোট্ট বাসা । আমার মনে একটি আশা । তোমায় ভালোবাসা ।  \n",

                "ღ_ღ_ পৃথিবীটা তোমারি থাক, পারলে একটু নীল দিও। আকাশটা তোমারি থাক, পারলে একটু তারা দিও।মেঘটা তোমারি থাক, শুধু একটু ভিজতে দিও। মনটা তোমারি থাক, পারলে একটু জায়গা দিও    \n",

                "ღ_ღ_ স্বপ্ন দিয়ে আঁকি আমি, সুখের সীমানা । হৃদয় দিয়ে খুজি আমি, মনের ঠিকানা । ছায়ার মত থাকবো আমি, শুধু তার পাশে, যদি বলে সে আমায় সত্যি ভালবাসে॥   \n",

                "ღ_ღ_ সুন্দর রাত তার চেয়ে সুন্দর তুমি, মনের দরজা খুলে দেখ তোমার অপেক্ষায় দাড়িয়ে আছি আমি। দু'হাত বাড়ালাম আমি তোমার তরে, তুমি কী নিবে আমায় ভালবেসে আপন করে ?  \n",

                "ღ_ღ_ ফুল লাল পাতা সবুজ,মন কেন এতো অবুজ । কথা কম কাজ বেশি, মন চায় তোমার কাছে আসি । মেঘ চায় বৃষ্টি, চাঁদ চায় নিশি, মন বলে আমি তোমায় অনেক ভালোবাসি ।   \n",

                "ღ_ღ_ যদি বৃষ্টি হতাম...... তোমার দৃষ্টি ছুঁয়ে দিতাম। চোখে জমা বিষাদ টুকু এক নিমিষে ধুয়ে দিতাম। মেঘলা বরণ অঙ্গ জুড়ে তুমি আমায় জড়িয়ে নিতে,কষ্ট আর পারতো না তোমায় অকারণে কষ্ট দিতে..!  \n",

                "ღ_ღ_ আমার জীবনে কেউ নেই তুমি ছাড়া, আমার জীবনে কোনো স্বপ্ন নেই তুমি ছাড়া , আমার দুচোখ কিছু খোজেনা তোমায় ছাড়া, আমি কিছু ভাবতে পারিনা তোমায় ছাড়া , আমি কিছু লিখতে পারিনা তোমার নাম ছাড়া, আমি কিছু বুঝতে চাইনা তোমায় ছাড়া !   \n",

                "ღ_ღ_ একদিন দুজনে হাঁটব আবার উড়বে তোমার চুল, একদিন শূন্য বাতাস ছুয়ে যাবে কৃষ্ণচুড়ার ফুল.........।।   \n",

                "ღ_ღ_ হাতে হাত ,কানের কাছে মুখটি এনে বলে , এসো না কাছে ,দুজন ভিজি আজ বৃষ্টির জলে !  \n",

                "ღ_ღ_ তুমি যেমনবলতে পারবে না,আঁকাশে কতগুলো তাঁরা আছে..?সাগরে কতফোঁটা পানি আছে..?ঠিক তেমনি আমিওবলতে পারবো না,তোমার জন্য আমার হৃদয়ে, কতটুকুভালবাসা আছে......??!   \n",

                "ღ_ღ_ মন যদি আকাশ হতো তুমি হতে চাঁদ ভালবেসে যেতাম শুধু হাতে রেখে হাত। সুখ যদি হৃদয় হত তুমি হতে হাসি হৃদয়ের দুয়ার খুলে বলতাম তোমায় ভালবাসি।   \n",

                "ღ_ღ_ নীল রংগের পূরি তুমি..সাদা মেঘের হাসিঁকালু মেঘেরছায়া তুমি......অনেক ভাল বাসিকাশ ফুলেরসাদা রংআমি গায়ে মাখি,ভোমার হাটারপথে আমি,ছায়া হয়ে থাকি,তাই ত আমিতোমার,এতভালবাসি।।।।।   \n",

                "ღ_ღ_ করি নি, Love করবো না পাপ, খাইনি ছেকা, ভালো আছি একা আমি বলবো জান, সে করবে ফান, সাবধান, Love এরমদ্ধে আছে, ইবলিশ সয়তান —  \n",

                "ღ_ღ_ পাখি কে নয় ,তার সুর কে ভালোবাসো ..ফুল কে নয় ,তার সুবাস কে ভালোবাসো ..গান কে নয় ,তার কথা কে ভালোবাসো ..মানুষের সুন্দরয কে নয় ,তার মন কে ভালোবাসো...!!!   \n"
        };

        for (String sms : smsArray) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", sms);
            arrayList.add(map);
        }
    }



    private void premikaHappy() {
        arrayList.clear();

        String[] smsArray = {
                "টিপ, টিপ বৃষ্টি পরে।\n" +
                        "        একা বসে আছি ঘরে।\n" +
                        "        তুমি আছো কেমন করে।\n" +
                        "        জানাও আমায় এস এম এস করে।\n" +
                        "        তোমার কথা মনে করে।\n" +
                        "        মেসেজ দিলাম সেন্ট করে।",

                "যদি দেখা না হয় ভেবোনা দূরে আছি।\n" +
                        "        যদি কথা না হয় ভেবোনা ভূলে গেছি।\n" +
                        "        যদি না হাসি ভেবোনা অভিমান করেছি।\n" +
                        "        যদি ফোন না করি ভেবোনা হারিয়ে গেছি।\n" +
                        "        মনে রেখো তোমায় আমি ভালোবাসি।",

                "কিছু, কিছু কবিতা লিখার\n" +
                        "        আগে ভাষা হারিয়ে যায়।\n" +
                        "        কিছু, কিছু কথা বলার\n" +
                        "        আগে সময় ফুরিয়ে যায়।\n" +
                        "        কিছু, কিছু স্বপ্ন দেখার\n" +
                        "        আগে ঘুম ভেঙ্গে যায়।\n" +
                        "        কিছু, কিছু মানুষ আপন\n" +
                        "        হওয়ার আগে দূরে চলে যায়।\n" +
                        "        কেন এমন হয়?",

                "দূর নীলিমায় নয়,\n" +
                        "        আছি তোমার পাশে,\n" +
                        "        খুজে দেখ আমায়,\n" +
                        "        পাবে হৃদয়ের মাঝে,\n" +
                        "        শুনবনা কোন গল্প,\n" +
                        "        গাইবো শুধু গান যে-গান-এ\n" +
                        "        আছে শুধু ভালোবাসার টান.\n" +
                        "        ”শুভ ভালোবাসা দিবস”",

                "ভালোবাসার মাঝে সুখ আছে।\n" +
                        "        ভালো থাকার মাঝে কষ্ট আছে।\n" +
                        "        দূরে রাখার মাঝে টান আছে।\n" +
                        "        মনে রাখার মাঝে প্রান আছে।\n" +
                        "        তাই মনে রেখ আমায় চিরদিন।\n" +
                        "        ভালোবাসি তোমায় অনেক",

                "এক টু কানে, কানে শুনো।\n" +
                        "        এক টু আমায় জানো?\n" +
                        "        এক টু সময় দিয়।\n" +
                        "        এক টু আমার খবর নিয়ো?\n" +
                        "        এক টু যখন থাকো একা।\n" +
                        "        এক টু দিয়ো তুমি দেখা?\n" +
                        "        এক টু নিয়ো খোঁজ।\n" +
                        "        এক টু ভেবো আমায় রোজ?\n" +
                        "        এক টু আমায় ডেকো।\n" +
                        "        ভীষন ভালো থেকো",

                "ভোরের মিষ্টি রোদের\n" +
                        "        আলোয় মিস কল দিয়,\n" +
                        "        কান্ত দুপুরে মনে পরলে SMS কর,\n" +
                        "        গভীর নিঝুম রাতে যদি\n" +
                        "        একা লাগে তবে ফোন কর\n" +
                        "        রাত ভরে শুনাবো তোমায়\n" +
                        "        প্রেমের ভালোবাসার ছন্দ।",

                "পৃথিবীটা তোমারি থাক,\n" +
                        "        পারলে একটু নীল দিও।\n" +
                        "        আকাশটা তোমারি থাক,\n" +
                        "        পারলে একটু তারা দিও।\n" +
                        "        মেঘটা তোমারি থাক,\n" +
                        "        শুধু একটু ভিজতে দিও।\n" +
                        "        মনটা তোমারি থাক,\n" +
                        "        পারলে একটু জায়গা দিও।",

                "এক বিন্দু জল যদি চোখ দিয়ে পড়ে,\n" +
                        "        সেই জলের ফোটা\n" +
                        "        সুধু তোমার কথা বলে..\n" +
                        "        মনের কথা বুঝনা তুমি মুখে বলি তাই,\n" +
                        "        শত আঘাতের পরেও\n" +
                        "        তোমায় ভালবেসে যাই.",

                "বন্ধু আমি চাইনা তোমার\n" +
                        "        অসীম সুখের ভাগ,\n" +
                        "        কিন্তু যখন থাকবে\n" +
                        "        দুঃখে দিও আমায় ডাক,\n" +
                        "        তোমার মুখে কান্না\n" +
                        "        নয় দেখতে চাই হাসি,\n" +
                        "        মনে রেখো বন্ধু তোমায়\n" +
                        "        অনেক ভালোবাসি !",

                "একটু ভালোবাসা দিবি?\n" +
                        "        যে ভালোবাসায় থাকবে না কোন দুঃখ,\n" +
                        "        থাকবে না, না পাওয়ার যন্ত্রনা,\n" +
                        "        থাকবে না মায়া কাঁন্না,\n" +
                        "        থাকবে শুধু সীমাহীন অনুভূতি,\n" +
                        "        যেই অনুভূতি কে সাথী করে\n" +
                        "        কাটিয়ে দিবো সারাটা জীবন।",

                "শুধু কাছে পাওয়ার\n" +
                        "        জন্য ভালবাসা নয়।\n" +
                        "        শুধু ভালো লাগার\n" +
                        "        জন্য ভালবাসা নয়।\n" +
                        "        নিজের সুখ বিসর্জন\n" +
                        "        দিয়ে ভালোবাসার\n" +
                        "        মানুষকে সুখী রাখার\n" +
                        "        নামই ভালবাসা।",

                "কেউ যদি অভিমানে\n" +
                        "        তোমার সাথে কথা না বলে,\n" +
                        "        বুঝে নিবে সে তোমায়\n" +
                        "        আড়ালে মিস করে..\n" +
                        "        আর কেউ যদি না দেখে কাঁদে,\n" +
                        "        বুঝে নিবে সে তোমায়\n" +
                        "        ভীষণ ভালবাসে..!",

                "একটা ভালো রিলেশনের জন্য\n" +
                        "        খুব বেশি টাকা পয়সার প্রয়োজন হয় না!\n" +
                        "        শুধু ভালো দুইটা মনের দরকার হয়।\n" +
                        "        যেখানে একজন আরেকজনকে\n" +
                        "        নিজের মতই বিলিভ করবে,\n" +
                        "        এর নামই হচ্ছে প্রকৃত ভালোবাসা",

                "মনকে প্রশ্ন করলাম\n" +
                        "        তোমার আপন মানুষ যদি\n" +
                        "        তোমাকে ছেড়ে চলে যায়\n" +
                        "        তখন তুমি কি করবে?\n" +
                        "        মন বললো আপন মানুষ\n" +
                        "        কখনো চলে যায় না।\n" +
                        "        আর যে চলে যায়\n" +
                        "        সে কখনো আপন ছিল না।",

                "স্বপ্নটা কেমন ছিল,\n" +
                        "        তা ঘুম ভাঙ্গার পর বুঝা যায়।\n" +
                        "        ঠিক তেমনি কাছের\n" +
                        "        মানুষ কেমন ছিল,\n" +
                        "        তা হারিয়ে যাবার পরই বুঝা যায়।",

                "দ্বিতীয়, তৃতীয়, চতুর্থ, পঞ্চম\n" +
                        "        প্রেম বলে কিছু নেই।\n" +
                        "         মানুষ যখন প্রেমে পরে তখন\n" +
                        "        প্রতিটি প্রেমই প্রথম প্রেম।\n" +
                        "        - হুমায়ূন আজাদ",

                "সুন্দর মনের থেকে সুন্দর\n" +
                        "        শরীর অনেক আকর্ষনীয়।\n" +
                        "        কিন্তু ভন্ডরা বলেন উলটো কথা\n" +
                        "        - হুমায়ূন আজাদ",

                "সবাই তোমাকে কষ্ট দিবে,\n" +
                        "        তোমাকে শুধু এমন একজন\n" +
                        "        কে খুঁজে নিতে হবে যার\n" +
                        "        দেয়া কষ্ট তুমি সহ্য করতে পারবে\n" +
                        "        - হুমায়ূন আহমেদ",

                "অধিকাংশ রূপসীর হাসির\n" +
                        "        শোভা মাংসপেশির কৃতিত্ব,\n" +
                        "        হৃদয়ের কৃতিত্ব নয়\n" +
                        "        - হুমায়ূন আজাদ"
        };

        for (String sms : smsArray) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", sms);
            arrayList.add(map);
        }
    }


    private void monFine() {
        arrayList.clear();

        String[] smsArray = {
                "তোমায় যদি না পাই আমি জীবনের তরে,\n" +
                        "        এ জীবন যাবে আঁধারে আঁধারে।\n" +
                        "        আলো তুমি আমার দুই নয়নের মাঝে,\n" +
                        "        মনের মানুষ তুমি প্রেমের পৃথিবীতে॥",

                "আমি এখনও সেই পথে বসে আছি\n" +
                        "        যে পথে তুমি চলে গেছো.\n" +
                        "        আমি এই পথে বসে থাকবো\n" +
                        "        যতদিন না তুমি ফিরে আসো.",

                "কোটি বছর আগে জন্মে ছিলো\n" +
                        "        তোমার জন্য ভালোবাসা,\n" +
                        "        এখনো অপেক্ষায় আছি\n" +
                        "        তুমি ভালোবাসবে বলে।\n" +
                        "        তোমাকে ধরতে আসিনি\n" +
                        "        এসেছি ধরা দিতে.",

                "জীবনটা কি তা ভালোবাসার\n" +
                        "        পরই বুঝলাম কারণ\n" +
                        "        ভালোবাসার মধ্যে\n" +
                        "        সুখ,দুঃখ,আনন্দ,\n" +
                        "        বেদনা,বিরহ সবই আছে|",

                "জানে এই মন জানে তুমি যে আমার,,\n" +
                        "        তোমারি স্বপ্নে আমার রোজ পারাপার,,\n" +
                        "        ভালোবাসার খামে চিঠি লিখে তোমার নামে,,\n" +
                        "        পাঠিয়ে দিয়েছি আমি পড়ে নাও এবার।",

                "তবে আবার আসিব ফিরে\n" +
                        "        হয়তো পথ হারা পথিক দূরে\n" +
                        "        রেখে সব দুঃখ অভিনয়ে\n" +
                        "        হাসাবো সবার মুখ।\n" +
                        "        কেটে যাবে সব ঘোর রাত\n" +
                        "        পেরিয়ে হবে ভোর ভাঙ্গা\n" +
                        "        মনে থেকে যাবে লেখা নাম তোর।",

                "আবার আসিব ফিরে বাবা-মার ছোট ছেলে হয়ে\n" +
                        "        ভাসবে যেন সুখের জোয়ার হাসি যাবে বয়ে।\n" +
                        "        কষ্টে পড়া ভাঙ্গা মনে দাগ যাবে রয়ে\n" +
                        "        অশ্রু বিসর্জনে আড়ালে হারিয়ে যাবে ভয়ে।",

                "ভালবাসায় কোন অধিকারের\n" +
                        "        মধ্যে কাউকে আটকিয়ে ফেলে না,,\n" +
                        "        বরং তাকে নতুন স্বাধীনতা দান করে।",

                "পৃথিবীটা অনেক সুন্দর\n" +
                        "        যদি সাজাতে পারো\n" +
                        "        মানুষ অনেক ভাল\n" +
                        "        যদি আপন করে নিতে পারো\n" +
                        "        গল্প অনেক মধুর\n" +
                        "        যদি বলতে পারো\n" +
                        "        সময় অনেক দামি\n" +
                        "        যদি কাজে লাগাতে পারো।",

                "যেখানে কষ্ট পাওয়া স্বাভাবিক\n" +
                        "        সেখানে কান্না করাটা অস্বাভাবিক।\n" +
                        "        কষ্টকে অনুভব নয় বরং\n" +
                        "        উপভোগ করতে শিখো।\n" +
                        "        যেমন টা আজ আমি করছি",

                "জানি না আমার অস্তিত্ব কোথায়\n" +
                        "        তোমার মন খারাপের কারনে\n" +
                        "        নাকি ফুটে উঠা হাাসির মাঝে।\n" +
                        "        I love You My Love",

                "মন মাঝি তোর বৈঠা নেরে…\n" +
                        "        আমি বাইতে পারলাম না।\n" +
                        "        সারা জনম বাইলাম তরি\n" +
                        "        পাড়ের নাগাল পাইলাম না\n" +
                        "        আজও কি আর পাবো তরে।",

                "কষ্ট সেখানেই যেখানে সত্যি\n" +
                        "        থাকে হয়ত এটা একটা পরীক্ষা।\n" +
                        "        আনন্দ সেখানে যেখানে মিথ্যে\n" +
                        "        দিয়ে ভরপুর হয়ত এটাও পরীক্ষা",

                "কষ্ট তখনই হয়\n" +
                        "        যখন প্রিয় মানুষটি দূরে\n" +
                        "        চলে যায়\n" +
                        "        i love you ……।",

                "দুর নিলিমায় রয়েছি তুমার পাসে।\n" +
                        "        খুঁজে দেখ আমায়\n" +
                        "        পাবে হৃদয়ের কাছে।\n" +
                        "        দশোনাব না কোন\n" +
                        "        গল্প, গাইব শুধু গান।\n" +
                        "        যে গানে খুঁজে পাবে\n" +
                        "        ভালবাসার টান।",

                "জীবন ফুরিয়ে যাবে\n" +
                        "        ভালোবাসা ফুরাবে না জীবনে,\n" +
                        "        মোরা আরও আগে\n" +
                        "        কেন আসিনি এই ভূবনে।",

                "এক জীবনের অনেক কূল,\n" +
                        "        একটি গাছে অনেক ফুল,\n" +
                        "        এক আকাশে অনেক তারা,\n" +
                        "        এক নদীতে অনেক ধারা,\n" +
                        "        একটি মনের অনেক চাওয়া,\n" +
                        "        ভালো একটি বন্ধু পাওয়া",

                "অল্প, অল্প মেঘ থেকে,\n" +
                        "        হালকা, হালকা বৃষ্টি হয় ।\n" +
                        "        ছোট্ট, ছোট্ট গল্প থেকে,\n" +
                        "        ভালবাসার সৃষ্টি হয় ।\n" +
                        "        মাঝে, মাঝে ফোন করলে,\n" +
                        "        সম্পর্কটা মিষ্টি হয় ।",

                "সুখে থাকো দুঃখে থাকো,\n" +
                        "        খবর তো আর রাখো না ।\n" +
                        "        এখন তো আমায় তুমি ভালো\n" +
                        "        আর বাসো না ।\n" +
                        "        যতো ভালোবাসা ছিলো দিয়ে\n" +
                        "        ছিলাম তোমাকে ।\n" +
                        "        তবু তুমি কিছুতেই,\n" +
                        "        বুঝলেনা আমাকে ।",

                "আজকে তুমি রাগ করছো,\n" +
                        "        দুঃখ পাবো তাতে ।\n" +
                        "        কালকে যখন মরে যাবো,\n" +
                        "        রাগ দেখাবা কাকে ?\n" +
                        "        বিধির বিধান এই রকমি,\n" +
                        "        একদিন তো যাবো মরে,\n" +
                        "        বুঝবে সেদিন তুমি,\n" +
                        "        ভালবাসতাম শুধু তোমাকে ."
        };

        for (String sms : smsArray) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", sms);
            arrayList.add(map);
        }
    }


    private void missSMS() {
        arrayList.clear();

        String[] smsArray = {
                "মনেরই নীল খামে,\n" +
                        "        প্রথম চিঠি তোমার নামে।\n" +
                        "        তাও আবার মনে, মনে,\n" +
                        "        পাঠিয়েছি মোবাইল ফোনে,\n" +
                        "        পড়ে দেখ শেষ লাইন,\n" +
                        "        মিস করছি ‘\n" +
                        "        all time’I MISS You",

                "কিছু কথা কিছু \u200Dপরিচয়,\n" +
                        "      ক্ষনিকের হয়।\n" +
                        "      কিছু ব্যাথা কিছু সৃষ্টি, ভূলার নয়।\n" +
                        "      কিছু মানুষ কিছু বন্ধু\n" +
                        "      চিরদিনের হয়। I Miss You",

                "একা, একা সারাহ্মন পথ চেয়ে থাকি.\n" +
                        "        কল্পনাতে শুধু তারি ছবি আঁকি.\n" +
                        "        বর্ষার কাব্য লাগেনা যে ভালো.\n" +
                        "        তাকে শুধু মনে পড়ে.\n" +
                        "        I Miss You",

                "মনে রাখব তোমাকে চিরদিন,\n" +
                        "        তুমি যেখানেই থাক যতদিন.\n" +
                        "        তোমাকে নিয়ে ঘুরবো স্মৃতির ঘর,\n" +
                        "        যদিও তুমি হয়ে গেছ আমার পর,\n" +
                        "        তবুও Miss করব তোমায় জীবনভর.",

                "তুমি কি অনুভব করতে পারো\n" +
                        "        আমার হৃদয় ভাঙ্গার বেদনা?\n" +
                        "        তুমি কি শুনতে পাও আমার\n" +
                        "        সপ্ন ভাঙ্গার কান্না?\n" +
                        "        যদি তুমি আমাকে বুঝতে\n" +
                        "        তবে আমাকে একা\n" +
                        "        ফেলে চলে যেতে না I Miss U.",

                "তোমার হারানো srity\n" +
                        "        আমাকে এখনো কাঁদায়,\n" +
                        "        কেন চলে গেলে আমাকে ছেড়ে,\n" +
                        "        তোমাকে ভুলে যাবার\n" +
                        "        অনেক চেষ্টা করেছি,\n" +
                        "        কিন্তু তোমাকে ভুলতে পারি নাই…\n" +
                        "         I miss u",

                "মাঝে, মাঝে তোমার কথা ভেবে,\n" +
                        "        আমার চোখে পানি এসে পড়ে,\n" +
                        "        এতটা miss করি তোমাকে.\n" +
                        "        আবার সাথে, সাথে যখন,\n" +
                        "        তোমার সাথে কাটানো প্রিয়,\n" +
                        "        মুহূর্ত গুলোর কথা মনে পড়ে,\n" +
                        "        তখন আমি কান্না ভেজা\n" +
                        "        চোখেই হেসে উঠি,\n" +
                        "        I miss U.",

                "মাঝে, মাঝে তোমার স্মৃতি\n" +
                        "        গুলো ভীষন কাঁদায়।\n" +
                        "        আনমনে ভাবতে থাকি,,\n" +
                        "        কোথায় যেন হারিয়ে যাই।\n" +
                        "        তখন তোমাকে কাছে\n" +
                        "        পাবার বাসনা খুব তীব্র হয়।",

                "একটু খানি শোন\n" +
                        "        একটু আমায় জানো..\n" +
                        "        একটু খবর নিয়ো,,\n" +
                        "        একটু যখন একা,\n" +
                        "        একটু দিয়ো দেখা,,\n" +
                        "        একটু নিয়ো খোঁজ\n" +
                        "        Sms দিয়ো রোজ\n" +
                        "        I Miss You.",

                "তুমিও আমাকে এক\n" +
                        "        দিন ভালোবাসবে,\n" +
                        "        যেভাবে তোমাকে\n" +
                        "        আমি ভালোবাসতাম,\n" +
                        "        I miss u.",

                "জানিনা কিভাবে\n" +
                        "        তোমার দেখা পাবো,\n" +
                        "        জানিনা কিভাবে\n" +
                        "        তোমাকে কাছে পাবো,\n" +
                        "        জানিনা কতটা আপন\n" +
                        "        ভাবো তুমি আমায়।\n" +
                        "        শুধু জানি এই অবুজ মনটা\n" +
                        "        অনেক মিস করে তোমায়।",

                "দুষ্টুমি নয় really\n" +
                        "        ফাইজলামি নয় সিরিয়াসলি\n" +
                        "        ধোকা নয় সত্যি\n" +
                        "        History নয় বাস্তব\n" +
                        "        অন্য কেউ কে নয়\n" +
                        "        শুধু তোমাকে বলছি,\n" +
                        "        আই মিস ইউ",

                "কত দিন হয়নি কথা,\n" +
                        "        ভাবছি বসে একা, একা,\n" +
                        "        জানি তুমি busy আছো,\n" +
                        "        হয়তো আমায় ভুলে গেছো.\n" +
                        "        যেওনা ভুলে তুমি প্লিজ,\n" +
                        "        পড়লে মনে দিও মিস.\n" +
                        "        I always miss u.",

                "আমার ১টা বন্ধু আছে রাগ তার বেশি,\n" +
                        "        ১টা কিছু বললে আমি হয়ে যাই ধোশি ,\n" +
                        "        মুখে তার মিস্টি হাসি, মিষ্টি তার মন,\n" +
                        "        তাইতো তাকে ভালো লাগে মিস করি ভীষণ.",

                "ভালোবাসা কি শুধু আই লাভ ইউ বলা,\n" +
                        "        ভালোবাসা কি শুধু কথা বলা.\n" +
                        "        ভালোবাসা কি শুধু দেখা করা;\n" +
                        "        Reall ভালোবাসা হলো মন থেকে\n" +
                        "        কাউকে.. ভীষণ মিস করা ।",

                "চার রাস্তার মোড়ে\n" +
                        "        একলা একা দাঁড়িয়ে\n" +
                        "        আকাশের পানে চেয়ে\n" +
                        "        ভাবছি মনে, মনে\n" +
                        "        কখন আসবে ফিরে\n" +
                        "        মোর প্রিয়া ঘরে\n" +
                        "        মনে পরে তারে\n" +
                        "        আই মিস ইউ জানু",

                "অনেক দিন হয়না কথা,\n" +
                        "        বন্ধু তোমার সাথে.\n" +
                        "        ভাবি শুধু তোমার কথা\n" +
                        "        সকাল সন্ধ্যা রাতে.\n" +
                        "        মন চায় একটি পলক\n" +
                        "        দেখে আসি তোমায়.\n" +
                        "        তুমি কি গো কখনো\n" +
                        "        মিস করোনা আমায় ?",

                "গল্প লেখা যায় মনের ভাষা দিয়ে,\n" +
                        "        কবিতা  লেখা যায় মনের আবেগ দিয়ে,\n" +
                        "        দুঃখ দেখানো যায় চোখের অশ্রু দিয়ে,\n" +
                        "         কিন্তু  তোমায় যে আমি খুব মিস করি\n" +
                        "        তা আমি তোমাকে বুঝাবো কি দিয়ে ।",

                "মনের মায়া বড় মায়া,\n" +
                        "        আমি সেটা বুঝি..\n" +
                        "        তাই তো আমি বারে বারে,\n" +
                        "        শুধু তোমায় খুঁজি..\n" +
                        "        ভালো যদি ভেসে থাকো,\n" +
                        "        কাছে আমার আসো..\n" +
                        "        আগেরই মতো করে\n" +
                        "        আমায় ভালোবাসো.\n" +
                        "        আই মিস ইউ",

                "কেন তুমি স্বপ্নে এসে ভেঙে দাও ঘুম ?\n" +
                        "        কেন তুমি কাদাও\n" +
                        "        আমায় রাত্রি নিজুম ?\n" +
                        "        কেন এস বার, বার\n" +
                        "        মনের আঙিনায় ?\n" +
                        "        সত্যি কি তুমি বুঝনা,\n" +
                        "        কতটা মিস করি আমি তোমায় ?"
        };

        for (String sms : smsArray) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", sms);
            arrayList.add(map);
        }
    }


    private void loveSMS() {
        arrayList.clear();

        String[] loveMessages = {
                "আমি জানি আমি কখনো তোমায় পাব না!\n" +
                        "        এটাই আমার জীবনের ধ্রুব সত্য।\n" +
                        "        শুধু একটা কথা দাও,\n" +
                        "        একদিন দু’জনে হাত ধরে বৃষ্টিতে ভিজবে;\n" +
                        "        আর আমার কিছু চাওয়ার নেই।",

                "অসীম্ আধার জন্ম দিয়েছে আমার,\n" +
                        "        তাই ভয় নেই আধারের।\n" +
                        "        আমি কোন অতীত চাইনা,\n" +
                        "        চাইনা বর্তমান, তাই\n" +
                        "        ভবিষ্যৎ বলেও কিছু নেই।\n" +
                        "        আমার আছে শুধু মহাকাল।",

                "প্রতিটি ভোর, প্রতিটি ক্ষন, \n" +
                        "        তুমি ছিলে আমার হৃদয়েতে।\n" +
                        "        হয়নি পাওয়া যা ছিল চাওয়া,\n" +
                        "        শুধু মিথ্যা আশায় বেচে থাকা।\n" +
                        "        মানে না এই মন তুমি হিনা।\n" +
                        "        মনে কি পড়েনা আমাকে\n" +
                        "        করেছি যত ভুল, অবহেলা,\n" +
                        "        দিয়েছি যত কষ্ট, ব্যাথা জ্বালা;\n" +
                        "        দু’হাতে সব স্মৃতিগুলো দূরে\n" +
                        "        ঠেলে এই মনে ফিরে আসো।\n" +
                        "        নি:স্ব আমায় বড় একা করে\n" +
                        "        চলে গেছ তুমি বহুদূরে,\n" +
                        "        সঙ্গী হারা কোন পাখির মত,\n" +
                        "        মন আমার শুধু কেদে মরে।\n" +
                        "        মানেনা এই মন তুমি হিনা\n" +
                        "        মনে কি পড়ে না আমায় ?? বলো না-",

                "ফিরিয়ে তুমি দেবে কি আমায়?\n" +
                        "        কখনো যদি তোমাকে শুধাই?\n" +
                        "        যাবে কি অদূরে চলে তুমি?\n" +
                        "        যদি ভালোবাসি বলি তোমায়?\n" +
                        "        আমারি স্পর্শে কি তোমার\n" +
                        "        হৃদয়ে বৃষ্টি কখনো হয়?\n" +
                        "        না বলোনা কিছুই তুমি, যদি\n" +
                        "        ভালোবাসি বলি তোমায়।\n" +
                        "        আমি চাই শুধু তোমার হতে।\n" +
                        "        তোমার নি:শ্বাসে, বিশ্বাসে।\n" +
                        "        আমার সব কিছু তোমার হাতে।\n" +
                        "        চাইলেই নিতে পার প্রাণ আমার।\n" +
                        "        একাকী নি:সঙ্গ জীবন;\n" +
                        "        তোমারি করে নেবে কি আপন!\n" +
                        "        তোমারি পাশে মনেরি আকাশে\n" +
                        "        রেখো আমায় আপন করে।\n" +
                        "        আর তো কিছু চাই না আমি।",

                "তোমার প্রেমে একটু বোকা\n" +
                        "        হলাম না হয় আজকে,\n" +
                        "        কে দেখেছে আমার মত\n" +
                        "        তোমার মনের ভাজকে।",

                "নখের নীচে রেখে ছিলাম তোমার জন্য প্রেম,\n" +
                        "        কাটতে, কাটতে সব খোয়ালাম বললে নাকো শ্যাম।\n" +
                        "        এইতো আমি তোমার ভূমি, ভালোবাসার নালা,\n" +
                        "        আঙ্গুল ধরো, লাঙল চাষো পুরাও প্রণয় মেলা।",

                "যদি কখনো\n" +
                        "        মেঘবতি হয়ে আমার হৃদয়ে\n" +
                        "        উড়ে আসে ঝড়ো বাতাসে\n" +
                        "        আকাশের বুকে ভেসে যাওয়া\n" +
                        "        সাদা মেঘ এক রাশি\n" +
                        "        তার কাছে গিয়ে একটু ভয়ে\n" +
                        "        বলবো ভালোবাসি।।",

                "আগুন আর কতটুকু পুড়ে?\n" +
                        "        সীমাবদ্ধ কার ক্ষয়, সীমিত বিনাশ।\n" +
                        "        মানুষের মত আর অতো নয়, আগুনের সোনালী সন্ত্রাস।\n" +
                        "        আগুন পুড়ালে তবু কিছু রাখে, কিছু থাকে\n" +
                        "        হোকনা তা ধূসর শ্যামল রং ছাই।\n" +
                        "        মানুষ পুড়ালে কিছুই রাখে না,\n" +
                        "        কিছুই থাকে না, খা, খা বিরান;\n" +
                        "        আমার কিছুই নেই।",

                "বেচে আছি, বাচার জন্য বেচে আছি,\n" +
                        "        খাচ্ছি, দাচ্ছি, ঘুমাচ্ছি, আর কি?\n" +
                        "        ভুলে গেছি কিভাবে ভালোবাসতে হয়,\n" +
                        "        আমি তা একেবারেই ভুলে গেছি।\n" +
                        "        তোমার হাতের স্পর্শ এখন আর\n" +
                        "        মনে জাগে না, জাগেনা কোন ছন্দ।\n" +
                        "        তোমার বুকে যে আগুন দেখেছিলাম এখন\n" +
                        "        আর সে আগুনে পুড়ে মরার লোভ হয় না।\n" +
                        "        বেচে আছি, মৃত্যুর জন্য বেচে আছি।",

                "জল পড়ে, পাতা নড়ে, তুমি যেন নড়না,\n" +
                        "        তুমি আবার আমার হবে ভুলেও তা ভাবি না।\n" +
                        "        এই বেশ ভালো আছি সব যেন আমার,\n" +
                        "        আমার ভাবনা আমারই থাক,\n" +
                        "        তোমার ভাবনা তোমার।",

                "বৃষ্টির জন্য বসে আছি; \n" +
                        "        বৃষ্টির পখ চেয়ে আছি।\n" +
                        "        আজ বৃষ্টি নামবে।\n" +
                        "        আকাশ ভেঙে প্রবল বৃষ্টি নামবে আজ।\n" +
                        "        আকাশে মেঘ, বাতাসে স্তব্ধতা।\n" +
                        "        আমার আকাঙ্খা বৃষ্টি হয়ে তুমি আসবে।",

                "তুমি কি আসবে?\n" +
                        "        পথ ভোলা নদীর দেশে,\n" +
                        "        ঢেউয়ে, ঢেউয়ে ভাসব হৃদয়ে,\n" +
                        "        হব দু’জন সাথী।\n" +
                        "        তুমি কি নীল মেঘে ঢাকা \n" +
                        "        আকাশে ঝড় তোলা ভোরে,\n" +
                        "        আমারি মন ভাঙা ঘরে \n" +
                        "        রোদেলা দিনে ফাগুন হবে?\n" +
                        "        তুমি কি দেবে বাবুই পাখির ঠোঠে\n" +
                        "        ঠোটে বোনা সুখের বসতী?",

                "শোনবনা কোন আর মন ভাঙ্গা গান,\n" +
                        "        চাইবনা ভালোবাসার প্রতিদান।\n" +
                        "        সব জ্বালা বুকে সয়ে সব কিছু ভুলে গিয়ে\n" +
                        "        হৃদয়টা বেধে নেব।",

                "তুমি সুখী হলে এই আমি দূরে রব।\n" +
                        "        ভেবোনা না মিসে বিরহ চিরদিন দু:খ পাব।\n" +
                        "        হৃদয় কতনা রঙয়ের স্বপ্ন দেখে যায়।\n" +
                        "        বলো তার সব কি সত্যি হয়?\n" +
                        "        দু’চোখে যদি নামে অকারন শ্রাবণ\n" +
                        "        ভেবে নেবে সব কিছু ভাগ্যের লিখন।\n" +
                        "        স্মৃতি যদি হয়ে যায় বেদনা,\n" +
                        "        পিছু ফিরে আমি আর যাব না।"
        };

        for (String message : loveMessages) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", message);
            arrayList.add(map);
        }
    }


    private void loveStatus() {
        arrayList.clear();

        String[] loveStatuses = {
                "ღ_ღ_ নয় থাকলে আরো কিছুক্ষণ নয় রাখলে হাতে দুটি হাত নয় ডাকলে আরো কিছু কাছে দেখ জোছনা ভেজা এই রাত তোমাকেই দুচোখ ভরে দেখি কাজল মুছে গেছে একি। এখনই বলোনা চলে যাই শুধু একটু ভাবতে দাও তুমি যে আপন।। প্রদীপ পাহারা দেয় যেন দ্বারের হীরামন যেন পালিয়ে না যায় আজ মনের প্রাসাদ ছেড়ে মন মনে হয় তোমাকে নিয়ে থাকি সেই প্রাসাদে গিয়ে। তুমি যে আমারাই রাজকন্যা সেটুকু ভেবে হোক বাসর যাপন।।",
                "ღ_ღ_ তুমি চাইলে জোছনা, স্বপ্নীল কোনো এক রাতে আকাশটা ঘিরে প্রার্থনা, চাঁদটা বলবে হেসে, জোছনা এলে শেষে জানিও তোমার অভ্যর্থনা।।",
                "ღ_ღ_ সত্যি বলছি তোমাকে আর ভালবাসিনা ..! তো্মার জন্য মিছেমিছি রাত-ও জাগিনা ...! সত্যি বলছি তোমায় নিয়ে আর সপ্ন দেখিনা ...! তো্মার জন্য মিছেমিছি কা্ব্য লিখিনা ...! ~~এখন আমি ভালবাসি সুধু আমাকেই~~",
                "ღ_ღ_ তুমি মিষ্টি করে দুষ্টু বল শুনতে ভালো লাগে, আমার এই মন ভরে যায় মধুর অনুরাগে......",
                "ღ_ღ_ সময় যখন মরুর ঝড়ে, এ মন হারায় কেমন করে, আমি তখন যোজন দূরে একাকি সঙ্গি মৌনতা আকাশ যখন আঁধার ভীষণ এক ফোঁটা জল চেয়েছে মন অবহেলায় অপমানে পেয়েছে রিক্ত শুন্যতা। সমান্তরাল পথের বাকে, তোমার পথের দিশা থাকে, সে দিশা খোঁজে তোমাকে দীপান্বিতা ...গাছের সবুজ পাতার ফাঁকে, তোমার ছোঁয়া মিশে থাকে,...",
                "ღ_ღ_ খুব ইচ্ছে করে কারো সাথে সারাক্ষন খুনসুটি করার, খুব ইচ্ছে করে ফোনের অপর পাশের কারো সাথে রাতভর কথায় কথায় কাটিয়ে দেওয়ার, খুব ইচ্ছে হয় কিভাবে সকাল হয় তা কারো সাথে দেখার, খুব ইচ্ছে করে কাউকে ভালোবাসি কথাটা বলতে পারার, খুব ইচ্ছে করে কোন কুয়াশা ছন্ন সকালে কারো পাশাপাশি হাটার, কিংবা ঝুম বষ্টিতে তার সাথে ভেজার, খুব ইচ্ছে করে প্রতিটা সময় ই তার সাথে থাকার, খুব ইচ্ছে করে কাউকে জান বলে ডাকার, খুব ইচ্ছে হয় কাউকে আপন করে কাছে পাবার, খুব ইচ্ছে করে কাউকে নিয়ে সপ্ন দেখার। ধুর সালা !! আমি তো মধ্যবিত্ত !! সপ্ন...",
                "ღ_ღ_ মধু কই কই বিষ খাওয়াইলা কোন কারণে ভালবাসার দাম ন দিলা",
                "ღ_ღ_ আজ স্মৃতির পাতা উল্টে দেখলাম কত মানুষ জীবন থেকে হারিয়ে গেছে।কত চেনা মানুষ , অচেনা হয়ে গেছে হয়তো কোন এক দিন ,আমিও অচেনা হয়ে যাবো এই পৃথিবী থেকে !!",
                "ღ_ღ_ পৃথিবীতে অনেক ধরনের অত্যাচার আছে। ভালবাসার অত্যাচার হচ্ছে সবচেয়ে ভয়ানক অত্যাচার। এ অত্যাচারের বিরুদ্ধে কখনো কিছু বলা যায় না, শুধু সহ্য করে নিতে হয়।",
                "ღ_ღ_ আবার গাঙ্গে আসবে জোয়ার, দুলবে তরী রঙ্গে সেই তরীতে হয়তো কেহ থাকবে তোমার সঙ্গে দুলবে তরী রঙ্গে, পড়বে মনে সে কোন রাতে এক তরীতে ছিলে সাথে এমনি গাঙে ছিল জোয়ার নদীর দুধার এমনি আধার তেমনি তরী ছুটবে- বুঝবে সেদিন বুঝবে।",
                "♣♣♣ ছেলেরা ভালোবাসার অভিনয় করতে করতে যে কখন সত্যি সত্যি ভালোবেসে ফেলে তারা তা নিজেও জানেনা ... মেয়েরা সত্যিকার ভালোবাসতে বাসতে যে কখন অভিনয় শুরু করে তারা তা নিজেও জানেনা- সমরেশ মজুমদার ।",
                "♣♣♣ প্রেম হয় শুধু দেখা ও চোখের ভাল লাগা থেকে, রাগ থেকে প্রেম হয়, ঘৃণা থেকে প্রেম হয়, প্রেম হয় অপমান থেকে, এমনকি প্রেম হয় লজ্জা থেকেও। প্রেম আসলে লুকিয়ে আছে মানবসম্প্রদায়ের প্রতিটি ক্রোমসমে। একটু সুযোগ পেলেই সে জেগে উঠে-হুমায়ূন আহমেদ।",
                "♣♣♣ প্রেমের আনন্দ থাকে স্বল্পক্ষণ কিন্তু বেদনা থাকে সারাটি জীবন- রবীন্দ্রনাথ ঠাকুর।",
                "♣♣♣ প্রেম মানুষকে শান্তি দেয় কিন্তু স্বস্তি দেয় না-বায়রন।",
                "♣♣♣ কাউকে প্রচন্ডভাবে ভালবাসার মধ্যে এক ধরনের দুর্বলতা আছে। নিজেকে তখন তুচ্ছ এবং সামান্য মনে হয়। এই ব্যাপারটা নিজেকে ছোট করে দেয়- হুমায়ূন আহমেদ।",
                "♣♣♣ প্রেমে পড়লে বোকা বুদ্ধিমান হয়ে উঠে, বুদ্ধিমান বোকা হয়ে যায়- স্কুট হাসসুন।",
                "♣♣♣ ছেলে এবং মেয়ে বন্ধু হতে পারে, কিন্তু তারা অবশ্যই একে অপরের প্রেমে পড়বে। হয়ত খুবই অল্প সময়ের জন্য, অথবা ভুল সময়ে। কিংবা খুবই দেরিতে, আর না হয় সব সময়ের জন্য। তবে প্রেমে তারা পড়বেই- হুমায়ূন আহমেদ।",
                "♣♣♣ ছেলেদের জন্য পৃথিবীতে সব চাইতে মূল্যবান হল মেয়েদের হাসি- হুমায়ূন আহমেদ।",
                "♣♣♣ ভালবাসা কারো জন্য দীর্ঘ প্রক্রিয়া আবার কারো জন্য স্বল্প। কিন্তু কষ্ট দুটোতেই সমান- হুমায়ূন আহমেদ।",
                "♣♣♣ যে ভালবাসা যত গোপন, সেই ভালবাসা তত গভীর- হুমায়ূন আহমেদ।"
        };

        for (String status : loveStatuses) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", status);
            arrayList.add(map);
        }
    }


    private void premerSokal() {
        arrayList.clear();

        String[] morningMessages = {
                "নতুন দিন শুরু হল,\nমনটা আমার ভালো হলো !\nসূর্য মামা উঁকি দিল,\nপাখিরা সব উড়ে গেল !\nমা আমাকে বকা দিল,\nতাইতো আমার ঘুম ভাংলো ।\nবন্ধু তুমায় জানাই Good Morning !!",
                "চোখটা একটু খুলে দেখ,\nবলছি তোমায় ভাল থেকো !\nসূর্য মামার মিষ্টি হাসি,\nফুল ফুটেছে রাশি, রাশি !\nশুভ হোক আজ্কের দিন,\nবলছি তোমায় Bondhu গুড মর্নিং !!",
                "শুনে যাও ভোরের পাখি,\n১টা কথা বলে রাখি,\nআছে ১ বন্ধু আমার,\nমনে পড়ে সকাল বিকাল,\nকিভাবে যে কাটল রাত,\nজানাই তাকে শুভ সুপ্রভাত ।",
                "আমি কল্পনায় ভাসি,\nতুমি ভালোবাসো বলে,\nআমি সুখের মাঝে হারাই,\nতুমি ভালোবাসো বলে,\nআমার সকাল শুভ হয়,\nতুমি আমায় ভালোবাসো বলে।\nতাইতো আমি তোমায়\nজানাই শুভ সকাল !!",
                "সূর্য মামার কিরণে\nআঁধার গেল পালিয়ে।\nভোরের শিশির ফোটায়,\nফুল উঠল জেগে।\nওঠ তুমি মেল আঁখি।\nসকাল তোমার নিকটবর্তী।\nঅতীত কে পিছনে ফেলে\nসাজাও তোমার সকাল খানি।\nশুভ সকাল !!",
                "দিনে আলো, রাতে জোসনা,\nকেন তোমায় লাগে ভালো?\nগোলাপ লাল, কোকিল কালো,\nসবার চাইতে তুমি ভালো।\nআকাশ নীল, মেঘ সাদা,\nসবার চাইতে বন্ধু তুমি আলাদা।\nGood Morning !!!",
                "সূর্যের আলো ঘুম ভাঙাল !\nভোরের পাখি গান শুনাল !\nদূর আকাশের ঝাপসা আলো !\nকানে, কানে বলে গেল !\nসকাল যে হয়ে এল !\nবন্ধু তোমরা সবাই আছো ভাল??",
                "সকাল মানে ঘুম চোখে একটু জেগে ওঠা,\nসকাল মানে ভরের আলোয়,\nনতুন গোলাপ ফোঁটা,\nসকাল মানে নতুন,\nআশায় বাড়িয়ে দেয়া হাত।\nআজ সকালে তোমায়\nজানাই নতুন Good morning !!",
                "জেগেছে সুর্য দিবে আলো,\nদিনটা তোমার কাটুক ভাল,\nজেগেছে পাখি গাইবে গান,\nনতুন দিনের আহবান,\nজেগেছে মাঝি তুলবে পাল,\nতুমাকে জানাই শুভ সকাল।",
                "মেঘ বলেছে যাব, যাব\nরাত বলেছে যাই\nসাগর বলে কূল মিলেছে\nআমিতো আর নাই !!\nবন্ধু জানাই তুমায় শুভ সকাল ..",
                "রাত্রি কেটে গেল\nনতুন আলোর ছোঁয়ায়,\nনতুন আলোয় সাজল নতুন দিন,\nসেই খুশিতে তোমায় জানাই,\nশুভ সকাল শুভ দিন !",
                "চাঁদের জন্য নিশি !\nদিনের জন্য আলো !\nসুখের জন্য স্বপ্ন !\nদুখের জন্য হাসি !\nমনের জন্য আশা !\nতোমার জন্য রইল-\nআমার ভালোবাসা !!\nশুভ সকাল !!",
                "আজ টিপ-টিপ কুয়াশা-\nসারা সকাল পড়ছে !\nহিম-হিম শীতে শরীরটা কাপছে !\nরিমঝিম হৃদয় টা উদাস কেন হচ্ছে ! কুয়াশা ভেজা মনটা-\nতোমাকে শুভ সকাল বলছে।",
                "নতুন রোদ, নতুন আলো,\nনতুন ভোর, নতুন আশা,\nমিষ্টি হাসি, দুষ্ট চোখ,\nস্বপ্ন গুলো বন্ধু তুমার পূরণ হোক,\nআকাশে সুর্য, নিচ্ছে আলো,\nদিনটি তোমার কাটুক ভাল।\nGood Morning !!",
                "সকাল মানে মিষ্টি,\nসূর্য রোদের আনাগোনা।\nসকাল মানে নীল,\nআকাশে পাখির গান শোনা।\nসকাল মানে জীবন,\nথেকে একটি দিন কমা।\nসকাল মানে জীবন পথে,\nএগিয়ে চলার তীব্র বাসনা।\nGood Morning Friend !!!",
                "রাতের আধার পালিয়ে,\nগেল সূর্য মামার ভয়ে।\nপাখি গুলো গান গাইল,\nতুমি উঠবে বলে।\nআকাশ ভরা রুপালি আলো।\nআজকের সকালটা,\nবন্ধু তোমার কাটুক ভাল।\nGood Morning !!",
                "দেখ তাকিয়ে পুব আকাশে\nসুর্য মামা হাসে।\nসোনার শিশির লেগে আছে\nস্নিগ্ধ ঘাসে ঘাসে।\nদরজা খোল সকাল হলো,\nফুরিয়ে গেছে রাত।\nতোমার দুয়ারে দাড়িয়ে আছি\nতোমায় জানাতে শুভ সকাল !!",
                "ভোরের আলো দিল উঁকি !!\nতোমার আশায় আমি থাকি !!\nআমার কাছে আসবে বলে !!\nবাগান ভরা ফুলে, ফুলে !!\nখুশির জোয়ার সীমাহীন !!\nতোমায় জানাই বন্ধু !!\nGood Morning !!",
                "স্বপ্ন দেখার প্রহর শেষে,\nফিরল পরি ঘুমের দেশে।\nকাল মেঘের আড়াল থেকে\nসুর্য দিল দেখা।\nতাকিয়ে দেখ ভোরের আলোয়\nনতুন স্বপ্ন লেখা।\nGood Morning Friend !!"
        };

        for (String msg : morningMessages) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", msg);
            arrayList.add(map);
        }
    }


    private  void RomanticFbCaption(){
        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83C\uDF1A\uD83D\uDE45\u200D♀️\uD83D\uDCA6\n" +
                "❥︎কারো ভাবের ধার -ধারী না\uD83C\uDF1A\uD83D\uDE45\u200D♀️\uD83D\uDCA6\n" +
                "❥︎\n" +
                "☹︎ইচ্ছে シ︎গুলো খুবই ছোট কেন জানি পুরণ হতে চাই না ಠ_ಠ\n" +
                "\n" +
                "\uD83C\uDF1A\uD83D\uDE45\u200D♀️\uD83D\uDCA6");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","❥︎না☹︎আছিシ︎কারোরシ︎ছায়াতেಠ_ಠ\n" +
                "❌\n" +
                "༆নাシ︎আছিシ︎কারোরシ︎মায়াতে☻︎\n" +
                "❌\n" +
                "❥︎আমি㋛︎শুধুシ︎আমাতেই☺︎︎\n" +
                "\uD83D\uDE42\uD83D\uDC94\n" +
                "°");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","═❥᭄●আমাকেツকেউツখারাপツভাবলে༆༊᭄●\n" +
                "═❥᭄●আমিツকিছুツমনেツকরিツনা༆༊᭄●\n" +
                "═❥༊═❥কারণ༆༊᭄●\n" +
                "═❥᭄●একজনツমানুষツকখনোツসবার༆༊᭄●\n" +
                "═❥᭄●প্রিয়ツহতেツপারেツনা \uD83E\uDD1F\uD83D\uDC94\uD83D\uDC94 ... ...\n" +
                "»  ::  :  ::(S):::::::::«");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","_ভালোবাসা অবিরাম প্রিয় দল আর্জেন্টিনার\n" +
                "এর জন্য ❤️\uD83E\uDD40\n" +
                "◢\uD83C\uDDE7\uD83C\uDDE9◣            ◢\uD83C\uDDE6\uD83C\uDDF7◣\n" +
                "\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE7\uD83C\uDDE9◣ ◢\uD83C\uDDE6\uD83C\uDDF7\uD83C\uDDE6\uD83C\uDDF7\uD83C\uDDE6\uD83C\uDDF7\n" +
                "◥\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE6\uD83C\uDDF7\uD83C\uDDE6\uD83C\uDDF7\uD83C\uDDE6\uD83C\uDDF7◤\n" +
                "◥\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE7\uD83C\uDDE9\uD83C\uDDE6\uD83C\uDDF7\uD83C\uDDE6\uD83C\uDDF7◤\n" +
                "◥❤❤◤\n" +
                "❤\n" +
                "\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83C\uDF3C\uD83E\uDD70༅༎༅আমার༅༎༅ জীবনের༅༎༅ প্রতিটা ༅༎༅গল্পে ༅༎༅আমিই ༅༎༅দোষী༅༎༅\uD83D\uDE0C\uD83C\uDF3C\n" +
                "\uD83C\uDF3B༅༎༅তাই তো༅༎༅ আমি ༅༎༅আনন্দিত༅༎༅ না ༅༎༅হয়েও༅༎༅\uD83C\uDF3A\n" +
                "\uD83C\uDF3B\uD83D\uDC96༅༎༅বরাবর༅༎༅আমি༅༎༅হাসিখুশি༅༎༅ \uD83E\uDD70\uD83C\uDF3C");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","༆᭄̲̲̲̞̎̎͢༊═══ভালোবাসা༎༅ যদি༎༅ হয়༎༅ দামি,..!❥┼─༊\uD83E\uDD8B\uD83E\uDD40  ,\uD83E\uDD40\uD83C\uDF3F༆᭄̲̲̲̞̎̎͢༊═══তবে কেন ভালোবাসার༎༅ জন্য ༎༅ঝরে চোখের ༎༅পানি।..!❥┼─༊\uD83E\uDD40\uD83C\uDF3F  \uD83E\uDD40\uD83C\uDF3F༆᭄̲̲̲̞̎̎͢༊═══");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","~``প্রজাপ্রতির পিছনে ছুটে সময় নষ্ট করো\n" +
                "না...!!\uD83D\uDE42\uD83E\uDD40\n" +
                "~``ফুলের চাষ করো...!!\uD83E\uDD40\uD83E\uDD40\n" +
                "~``দেখবে প্রজাপতি তোমার পিছনে\n" +
                "ছুটবে...!!☺️\uD83D\uDC97");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","●───༆\uD83C\uDF3A●───༆༊\uD83E\uDD8B\n" +
                "-۵ღ❝   হয় নি বলা অনেক কথা  ❞ツ\uD83C\uDF42\n" +
                "-۵ღ❝ তোমায় অনেক ভালবাসি বাবা ❞ツ\uD83D\uDC9A\n" +
                "●───༆\uD83C\uDF3C●───༆༊");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83D\uDC30-!<'\uD835\uDE01\uD835\uDDF5\uD835\uDDF6\uD835\uDE00 \uD835\uDDEE\uD835\uDDEF\uD835\uDDFC\uD835\uDE02\uD835\uDE01 \uD835\uDDF9\uD835\uDDF6\uD835\uDDFB\uD835\uDDF2'✨\uD83C\uDF52\uD83C\uDF6D\"\n" +
                "_____কিছু কিছু  মানুষ কল্পনাতেই বেশি সুখী!< \uD83D\uDC30\uD83D\uDD10✨\n" +
                "____\uD83D\uDC9C\uD83D\uDD10____");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","━ღ༎\uD83D\uDC9Aস্বপ্ন তাকে নিয়েই দেখা উচিতヅ\uD83D\uDDA4\uD83E\uDD40\n" +
                "༊-যে শুধু স্বপ্ন দেখাবে না\"\"\uD83D\uDC94\uD83D\uDE42\uD83C\uDF40\n" +
                "༺''স্বপ্নগুলো বাস্তবও করবেll\"༻ღ━༎");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","শুভ!!জন্মদিন!কলিজা/\uD83D\uDC90❤️\uD83D\uDE3D\n" +
                "\uD83C\uDF39\uD835\uDC07\uD835\uDC1A\uD835\uDC29\uD835\uDC29\uD835\uDC32 \uD835\uDC01\uD835\uDC22\uD835\uDC2B\uD835\uDC2D\uD835\uDC21\uD835\uDC1D\uD835\uDC1A\uD835\uDC32 \uD835\uDC13\uD835\uDC28\uD835\uDC28 \uD835\uDC18\uD835\uDC28\uD835\uDC2E\uD83C\uDF3B\uD83C\uDF82\n" +
                "ღসুন্দর!!এই!!ভূবনে!সুন্দরতম!!জীবন!!হোক! তর !!\uD83D\uDE3B\uD83E\uDD70\n" +
                "ღপূরন!হোক!প্রতিটি!স্বপ্ন!প্রতিটি!আশা!বেচে!থাকো!হাজার!বছর!!\uD83C\uDF43\uD83C\uDF3B\n" +
                "\uD83C\uDF82ღ\uD835\uDC6F\uD835\uDC82\uD835\uDC91\uD835\uDC91\uD835\uDC9A \uD835\uDC69\uD835\uDC8A\uD835\uDC93\uD835\uDC95\uD835\uDC89\uD835\uDC85\uD835\uDC82\uD835\uDC9A \uD835\uDC7B\uD835\uDC90\uD835\uDC90 \uD835\uDC80\uD835\uDC90\uD835\uDC96☺ღ\uD835\uDC74\uD835\uDC82\uD835\uDC8F\uD835\uDC9A \uD835\uDC74\uD835\uDC82\uD835\uDC8F\uD835\uDC9A \uD835\uDC6F\uD835\uDC82\uD835\uDC91\uD835\uDC91\uD835\uDC9A \uD835\uDC79\uD835\uDC86\uD835\uDC95\uD835\uDC96\uD835\uDC93\uD835\uDC8F \uD835\uDC76\uD835\uDC87\uD835\uDC87 \uD835\uDC7B\uD835\uDC89\uD835\uDC86 \uD835\uDC6B\uD835\uDC82\uD835\uDC9Aღ\uD83C\uDF82\uD83D\uDC51\uD83C\uDF88\n" +
                "─━━━━━━⊱✿⊰━━━━━━─@ ");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle",">\uD83D\uDC39✨\uD835\uDE01\uD835\uDDF5\uD835\uDDF6\uD835\uDE00 \uD835\uDDEE\uD835\uDDEF\uD835\uDDFC\uD835\uDE02\uD835\uDE01 \uD835\uDDF9\uD835\uDDF6\uD835\uDDFB\uD835\uDDF2-!>\uD83D\uDD10\uD83D\uDD87️\uD83D\uDCAD\n" +
                "- মন থেকে ভালোবেসে দেখো \":-) \uD83E\uDEE0\uD83E\uDD8B\uD83C\uDF38\n" +
                "- নির্দিষ্ট মানুষ ছাড়া\" আর অন্য কাউকে ভালো লাগবে না-!!-)\uD83D\uDE0A\uD83E\uDDF8");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83E\uDD70❥︎----ღ᭄_কাউকে ভালবাসলে❞࿐.❤️..\n" +
                "❥︎----ღ᭄_গাছের শিকড়ের মতো ভালোবাসা উচিত❞࿐.\uD83C\uDF34....\n" +
                "❥︎----ღ᭄_ফুলের মতো নয়❞࿐.\uD83C\uDF39....\n" +
                "❥︎---কারণ.༎༅..\uD83C\uDF40..\n" +
                "❥︎----ღ᭄_ফুল তো শুধু সুবাস দিয়েই ঝরে যায়❞࿐.\uD83E\uDD40.\n" +
                "❥︎----ღ᭄_আর শিকড়\" সে তো মৃত্যু আগে পর্যন্ত থেকে যায় ❞࿐\uD83E\uDD70..");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","Ex: ভুলে গেছো আমাকে?\n" +
                "Me: গোসল করার সময় গামছা নিতে ভুলে যাই তোকে কি মনে রাখবো?\uD83D\uDE42");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","- bf  তার  Gf  কে ম্যসেজ  করল  গুড নাইট হানী ..! \uD83D\uDE04\n" +
                "-  ওইদিক  থেকে  তার মা রিপ্লাই  দিল  তোমার হানী ঘুমিয়ে  পড়েছে  আমি  মোমাছি  বলছি... \uD83D\uDE01\uD83E\uDD2A");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle",">\uD83D\uDC39✨\uD835\uDE01\uD835\uDDF5\uD835\uDDF6\uD835\uDE00 \uD835\uDDEE\uD835\uDDEF\uD835\uDDFC\uD835\uDE02\uD835\uDE01 \uD835\uDDF9\uD835\uDDF6\uD835\uDDFB\uD835\uDDF2-!>\uD83D\uDD10\uD83D\uDD87️\uD83D\uDCAD\n" +
                "- মন থেকে ভালোবেসে দেখো \":-) \uD83E\uDEE0\uD83E\uDD8B\uD83C\uDF38\n" +
                "- নির্দিষ্ট মানুষ ছাড়া\" আর অন্য কাউকে ভালো লাগবে না-!!-)\uD83D\uDE0A\uD83E\uDDF8");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","۞❤️█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█❤️۞\n" +
                "\uD83D\uDC9A ༒︎অহংকার আর হিংসা ত্যাগ করো.༒︎\uD83D\uDC9A\n" +
                "☘︎ ︵কারণ︵ ☘︎\n" +
                "꧁ তুমি এই পৃথিবীর অতিথি মাত্র.꧂\n" +
                "☞︎︎︎ মালিক নয়.☜︎︎︎\n" +
                "❥━──➸➽❥❥━──➸➽\n" +
                "۞\uD83D\uDC99█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█\uD83D\uDC99۞");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83E\uDD25মেয়েরা নামের আগে যতো'ই \uD83E\uDD71\n" +
                "Princess⇘\uD83D\uDC78Angel⇘\uD83D\uDC7CQueen⇘\uD83D\uDC70\n" +
                "বসাও না কেন..!\uD83E\uDD37\u200D♂️\n" +
                "বিয়ের পর সেই,⇘\n" +
                "পল্টুর মা,\uD83D\uDC7C\n" +
                "বল্টুর মা,\uD83D\uDC76\n" +
                "টুনির মা\uD83D\uDC76\uD83C\uDFFB\n" +
                "ওইসব নাম'ই জোটবে..!\uD83E\uDD23");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","তোমাকে ছাড়া যদি ভালো থাকতাম\uD83E\uDD17\n" +
                "আর কাউকে ভালোবাসতে পারতাম\uD83D\uDE0D\n" +
                "তাহলে এতোদিনে অন্য কাউকে খুজে নিতাম\uD83D\uDE42\uD83D\uDC94\uD83E\uDD40\n" +
                "//--- প্রিয়////- \uD83D\uDE42\uD83D\uDC94");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\" পৃথিবীতে ভালবাসার অধিকার সবার আছে\uD83D\uDC9E\n" +
                "কিন্তু ভালোবাসা পাওয়ার ভাগ্যটা সবার হয় না \uD83D\uDE05\n" +
                "....\uD83D\uDE0C\uD83D\uDDA4");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","___\uD83D\uDC9C\uD83D\uDD10\uD83C\uDF08 ___\n" +
                "- \uD835\uDDE7\uD835\uDDF5\uD835\uDDF6\uD835\uDE00 \uD835\uDDEE\uD835\uDDEF\uD835\uDDFC\uD835\uDE02\uD835\uDE01 \uD835\uDDF9\uD835\uDDF6\uD835\uDDFB\uD835\uDDF2\"-!! \uD83D\uDE42\uD83E\uDDE1✨\n" +
                "\uD83D\uDC30!<'— পূর্ণতা পাক পৃথিবীর সকল ভালোবাসা, সুন্দর হোক সম্পর্কের শেষটা -!!-) \uD83D\uDE0A\uD83D\uDDA4\uD83C\uDF3B");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","___ \uD83C\uDF52\uD83D\uDD87️✨___\n" +
                "\uD83C\uDF38\uD83D\uDC9A--//সম্পর্কের\"নাম\"যাই হোক\" না কেন--\uD83D\uDC99\uD83C\uDF3C.\n" +
                "\uD83D\uDDA4\uD83E\uDD40মন খারাপের সময় যে\" /পাশে থাকে\uD83C\uDF3A\uD83C\uDF3C\n" +
                "\uD83C\uDF3A\uD83D\uDC9Eসেই প্রিয় \" মানুষ ❥━➸➽❥\uD83D\uDDA4");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","─༅༎•\uD83C\uDF3A\uD83C\uDF38༅༎•─\n" +
                "─༅༎•\uD83E\uDD40\uD83C\uDF3A-যে আমাকে গুরুত্ব দেয়༎༄꧂\uD83E\uDD40\uD83C\uDF3A\n" +
                "_༎তাকেই আমি গুরুত্ব দেই༎༄꧂\uD83E\uDD40\uD83C\uDF3A\n" +
                "_༎যে আমাকে ༎༄꧂\uD83E\uDD40\uD83C\uDF3A\n" +
                "_༎গুরুত্ব দেয়না,,তার༎༄꧂\uD83E\uDD40\uD83C\uDF3A\n" +
                "_༎দিকে ফিরেও তাকাই না ༅༎•\uD83E\uDD40\uD83C\uDF3A\n" +
                "_༎হ্যাঁ এটাই আমি༅༎•\uD83E\uDD40\uD83C\uDF3A\n" +
                "─༅༎•\uD83C\uDF3A\uD83C\uDF38༅༎•─");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle",".\n" +
                "\uD83E\uDD74(ᗒᗩᗕ)আমি পারি না আর পারি না ,,\uD83E\uDD74\uD83E\uDD74\n" +
                "\uD83E\uDD28(╥﹏╥)আমি কেন মরি না \uD83E\uDD7A\uD83E\uDD7A\n" +
                "\uD83D\uDC7A(;ŏ﹏ŏ)আজরাহিল কেন চিনে না \uD83E\uDD74\uD83E\uDD74\n" +
                "\uD83E\uDD26(╬☉д⊙)⊰⊹ฺআমারেরে \uD83D\uDE4D\uD83D\uDE4D\uD83D\uDE4D\n" +
                "\uD83D\uDE2D( ՞ਊ ՞）→আমি পারি না আর পারি না \uD83D\uDE22");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","____সবাই শুধু  Friend Request দেই __✔✔❤\uD83D\uDC9A\n" +
                "___ কিন্ত কেউ Prem Request দেই না___❌❌\uD83E\uDD14\uD83E\uDD14\n" +
                "অন্তত ইনবক্স রিকোয়েস্ট দে\n" +
                "                       ✧\uD83C\uDF3A✧\uD83C\uDF3A꧂\n" +
                "┊┊┊⇣❥\n" +
                "┊┊⇣❥\n" +
                "┊⇣❥\n" +
                "⇣\uD83D\uDC95");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","প্রিয়\uD83D\uDE09\n" +
                "শুধু বিস্বাসটা তোমার\uD83E\uDD14 আর \uD83D\uDE0F\n" +
                "তোমাকে আগলে রাখার দায়িত্ব আমার\uD83D\uDC94");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","_____\uD83C\uDF38কার পছন্দ হলো কার হলো না ……\uD83D\uDDA4\n" +
                "\uD835\uDC70\uD835\uDC95'\uD835\uDC94 \uD835\uDC85\uD835\uDC90\uD835\uDC86\uD835\uDC94\uD835\uDC8F'\uD835\uDC95 \uD835\uDC74\uD835\uDC82\uD835\uDC95\uD835\uDC95\uD835\uDC86\uD835\uDC93\n" +
                "\uD835\uDC69\uD835\uDC86\uD835\uDC84\uD835\uDC82\uD835\uDC96\uD835\uDC94\uD835\uDC86\n" +
                "\" সৃষ্টিকর্তা \" তার পছন্দ মতই বানিয়েছে.....!!!\n" +
                "\uD83E\uDD70❤️\uD83D\uDDA4 \uD83C\uDF3C");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","সব নষ্টের মূল হলো... p\uD83E\uDD74\n" +
                "P=পড়াশোনা\n" +
                "P=পরীক্ষা\n" +
                "p=প্রেম\n" +
                "p=প্যারা\uD83D\uDE36\uD83D\uDE36\n" +
                "P= পরি মুনি খালা \uD83E\uDD23\n" +
                "\uD83E\uDD28  সালার কপাল \uD83E\uDD14");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","........!!\uD83D\uDC94\uD83E\uDD40\n" +
                "-কী খুঁজছেন....?\n" +
                "!! ভালোবাসা!!\n" +
                "-সেটাও আজকাল Wifi এর মতো হয়ে গেছে\n" +
                "\uD83C\uDF38❤️ যতক্ষণ পাশে থাকবেন ততক্ষণ\n" +
                "Connected\n" +
                "-দূরে গেলেই Searching Network....!!\uD83D\uDC94\uD83E\uDD40\uD83D\uDE42\uD83D\uDE0A");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","বালিকা,,\uD83D\uDC70\u200D♀️\n" +
                "\n" +
                "তোমরা নিজেকে নায়কা ভেবো না,,\uD83E\uDD71\n" +
                "পরে,,\uD83E\uDD14\n" +
                "অপু বিশ্বাস,,হয়ে যাবা,,\uD83E\uDD31\n" +
                "সন্তান পাবা,,মাগার স্বামী পাবা না,,\uD83D\uDE07\uD83D\uDE24\uD83D\uDE10");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","︵\uD83E\uDD8B\uD83E\uDD0D\n" +
                "___শূণ্যতা ঠিকই একদিন পূর্ণতা পাবে༎\uD83D\uDDA4\uD83E\uDD40\uD83C\uDF3A\n" +
                "শুধু থেকে যাবে পুরোনো স্মৃতি গুলো\uD83D\uDE42\uD83D\uDC94︵\uD83E\uDD8B@()          ()\n" +
                "(•ᴗ•)          (•ᴗ•)\n" +
                "\uD83C\uDF3A- যার দুঃখ বেশি, তার প্রতি আল্লাহর\uD83C\uDF3A\n" +
                "\uD83E\uDD40 \uD83D\uDD4B _ভালোবাসাও বেশি.!\uD83D\uDD4B\uD83E\uDD40");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD835\uDC3C \uD835\uDC64\uD835\uDC56\uD835\uDC60ℎ...!\"\uD83D\uDE07\n" +
                "_____°\uD83E\uDD40ঝড়ে...! \"\uD83C\uDF34\n" +
                "-/ কারো চিকনা  BF উড়ে...!\"\uD83D\uDC70\n" +
                "____°\uD83E\uDD40এসে আমার কোলে পড়বে...! \"\uD83D\uDE0C");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","•____ღ•\uD83E\uDD74 ওই ,,,,,,,  ______\n" +
                "ঢং .না করে আমাকে  পটিয়ে নাও ︵.!!\uD83D\uDE48\n" +
                "•একা আর কতো দিন থাকবে \uD83D\uDE48\uD83E\uDD27\uD83E\uDD27....\n" +
                "┌───\uD83E\uDD40♣\uD83E\uDD40───┐\n" +
                "\uD835\uDC76\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A࿐∘∘______\uD83C\uDF3E\uD83E\uDDDA\u200D♀️\uD83D\uDE43\n" +
                "শুনতেছো গো\uD83E\uDD74\uD83C\uDF3A\n" +
                "-_-_–––—–হুম তোমায় বলছি \uD83E\uDD70");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","──\uD83E\uDD8Bლ-ლ───\n" +
                "༉༎༉\uD83D\uDC9A\uD83D\uDE3D༉༎༉♡\n" +
                "___বিশ্বাস ছাড়া ভালোবাসা অর্থহীন আর অধিকার ছাড়া সম্পর্ক মূল্যহীন-!\"\uD83D\uDC94\uD83D\uDE42\uD83C\uDF3A");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83E\uDD70তুমি ভুল; •••••\n" +
                "কিনবা ফুল তুমি তো আমারি \uD83D\uDE46\u200D♀️  but k \uD83D\uDE26\n" +
                "~~~●● আমার তো কেউ নেই \uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","─༅༎•\uD83C\uDF3Aএকদিন তুমিও কান্না করবা\uD83C\uDF38༅༎•──\uD83E\uDD7A\uD83E\uDD7A\n" +
                "༅༎•\uD83C\uDF3Aঅনেক কান্না করবা \uD83C\uDF38༅༎•──༅༎•\uD83C\uDF3Aএইটা ভেবে যে\uD83C\uDF38༅༎•─\n" +
                "─༅༎•\uD83C\uDF3Aআমার মতো তুমাকে আর কেউ ভালোবাসতে পারবে না \uD83C\uDF38༅༎•─\uD83E\uDD70\n" +
                "•❤️\uD83D\uDC99");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle",">\uD83D\uDC30✨\uD835\uDDE7\uD835\uDDF5\uD835\uDDF6\uD835\uDE00 \uD835\uDDEE\uD835\uDDEF\uD835\uDDFC\uD835\uDE02\uD835\uDE01 \uD835\uDDDF\uD835\uDDF6\uD835\uDDFB\uD835\uDDF2!-\uD83D\uDC9C\uD83D\uDCAD❤️\n" +
                "--আপনি আমার সেই প্রিয় মানুষ:-)\uD83D\uDC9C\uD83C\uDF52\n" +
                "__যাকে দেখলে আমার মুখে  আচমকা হাসি ফুটে উঠে !!\uD83D\uDE42\uD83C\uDF38");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","___\uD83D\uDE3D\uD83C\uDF39 l Wish এ'ই Group থেকে\n" +
                "❥一 কোনো এক দিন যে কোনাে এক'টা\n" +
                "༉\uD83D\uDC9A\uD83E\uDD70彡ছেলে  কে নিয়া দৌড় দি'মু\uD83C\uDFC3\u200D♂️\uD83C\uDFC3\u200D♀️ \uD83D\uDE48\uD83D\uDE07\uD83D\uDE01 ইনশাআল্লাহ \uD83D\uDE06\uD83D\uDE06");
        arrayList.add(hashMap);



        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83E\uDD40\uD83E\uDD8B༆᭄̲̲̲̞̎̎͢༊জিবনের༊᭄༊\n" +
                "❥┼─༊\uD83C\uDF39\uD83E\uDD40\uD83C\uDF41\uD83E\uDD40\n" +
                "\uD83E\uDD40\uD83E\uDD8B\uD83E\uDD8B༆᭄̲̲̲̞̎̎͢༊সবচেয়ে══❥বড়༊᭄সারপ্রাইজ༊᭄হচ্ছে༊᭄মৃত্যু❥┼─༊\uD83D\uDE05\uD83D\uDC94\uD83E\uDD40\uD83C\uDF41\n" +
                "\uD83E\uDD40\uD83E\uDD8B༆᭄̲̲̲̞̎̎͢༊═══❥༊᭄হঠাৎ ༊᭄একদিন༊᭄এসে❥┼─༊\uD83E\uDD14\uD83D\uDE22\uD83E\uDD40\uD83D\uDC94\uD83C\uDF40\uD83C\uDF40\uD83E\uDD40\n" +
                "\uD83E\uDD40\uD83E\uDD8B\uD83E\uDD8B༆᭄̲̲̲̞̎̎͢༊\uD83E\uDD40═══❥সবাইকে༊᭄চমকে༊᭄দিবে\uD83D\uDC94\uD83C\uDF39\uD83E\uDD40❥┼─༊\uD83E\uDD40\uD83D\uDC94\uD83C\uDF39 মরন \uD83D\uDC94\n" +
                ",\uD83D\uDE2A\uD83D\uDE2A\uD83D\uDE2A\uD83D\uDE2A\uD83D\uDE2A");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","•____ღ•\uD83E\uDD74 ওই ,,,,,,,  ______\n" +
                "ঢং .না করে আমাকে  পটিয়ে নাও ︵.!!\uD83D\uDE48\n" +
                "•একা আর কতো দিন থাকবে \uD83D\uDE48\uD83E\uDD27\uD83E\uDD27....\n" +
                "┌───\uD83E\uDD40♣\uD83E\uDD40───┐\n" +
                "\uD835\uDC76\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A\uD835\uDC8A࿐∘∘______\uD83C\uDF3E\uD83E\uDDDA\u200D♀️\uD83D\uDE43\n" +
                "শুনতেছো গো\uD83E\uDD74\uD83C\uDF3A\n" +
                "-_-_–––—–হুম তোমায় বলছি \uD83E\uDD70");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","°  - পম পম টিপার বয়সে,মোবাইল টিপতাছি\uD83D\uDC94\uD83D\uDE42\uD83C\uDF7C\uD83C\uDF4E  °");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","⎝࿐\uD83E\uDD8B\uD83C\uDF3A\uD83E\uDD70\n" +
                "༆মেয়েরা༆মেসেনজারে༆যে༆পরিমান༆সালাম༆পায়༆\n" +
                "༆মসজিদের༆কোনো༆ইমাম༆\n" +
                "⎝⎝সাহেব\"ও༆সেই༆পরিমান༆\n" +
                "সালাম༆পায়༆না,\uD83D\uDE12\n" +
                "࿐༆⎝\uD83E\uDD8B\uD83C\uDF3A\uD83E\uDD26\u200D♂️");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","❥══❥\uD83C\uDF3Aযদি কাঁদতেই হয় তাহলে,❥\n" +
                "❥❥নামাজ পড়ে কাঁদো❥༊᭄\n" +
                "❥কারন,༊\uD83C\uDF41\n" +
                "তোমার চোখের পানির মূল্য❥\uD83E\uDD40\n" +
                "༊᭄❥┼আর কেউ না দিলেও\n" +
                "\uD83C\uDF41আল্লাহ দিবেন.....।\uD83C\uDF3A\n" +
                "\uD83C\uDFF5️\"ইনশাআল্লাহ \"\uD83C\uDFF5️");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","__পুরুষের তুলনায় নারী নফল ইবাদতে\n" +
                "____ বেশি আগ্রহী তবুও কেন অধিকাংশ\n" +
                "____ নারী জাহান্নামী?\n" +
                "____কারনঃ নারী  তসবিহ হাতেও\n" +
                "_____গীবতকারী...!!!\uD83D\uDE2D");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle"," \uD83C\uDF28️ヽ｀ヽ｀、ヽ｀｀、ヽ\uD83C\uDF28️｀、ヽ｀ヽ｀、、ヽ｀\uD83C\uDF28️ヽ｀、 ｀、、ヽ｀☂ヽ｀、ヽ｀｀、ヽ｀、ヽ｀☂️ヽ｀、、ヽ｀ヽ｀、ヽ｀｀、ヽ｀、ヽ｀ヽ｀、、ヽ｀ヽ｀、ヽ｀｀、ヽ｀、ヽ｀ヽ｀、、ヽ、ヽ｀ヽ｀、ヽ｀｀\n" +
                "উফফ! কি যে বৃষ্টি শুরু হলো । কারোর মনে এটু জায়গা হবে গো.?! \uD83E\uDD7A একদম ভিজে গেছি \uD83E\uDD23\uD83D\uDC45");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","\uD83D\uDC1D\uD83D\uDC1D\n" +
                "                \uD83D\uDC1D\uD83D\uDC1D\n" +
                "            \uD83D\uDC1D\uD83D\uDC1D\n" +
                "         \uD83D\uDC1D\uD83D\uDC1D\n" +
                "      \uD83D\uDC1D\uD83D\uDC1D\n" +
                "   \uD83D\uDC1D\uD83D\uDC1D\n" +
                "  \uD83D\uDC1D\uD83D\uDC1D\n" +
                " \uD83D\uDC1D\uD83D\uDC1D\n" +
                "\uD83D\uDC1D\uD83D\uDC1D\n" +
                "\uD83D\uDC1D\uD83D\uDC1D\n" +
                " \uD83D\uDC1D\uD83D\uDC1D\n" +
                " \uD83E\uDD97যা মৌমাছি যা______\uD83E\uDD97\uD83E\uDD97\n" +
                ",,,, এই গুরুপের Admin,,, পাছাই,, \uD83D\uDE48\uD83D\uDE48\n" +
                "  কামর দে,,, \uD83D\uDE12\n" +
                ",,\uD83D\uDC1D\uD83D\uDC1Dআমারে gf দেয় না কেন \uD83D\uDE12\uD83D\uDE12");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","࿐আতা গাছে তোতা পাখি࿐\uD83D\uDC26\n" +
                "                    ࿐ডালিম গাছে মৌ࿐\uD83D\uDC30\n" +
                "࿐আমি একটা প্রেম করুম࿐\n" +
                "                  ࿐প্রোপোজ করো কেউ࿐\n" +
                "\uD83E\uDD70\uD83E\uDD70\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\uD83E\uDD17\n" +
                "★-------\uD83C\uDF52\uD83D\uDC26\uD83D\uDE15\n" +
                "\uD83C\uDFB1➖✊➖➖✊➖\uD83C\uDFB1\n" +
                "\uD83C\uDF1F / \uD83C\uDF1F\n" +
                "⭐ ?/ ⭐\n" +
                "✨ \uD83C\uDFBD ✨\n" +
                "               / \uD83D\uDC5F \uD83D\uDC5F");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","__\uD83E\uDD8B\uD83E\uDD2D\n" +
                " তোর এক কথায় আমি রাখবো হাজার বাজি,,,!\n" +
                "__\uD83E\uDD8Bতোর ইশারায় আমি,\n" +
                ", বাথরুম সাপ করতেও রাজি,,,!!\uD83E\uDD26\u200D♀️\uD83E\uDD23");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","--/ \uD835\uDC08\uD835\uDC0D \uD835\uDC13\uD835\uDC21\uD835\uDC1E \uD835\uDC16\uD835\uDC28\uD835\uDC2B\uD835\uDC25\uD835\uDC1D \uD835\uDC0E\uD835\uDC1F -!!❤️\uD83C\uDF38\n" +
                "     \"একটা গেলে আরেকটা আসবে\"\uD83D\uDE42\uD83C\uDF52\n" +
                "--/ \uD835\uDC01\uD835\uDC2E\uD835\uDC2D \uD835\uDC08 \uD835\uDC0D\uD835\uDC1E\uD835\uDC1E\uD835\uDC1D -!!❤️\uD83C\uDF38\n" +
                "     \"আমার আপনাকেই লাগবে\"\uD83C\uDF3A\uD83E\uDD40");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","︵❝།།\uD83C\uDF52\n" +
                "︵ღ۵\uD835\uDDDC \uD835\uDDD5\uD835\uDDF2\uD835\uDDF9\uD835\uDDF6\uD835\uDDF2\uD835\uDE03\uD835\uDDF2 \uD835\uDE01\uD835\uDDF5\uD835\uDDEE\uD835\uDE01-\uD83C\uDF3Bযে থাকার সে শত খারাপ জেনে ও পাশে থাকবে-!!\uD83D\uDDA4\uD83C\uDF3A\n" +
                "° ");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","______\uD835\uDC08 \uD835\uDC30\uD835\uDC22\uD835\uDC2C\uD835\uDC21ღ᭄ °°°°ღ۵\uD83D\uDE42\uD83D\uDC94࿐\n" +
                "༊”㋛সেদিন সবার প্রিয় হবো ㋛”࿐\n" +
                "❞যেদিন সাদা কাপড়ে জড়িয়ে যাবো彡\uD83E\uDD70");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","- \uD835\uDDDE\uD835\uDDDA\uD835\uDDD9 দেখা শেষ !\uD83D\uDC40\n" +
                " এখন শুধু \uD835\uDDDE আমার GF হবে\uD83D\uDC38\n" +
                "- এইটা দেখার অপেক্ষায় আছি.!!\uD83E\uDD27");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","❥━ღ!!নিজের!!-\uD83E\uDD40\uD83D\uDDA4☬\uD83E\uDD8B\uD83E\uDD8B༎༊࿐\n" +
                "!!অনুভূতি!!গুলো!! কাউকে!!বোঝানো!\n" +
                "                     সত্যিই!! খুব!!কঠিনツ\n" +
                "︵\uD83D\uDC9A\uD83C\uDF3A☬\uD83E\uDD8B\uD83E\uDD8B༎༊࿐☬\uD83E\uDD8B\uD83E\uDD8B༎༊࿐");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle",".>\uD83D\uDC30✨\uD835\uDC2D\uD835\uDC21\uD835\uDC22\uD835\uDC2C \uD835\uDC1A\uD835\uDC1B\uD835\uDC28\uD835\uDC2E\uD835\uDC2D \uD835\uDC25\uD835\uDC22\uD835\uDC27\uD835\uDC1E!ᵉ-\uD83D\uDD87️\uD83C\uDF08\uD83D\uDCCC\n" +
                "︵\uD83C\uDF38\uD83E\uDD40\n" +
                "খুজতে হলে ভালো মনের মানুষ খুজো༎ \uD83E\uDD70\uD83E\uDD73\n" +
                "❥┈সব কিছু \uD835\uDDFD\uD835\uDDF2\uD835\uDDFF\uD835\uDDF3\uD835\uDDF2\uD835\uDDF0\uD835\uDE01 খুজতে গেলে ভালোবাসাটাই পাবে না......\uD83E\uDD40\n" +
                "★\uD83E\uDEE0");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","••\n" +
                "___°༅༎\uD83C\uDF38ღ\n" +
                "\uD83E\uDD75-ধাক্কা-ধাক্কী করবেন না আস্তে\n" +
                "আস্তে সুন্দর করে হাহা React দিবেন\uD83D\uDE12\n" +
                "••");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","- -༊\uD83C\uDF52\uD835\uDE78\uD835\uDE97\uD835\uDE8B\uD835\uDE98\uD835\uDEA1 a সংসার করার _____বয়সে \uD83D\uDE44\uD83D\uDE12\n" +
                "  _এতিমের মতো \uD835\uDE90\uD835\uDE9B\uD835\uDE98\uD835\uDE9E\uD835\uDE99\uD835\uDE8E \uD835\uDE90\uD835\uDE9B\uD835\uDE98\uD835\uDE9E\uD835\uDE99\uD835\uDE8E _ঘুরে বেড়াচ্চি!\uD83D\uDEB6\uD83D\uDEB6! \uD83D\uDE42\uD83D\uDC48 \uD83D\uDC49______\uD83D\uDC38\uD83D\uDC38______\uD83D\uDC48\n" +
                "    ❤️ কপাল \uD83E\uDD40\uD83E\uDD26");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","- \uD835\uDE14\uD835\uDE30\uD835\uDE30\uD835\uDE25 \uD835\uDE30\uD835\uDE27\uD835\uDE27 \uD83D\uDE22\n" +
                "- সবাই facebook Uninstall করে দাও প্লিজ!\uD83D\uDE44\n" +
                "- আমাকে একটু একা থাকতে দাও.!\uD83E\uDD79\uD83D\uDC38");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","●══❥\uD834\uDD1E⋆⃝\uD83E\uDD70 ভাগ্যর ༅༎༅ উপর ༅༎༅ কারো ༅༎༅ হাত নেই ,,!!\uD83D\uDC9A\n" +
                "                    \uD83D\uDE0A\n" +
                "\uD83E\uDD8B\uD83C\uDF3A-༅༎༅ সব ༅ ༎༅ কিছু ༅༎༅ পরিবর্তন ლ\uD83C\uDF3C\n" +
                "༅༎༅ করার মালিক \uD83C\uDF3B\uD83D\uDE3D\uD83D\uDC96\n" +
                "\uD83D\uDD4A༎༅ একমাত্র ༅༎༅ আল্লাহ \uD83C\uDF3A❤\uD83D\uDE0A");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","༆••༉\uD83E\uDD8B࿔࿔••ღ\n" +
                "༊জীবন'টা সেই মানুষের সাথে কাটানো❥••༅༅☘️\n" +
                "           \uD83C\uDF39●─༅༅উচিত●─༅༅\uD83C\uDF3A\n" +
                "••۵༅যার চেহারা থেকে মন'টা অনেক༅••বেশী\n" +
                "                 \uD83E\uDD37 ❥••ღসুন্দর࿐۵☺️");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","❥━ღ!!নিজের!!-\uD83E\uDD40\uD83D\uDDA4☬\uD83E\uDD8B\uD83E\uDD8B༎༊࿐\n" +
                "!!অনুভূতি!!গুলো!! কাউকে!!বোঝানো!\n" +
                "                     সত্যিই!! খুব!!কঠিনツ\n" +
                "︵\uD83D\uDC9A\uD83C\uDF3A☬\uD83E\uDD8B\uD83E\uDD8B༎༊࿐☬\uD83E\uDD8B\uD83E\uDD8B༎༊࿐");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle",".>\uD83D\uDC30✨\uD835\uDC2D\uD835\uDC21\uD835\uDC22\uD835\uDC2C \uD835\uDC1A\uD835\uDC1B\uD835\uDC28\uD835\uDC2E\uD835\uDC2D \uD835\uDC25\uD835\uDC22\uD835\uDC27\uD835\uDC1E!ᵉ-\uD83D\uDD87️\uD83C\uDF08\uD83D\uDCCC\n" +
                "︵\uD83C\uDF38\uD83E\uDD40\n" +
                "খুজতে হলে ভালো মনের মানুষ খুজো༎ \uD83E\uDD70\uD83E\uDD73\n" +
                "❥┈সব কিছু \uD835\uDDFD\uD835\uDDF2\uD835\uDDFF\uD835\uDDF3\uD835\uDDF2\uD835\uDDF0\uD835\uDE01 খুজতে গেলে ভালোবাসাটাই পাবে না......\uD83E\uDD40\n" +
                "★\uD83E\uDEE0");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","╮/╱\uD83C\uDF3A\uD83C\uDF3F\uD83C\uDF3B\n" +
                "╰\uD83C\uDF41•••⊰❂⊱❤️_\uD83E\uDD70\n" +
                "╱/ ╰┛\"--☞〴⇢──\uD83D\uDC97\uD83E\uDD70▓ ⋆⃝⋆⃝\uD83C\uDF41ভাঁলােঁবাঁসঁলেঁ ○☆ এঁমঁনঁ ভাঁবেঁ বাঁসোঁ  ⋆⃝\uD83C\uDF41\n" +
                "➠̶̶̶̶̶̶̶᭄╌──┈⊰᯽⊱ ➠̶̶̶᭄▓ যাঁতেঁ ছেঁড়েঁ  যাঁওঁয়াঁরঁ  কঁথাঁ  মাঁথাঁয়ঁ  আঁসঁলেঁ ও  ▓▓▓▓▓▓\n" +
                "┇\uD83D\uDC9D       ⋆⋆⃝⋆⃝\uD83C\uDF41 রুঁহুঁ  কেঁপেঁ  উঁঠঁবেঁ\uD83E\uDD70\uD83E\uDD70\uD83E\uDD70\n" +
                "..  \uD83C\uDF39\uD83C\uDF39");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","☆..•°•..☆ ছেলেরা বলে মেয়েরা স্বার্থপর.!\n" +
                ".*•~•°•~•*. আর মেয়েরা বলে ছেলেরা.°.!\n" +
                "\uD83C\uDF42  ..•°•..স্বার্থপর..•°•.. \uD83C\uDF42\n" +
                "...আসলে আসল স্বার্থপর কে জানো..\n" +
                "\uD83E\uDD40   `°`•`সময় `°`•`\uD83E\uDD40\n" +
                "..•°•.সময় মানুষকে ভালোবাসতে শেখায়.\n" +
                ".....°\"°....সময় মানুষকে ঘৃণা করতে..°\"°..\n" +
                "¡.\uD83C\uDF3A ☆☆☆শেখায়☆☆☆\uD83C\uDF3A.¡\n" +
                "°°•.•°°ছেলে কিংবা মেয়ে স্বার্থপর নয়..!");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle"," .\n" +
                "- তোমার অবহেলা পেয়ে হারিয়ে যাওয়ার জন্য ভালোবাসিনি,\uD83D\uDE13 - ভালোবেসেছি শত অবহেলার মাঝেও তোমায় খুঁজে পাওয়ার আশায়।।\uD83D\uDE0A\uD83D\uDC94\n" +
                "- কিছু কথা ছিলো, আর হয়তবা বলা হবে না!\uD83D\uDE14\uD83D\uDC94");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","༆㋛︎ \uD835\uDC70 \uD835\uDC8D\uD835\uDC90\uD835\uDC97\uD835\uDC86 \uD835\uDC9A\uD835\uDC90\uD835\uDC96 \uD83D\uDE3D\n" +
                "ⵗⵗ̥̥̊̊ⵗ̥̥̥̥̊̊̊ⵗ̥̥̥̥̥̊̊̊̊ⵗ̥̥̥̥̥̥̊̊̊̊̊ⵗ̥̥̥̥̥̥̥̊̊̊̊̊ⵗ̥̥̥̥̥̥̥̥̊̊̊̊ⵗ̥̥̥̥̥̥̥̥̥̊̊̊ⵗ̥̥̥̥̥̥̥̥̥̥̊̊ ⵗ̥̥̥̥̥̥̥̥̥̥̥ⵗ̥̥̥̥̥̥̥̥̥̥̊̊ⵗ̥̥̥̥̥̥̥̥̥̊̊̊ⵗ̥̥̥̥̥̥̥̥̊̊̊̊ⵗ̥̥̥̥̥̥̥̊̊̊̊̊ⵗ̥̥̥̥̥̥̊̊̊̊̊ⵗ̥̥̥̥̥̊̊̊̊ⵗ̥̥̥̥̊̊̊ⵗ̥̥̊\n" +
                "༆㋛︎ . মায়া!\" লাগলে\"\uD83E\uDD7A\n" +
                "\uD835\uDDDC\uD835\uDDFB\uD835\uDDEF\uD835\uDDFC\uD835\uDE05 a গুতা মাইরো\"\uD83D\uDE48");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","টিপে ধরে রাখলেই বড় হয়ে যায়, মেসেঞ্জারের ইমুজি গুলা!'\uD83D\uDE0C");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","ঝালমুড়ি খেয়ে ঝালের ঠেলায় \uD83D\uDE2D\n" +
                "---- চোখে পানি আসছে\n" +
                "চোখ মুছতে \uD83D\uDE2D মুছতে যাচ্ছিলাম,,,\n" +
                "কেউ একজন\n" +
                "---- ছবি তুলে পোস্ট করে দিলো \uD83E\uDD29\n" +
                "ভালোবাসা কেনো এতো কাঁদায় ----/-- \uD83D\uDE14\n" +
                "---- কেমন ডা লাগে  \uD83D\uDE10");
        arrayList.add(hashMap);

        //=================================
        hashMap=new HashMap<>();
        hashMap.put("tittle","প্রিয় মানুষটাকে কি বলে ডাকো? \uD83E\uDD37\u200D♂️\n" +
                "❤️                \uD83D\uDE32                \uD83E\uDD72              \uD83D\uDE0D\n" +
                "*তুই*            *তুমি*         \"আপনি\"        \"জান\"\n" +
                "\uD83D\uDE17                 \uD83D\uDE20               \uD83D\uDE07               \uD83D\uDE1B\n" +
                "\"বাবু\"              *ওই*           *বেবি*         *সোনা*\n" +
                "☺️                  \uD83E\uDD2D              \uD83D\uDE05                \uD83D\uDE02\n" +
                "\"কলিজা\"        *পাখি*         \"লুচ্চা\"          *সয়তান*,,নাকি,,নাই,\uD83D\uDE2D\uD83D\uDE2D");
        arrayList.add(hashMap);

    }

    private void novoborsho() {
        arrayList.clear();

        String[] novoborshoMessages = {
                "ইচ্ছে গুলো আকাশ ছুলো, ভাসলো\n মেঘের সারি খুশির ঝরে তেপান্তরে\n হৃদয় দিল পারি। মনের মাঝে সেতারা\n বাজে, খুশিতে মন সাজে নববর্ষ হোক\n রঙিন এই কামনাতে\n শুভ নববর্ষ",
                "নতুন বছর, নতুন ভাবে\n নতুন সাজে, নতুন কাজে\n নতুন আনন্দে, নতুন ভালোবাসায়\n নতুন সম্ভাবনায়, নতুনত্ত ছুয়ে\n যাক তোমার হৃদয়। শুভ নববর্ষ",
                "সুখের স্মৃতি রেখো মনে\n মিশে থেকো আপন জন্\u200C মান\n অভিমান সব ভুলে, খুশির প্রদীপ\n রেখো,জ্বেলে হাজার সূর্য তোমার\n চোখে, বন্ধু তুমি থকো সুখে\n শুভ নববর্ষ-",
                "নব আনন্দে জাগো আজি\n বৈশাখের পুণ্য প্রভাতে\n সব জ্বালাযন্ত্রণা যাক মুছে\n আসুক এক নতুন ভোর\n শুভ নববর্ষ-",
                "পান্তা ইলিশ আর ভর্তা\n ভাজি বাঙ্গালির প্রান\n নতুন বছরে সবাই\n গাইবে বৈশাখের গান\n এসো হে বৈশাখ এসো, এসো\n শুভ নববর্ষ-",
                "বাউল গানের সান্ধ্য তালে\n নতুন বছর এসেছে ঘুরে\n উদাসী হাওয়ার সুরে, সুরে\n রাঙ্গা মাটির পথটি জুড়ে\n শুভ নববর্ষ-",
                "দিন যদি চলে যায় দিগন্তের\n শেষে রাত যদি চলে যায় তারার দেশে\n ভেব না বন্ধু আমি থাকব\n তোমাদের পাশে। শুভ নববর্ষ-",
                "নতুন আশা নতুন প্রান\n নতুন সুরে নতুন গান\n নতুন জীবনের নতুন আলো\n নতুন বছর কাটুক ভালো\n শুভ নববর্ষ-",
                "নতুন পোশাক নতুন সাঁজ\n নতুন বছর শুরু আজ।মিষ্টি\n মন মিষ্টি হাঁসি।শুভেচ্ছা জানাই\n রাশি রাশিশুভ নববর্ষ-",
                "নতুন বছর আসুক নিয়ে নতুন, নতুন আশা\n পৃথিবীতে ছড়িয়ে দিক শুধুই ভালোবাসা\n হানা-হানি বেধাবেদ সব কিছু ভুলি\n এসো সবাই মিলে মিশে সৎ পথে চলি\n সবাইকে নতুন বছরের শুভেচ্ছা-",
                "কাটুক ভালো তোমার দিন\n আসছে বছর নতুন দিন\n প্ন দেখ রঙিন, রঙিন\n আসছে বছর নতুন দিন\n শুভ নববর্ষ-",
                "স্বপ্ন সাঁজাও রঙের মেলায়\n জীবন ভাসাও রঙিন ভেলায়\n ফিরে চলো মাটির টানে\n নতুন সুরে নতুন গানে\n নতুন আশা জাগাও প্রানে\n খুজে নাও বাঁচার মানে\n সবাইকে ইংরেজি নববর্ষের\n অনেক, অনেক-",
                "নতুন বছর নতুন ভাবে, নতুন\n সাঁজে নতুন কাজে , নতুন আনন্দে\n নতুন ভালোবাসায়, নতুন সম্ভাবনায়\n ছুঁয়ে যাক তোমার হৃদয়\n শুভ নববর্ষ-",
                "আসছে নতুন বছর\n সবাইকে জানাই সুখবর\n সবার মনে আনন্দ\n তবে কেন মুখ বন্ধ\n জোরে, জোরে বলা\n দরকার, শুভ নববর্ষ-",
                "নবরুপে সাজো, সাঁজাও\n মনটা নতুন রঙে , মুছে ফেলো\n মন থেকে দুঃখ বেদনার সৃতি\n নব ছন্দে এগিয়ে চলো\n শুভ নববর্ষ-",
                "নবীন প্রভাতের নতুন আলোকে\n স্বাগত জানাই এই ধরণী লোকে\n আনন্দ মনে বারিনু তোমাকে\n আগাম শুভেচ্ছা জানাই সাধরে\n শুভ নববর্ষ-",
                "নীলিমার নীলে হেমন্তের\n সোনালী ডাকের শিষে\n সারাবেলা মাতাল হাওয়া যেমন করে ভাসে\n তেমন করে সবার জীবন কাটুক\n আনন্দ আর উচ্ছাসে । জানাই\n নতুন বছরের শুভেচ্ছা-",
                "নতুন আশা নতুন প্রান\n নতুন হাসি নতুন গান\n নতুন সকাল নতুন আলো\n নতুন দিন হোক ভালো\n দুঃখকে ভুলে যাই\n নতুনকে স্বাগত জানাই\n শুভ নববর্ষ-",
                "নববর্ষে নবরূপ রাঙিয়ে দিক\n প্রতিটি মুহূর্ত সুন্দর সমৃদ্ধ\n হোক আগামীর দিনগুলো\n শুভ নববর্ষ-"
        };

        for (String message : novoborshoMessages) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", message);
            arrayList.add(map);
        }
    }


    private void eidSMS() {
        arrayList.clear();

        String[] eidMessages = {
                "সোনালি সকাল, রোদেলা দুপুর\n পরন্ত বিকেল, গুধোলী সন্ধা, চাদণি রাত\n সব রঙ্গে রাঙ্গিয়ে থাক\n আপনার সারাটি বছর, সারাটি জীবন\n এই কামনায় ঈদ মোবারাক বন্ধু",
                "শপ্ন গুলো সত্যি হোক\n সকল আশা পুরনো হোক\n দু:খ দুরে যাক\n সুখে জীবন ভরে যাক\n জীবনটা হোক ধন্য\n ঈদ মোবারাক তোমার জন্য\n ঈদ মোবারাক বন্ধু",
                "আজ দুঃখ ভুলার দিন\n আজ মন হবে যে রঙ্গিন\n আজ প্রান খুলে শুধু গান হবে\n আজ সুখ হবে সিমাহীন\n তার একটাই কারন\n আজ যে ঈদের দিন\n ঈদ মোবারাক দোস্ত",
                "নতুন সকাল নতুন দিন\n শুভ হোক ঈদের দিন\n নতুন রাত বাকা চাঁদ\n রঙ্গীন হোক ঈদের রাত\n ঈদ মোবারক Friend",
                "চাঁদ উঠেছে ফুল ফুটেছে\n দেখবি কে, কে আয়\n নতুন চাদেঁর আলো\n এসে পড়ল সবার গায়\n ঈদ মোবারক Friend",
                "হাঁসের ডিম মুরগির ডিম\n দেখা হবে ঈদের দিন\n ঈদ মানে আনন্দ ঈদ মানে খুশি\n ঈদের দাওয়াত না দিলে\n মারবো একটা ঘুষি\n ঈদ মোবারক Friend",
                "ঈদের দাওয়াত তোমার তরে\n আসবে তুমি আমার ঘরে কবুল করো আমার দাওয়াত\n না করলে পাবো আঘাত\n তখন কিন্তু দেবো আড়ি\n যাবো না আর তোমার বাড়ি\n ঈদ মোবারক",
                "রংধনু আসে রঙের টানে\n সুবাস আসে ফুলের টানে\n বন্ধু আসে বন্ধুত্বের টানে\n মন কেটে যায় মনের টানে\n ঈদ আসে খুশির টানে\n ঈদ মোবারক Friend",
                "রিমঝিম এই বৃষ্টিতে\n ঈদ কাটাবো সৃষ্টিতে খুশির হাওয়া লাগলো মনে\n নাচবে খুকি ক্ষণে, ক্ষণে\n সাজবে সবাই নতুন পোশাক\n ঈদ যেন সারা জীবন রয়ে যাক\n ঈদ মোবারক Friend !!",
                "ঈদ-এর খুশির দিনে তোমায় পরে মনে\n তুমি কাছে এলে দুঃখ যাই সব ভুলে\n তুমি দুরে গেলে কষ্ট গুলো বারে\n তাইত তোমায় রেখেছি\n আমার মনের ১টি কোণে\n ঈদ মুবারক Friend!!",
                "মন চাইছে কারো সাথে কথা বলি\n মন চাইছে কোন প্রিয়জনকে স্মরণ করি\n ঈদ মোবারক বলার সিদ্ধান্ত যখন নিলা\n ভাবলাম তোমাকে দিয়েই শুরু করি\n ঈদ মোবারক Friend!!",
                "ঈদের দাওয়াত তোমার তরে\n আসবে তুমি আমার ঘরে\n কবুল করো আমার দাওয়াত\n না করলে পাবো আঘাত\n তখন কিন্তু দেবো আড়ি\n যাবো না আর তোমার বাড়ি Friend!!",
                "নীল আকাশে ঈদ-এর চাঁদ\n ঈদের আগে চাঁদনী রাত\n ঈদ হল খুশির দিন\n দাওয়াত রইলো ঈদের দিন\n ভালো থেকো সীমাহীন\n ঈদ-এর দিন টা তোমার হোক রঙিন\n ঈদ মুবারক Friend!!",
                "ফুলে, ফুলে সাজিয়ে রেখেছি এই মন\n তুমি আসলে দুজনে মিলে\n আনন্দ করবো সারাক্ষণ\n বন্ধু তুমি আসবে বলে\n দরজায় থাকি দরিয়ে\n ঈদ মোবারক Friend!!\n শুভ হোক তোমার ঈদের দিন",
                "চিঠি দিয়ে নয় ফুল দিয়ে নয়\n কার্ড দিয়ে নয় কল দিয়ে নয়\n মনের গহীন থেকে মিষ্টি SmS দিয়ে\n জানাই সবাই কে\n অগ্রিম ঈদের শুভেচছা\n ঈদ মোবারক Friend!!",
                "দিনে গরম রাতে শীত\n সামনে আসছে কুরবানি ঈদ\n সাদা রুটি মাংসের ঝোল\n খেতে তোমরা করোনা ভুল\n ঈদে থাকব হাসি খুশি\n তোমাকে চাই পাশাপাশি\n ঈদ মোবারক Friend!!"
        };

        for (String message : eidMessages) {
            HashMap<String, String> map = new HashMap<>();
            map.put("tittle", message);
            arrayList.add(map);
        }
    }





    //image set up  file=================
    //image set up  file=================




    //download image code================================
    //download image code================================




    //permission==============================


     // সেভ করার জন্য Bitmap এখানে রাখবে

    // saveImage কল করার আগে পারমিশন চেক ও রিকোয়েস্ট করবে এই মেথড
    public void saveImageWithPermissionCheck(Bitmap finalBitmap) {
        bitmapToSave = finalBitmap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ (API 29+) এ পারমিশন ছাড়াই সেভ করবে
            saveImage(bitmapToSave);
        } else {
            // Android 9 (API 28) এবং তার নিচের ভার্সন
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            } else {
                saveImage(bitmapToSave);
            }
        }
    }

    // পারমিশন রেসপন্স এখানে হ্যান্ডেল করো
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (bitmapToSave != null) {
                    saveImage(bitmapToSave);
                }
            } else {
                Toast.makeText(this, "Permission denied! Cannot save image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // আসল saveImage মেথড (API 23 থেকে 36 পর্যন্ত কাজ করবে)
    private void saveImage(Bitmap finalBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ Scoped Storage ব্যবহার
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis() + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Status save");

            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try {
                if (imageUri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    if (outputStream != null) outputStream.close();
                    Toast.makeText(this, "Download Done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to create new MediaStore record.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Android 9 বা তার নিচে পুরনো স্টোরেজ API ব্যবহার
            String folderName = "Status save";
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/" + folderName);
            if (!myDir.exists()) myDir.mkdirs();

            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(this, "Download Done!", Toast.LENGTH_SHORT).show();

                // মিডিয়া স্ক্যানার আপডেট করা (গ্যালারিতে দেখানোর জন্য)
                MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                        (path, uri) -> {
                            // Optional: লগ করতে পারো
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // saveImage end here ============

    // rating==================================

    private void  setRateUs(){
        dialog =new Dialog(Status_View_activity.this);
        dialog.setContentView(R.layout.rate_us_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.animation;

        final AppCompatButton rateNowBtn= dialog.findViewById(R.id.rateNowBtn);
        final AppCompatButton laterBtn=dialog.findViewById(R.id.laterBtn);




        rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }


            }
        });

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();
    }

    //==============================copy
    private  void copyToClipboard(String text){
        ClipboardManager clipboardManager= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clipData= ClipData.newPlainText("Copied data",text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }

}