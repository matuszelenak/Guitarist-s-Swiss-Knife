package sk.matus.ksp.guitarist_swiss_knife;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChordActivity extends AppCompatActivity {

    RadioButton rootChooser;
    Dialog rootDialog;
    Chord currentChord;
    TextView scaleDisplay;
    TextView chordDisplay;
    RadioGroup typeRG;
    RadioGroup fifthRG;
    RadioGroup seventhRG;
    RadioGroup augDimRG;
    RadioGroup susRG;
    RadioGroup ninthRG;
    RadioGroup eleventhRG;
    RadioGroup thirteenthRG;
    RadioGroup add29RG;
    RadioGroup add411RG;
    RadioGroup add613RG;
    ToneUtils toneUtils;
    String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);

        rootChooser = (RadioButton) findViewById(R.id.rootChooser);
        scaleDisplay = (TextView) findViewById(R.id.scaleDisplay);
        chordDisplay = (TextView) findViewById(R.id.chordDisplay);
        typeRG = (RadioGroup) findViewById(R.id.groupType);
        fifthRG = (RadioGroup) findViewById(R.id.group5);
        seventhRG = (RadioGroup) findViewById(R.id.group7);
        augDimRG = (RadioGroup) findViewById(R.id.groupAD);
        susRG = (RadioGroup) findViewById(R.id.groupSus);
        ninthRG = (RadioGroup) findViewById(R.id.group9);
        eleventhRG = (RadioGroup) findViewById(R.id.group11);
        thirteenthRG = (RadioGroup) findViewById(R.id.group13);
        add29RG = (RadioGroup) findViewById(R.id.groupAdd29);
        add411RG = (RadioGroup) findViewById(R.id.groupAdd411);
        add613RG = (RadioGroup) findViewById(R.id.groupAdd613);
        toneUtils = new ToneUtils(this.getResources());
        rootDialog = constructRootDialog();
        currentChord = new Chord(toneUtils);
        root = "C";
        updateChord();
    }

        @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("root",root);
        savedInstanceState.putString("chord_type",currentChord.type);
        savedInstanceState.putString("chord_sus",currentChord.sus);
        savedInstanceState.putString("chord_augdim",currentChord.augdim);
        savedInstanceState.putString("chord_fifth",currentChord.fifth);
        savedInstanceState.putString("chord_seventh",currentChord.seventh);
        savedInstanceState.putString("chord_ninth",currentChord.ninth);
        savedInstanceState.putString("chord_eleventh",currentChord.eleventh);
        savedInstanceState.putString("chord_thirteenth",currentChord.thirteenth);
        savedInstanceState.putString("chord_add29",currentChord.add29);
        savedInstanceState.putString("chord_add411", currentChord.add411);
        savedInstanceState.putString("chord_add613", currentChord.add613);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        root = savedInstanceState.getString("root");
        currentChord.type = savedInstanceState.getString("chord_type");
        currentChord.sus = savedInstanceState.getString("chord_sus");
        currentChord.augdim = savedInstanceState.getString("chord_augdim");
        currentChord.fifth = savedInstanceState.getString("chord_fifth");
        currentChord.seventh = savedInstanceState.getString("chord_seventh");
        currentChord.ninth = savedInstanceState.getString("chord_ninth");
        currentChord.eleventh = savedInstanceState.getString("chord_eleventh");
        currentChord.thirteenth = savedInstanceState.getString("chord_thirteenth");
        currentChord.add29 = savedInstanceState.getString("chord_add29");
        currentChord.add411 = savedInstanceState.getString("chord_add411");
        currentChord.add613 = savedInstanceState.getString("chord_add613");
        updateChord();
    }

    private void setRoot(View v){
        root = (String) v.getTag();
        updateChord();
        rootChooser.setText(root);
    }

    private void updateChord(){
        rootChooser.setText(root);
        currentChord.setScale(toneUtils.getScaleTones(root));
        chordDisplay.setText(currentChord.getProgression(root));
        scaleDisplay.setText(toneUtils.getScaleText(root));
    }

    public void setType(View v){
        currentChord.type="";
        RadioButton rb = (RadioButton) findViewById(typeRG.getCheckedRadioButtonId());
        if (rb!=null){
            currentChord.type = (String)(rb.getTag());
            RadioButton sus2Button = (RadioButton) findViewById(R.id.buttonSus2);
            RadioButton sus4Button = (RadioButton) findViewById(R.id.buttonSus4);
            if (sus2Button.isChecked())
                sus2Button.performClick();
            if (sus4Button.isChecked())
                sus4Button.performClick();
        }
        updateChord();
    }

    public void setFifth(View v){
        currentChord.fifth="";
        RadioButton rb = (RadioButton) findViewById(fifthRG.getCheckedRadioButtonId());
        if (rb !=null) currentChord.fifth = (String)(rb.getTag());
        updateChord();
    }

    public void setSeventh(View v){
        currentChord.seventh="";
        RadioButton rb = (RadioButton) findViewById(seventhRG.getCheckedRadioButtonId());
        if (rb !=null) currentChord.seventh = (String)(rb.getTag());
        updateChord();
    }

    public void setAugDim(View v){
        switch (augDimRG.getCheckedRadioButtonId()){
            case R.id.buttonAug: {
                RadioButton majorButton = (RadioButton) findViewById(R.id.buttonMajor);
                RadioButton fifthSharp = (RadioButton) findViewById(R.id.button5sharp);
                if (!fifthSharp.isChecked())
                fifthSharp.performClick();
                if (!majorButton.isChecked())
                majorButton.performClick();
                break;
            }
            case R.id.buttonDim: {
                RadioButton minorButton = (RadioButton) findViewById(R.id.buttonMinor);
                RadioButton fifthFlat = (RadioButton) findViewById(R.id.button5flat);
                if (!fifthFlat.isChecked()) fifthFlat.performClick();
                if (!minorButton.isChecked()) minorButton.performClick();
                break;
            }
            default:{
                RadioButton majorButton = (RadioButton) findViewById(R.id.buttonMajor);
                RadioButton fifthButton = (RadioButton) findViewById(R.id.button5);
                if (!majorButton.isChecked()) majorButton.performClick();
                if (!fifthButton.isChecked()) fifthButton.performClick();
                break;
            }
        }
        updateChord();
    }

    public void setSus(View v){
        currentChord.sus="";
        RadioButton rb = (RadioButton) findViewById(susRG.getCheckedRadioButtonId());
        if (rb !=null){
            currentChord.sus = (String)(rb.getTag());
            RadioButton majorButton = (RadioButton) findViewById(R.id.buttonMajor);
            RadioButton minorButton = (RadioButton) findViewById(R.id.buttonMinor);
            if (majorButton.isChecked())
                majorButton.performClick();
            if (minorButton.isChecked())
                minorButton.performClick();
        }
        updateChord();
    }

    public void setNinth(View v){
        currentChord.ninth="";
        RadioButton rb = (RadioButton) findViewById(ninthRG.getCheckedRadioButtonId());
        if (rb !=null) {
            currentChord.ninth = (String)(rb.getTag());
            if (ninthRG.getCheckedRadioButtonId() == R.id.button9 && seventhRG.getCheckedRadioButtonId()==-1){
                RadioButton seventhButton = (RadioButton) findViewById(R.id.button7);
                seventhButton.performClick();
            }
        }
        updateChord();
    }

    public void setEleventh(View v){
        currentChord.eleventh="";
        RadioButton rb = (RadioButton) findViewById(eleventhRG.getCheckedRadioButtonId());
        if (rb !=null) {
            currentChord.eleventh = (String)(rb.getTag());
            if (eleventhRG.getCheckedRadioButtonId() == R.id.button11 && ninthRG.getCheckedRadioButtonId()==-1){
                RadioButton ninthButton = (RadioButton) findViewById(R.id.button9);
                ninthButton.performClick();
            }
        }
        updateChord();
    }

    public void setThirteenth(View v){
        currentChord.thirteenth="";
        RadioButton rb = (RadioButton) findViewById(thirteenthRG.getCheckedRadioButtonId());
        if (rb !=null) {
            currentChord.thirteenth = (String)(rb.getTag());
            if (thirteenthRG.getCheckedRadioButtonId() == R.id.button13 && eleventhRG.getCheckedRadioButtonId()==-1){
                RadioButton eleventhButton = (RadioButton) findViewById(R.id.button11);
                eleventhButton.performClick();
            }
        }
        updateChord();
    }

    public void setAdd29(View v){
        currentChord.add29="";
        RadioButton rb = (RadioButton) findViewById(add29RG.getCheckedRadioButtonId());
        if (rb !=null) {
            currentChord.add29 = (String)(rb.getTag());
        }
        updateChord();
    }

    public void setAdd411(View v){
        currentChord.add411="";
        RadioButton rb = (RadioButton) findViewById(add411RG.getCheckedRadioButtonId());
        if (rb !=null) {
            currentChord.add411 = (String)(rb.getTag());
        }
        updateChord();
    }

    public void setAdd613(View v){
        currentChord.add613 = "";
        RadioButton rb = (RadioButton) findViewById(add613RG.getCheckedRadioButtonId());
        if (rb !=null) {
            currentChord.add613 = (String)(rb.getTag());
        }
        updateChord();
    }

    private Dialog constructRootDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.root_dialog);
        dialog.setTitle("Pick a root tone");
        ArrayList<SemiTone>semiTones = toneUtils.getSemiTones();
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                Button btn = (Button) dialog.findViewById(getResources().getIdentifier(String.format("rootDialogBtn%d",i*4+j),"id",getPackageName()));
                String tone = semiTones.get(i*4+j).getNames().get(0);
                btn.setText(tone);
                btn.setTag(tone);
                btn.setTransformationMethod(null);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRoot(v);
                        dialog.dismiss();
                    }
                });
            }

        }
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void chooseRoot(View v){
        rootDialog.show();
    }
}
