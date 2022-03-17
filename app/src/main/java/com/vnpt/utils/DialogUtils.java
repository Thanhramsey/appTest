package com.vnpt.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeNoticeDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.staffhddtpos58.R;
import com.vnpt.view.NumberTextWatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


public class DialogUtils {
    private static final int TYPE_INFO = 0;
    private static final int TYPE_ERROR = TYPE_INFO + 1;
    private static final int TYPE_PROGRESS = TYPE_ERROR + 1;
    private static final int TYPE_WARNING = TYPE_PROGRESS + 1;
    private static final int TYPE_NOTICE = TYPE_WARNING + 1;
    private static final int TYPE_SUCCESS = TYPE_NOTICE + 1;

    private DialogUtils() {
        // This utility class is not publicly instantiable
    }

    public static AwesomeProgressDialog showLoadingDialog(String msg, Context context, DialogInterface.OnCancelListener onCancelListener) {
        return showProgressDialog(context, msg, "", false);
    }

    public static void showMonthYearPickerDialog(Context context, final TextView txt, final Calendar cal, String title, DatePickerDialog.OnDateSetListener callBackOutside) {
        DatePickerDialog.OnDateSetListener callback;

        if (callBackOutside != null) {
            callback = callBackOutside;
        } else {
            callback = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat(MyTimeUtils.MONTH_FORMAT, Locale.getDefault());
                    txt.setText(sdf.format(cal.getTime()));
                }
            };

        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //create DatePickerDialog without Date Field
        DatePickerDialog dpd = new DatePickerDialog(context, callback, year, month, day) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null) {
                        //Set Day view visibility Off/Gone
                        dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        dpd.setTitle(title);
        dpd.show();
    }

    public static void showYearPickerDialog(Context context, final TextView txt, final Calendar cal, String title, DatePickerDialog.OnDateSetListener callBackOutside) {
        DatePickerDialog.OnDateSetListener callback;

        if (callBackOutside != null) {
            callback = callBackOutside;
        } else {
            callback = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat(MyTimeUtils.YEAR_FORMAT, Locale.getDefault());
                    txt.setText(sdf.format(cal.getTime()));
                }
            };
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //create DatePickerDialog without Date Field
        DatePickerDialog dpd = new DatePickerDialog(context, callback, year, month, day) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null) {
                        //Set Day view visibility Off/Gone
                        dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        dpd.setTitle(title);
        dpd.show();
    }

    @SuppressLint("SetTextI18n")
    public static void showTuanQuyDialog(Context context, final TextView txt, String title, int minValue, int maxValue, int display, final String dvt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        final NumberPicker picker = new NumberPicker(context);
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        final Calendar c = Calendar.getInstance();
        try {
            picker.setValue(display);
        } catch (Exception e) {
            picker.setValue(minValue);
        }
        final FrameLayout parent = new FrameLayout(context);
        parent.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        builder.setView(parent);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_about);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                picker.clearFocus();
                txt.setText(c.get(Calendar.YEAR) + "-" + picker.getValue() + " " + dvt);
                txt.setTag(picker.getValue());
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static void showDayMonthYearPickerDialog(Context context, final TextView txt, final Calendar cal, String title, DatePickerDialog.OnDateSetListener callBackOutside) {
        DatePickerDialog.OnDateSetListener callback;

        if (callBackOutside != null) {
            callback = callBackOutside;
        } else {
            callback = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat(MyTimeUtils.DATE_FORMAT, Locale.getDefault());
                    txt.setText(sdf.format(cal.getTime()));
                }
            };
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //create DatePickerDialog without Date Field
        DatePickerDialog dpd = new DatePickerDialog(context, callback, year, month, day) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null) {
                        //Set Day view visibility Off/Gone
                        dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        dpd.setTitle(title);
        dpd.show();
    }

    public static SmoothDateRangePickerFragment showFromDayToDayPickerDialog(FragmentManager fragmentManager, final TextView txt, final Calendar cal, String title, final DatePickerDialog.OnDateSetListener callBackOutside) {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                SmoothDateRangePickerFragment
                        .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                            @Override
                            public void onDateRangeSet(SmoothDateRangePickerFragment view, int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {
//                                String date = dayStart + "/" + (+monthStart)
//                                        + "/" + yearStart + (char)12 + "|" + dayEnd + "/"
//                                        + (+monthEnd) + "/" + yearEnd;
                                //txt.setText(date);
//                                Log.d("monthStart","monthStart:"+(+monthStart));
                                String fromDate = "" + dayStart + "/" + (++monthStart) + "/" + yearStart;
                                StoreSharePreferences.getInstance(view.getActivity().getBaseContext()).saveStringPreferences(Common.REF_KEY_DATE_SYNC_FROM, fromDate);
                                String toDate = "" + dayEnd + "/" + (++monthEnd) + "/" + yearEnd;
                                StoreSharePreferences.getInstance(view.getActivity().getBaseContext()).saveStringPreferences(Common.REF_KEY_DATE_SYNC_TO, toDate);
                                Log.d("startDate", "startDate:" + fromDate);
                                Log.d("endDate", "endDate" + toDate);
                                if(txt!=null)
                                    txt.setText(fromDate + (char) 12 + "|" + toDate);
                            }
                        });
        smoothDateRangePickerFragment.show(fragmentManager, "Datepickerdialog");
        return smoothDateRangePickerFragment;
    }
    public static SmoothDateRangePickerFragment showFromDayToDayPickerDialogEvent(FragmentManager fragmentManager, final TextView txt, final Calendar cal, String title, final OnEventControlListener callBackOutside) {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                SmoothDateRangePickerFragment
                        .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                            @Override
                            public void onDateRangeSet(SmoothDateRangePickerFragment view, int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {
//                                String date = dayStart + "/" + (+monthStart)
//                                        + "/" + yearStart + (char)12 + "|" + dayEnd + "/"
//                                        + (+monthEnd) + "/" + yearEnd;
                                //txt.setText(date);
//                                Log.d("monthStart","monthStart:"+(+monthStart));
                                String fromDate = "" + String.format("%02d", dayStart) + "/" + String.format("%02d", (++monthStart)) + "/" + yearStart;
                                StoreSharePreferences.getInstance(view.getActivity().getBaseContext()).saveStringPreferences(Common.REF_KEY_DATE_SYNC_FROM, fromDate);
                                String toDate = "" + String.format("%02d", dayEnd) + "/" + String.format("%02d", (++monthEnd)) + "/" + yearEnd;
                                StoreSharePreferences.getInstance(view.getActivity().getBaseContext()).saveStringPreferences(Common.REF_KEY_DATE_SYNC_TO, toDate);
                                StoreSharePreferences.getInstance(view.getActivity().getBaseContext()).saveIntPreferences(Common.REF_KEY_CHANGE_DATE, 1);
                                Log.d("startDate", "startDate:" + fromDate);
                                Log.d("endDate", "endDate" + toDate);
                                callBackOutside.onEvent(ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_DATE, null,null);
                                if(txt!=null)
                                    txt.setText(fromDate + (char) 12 + "|" + toDate);
                            }
                        });
        smoothDateRangePickerFragment.show(fragmentManager, "Datepickerdialog");
        return smoothDateRangePickerFragment;
    }

    public static void showFromMonthToMonthPickerDialog(FragmentManager fragmentManager, final TextView txt, final Calendar cal, String title, DatePickerDialog.OnDateSetListener callBackOutside) {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                SmoothDateRangePickerFragment
                        .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                            @Override
                            public void onDateRangeSet(SmoothDateRangePickerFragment view, int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {
                                String date = (++monthStart)
                                        + "/" + yearStart + (char) 12 + "|" + (++monthEnd) + "/" + yearEnd;
                                txt.setText(date);
                            }
                        });
        smoothDateRangePickerFragment.show(fragmentManager, "Datepickerdialog");
    }

    @SuppressLint("SetTextI18n")
    public static void showNumberPickerDialog(Context context, final TextView txt, String title, int minValue, int maxValue, final String dvt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        final NumberPicker picker = new NumberPicker(context);
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        try {
            picker.setValue(Integer.parseInt(txt.getTag().toString().trim()));
        } catch (Exception e) {
            picker.setValue(minValue);
        }
        final FrameLayout parent = new FrameLayout(context);
        parent.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        builder.setView(parent);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_about);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                picker.clearFocus();
                txt.setText(picker.getValue() + " " + dvt);
                txt.setTag("" + picker.getValue());
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDatePickerDialog(Context context, final TextView txt, final Calendar cal, String title) {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(MyTimeUtils.DATE_FORMAT, Locale.getDefault());
                txt.setText(sdf.format(cal.getTime()));
                txt.setError(null);
            }
        };
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pic = new DatePickerDialog(context, callback, year, month, day);
        pic.setTitle(title);
        pic.show();
    }

    /**
     * Hàm hiển thị TimePicker Dialog
     */
    public static void showTimePickerDialog(Context context, final TextView txt, final Calendar cal, String title) {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //lưu vết lại giờ vào hourFinish
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat(MyTimeUtils.TIME_FORMAT, Locale.getDefault());
                txt.setText(sdf.format(cal.getTime()));
                txt.setError(null);
            }
        };
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog time = new TimePickerDialog(context, callback, hour, min, true);
        time.setTitle(title);
        time.show();
    }

    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //region Show info dialog
    public static void showInfoDialog(Context context, String title, String message) {
        if (message.contains(ConstantsApp.TextLoai_ErrorOnServer.ERR_GENERY)) {
            try {
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(message.split(ConstantsApp.TextLoai_ErrorOnServer.ERR_GENERY)));
                message = message.replace(ConstantsApp.TextLoai_ErrorOnServer.ERR_DATA_NOT_FOUND, "");
                message = message.replace(ConstantsApp.TextLoai_ErrorOnServer.ERR_INVALID_ACTION, "");
                message = arr.get(0).trim().replace(ConstantsApp.TextLoai_ErrorOnServer.ERR_INVALID_DATA, "");
            } catch (Exception e) {
                message = message;
            }
        } else if (message.contains("Lỗi kết nối")) {
            message = "Không có dữ liệu";
        } else {
            message = message;
        }
        showDialog(TYPE_INFO, context, title, message, true, false, null, null, true, null, null, false, null, null, false, null, null, null, null);
    }

    public static void showInfoDialog(Context context, String title, String message, boolean isCancelable, Closure neutralButtonClick) {
        showDialog(TYPE_INFO, context, title, message, isCancelable, false, null, null, true, null, neutralButtonClick, false, null, null, false, null, null, null, null);
    }

    public static void showInfoDialog(Context context, String title, String message, boolean isCancelable,
                                      boolean hasNeutralButton,
                                      String neutralButtonText,
                                      Closure neutralButtonClick) {
        showDialog(TYPE_INFO, context, title, message, isCancelable, false, null, null, hasNeutralButton, neutralButtonText, neutralButtonClick, false, null, null, false, null, null, null, null);
    }

    public static void showInfoDialog(Context context, String title, String message, boolean isCancelable,
                                      boolean hasPositiveButton,
                                      String positiveButtonText,
                                      Closure positiveButtonClick,
                                      boolean hasNegativeButton,
                                      String negativeButtonText,
                                      Closure negativeButtonClick) {
        showDialog(TYPE_INFO, context, title, message, isCancelable, hasPositiveButton, positiveButtonText, positiveButtonClick, false, null, null, hasNegativeButton, negativeButtonText, negativeButtonClick, false, null, null, null, null);
    }

    public static void showInfoDialog(Context context, String title, String message, boolean isCancelable,
                                      boolean hasPositiveButton,
                                      String positiveButtonText,
                                      Closure positiveButtonClick,
                                      boolean hasNeutralButton,
                                      String neutralButtonText,
                                      Closure neutralButtonClick,
                                      boolean hasNegativeButton,
                                      String negativeButtonText,
                                      Closure negativeButtonClick) {
        showDialog(TYPE_INFO, context, title, message, isCancelable, hasPositiveButton, positiveButtonText, positiveButtonClick, hasNeutralButton, neutralButtonText, neutralButtonClick, hasNegativeButton, negativeButtonText, negativeButtonClick, false, null, null, null, null);
    }

    //endregion

    //region Show error dialog
    public static void showErrorDialog(Context context, String title, String message) {
        if (message.contains(ConstantsApp.TextLoai_ErrorOnServer.ERR_GENERY)) {
            try {
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(message.split(ConstantsApp.TextLoai_ErrorOnServer.ERR_GENERY)));
                message = message.replace(ConstantsApp.TextLoai_ErrorOnServer.ERR_DATA_NOT_FOUND, "");
                message = message.replace(ConstantsApp.TextLoai_ErrorOnServer.ERR_INVALID_ACTION, "");
                message = arr.get(0).trim().replace(ConstantsApp.TextLoai_ErrorOnServer.ERR_INVALID_DATA, "");
            } catch (Exception e) {
                message = message;
            }
        } else {
            message = message;
        }
        showDialog(TYPE_ERROR, context, title, message, true, false, null, null, false, null, null, false, null, null, true, null, null, null, null);
    }

    public static void showErrorDialog(Context context, String title, String message, boolean isCancelable, Closure errorButtonClick) {
        showDialog(TYPE_ERROR, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, true, null, errorButtonClick, null, null);
    }

    public static void showErrorDialog(Context context, String title, String message, boolean isCancelable,
                                       boolean hasOkButton,
                                       String okButtonText,
                                       Closure errorButtonClick) {
        showDialog(TYPE_ERROR, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, hasOkButton, okButtonText, errorButtonClick, null, null);
    }
    //endregion

    //region Show progress dialog
    public static void showProgressDialog(Context context, String title, String message) {
        showDialog(TYPE_PROGRESS, context, title, message, false, false, null, null, false, null, null, false, null, null, false, null, null, null, null);
    }

    public static AwesomeProgressDialog showProgressDialog(Context context, String title, String message, boolean isCancelable) {
        //showDialog(TYPE_PROGRESS, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, false, null, null, null, null);
        AwesomeProgressDialog dialog = new AwesomeProgressDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isCancelable);
        dialog.show();
        return dialog;
    }
    //endregion

    //region Show warning dialog
    public static void showWarningDialog(Context context, String title, String message) {
        showDialog(TYPE_WARNING, context, title, message, true, false, null, null, false, null, null, false, null, null, true, null, null, null, null);
    }

    public static void showWarningDialog(Context context, String title, String message, boolean isCancelable, Closure warningButtonClick) {
        showDialog(TYPE_WARNING, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, true, null, null, warningButtonClick, null);
    }

    public static void showWarningDialog(Context context, String title, String message, boolean isCancelable,
                                         boolean hasOkButton,
                                         String okButtonText,
                                         Closure warningButtonClick) {
        showDialog(TYPE_WARNING, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, hasOkButton, okButtonText, null, warningButtonClick, null);
    }
    //endregion

    //region Show notice dialog
    public static void showNoticeDialog(Context context, String title, String message) {
        showDialog(TYPE_NOTICE, context, title, message, true, false, null, null, false, null, null, false, null, null, true, null, null, null, null);
    }

    public static void showNoticeDialog(Context context, String title, String message, boolean isCancelable, Closure noticeButtonClick) {
        showDialog(TYPE_NOTICE, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, true, null, null, null, noticeButtonClick);
    }

    public static void showNoticeDialog(Context context, String title, String message, boolean isCancelable,
                                        boolean hasOkButton,
                                        String okButtonText,
                                        Closure noticeButtonClick) {
        showDialog(TYPE_NOTICE, context, title, message, isCancelable, false, null, null, false, null, null, false, null, null, hasOkButton, okButtonText, null, null, noticeButtonClick);
    }
    //endregion

    //region Show success dialog
    public static void showSuccessDialog(Context context, String title, String message) {
        showDialog(TYPE_SUCCESS, context, title, message, true, true, null, null, false, null, null, false, null, null, false, null, null, null, null);
    }

    public static void showSuccessDialog(Context context, String title, String message, boolean isCancelable, Closure positiveButtonClick) {
        showDialog(TYPE_SUCCESS, context, title, message, isCancelable, true, null, positiveButtonClick, false, null, null, false, null, null, false, null, null, null, null);
    }

    public static void showSuccessDialog(Context context, String title, String message, boolean isCancelable,
                                         boolean hasPositiveButton,
                                         String positiveButtonText,
                                         Closure positiveButtonClick) {
        showDialog(TYPE_SUCCESS, context, title, message, isCancelable, hasPositiveButton, positiveButtonText, positiveButtonClick, false, null, null, false, null, null, false, null, null, null, null);
    }

    public static void showSuccessDialog(Context context, String title, String message, boolean isCancelable,
                                         boolean hasPositiveButton,
                                         String positiveButtonText,
                                         Closure positiveButtonClick,
                                         boolean hasNegativeButton,
                                         String negativeButtonText,
                                         Closure negativeButtonClick) {
        showDialog(TYPE_SUCCESS, context, title, message, isCancelable, hasPositiveButton, positiveButtonText, positiveButtonClick, false, null, null, hasNegativeButton, negativeButtonText, negativeButtonClick, false, null, null, null, null);
    }
    //endregion

    public static void showDialog(int dialogType, Context context, String title, String message, boolean isCancelable,
                                  boolean hasPositiveButton,
                                  String positiveButtonText,
                                  Closure positiveButtonClick,
                                  boolean hasNeutralButton,
                                  String neutralButtonText,
                                  Closure neutralButtonClick,
                                  boolean hasNegativeButton,
                                  String negativeButtonText,
                                  Closure negativeButtonClick,
                                  boolean hasOkButton,
                                  String okButtonText,
                                  Closure errorButtonClick,
                                  Closure warningButtonClick,
                                  Closure noticeButtonClick) {
        if (context == null)
            return;

        //AwesomeDialogBuilder dialog;
        switch (dialogType) {
            case TYPE_INFO:
                AwesomeInfoDialog infoDialog = new AwesomeInfoDialog(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setColoredCircle(R.color.dialogInfoBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(isCancelable)
                        .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNeutralButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setNeutralButtonTextColor(R.color.white)
                        .setNegativeButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(positiveButtonClick)
                        .setNeutralButtonClick(neutralButtonClick)
                        .setNegativeButtonClick(negativeButtonClick);

                //config positive
                if (!TextUtils.isEmpty(positiveButtonText)) {
                    infoDialog.setPositiveButtonText(positiveButtonText);
                } else if (hasPositiveButton) {
                    infoDialog.setPositiveButtonText(context.getString(R.string.dialog_yes_button));
                }

                //config neutral
                if (!TextUtils.isEmpty(neutralButtonText)) {
                    infoDialog.setNeutralButtonText(neutralButtonText);
                } else if (hasNeutralButton) {
                    infoDialog.setNeutralButtonText(context.getString(R.string.dialog_ok_button));
                }

                //config negative
                if (!TextUtils.isEmpty(negativeButtonText)) {
                    infoDialog.setNegativeButtonText(negativeButtonText);
                } else if (hasNegativeButton) {
                    infoDialog.setNegativeButtonText(context.getString(R.string.dialog_no_button));
                }

                //show the dialog
                infoDialog.show();
                break;
            case TYPE_ERROR:
                AwesomeErrorDialog errorDialog = new AwesomeErrorDialog(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setColoredCircle(R.color.dialogErrorBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                        .setCancelable(isCancelable)
                        .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                        .setErrorButtonClick(errorButtonClick);

                //config ok
                if (!TextUtils.isEmpty(okButtonText)) {
                    errorDialog.setButtonText(okButtonText);
                } else if (hasOkButton) {
                    errorDialog.setButtonText(context.getString(R.string.dialog_ok_button));
                }

                //show the dialog
                errorDialog.show();
                break;
            case TYPE_PROGRESS:
                new AwesomeInfoDialog(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setColoredCircle(R.color.dialogInfoBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(isCancelable)
                        .show();
                break;
            case TYPE_WARNING:
                AwesomeWarningDialog warningDialog = new AwesomeWarningDialog(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                        .setCancelable(isCancelable)
                        .setButtonBackgroundColor(R.color.dialogNoticeBackgroundColor)
                        .setWarningButtonClick(warningButtonClick);

                //config ok
                if (!TextUtils.isEmpty(okButtonText)) {
                    warningDialog.setButtonText(okButtonText);
                } else if (hasOkButton) {
                    warningDialog.setButtonText(context.getString(R.string.dialog_ok_button));
                }

                //show the dialog
                warningDialog.show();
                break;
            case TYPE_NOTICE:
                AwesomeNoticeDialog noticeDialog = new AwesomeNoticeDialog(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setColoredCircle(R.color.dialogNoticeBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                        .setCancelable(isCancelable)
                        .setButtonBackgroundColor(R.color.dialogNoticeBackgroundColor)
                        .setNoticeButtonClick(noticeButtonClick);

                //config ok
                if (!TextUtils.isEmpty(okButtonText)) {
                    noticeDialog.setButtonText(okButtonText);
                } else if (hasOkButton) {
                    noticeDialog.setButtonText(context.getString(R.string.dialog_ok_button));
                }

                //show the dialog
                noticeDialog.show();
                break;
            case TYPE_SUCCESS:
                AwesomeSuccessDialog successDialog = new AwesomeSuccessDialog(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(isCancelable)
                        .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(positiveButtonClick)
                        .setNegativeButtonClick(negativeButtonClick);

                //config positive
                if (!TextUtils.isEmpty(positiveButtonText)) {
                    successDialog.setPositiveButtonText(positiveButtonText);
                } else if (hasPositiveButton) {
                    successDialog.setPositiveButtonText(context.getString(R.string.dialog_yes_button));
                }

                //config negative
                if (!TextUtils.isEmpty(negativeButtonText)) {
                    successDialog.setNegativeButtonText(negativeButtonText);
                } else if (hasNegativeButton) {
                    successDialog.setNegativeButtonText(context.getString(R.string.dialog_no_button));
                }

                //show the dialog
                successDialog.show();
                break;
        }
    }

    //-------Input money--------
    public static void showDialogInputMoney(Context context, View view, String title, final EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_input_money, (ViewGroup) view, false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        input.setText(editText.getText());
        input.addTextChangedListener(new NumberTextWatcher(input, null));
        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                editText.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}
