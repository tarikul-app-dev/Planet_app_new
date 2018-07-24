package planet.it.limited.planetapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.database.ContactsDB;
import planet.it.limited.planetapp.model.ContactModel;
import planet.it.limited.planetapp.utill.Constant;
import planet.it.limited.planetapp.utill.FontCustomization;
import planet.it.limited.planetapp.utill.SendFileSMS;
import planet.it.limited.planetapp.utill.SendMultipleSMS;

import static planet.it.limited.planetapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;

public class FileToSMSActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_FILEOPEN = 19;
    Button btnChooseFile;
    public static TextView txvExtension;
    private List<String[]> collection;
    private String[] numberList;
    AutoCompleteTextView txvSender;
    public static EditText edtContentMsg;
    String senderNumber = " ";
    String userName = "";
    String password = "";
    public ArrayList<ContactModel> contactNumList = new ArrayList<>();
    Button btnSendMsg;
    Toolbar toolbar;
    TextView txvMsgCount,txvLengthOfText,txvTotalContacts,txvToolbarText,txvFrom,txvContent,viewFilePreview;
    ContactsDB contactsDB;
    public Constant constant;
    String allCommmaSepNumber = " ";
    SendFileSMS sendFileSMS;
    ImageView imgvClosePWindow,filePreview;

    int pStatus = 0;
    private Handler handler = new Handler();
    ParcelFileDescriptor pFileDescriptor;
    FileDescriptor fileDescriptor;
    FontCustomization fontCustomization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_to_sms);
        toolbar = (Toolbar)findViewById(R.id.toolbar_file_to_sms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
        initViews();
    }

    public void initViews(){
        fontCustomization = new FontCustomization(FileToSMSActivity.this);
        txvToolbarText = (TextView)findViewById(R.id.txv_main);
        btnChooseFile = (Button)findViewById(R.id.btn_choose_file);
        txvExtension = (TextView) findViewById(R.id.txv_file_extension);
        collection = new ArrayList<String[]>();
        txvLengthOfText = (TextView)findViewById(R.id.txv_text_length);
        txvMsgCount = (TextView)findViewById(R.id.txv_message_count);
        txvFrom = (TextView)findViewById(R.id.txv_from);
        txvContent = (TextView)findViewById(R.id.txv_content);
        txvSender = (AutoCompleteTextView) findViewById(R.id.txv_sender);
        edtContentMsg = (EditText)findViewById(R.id.edt_msg_content);
        btnSendMsg = (Button)findViewById(R.id.btn_send_msg);
        viewFilePreview = (TextView)findViewById(R.id.txv_preview);

        // to set font style
        txvToolbarText.setTypeface(fontCustomization.getMerlin());
        txvLengthOfText.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvMsgCount.setTypeface(fontCustomization.getTexgyreHerosRegular());

        txvSender.setTypeface(fontCustomization.getTexgyreHerosRegular());
        edtContentMsg.setTypeface(fontCustomization.getTexgyreHerosRegular());
        btnSendMsg.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvExtension.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvFrom.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvContent.setTypeface(fontCustomization.getTexgyreHerosRegular());
        btnChooseFile.setTypeface(fontCustomization.getTexgyreHerosRegular());

        senderNumber = getValueFromSharedPreferences("sender_number",FileToSMSActivity.this);
        userName = getValueFromSharedPreferences("user_name",FileToSMSActivity.this);
        password = getValueFromSharedPreferences("pass_word",FileToSMSActivity.this);

        constant = new Constant(FileToSMSActivity.this);
        sendFileSMS = new SendFileSMS(FileToSMSActivity.this);
        txvSender.setText(senderNumber);
        btnChooseFile.setTransformationMethod(null);
        btnSendMsg.setTransformationMethod(null);


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                String[] mimetypes = {"text/plain","text/rtf","text/csv","application/pdf","application/csv","application/x-csv","text/comma-separated-values","text/x-comma-separated-values","application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/vnd.openxmlformats-officedocument.presentationml.presentation","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "text/*","application/msword","application/vnd.ms-excel","application/vnd.ms-powerpoint"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_CODE_FILEOPEN);


            }
        });




        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentMsg = edtContentMsg.getText().toString();

                if(contentMsg.length()>0 && numberList.length>0){
                    if(senderNumber!=null){
                        if(senderNumber.length()>0){
                            if(constant.isConnectingToInternet()){
                                sendFileSMS.sendToFileSMS(senderNumber,numberList,contentMsg,userName,password);
                            }else {
                                Toast.makeText(FileToSMSActivity.this,"Ohh!Your Device is Offline",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }else {
                        Toast.makeText(FileToSMSActivity.this,"Please set your sender from Settings",Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


        viewFilePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createImageView();
            }
        });


        final TextWatcher txwatcher = new TextWatcher() {
            int smsLength =0;
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                smsLength = s.length();
                int smsTotalChar = 160 ;
                if(s.length()>0){
                    int smsCount = smsTotalChar - s.length();
                    if(smsCount>0){
                        txvLengthOfText.setText(String.valueOf(smsCount));

                    }
                    if(smsLength>159){
                        int secTotalSMSCount = 145;
                        int smsLength2 = 305;

                        txvLengthOfText.setText(String.valueOf(secTotalSMSCount));
                        int SecSMSCount = smsLength2 - s.length();
                        if(SecSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(SecSMSCount));
                        }
                        if(s.length()==161){
                            txvMsgCount.setText("2");
                        }

                    }

                    if(smsLength>304){
                        int thirdTotalSMSCount = 152;
                        int smsLength3 = 457;

                        txvLengthOfText.setText(String.valueOf(thirdTotalSMSCount));
                        int thirdSMSCount = smsLength3 - s.length();
                        if(thirdSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(thirdSMSCount));
                        }
                        if(s.length()==305){
                            txvMsgCount.setText("3");
                        }

                    }

                    if(smsLength>456){
                        int fourTotalSMSCount = 152;
                        int smsLength4 = 609;

                        txvLengthOfText.setText(String.valueOf(fourTotalSMSCount));
                        int fourSMSCount = smsLength4 - s.length();
                        if(fourSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(fourSMSCount));
                        }
                        if(s.length()==457){
                            txvMsgCount.setText("4");
                        }

                    }

                    if(smsLength>608){
                        int fiveTotalSMSCount = 152;
                        int smsLength5 = 761;

                        txvLengthOfText.setText(String.valueOf(fiveTotalSMSCount));
                        int fiveSMSCount = smsLength5 - s.length();
                        if(fiveSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(fiveSMSCount));
                        }
                        if(s.length()==609){
                            txvMsgCount.setText("5");
                        }

                    }

                    if(smsLength>760){
                        int sixTotalSMSCount = 152;
                        int smsLength6 = 913;

                        txvLengthOfText.setText(String.valueOf(sixTotalSMSCount));
                        int sixSMSCount = smsLength6 - s.length();
                        if(sixSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(sixSMSCount));
                        }
                        if(s.length()==761){
                            txvMsgCount.setText("6");
                        }

                    }
                    if(smsLength>912){
                        int sevenTotalSMSCount = 152;
                        int smsLength7 = 1065;

                        txvLengthOfText.setText(String.valueOf(sevenTotalSMSCount));
                        int sevSMSCount = smsLength7 - s.length();
                        if(sevSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(sevSMSCount));
                        }
                        if(s.length()==913){
                            txvMsgCount.setText("7");
                        }

                    }

                    if(smsLength>1064){
                        int eightTotalSMSCount = 152;
                        int smsLength8 = 1217;

                        txvLengthOfText.setText(String.valueOf(eightTotalSMSCount));
                        int eightSMSCount = smsLength8 - s.length();
                        if(eightSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(eightSMSCount));
                        }
                        if(s.length()==1065){
                            txvMsgCount.setText("8");
                        }

                    }

                    if(smsLength>1216){
                        int nineTotalSMSCount = 152;
                        int smsLength9 = 1369;

                        txvLengthOfText.setText(String.valueOf(nineTotalSMSCount));
                        int nineSMSCount = smsLength9 - s.length();
                        if(nineSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(nineSMSCount));
                        }
                        if(s.length()==1217){
                            txvMsgCount.setText("9");
                        }

                    }

                    if(smsLength>1368){
                        int tenTotalSMSCount = 152;
                        int smsLength10 = 1521;

                        txvLengthOfText.setText(String.valueOf(tenTotalSMSCount));
                        int tenSMSCount = smsLength10 - s.length();
                        if(tenSMSCount>0){
                            txvLengthOfText.setText(String.valueOf(tenSMSCount));
                        }
                        if(s.length()==1369){
                            txvMsgCount.setText("10");
                        }

                    }

                }


            }

            public void afterTextChanged(Editable s) {

            }
        };

        edtContentMsg.addTextChangedListener(txwatcher);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        super.onActivityResult(requestCode, resultCode, returnIntent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILEOPEN:

                    if (returnIntent != null) {
                        Uri currentUri = returnIntent.getData();
                        String mimeType = FileToSMSActivity.this.getContentResolver().getType(currentUri);
                        try {
                            pFileDescriptor =
                                    FileToSMSActivity.this.getContentResolver().openFileDescriptor(currentUri, "r");

                            fileDescriptor = pFileDescriptor.getFileDescriptor();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        if(mimeType.contains("text/plain")||mimeType.contains("text/rtf")||mimeType.contains("text/csv")||mimeType.contains("application/csv")||mimeType.contains("application/x-csv")||mimeType.contains("text/comma-separated-values")||mimeType.contains("text/x-comma-separated-values")){
                            readTextFile(currentUri);
                          //  showPopup();
                        }

//                        if(mimeType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")||mimeType.contains("application/vnd.ms-excel")){
//
//                            readExcelData(fileDescriptor);
//
//                        }

                        try {

                            Cursor returnCursor = this.getContentResolver().query(currentUri, null, null, null, null);
                            if (returnCursor != null && returnCursor.moveToFirst()) {
                                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                String localFileName = returnCursor.getString(nameIndex);
                                if(localFileName!=null){
                                    txvExtension.setText(localFileName);
                                }

                                returnCursor.close();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    break;

                    default:
                        break;

            }

        }

    }

    private void readTextFile(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line;
            Log.i("","open text file - content"+"\n");
            while ((line = reader.readLine()) != null) {
                Log.i("",line+"\n");
                numberList = line.split(",");
                //collection.add(value);
            }
            reader.close();
            inputStream.close();

            showPopup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readExcelData(FileDescriptor fileDescriptor)  {
        ArrayList<ArrayList<String>> OUT = new ArrayList<ArrayList<String>>();
        InputStream stream = null ;
        try {
            stream = new FileInputStream(fileDescriptor);

            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook (stream);

            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();

            // Traversing over each row of XLSX file
            // Traversing over each row of XLSX file
            int count=1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                ArrayList<String> InnerArray = new ArrayList<String>() ;
                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            String c = cell.getStringCellValue();
                            //if(debug)System.out.print(c + "\t");
                            InnerArray.add(c);
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            double n = (double) cell.getNumericCellValue();
                            //if(debug)System.out.print(n + "\t");
                            InnerArray.add(String.valueOf(n));
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            boolean b = cell.getBooleanCellValue();
                            //  if(debug)System.out.print(b + "\t");
                            InnerArray.add(String.valueOf(b));
                            break;
                        default :
                    }
                }
                OUT.add(InnerArray);
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void showPopup() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.id.rl_custom_layout), false);

        final PopupWindow pwindo = new PopupWindow(layout, 600, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        txvTotalContacts = (TextView)layout.findViewById(R.id.txv_total_contacts);
        imgvClosePWindow = (ImageView)layout.findViewById(R.id.imgv_close_window);
        txvTotalContacts.setTypeface(fontCustomization.getTexgyreHerosRegular());

        if(numberList.length>0){
             txvTotalContacts.setText(String.valueOf(numberList.length));
        }
        if(Build.VERSION.SDK_INT>=21){
            pwindo.setElevation(5.0f);
        }
        imgvClosePWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwindo.dismiss();
            }
        });

//        Button btnAgree = (Button) layout.findViewById(R.id.ok);
//        btnAgree.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                pwindo.dismiss();
//            }
//        });

        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }
   public void createImageView(){
       LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       View layout = inflater.inflate(R.layout.file_peview_layout, (ViewGroup) findViewById(R.id.relative_layout), false);

       final PopupWindow pwindo = new PopupWindow(layout, 600, ViewGroup.LayoutParams.WRAP_CONTENT, false);
       final ImageView closeWindow = (ImageView)layout.findViewById(R.id.imgv_close_window);

       filePreview = (ImageView)layout.findViewById(R.id.imgv_file_preview);
       pwindo.setFocusable(true);
       closeWindow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pwindo.dismiss();
           }
       });

       if(Build.VERSION.SDK_INT>=21){
           pwindo.setElevation(5.0f);
       }

       pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

   }

}
